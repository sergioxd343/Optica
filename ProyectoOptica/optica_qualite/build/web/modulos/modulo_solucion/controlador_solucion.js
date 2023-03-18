let solucionSeleccionada;
let soluciones;
let datepicker;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaSolucion'));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblSolucionCompleta');
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

export function guardarSolucion() {
    let datos = null;
    let params = null;

    let solucion = new Object();
    solucion.producto = new Object();

    if (document.getElementById("txtIdSolucion").value.trim().length < 1)
    {
        solucion.idSolucion = 0;
        solucion.producto.idProducto = 0;
        solucion.producto.codigoBarras = "";
    } else
    {
        solucion.idSolucion = parseInt(document.getElementById("txtIdSolucion").value);
        solucion.producto.idProducto = parseInt(document.getElementById("txtIdProducto").value);
        solucion.producto.codigoBarras = document.getElementById("txtCodigoBarras").value;
    }

    if (validarVacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                if (validarExistencias() === true) {
                    solucion.producto.nombre = document.getElementById("txtNombre").value.toUpperCase();
                    solucion.producto.marca = document.getElementById("txtMarca").value.toUpperCase();
                    solucion.producto.precioCompra = document.getElementById("txtPrecioCompra").value;
                    solucion.producto.precioVenta = document.getElementById("txtPrecioVenta").value;
                    solucion.producto.existencias = document.getElementById("txtExistencias").value;

                    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
                    datos = {
                        datosSolucion: JSON.stringify(solucion)
                    };
                    //params = "datosEmpleado=" + JSON.stringify(empleado);
                    params = new URLSearchParams(datos);

                    fetch("api/solucion/save", // peticion al servidor para insertar o actualizar
                            {
                                method: "POST",
                                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                                body: params
                            })
                            .then(response => {
                                return response.json();
                            })
                            .then(function (data)
                            {
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
                                document.getElementById("txtIdSolucion").value = data.idSolucion;
                                document.getElementById("txtIdProducto").value = data.producto.idProducto;
                                document.getElementById("txtCodigoBarras").value = data.producto.codigoBarras;

                                Swal.fire('', 'Datos de la solución actualizados correctamente.', 'success');
                                console.log(data);
                                solucionSeleccionada = data;

                                refrescarTabla();
                                limpiarCampos();

                                document.getElementById('areaContenido').style.display = "none";
                                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                                document.getElementById('tblSolucionCompleta').style.display = "block";
                            });
                }
            }
        }
    }
}

export function eliminarSolucion() {
    let id = null;
    let param = null;
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás la solución seleccionada",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let idProducto = parseInt(document.getElementById("txtIdProducto").value);
            id = {
                id: JSON.stringify(idProducto)
            };
            param = new URLSearchParams(id);
            fetch("api/solucion/delete", // peticion al servidor de delete
                    {
                        method: "POST",
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                        body: param
                    }).then(response => {
                return response.json();
            }).then(function (data)
            {
                Swal.fire(
                        '¡Eliminado!',
                        'El accesorio ha sido eliminado.',
                        'success'
                        );
                limpiarCampos();
                document.getElementById('areaContenido').style.display = "none";
                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                document.getElementById('tblSolucionCompleta').style.display = "block";
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
    let url = "api/solucion/getAll?filtro=" + filtro + "&showDeleted=" + showInactivos;
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
                //alert('cargando tabla despues del fetch...');
                cargarTabla(data);
            });
}

