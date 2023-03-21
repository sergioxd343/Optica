let tratamientoSeleccionado;
let tratamientos;
let datepicker;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaTratamiento'));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblTratamientoCompleta');

    if (form.style.display === 'none') {
        form.style.display = "block";
        document.getElementById("letreroFormulario").innerHTML = "Cerrar formulario.";
        tbl.style.display = "none";
    } else {
        form.style.display = "none";
        document.getElementById("letreroFormulario").innerHTML = "Abrir formulario.";
        limpiarCampos();
        tbl.style.display = "block";
    }
}

export function guardarTratamiento() {
    let datos = null;
    let params = null;

    let tratamiento = new Object();

    if (document.getElementById("txtIdTratamiento").value.trim().length < 1) {
        tratamiento.idTratamiento = 0;
    } else {
        tratamiento.idTratamiento = parseInt(document.getElementById("txtIdTratamiento").value);
    }

    tratamiento.nombre = document.getElementById("txtNombre").value.toUpperCase();
    tratamiento.precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    tratamiento.precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
    datos = {
        datosTratamiento: JSON.stringify(tratamiento)
    };

    //params = "datosEmpleado=" + JSON.stringify(empleado);
    params = new URLSearchParams(datos);

    if (validarvacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                fetch("api/tratamiento/save",
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

                            document.getElementById("txtIdTratamiento").value = data.idTratamiento;
                            Swal.fire('', 'Datos del tratamiento actualizados correctamente.', 'success');

                            tratamientoSeleccionado = data;

                            refrescarTabla();
                            limpiarCampos();

                            document.getElementById('areaContenido').style.display = "none";
                            document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                            document.getElementById('tblTratamientoCompleta').style.display = "block";
                        });
            }
        }
    }
}

export function eliminarTratamiento() {
    let id = null;
    let param = null;

    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás el tratamiento seleccionado",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let idTratamiento = parseInt(document.getElementById("txtIdTratamiento").value);
            id = {
                id: JSON.stringify(idTratamiento)
            };
            console.log(id);
            param = new URLSearchParams(id);
            console.log(param);
            fetch("api/tratamiento/delete",
                    {
                        method: "POST",
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                        body: param
                    }).then(response => {
                return response.json();
            }).then(function (data)
            {
                Swal.fire(
                        'Eliminado',
                        'El tratamiento ha sido eliminado',
                        'success'
                        );
                limpiarCampos();
                document.getElementById('areaContenido').style.display = "none";
                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                document.getElementById('tblTratamientoCompleta').style.display = "block";
                refrescarTabla();
                document.getElementById("btnDelete").disabled = true;
            });
        }
    });
}

export function refrescarTabla() {
    //let url = "api/empleado/getAll?token=" + currentUser.usuario.lastToken;}
    let showInactivos;
    if (document.getElementById("radActivo").checked) {
        showInactivos = false;
    } else if (document.getElementById("radInactivo").checked) {
        showInactivos = true;
    }
    let filtro = document.getElementById("txtBusqueda").value;
    let url = "api/tratamiento/getAll?filtro=" + filtro + "&showDeleted=" + showInactivos;
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
                    window.location.replace('../index.html');
                    return;
                }
                console.log(data);
                cargarTabla(data);
            });
}

export function cargarTabla(data) {
    let contenido = "";
    tratamientos = data;
    tratamientos.forEach(function (tratamiento) {
        let registro =
                "<tr>" +
                "<td>" + tratamiento.idTratamiento + "</td>" +
                "<td>" + tratamiento.nombre + "</td>" +
                "<td>" + tratamiento.precioCompra + "</td>" +
                "<td>" + tratamiento.precioVenta + "</td>" +
                "<td><a onclick='moduloTratamientos.mostrarDetalle(" + tratamiento.idTratamiento + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblTratamiento").innerHTML = contenido;
}

export function mostrarDetalle(idTratamiento) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblTratamientoCompleta').style.display = "none";
    for (let i = 0; i < tratamientos.length; i++) {
        if (idTratamiento === tratamientos[i].idTratamiento) {
            document.getElementById("txtIdTratamiento").value = tratamientos[i].idTratamiento;
            document.getElementById("txtNombre").value = tratamientos[i].nombre;
            document.getElementById("txtPrecioCompra").value = tratamientos[i].precioCompra;
            document.getElementById("txtPrecioVenta").value = tratamientos[i].precioVenta;
        }
    }
    document.getElementById("btnDelete").disabled = false;
}

export function limpiarCampos() {
    document.getElementById("txtIdTratamiento").value = "";
    document.getElementById("txtNombre").value = "";
    document.getElementById("txtPrecioCompra").value = "";
    document.getElementById("txtPrecioVenta").value = "";
    document.getElementById("btnDelete").disabled = true;
}

export function filtrarTratamiento() {
    if (document.getElementById("radActivo").checked) {
        let url = "../api/tratamiento/getAll?filtro=1";
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
                        window.location.replace('../index.html');
                        return;
                    }
                    cargarTabla(data);
                });
    } else if (document.getElementById("radInactivo").checked) {
        let url = "../api/tratamiento/getAll?filtro=0";
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
                        window.location.replace('../index.html');
                        return;
                    }
                    cargarTabla(data);
                });
    }
}

// Validaciones

export function validarvacios() {
    let nombre = document.getElementById("txtNombre").value;
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;

    let valid = true;
    let input = document.formTratamiento;
    if (nombre === "") {
        notificacion(input.txtNombre, "Ingrese un nombre.");
        input.txtNombre.focus();
        valid = false;
    }
    if (precioCompra === "") {
        notificacion(input.txtPrecioCompra, "Ingrese un precio de compra.");
        input.txtPrecioCompra.focus();
        valid = false;
    }
    if (precioVenta === "") {
        notificacion(input.txtPrecioVenta, "Ingrese un precio de venta.");
        input.txtPrecioVenta.focus();
        valid = false;
    }
    return valid;
}

export function validarNumeros() {
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;

    let valid = true;
    let input = document.formTratamiento;

    if (isNaN(precioCompra)) {
        notificacion(input.txtPrecioCompra, "Ingrese un precio de compra válido");
        input.txtPrecioCompra.focus();
        valid = false;
    }
    if (isNaN(precioVenta)) {
        notificacion(input.txtPrecioVenta, "Ingrese un precio de venta válido");
        input.txtPrecioVenta.focus();
        valid = false;
    }
    return valid;
}

export function validarPrecios() {
    let precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    let precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    let valid = true;
    let input = document.formTratamiento;

    if (precioCompra <= 0 || precioVenta <= 0) {
        if (precioCompra <= 0) {
            notificacion(input.txtPrecioCompra, "Ingrese un precio de compra válido, mayor a cero");
            input.txtPrecioCompra.focus();
            valid = false;
        }
        if (precioVenta <= 0) {
            notificacion(input.txtPrecioVenta, "Ingrese un precio de venta válido, mayor a cero");
            input.txtPrecioVenta.focus();
            valid = false;
        }
    } else {
        if (precioCompra >= precioVenta) {
            notificacion(input.txtPrecioCompra, "Precio de compra mayor o igual al precio de venta, revisar.");
            input.txtPrecioCompra.focus();
            notificacion(input.txtPrecioVenta, "Precio de venta menor o igual al precio de compra, revisar.");
            input.txtPrecioVenta.focus();
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