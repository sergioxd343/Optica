let micaSeleccionada;;
let micas;
let datepicker = null;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaMica'));
    // document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblTipoMicaCompleta');
    if (form.style.display === 'none') {
        form.style.display = "block";
        document.getElementById("letreroFormulario").innerHTML = "Cerrar formulario.";
        tbl.style.display = "none";
    } else {
        form.style.display = "none";
        limpiarCampos();
        document.getElementById("letreroFormulario").innerHTML = "Abrir formulario.";
        tbl.style.display = "block";
    }
}

export function guardarTipoMica() {
    let datos = null;
    let params = null;

    let mica = new Object();

    if (document.getElementById("txtIdTipoMica").value.trim().length < 1) {
        mica.idTipoMica = 0;
    } else {
        mica.idTipoMica = parseInt(document.getElementById("txtIdTipoMica").value);
    }

    mica.nombre = document.getElementById("txtNombre").value.toUpperCase();
    mica.precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    mica.precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
    datos = {
        datosTipoMica: JSON.stringify(mica)
    };

    //params = "datosEmpleado=" + JSON.stringify(empleado);
    params = new URLSearchParams(datos);

    if (validarvacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                fetch("api/mica/save",
                        {
                            method: "POST",
                            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                            body: params
                        })
                        .then(response => {
                            return response.json();
                        })
                        .then(function (data) {
                            if (data.exception != null)
                            {
                                Swal.fire('', 'Error interno del servidor. Intente nuevamente mas tarde.', 'error');
                                return;
                            }
                            if (data.error != null)
                            {
                                Swal.fire('', data.error, 'warning');
                                return;
                            }
                            if (data.errorperm != null)
                            {
                                Swal.fire('', 'No tiene permiso para realizar esta operacion.', 'warning');
                                return;
                            }

                            document.getElementById("txtIdTipoMica").value = data.idTipoMica;
                            Swal.fire('', 'Datos de la mica actualizados correctamente.', 'success');
                            micaSeleccionada = data;

                            refrescarTabla();
                            limpiarCampos();

                            document.getElementById('areaContenido').style.display = "none";
                            document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                            document.getElementById('tblTipoMicaCompleta').style.display = "block";
                        });
            }
        }
    }
}

export function refrescarTabla() {
    //let url = "api/mica/getAll?token=" + currentUser.usuario.lastToken;
    let filtro = document.getElementById("txtBusqueda").value;
    let url = "api/mica/getAll?filtro="+filtro;
    fetch(url)
            .then(response => {
                return response.json();
            })
            .then(function (data)
            {
                if (data.exception != null)
                {
                    Swal.fire('',
                            'Error interno del servidor. Intente nuevamente mas tarde.',
                            'error');
                    return;
                }
                if (data.error != null)
                {
                    Swal.fire('', data.error, 'warning');
                    return;
                }
                if (data.errorsec != null)
                {
                    Swal.fire('', data.errorsec, 'error');
                    alert("Regresando al index por error");
                    window.location.replace('index.html');
                    return;
                }
                cargarTabla(data);
            });
}

export function cargarTabla(data) {
    let contenido = "";
    micas = data;
    micas.forEach(function (mica) {
        let registro =
                "<tr>" +
                "<td>" + mica.idTipoMica + "</td>" +
                "<td>" + mica.nombre + "</td>" +
                "<td>" + mica.precioCompra + "</td>" +
                "<td>" + mica.precioVenta + "</td>" +
                "<td><a onclick='moduloTipoMicas.mostrarDetalle(" + mica.idTipoMica + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblTipoMica").innerHTML = contenido;
}

export function mostrarDetalle(idTipoMica) {
    // document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblTipoMicaCompleta').style.display = "none";
    for (let i = 0; i < micas.length; i++) {
        if (idTipoMica === micas[i].idTipoMica) {
            document.getElementById("txtIdTipoMica").value = micas[i].idTipoMica;
            document.getElementById("txtNombre").value = micas[i].nombre;
            document.getElementById("txtPrecioCompra").value = micas[i].precioCompra;
            document.getElementById("txtPrecioVenta").value = micas[i].precioVenta;
        }
    }
    // document.getElementById("btnDelete").disabled = false;
}

export function limpiarCampos() {
    document.getElementById("txtIdTipoMica").value = "";
    document.getElementById("txtNombre").value = "";
    document.getElementById("txtPrecioCompra").value = "";
    document.getElementById("txtPrecioVenta").value = "";
    // document.getElementById("btnDelete").disabled = true;
}

// Validaciones

export function validarvacios() {
    let nombre = document.getElementById("txtNombre").value;
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;

    let valid = true;
    let input = document.formTipoMica;
    if (nombre === "") {
        notificacion(input.txtNombre, "Ingrese un nombre.");
        valid = false;
    }
    if (precioCompra === "") {
        notificacion(input.txtPrecioCompra, "Ingrese un precio de compra.");
        valid = false;
    }
    if (precioVenta === "") {
        notificacion(input.txtPrecioVenta, "Ingrese un precio de venta.");
        valid = false;
    }
    return valid;
}

export function validarNumeros() {
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;

    let valid = true;
    let input = document.formTipoMica;

    if (isNaN(precioCompra)) {
        notificacion(input.txtPrecioCompra, "Ingrese un precio de compra válido");
        valid = false;
    }
    if (isNaN(precioVenta)) {
        notificacion(input.txtPrecioVenta, "Ingrese un precio de venta válido");
        valid = false;
    }
    return valid;
}

export function validarPrecios() {
    let precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    let precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    let valid = true;
    let input = document.formTipoMica;

    if (precioCompra <= 0 || precioVenta <= 0) {
        if (precioCompra <= 0) {
            notificacion(input.txtPrecioCompra, "Ingrese un precio de compra válido, mayor a cero");
            valid = false;
        }
        if (precioVenta <= 0) {
            notificacion(input.txtPrecioVenta, "Ingrese un precio de venta válido, mayor a cero");
        valid = false;
        }
    } else {
        if (precioCompra >= precioVenta) {
            notificacion(input.txtPrecioCompra, "Precio de compra mayor o igual al precio de venta, revisar.");
            notificacion(input.txtPrecioVenta, "Precio de venta menor o igual al precio de compra, revisar.");
            valid = false;
        }
    }

    return valid;
}

export function notificacion(elm, msg) {
    // agrega un elemento p con el texto luego del input seleccionado
    if (!$(elm).next().is("p")) {
        $(elm).after(`<p style='color: red;'><strong><small>${msg}</small></strong></p>`);
        // elimina el mensaje luego de 2 segundos
        setTimeout(function () {
            $(elm).next().remove();
        }, 3000);
    }
}