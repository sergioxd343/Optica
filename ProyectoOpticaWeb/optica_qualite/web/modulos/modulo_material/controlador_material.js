let materialSeleccionado;;
let materiales;
let datepicker;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaMaterial'));
    // document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblMaterialCompleta');
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

export function guardarMaterial() {
    let datos = null;
    let params = null;

    let material = new Object();

    if (document.getElementById("txtIdMaterial").value.trim().length < 1) {
        material.idMaterial = 0;
    } else {
        material.idMaterial = parseInt(document.getElementById("txtIdMaterial").value);
    }

    material.nombre = document.getElementById("txtNombre").value.toUpperCase();
    material.precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    material.precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
    datos = {
        datosMaterial: JSON.stringify(material)
    };

    //params = "datosEmpleado=" + JSON.stringify(empleado);
    params = new URLSearchParams(datos);

    if (validarvacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                fetch("api/material/save",
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

                            document.getElementById("txtIdMaterial").value = data.idMaterial;
                            Swal.fire('', 'Datos del material actualizados correctamente.', 'success');

                            materialSeleccionado = data;

                            refrescarTabla();
                            limpiarCampos();

                            document.getElementById('areaContenido').style.display = "none";
                            document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                            document.getElementById('tblMaterialCompleta').style.display = "block";
                        });
            }
        }
    }
}

export function refrescarTabla() {
    //let url = "api/material/getAll?token=" + currentUser.usuario.lastToken;
    let filtro = document.getElementById("txtBusqueda").value;
    let url = "api/material/getAll?filtro="+filtro;
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
    materiales = data;
    materiales.forEach(function (material) {
        let registro =
                "<tr>" +
                "<td>" + material.idMaterial + "</td>" +
                "<td>" + material.nombre + "</td>" +
                "<td>" + material.precioCompra + "</td>" +
                "<td>" + material.precioVenta + "</td>" +
                "<td><a onclick='moduloMateriales.mostrarDetalle(" + material.idMaterial + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblMaterial").innerHTML = contenido;
}

export function mostrarDetalle(idMaterial) {
    // document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblMaterialCompleta').style.display = "none";
    for (let i = 0; i < materiales.length; i++) {
        if (idMaterial === materiales[i].idMaterial) {
            document.getElementById("txtIdMaterial").value = materiales[i].idMaterial;
            document.getElementById("txtNombre").value = materiales[i].nombre;
            document.getElementById("txtPrecioCompra").value = materiales[i].precioCompra;
            document.getElementById("txtPrecioVenta").value = materiales[i].precioVenta;
        }
    }
    // document.getElementById("btnDelete").disabled = false;
}

export function limpiarCampos() {
    document.getElementById("txtIdMaterial").value = "";
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
    let input = document.formMaterial;
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
    let input = document.formMaterial;

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
    let input = document.formMaterial;

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