export function cargarTabla(data) { // Carga en la tabla los datos que se envien
    let contenido = "";
    soluciones = data;
    soluciones.forEach(function (solucion) {
        let registro =
                "<tr>" +
                "<td>" + solucion.idSolucion + "</td>" +
                "<td>" + solucion.producto.codigoBarras + "</td>" +
                "<td>" + solucion.producto.nombre + "</td>" +
                "<td>" + solucion.producto.marca + "</td>" +
                "<td>" + solucion.producto.precioCompra + "</td>" +
                "<td>" + solucion.producto.precioVenta + "</td>" +
                "<td>" + solucion.producto.existencias + "</td>" +
                "<td><a onclick='moduloSoluciones.mostrarDetalle(" + solucion.producto.idProducto + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblSolucion").innerHTML = contenido;
}

export function mostrarDetalle(idProducto) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblSolucionCompleta').style.display = "none";

    for (let i = 0; i < soluciones.length; i++) {
        if (idProducto === soluciones[i].producto.idProducto) {
            // Datos de producto
            document.getElementById("txtIdProducto").value = soluciones[i].producto.idProducto;
            document.getElementById("txtCodigoBarras").value = soluciones[i].producto.codigoBarras;
            document.getElementById("txtNombre").value = soluciones[i].producto.nombre;
            document.getElementById("txtMarca").value = soluciones[i].producto.marca;
            document.getElementById("txtPrecioCompra").value = soluciones[i].producto.precioCompra;
            document.getElementById("txtPrecioVenta").value = soluciones[i].producto.precioVenta;
            document.getElementById("txtExistencias").value = soluciones[i].producto.existencias;
            // Datos de solucion
            document.getElementById("txtIdSolucion").value = soluciones[i].idSolucion;
        }
    }
    document.getElementById("btnDelete").classList.remove("disabled");
}

export function limpiarCampos() { // Limpia los campos
    document.getElementById("txtIdSolucion").value = "";

    document.getElementById("txtIdProducto").value = "";
    document.getElementById("txtCodigoBarras").value = "";
    document.getElementById("txtNombre").value = "";
    document.getElementById("txtMarca").value = "";
    document.getElementById("txtPrecioCompra").value = "";
    document.getElementById("txtPrecioVenta").value = "";
    document.getElementById("txtExistencias").value = "";

    document.getElementById("txtNombre").focus();
    document.getElementById("btnDelete").disabled = true;
}

// Validaciones

export function validarVacios() {
    let nombre = document.getElementById("txtNombre").value;
    let marca = document.getElementById("txtMarca").value;
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;
    let existencias = document.getElementById("txtExistencias").value;

    let valid = true;
    let input = document.formSolucion;

    if (nombre === "") {
        notificacion(input.txtNombre, "Ingrese un nombre");
        input.txtNombre.focus();
        valid = false;
    }
    if (marca === "") {
        notificacion(input.txtMarca, "Ingrese una marca");
        input.txtMarca.focus();
        valid = false;
    }
    if (precioCompra === "") {
        notificacion(input.txtPrecioCompra, "Ingrese un precio de compra válido");
        input.txtPrecioCompra.focus();
        valid = false;
    }
    if (precioVenta === "") {
        notificacion(input.txtPrecioVenta, "Ingrese un precio de venta válido");
        input.txtPrecioVenta.focus();
        valid = false;
    }
    if (existencias === "") {
        notificacion(input.txtExistencias, "Ingrese una existencia válida");
        input.txtExistencias.focus();
        valid = false;
    }

    return valid;
}

export function validarNumeros() {
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;
    let existencias = document.getElementById("txtExistencias").value;

    let valid = true;
    let input = document.formSolucion;

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
    if (isNaN(existencias)) {
        notificacion(input.txtExistencias, "Ingrese una cantidad válida");
        input.txtExistencias.focus();
        valid = false;
    }
    return valid;
}

export function validarPrecios() {
    let precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    let precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    let valid = true;
    let input = document.formSolucion;

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

export function validarExistencias() {
    let existencias = parseFloat(document.getElementById("txtExistencias").value);
    let valid = true;
    let input = document.formSolucion;

    if (Number.isInteger(existencias) === false) {
        notificacion(input.txtExistencias, "Ingrese una cantidad válida");
        input.txtExistencias.focus();
        valid = false;
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