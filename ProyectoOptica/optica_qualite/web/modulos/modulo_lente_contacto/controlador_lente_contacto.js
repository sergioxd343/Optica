let lenteSeleccionado;
let lentesContacto;
let datepicker = null;
let inputFileLenteContacto = null;

export function inicializar() {
    refrescarTabla();
    inputFileLenteContacto = document.getElementById("inputFileImagenLenteContacto");
    inputFileLenteContacto.onchange = function (evt) {
        cargarFotografia(inputFileLenteContacto);
    };
    configureTableFilter(document.getElementById("txtBusqueda"),
            document.getElementById("tablaLenteContacto"));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblLenteContactoCompleta');
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

export function guardarLenteContacto() {
    var datos = null;
    var params = null;

    var lentecontacto = new Object();
    lentecontacto.producto = new Object();

    if (document.getElementById("txtIdLenteContacto").value.trim().length < 1) {
        lentecontacto.idLenteContacto = 0;
        lentecontacto.producto.idProducto = 0;
        lentecontacto.producto.codigoBarras = "";
    } else {
        lentecontacto.idLenteContacto = parseInt(document.getElementById("txtIdLenteContacto").value);
        lentecontacto.producto.idProducto = parseInt(document.getElementById("txtIdProducto").value);
        lentecontacto.producto.codigoBarras = document.getElementById("txtCodigoBarras").value;

    }

    if (validarVacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                if (validarExistencias() === true) {
                    if (validarQueratometria() === true) {
                        // Datos de producto
                        lentecontacto.producto.nombre = document.getElementById("txtNombre").value.toUpperCase();
                        lentecontacto.producto.marca = document.getElementById("txtMarca").value.toUpperCase();
                        lentecontacto.producto.precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
                        lentecontacto.producto.precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);
                        lentecontacto.producto.existencias = parseInt(document.getElementById("txtExistencias").value);
                        // Datos de lente de contacto
                        lentecontacto.keratometria = parseInt(document.getElementById("txtQueratometria").value);
                        // Guardamos la base64 de la fotografía
                        lentecontacto.fotografia = document.getElementById("txtCodigoImagen").value;

                        datos = {
                            datos: JSON.stringify(lentecontacto)
                        };

                        params = new URLSearchParams(datos);

                        fetch("api/lentescontacto/save",
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
                                    if (data.exception != null) {
                                        Swal.fire('', "Error interno del servidor. Intente más tarde.", 'error');
                                        return;
                                    }

                                    if (data.error != null) {
                                        Swal.fire('', data.error, 'warning');
                                        return;
                                    }

                                    if (data.errorperm != null) {
                                        Swal.fire('', "No tiene permiso para realizar esta operacion", 'warning');
                                        return;
                                    }

                                    document.getElementById("txtIdProducto").value = data.producto.idProducto;
                                    document.getElementById("txtIdLenteContacto").value = data.idLenteContacto;
                                    document.getElementById("txtCodigoBarras").value = data.producto.codigoBarras;

                                    Swal.fire('', "Datos del lente de contacto actualizados correctamente", 'success');

                                    lenteSeleccionado = data;

                                    refrescarTabla();
                                    limpiarCampos();

                                    document.getElementById('areaContenido').style.display = "none";
                                    document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                                    document.getElementById('tblLenteContactoCompleta').style.display = "block";
                                });
                    }
                }
            }
        }
    }
}

