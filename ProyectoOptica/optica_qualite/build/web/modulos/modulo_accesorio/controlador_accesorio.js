let accesorioSeleccionado;
let accesorios;
let datepicker;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaAccesorio'));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblAccesorioCompleta');
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

export function guardarAccesorio() {
    let datos = null;
    let params = null;

    let accesorio = new Object();
    accesorio.producto = new Object();

    if (document.getElementById("txtIdAccesorio").value.trim().length < 1)
    {
        accesorio.idAccesorio = 0;
        accesorio.producto.idProducto = 0;
        accesorio.producto.codigoBarras = "";
    } else
    {
        accesorio.producto.codigoBarras = document.getElementById("txtCodigoBarras").value;
        accesorio.idAccesorio = parseInt(document.getElementById("txtIdAccesorio").value);
        accesorio.producto.idProducto = parseInt(document.getElementById("txtIdProducto").value);
    }

    if (validarVacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                if (validarExistencias() === true) {
                    accesorio.producto.nombre = document.getElementById("txtNombre").value.toUpperCase();
                    accesorio.producto.marca = document.getElementById("txtMarca").value.toUpperCase();
                    accesorio.producto.precioCompra = document.getElementById("txtPrecioCompra").value;
                    accesorio.producto.precioVenta = document.getElementById("txtPrecioVenta").value;
                    accesorio.producto.existencias = document.getElementById("txtExistencias").value;

                    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
                    datos = {
                        datosAccesorio: JSON.stringify(accesorio)
                    };
                    //params = "datosEmpleado=" + JSON.stringify(empleado);
                    params = new URLSearchParams(datos);

                    fetch("api/accesorio/save",
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
                                console.log(data);
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

                                document.getElementById("txtIdAccesorio").value = data.idAccesorio;
                                document.getElementById("txtIdProducto").value = data.producto.idProducto;
                                document.getElementById("txtCodigoBarras").value = data.producto.codigoBarras;

                                Swal.fire('', 'Datos del accesorio almacenados correctamente.', 'success');

                                accesorioSeleccionado = data;

                                refrescarTabla();
                                limpiarCampos();

                                document.getElementById('areaContenido').style.display = "none";
                                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                                document.getElementById('tblAccesorioCompleta').style.display = "block";
                            });
                }
            }
        }
    }
}

export function eliminarAccesorio() {
    let id = null;
    let param = null;
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás el accesorio seleccionado",
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
            fetch("api/accesorio/delete",
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
                        'El accesorio ha sido eliminado',
                        'success'
                        );
                limpiarCampos();
                document.getElementById('areaContenido').style.display = "none";
                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                document.getElementById('tblAccesorioCompleta').style.display = "block";
                refrescarTabla();
                document.getElementById("btnDelete").disabled = true;
            });
        }
    });
}

export function refrescarTabla() {
    let datos = null;
    let param = null;
//let url = "api/empleado/getAll?token=" + currentUser.usuario.lastToken;
    let showInactivos;
    if (document.getElementById("radActivo").checked) {
        showInactivos = false;
    } else if (document.getElementById("radInactivo").checked) {
        showInactivos = true;
    }
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    let filtro = document.getElementById("txtBusqueda").value;
    datos = {
        filtro: filtro,
        showDeleted: showInactivos,
        token: lastToken
    };
    param = new URLSearchParams(datos);
    fetch("api/accesorio/getAll",
            {
                method: "POST",
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                body: param
            }).then(response => {
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
                if (data.errorperm != null){
                    Swal.fire('Error', data.errorperm, 'error');
                    return;
                }
                if (data.errorsec != null)
                {
                    localStorage.clear();
                    Swal.fire({
                        title: 'Token incorrecto',
                        text: data.errorsec,
                        icon: 'error',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Iniciar sesion'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            console.log(data);
                            alert("consola");
                            window.location = "index.html";
                        }
                    });
                    return;
                }
                cargarTabla(data);
            });
        }

export function cargarTabla(data) {
    let contenido = "";
    accesorios = data;
    accesorios.forEach(function (accesorio) {
        let registro =
                "<tr>" +
                "<td>" + accesorio.producto.codigoBarras + "</td>" +
                "<td>" + accesorio.producto.nombre + "</td>" +
                "<td>" + accesorio.producto.marca + "</td>" +
                "<td>" + accesorio.producto.precioCompra + "</td>" +
                "<td>" + accesorio.producto.precioVenta + "</td>" +
                "<td>" + accesorio.producto.existencias + "</td>" +
                "<td><a onclick='moduloAccesorios.mostrarDetalle(" + accesorio.producto.idProducto + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblAccesorio").innerHTML = contenido;
}

export function mostrarDetalle(idProducto) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblAccesorioCompleta').style.display = "none";

    for (let i = 0; i < accesorios.length; i++) {
        if (idProducto === accesorios[i].producto.idProducto) {
            // Datos de producto
            document.getElementById("txtIdProducto").value = accesorios[i].producto.idProducto;
            document.getElementById("txtCodigoBarras").value = accesorios[i].producto.codigoBarras;
            document.getElementById("txtNombre").value = accesorios[i].producto.nombre;
            document.getElementById("txtMarca").value = accesorios[i].producto.marca;
            document.getElementById("txtPrecioCompra").value = accesorios[i].producto.precioCompra;
            document.getElementById("txtPrecioVenta").value = accesorios[i].producto.precioVenta;
            document.getElementById("txtExistencias").value = accesorios[i].producto.existencias;
            // Datos de accesorio
            document.getElementById("txtIdAccesorio").value = accesorios[i].idAccesorio;
        }
    }
    document.getElementById("btnDelete").classList.remove("disabled");
}

export function limpiarCampos() {
    document.getElementById("txtIdAccesorio").value = "";
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
    let input = document.formAccesorio;

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
    let input = document.formAccesorio;

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
    let input = document.formAccesorio;

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
    let input = document.formAccesorio;

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