export function eliminarLenteContacto() {
    let id = null;
    let param = null;
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás el lente de contacto seleccionado",
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
            fetch("api/lentescontacto/delete",
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
                        'El lente de contacto ha sido eliminado.',
                        'success'
                        );
                limpiarCampos();
                document.getElementById('areaContenido').style.display = "none";
                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                document.getElementById('tblLenteContactoCompleta').style.display = "block";
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
    fetch("api/lentescontacto/getAll",
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
    lentesContacto = data;
    lentesContacto.forEach(function (lentesContacto) {
        let registro =
                "<tr>" +
                "<td>" + lentesContacto.producto.codigoBarras + "</td>" +
                "<td>" + lentesContacto.producto.nombre + "</td>" +
                "<td>" + lentesContacto.producto.marca + "</td>" +
                "<td>" + lentesContacto.producto.precioVenta + "</td>" +
                "<td>" + lentesContacto.keratometria + "</td>" +
                "<td> <img class='img-fluid' src='data:image/" + obtenerImageFormat(lentesContacto.fotografia) + ";base64," + lentesContacto.fotografia + "' style='width: 156px; height: 156px'/> </td>" +
                "<td><a onclick='moduloLentesContacto.mostrarDetalle(" + lentesContacto.producto.idProducto + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblLenteContacto").innerHTML = contenido;
}

export function mostrarDetalle(idProducto) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblLenteContactoCompleta').style.display = "none";

    for (let i = 0; i < lentesContacto.length; i++) {
        if (idProducto === lentesContacto[i].producto.idProducto) {
            // Datos de producto
            document.getElementById("txtIdProducto").value = lentesContacto[i].producto.idProducto;
            document.getElementById("txtCodigoBarras").value = lentesContacto[i].producto.codigoBarras;
            document.getElementById("txtNombre").value = lentesContacto[i].producto.nombre;
            document.getElementById("txtMarca").value = lentesContacto[i].producto.marca;
            document.getElementById("txtPrecioCompra").value = lentesContacto[i].producto.precioCompra;
            document.getElementById("txtPrecioVenta").value = lentesContacto[i].producto.precioVenta;
            document.getElementById("txtExistencias").value = lentesContacto[i].producto.existencias;

            // Datos de lente de contacto
            document.getElementById("txtIdLenteContacto").value = lentesContacto[i].idLenteContacto;
            document.getElementById("txtQueratometria").value = lentesContacto[i].keratometria;

            // Cargamos base64 a textara
            document.getElementById("txtCodigoImagen").value = lentesContacto[i].fotografia;
            // Mostramos la imagen en la img
            document.getElementById("imgFoto").src = "data:image/" + obtenerImageFormat(lentesContacto[i].fotografia) + ";base64," + lentesContacto[i].fotografia;
        }
    }
    document.getElementById("btnDelete").classList.remove("disabled");
}

export function limpiarCampos() {
    // Datos de producto
    document.getElementById("txtIdProducto").value = "";
    document.getElementById("txtCodigoBarras").value = "";
    document.getElementById("txtNombre").value = "";
    document.getElementById("txtMarca").value = "";
    document.getElementById("txtPrecioCompra").value = "";
    document.getElementById("txtPrecioVenta").value = "";
    document.getElementById("txtExistencias").value = "";
    // Datos de lente de contacto
    document.getElementById("txtIdLenteContacto").value = "";
    document.getElementById("txtQueratometria").value = "";
    // Quitando archivo del input file, la base64 del textArea y la imagen de img
    document.getElementById("inputFileImagenLenteContacto").value = "";
    document.getElementById("txtCodigoImagen").value = "";
    document.getElementById("imgFoto").src = "img/fondo.png";

    document.getElementById("txtNombre").focus();
    document.getElementById("btnDelete").disabled = true;
}

export function mostrarImageDialog() {
    document.getElementById('inputFileImagenLenteContacto').click();
}

function cargarFotografia(objetoInputFile) {
    // Revisamos que el usuario haya seleccionado un archivo
    if (objetoInputFile.files && objetoInputFile.files[0]) {
        // Ayuda a leer la imagen del input file
        let reader = new FileReader();
        // Agregamos un oyente al lector del archivo para que,
        // en cuento el usuario cargue una imagen, esta se lea
        // y se convierta de forma autoamrica en una cadena de Base64
        reader.onload = function (e) {
            // El contenido del archivo despues de haberlo leido. Son datos binarios
            let fotoB64 = e.target.result;
            // $("#imgFoto").attr("src", fotoB64);
            // $("txtBase64").val(fotoB64.substring(22, fotoB64.length));
            // Se pone el la Base64 de la fotografia
            document.getElementById("imgFoto").src = fotoB64;
            // Colocamos la Base64 en el textArea
            document.getElementById("txtCodigoImagen").value =
                    fotoB64.substring(fotoB64.indexOf(",") + 1, fotoB64.length);
        };
        //Leemos el archivo que selecciono el usuario y lo
        //convertimos en un cadena de Base64
        reader.readAsDataURL(objetoInputFile.files[0]);
    }
}

function obtenerImageFormat(strb64) {
    let fc = strb64 !== null && strb64.length > 0 ? strb64.substr(0, 1) : "";
    switch (fc) {
        case "/" :
            return "jpg";
        case "i" :
            return "png";
        case "Q" :
            return "bmp";
        case "S" :
            return "tiff";
        case "J" :
            return "pdf";
        case "U" :
            return "wepd";
        case "R" :
            return "gif";
    }
}

// Validaciones

export function validarVacios() {
    // Datos de producto
    let nombre = document.getElementById("txtNombre").value;
    let marca = document.getElementById("txtMarca").value;
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;
    let existencias = document.getElementById("txtExistencias").value;
    // Datos lente contacto
    let queratometria = document.getElementById("txtQueratometria").value;
    let fotografia = document.getElementById("txtCodigoImagen").value;

    let valid = true;
    let input = document.formLenteContacto;

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

    if (queratometria === "") {
        notificacion(input.txtQueratometria, "Ingrese una queratometría válida");
        input.txtQueratometria.focus();
        valid = false;
    }
    if (fotografia === "") {
        notificacion(input.txtCodigoImagen, "Escoge una fotografía");
        input.txtCodigoImagen.focus();
        valid = false;
    }

    return valid;
}

export function validarNumeros() {
    // Producto
    let precioCompra = document.getElementById("txtPrecioCompra").value;
    let precioVenta = document.getElementById("txtPrecioVenta").value;
    let existencias = document.getElementById("txtExistencias").value;
    // Lente contacto
    let queratometria = document.getElementById("txtQueratometria").value;

    let valid = true;
    let input = document.formLenteContacto;

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

    if (isNaN(queratometria)) {
        notificacion(input.txtQueratometria, "Ingrese una queratometría válida");
        input.txtQueratometria.focus();
        valid = false;
    }
    return valid;
}

export function validarPrecios() {
    let precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
    let precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);

    let valid = true;
    let input = document.formLenteContacto;

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
    let input = document.formLenteContacto;

    if (Number.isInteger(existencias) === false) {
        notificacion(input.txtExistencias, "Ingrese una cantidad válida");
        input.txtExistencias.focus();
        valid = false;
    }
    return valid;
}

export function validarQueratometria() {
    let queratometria = parseFloat(document.getElementById("txtQueratometria").value);
    let valid = true;
    let input = document.formLenteContacto;
    if (Number.isInteger(queratometria) === false) {
        notificacion(input.txtQueratometria, "Ingrese una queratometría válida");
        input.txtQueratometria.focus();
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