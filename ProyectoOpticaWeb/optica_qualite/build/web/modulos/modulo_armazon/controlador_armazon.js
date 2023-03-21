let armazonSeleccionado;
let armazones;
let datePicker = null;
let inputFileArmazon = null;

export function inicializar() {
    refrescarTabla();
    inputFileArmazon = document.getElementById("inputFileImagenArmazon");
    inputFileArmazon.onchange = function (evt) {
        cargarFotografia(inputFileArmazon);
    };
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaArmazon'));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblArmazonCompleta');
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

export function guardarArmazon() {
    let datos = null;
    let params = null;

    let armazon = new Object();
    armazon.producto = new Object();

    if (document.getElementById("txtIdArmazon").value.trim().length < 1) {
        armazon.idArmazon = 0;
        armazon.producto.idProducto = 0;
        armazon.producto.codigoBarras = "";
    } else {
        armazon.idArmazon = parseInt(document.getElementById("txtIdArmazon").value);
        armazon.producto.idProducto = parseInt(document.getElementById("txtIdProducto").value);
        armazon.producto.codigoBarras = document.getElementById("txtCodigoBarras").value;
    }

    if (validarVacios() === true) {
        if (validarNumeros() === true) {
            if (validarPrecios() === true) {
                if (validarExistencias() === true) {
                    // Datos de producto
                    armazon.producto.nombre = document.getElementById("txtNombre").value.toUpperCase();
                    armazon.producto.marca = document.getElementById("txtMarca").value.toUpperCase();
                    armazon.producto.precioVenta = parseFloat(document.getElementById("txtPrecioVenta").value);
                    armazon.producto.precioCompra = parseFloat(document.getElementById("txtPrecioCompra").value);
                    armazon.producto.existencias = document.getElementById("txtExistencias").value;

                    // Datos de armazón
                    armazon.modelo = document.getElementById("txtModelo").value.toUpperCase();
                    armazon.color = document.getElementById("txtColor").value.toUpperCase();
                    armazon.dimensiones = document.getElementById("txtDimensiones").value.toUpperCase();
                    armazon.descripcion = document.getElementById("txtDescripcion").value.toUpperCase();
                    // Guardamos la base64 de la fotografía
                    armazon.fotografia = document.getElementById("txtCodigoImagen").value;

                    datos = {
                        datosArmazon: JSON.stringify(armazon)
                    };

                    params = new URLSearchParams(datos);

                    fetch("api/armazon/save",
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

                                document.getElementById("txtIdArmazon").value = data.idArmazon;
                                document.getElementById("txtIdProducto").value = data.producto.idProducto;
                                document.getElementById("txtCodigoBarras").value = data.producto.CodigoBarras;

                                Swal.fire('', 'Datos del armazón actualizados correctamente.', 'success');

                                armazonSeleccionado = data;

                                refrescarTabla();
                                limpiarCampos();

                                document.getElementById('areaContenido').style.display = "none";
                                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                                document.getElementById('tblArmazonCompleta').style.display = "block";
                            });
                }
            }
        }
    }
}

export function eliminarArmazon() {
    let id = null;
    let param = null;
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás el armazón seleccionado",
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
            fetch("api/armazon/delete",
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
                document.getElementById('tblArmazonCompleta').style.display = "block";
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
    fetch("api/armazon/getAll",
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
    armazones = data;
    armazones.forEach(function (armazon) {
        let registro =
                "<tr>" +
                "<td>" + armazon.producto.codigoBarras + "</td>" +
                "<td>" + armazon.producto.nombre + "</td>" +
                "<td>" + armazon.producto.marca + "</td>" +
                "<td>" + armazon.modelo + "</td>" +
                "<td>" + armazon.color + "</td>" +
                "<td>" + armazon.dimensiones + "</td>" +
                "<td>" + armazon.producto.precioCompra + "</td>" +
                "<td>" + armazon.producto.precioVenta + "</td>" +
                "<td>" + armazon.producto.existencias + "</td>" +
                "<td> <img class='img-fluid' src='data:image/" + obtenerImageFormat(armazon.fotografia) + ";base64," + armazon.fotografia + "' style='width: 156px; height: 156px'/> </td>" +
                "<td><a onclick='moduloArmazones.mostrarDetalle(" + armazon.producto.idProducto + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblArmazon").innerHTML = contenido;
}

export function mostrarDetalle(idProducto) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblArmazonCompleta').style.display = "none";

    for (let i = 0; i < armazones.length; i++) {
        if (idProducto === armazones[i].producto.idProducto) {
            // Datos de producto
            document.getElementById("txtIdProducto").value = armazones[i].producto.idProducto;
            document.getElementById("txtCodigoBarras").value = armazones[i].producto.codigoBarras;
            document.getElementById("txtNombre").value = armazones[i].producto.nombre;
            document.getElementById("txtMarca").value = armazones[i].producto.marca;
            document.getElementById("txtPrecioCompra").value = armazones[i].producto.precioCompra;
            document.getElementById("txtPrecioVenta").value = armazones[i].producto.precioVenta;
            document.getElementById("txtExistencias").value = armazones[i].producto.existencias;

            // Datos de armazón
            document.getElementById("txtIdArmazon").value = armazones[i].idArmazon;
            document.getElementById("txtModelo").value = armazones[i].modelo;
            document.getElementById("txtColor").value = armazones[i].color;
            document.getElementById("txtDimensiones").value = armazones[i].dimensiones;
            document.getElementById("txtDescripcion").value = armazones[i].descripcion;
            // Cargamos  base64 a textarea
            document.getElementById("txtCodigoImagen").value = armazones[i].fotografia;
            // Mostramos la imagen en la img
            document.getElementById("imgFoto").src = "data:image/" + obtenerImageFormat(armazones[i].fotografia) + ";base64," + armazones[i].fotografia;
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
    // Datos de armazón
    document.getElementById("txtIdArmazon").value = "";
    document.getElementById("txtModelo").value = "";
    document.getElementById("txtColor").value = "";
    document.getElementById("txtDimensiones").value = "";
    document.getElementById("txtDescripcion").value = "";
    // Quitando archivo del input file, la base64 del textArea y la imagen de img
    document.getElementById("inputFileImagenArmazon").value = "";
    document.getElementById("txtCodigoImagen").value = "";
    document.getElementById("imgFoto").src = "img/fondo.png";

    document.getElementById("txtNombre").focus();
    document.getElementById("btnDelete").disabled = true;
}

export function mostrarImageDialog() {
    document.getElementById("inputFileImagenArmazon").click();
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
    // Datos armazon
    let modelo = document.getElementById("txtModelo").value;
    let color = document.getElementById("txtColor").value;
    let dimensiones = document.getElementById("txtDimensiones").value;
    let descripcion = document.getElementById("txtDescripcion").value;
    let fotografia = document.getElementById("txtCodigoImagen").value;

    let valid = true;
    let input = document.formArmazon;

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

    if (modelo === "") {
        notificacion(input.txtModelo, "Ingrese un modelo");
        input.txtModelo.focus();
        valid = false;
    }
    if (color === "") {
        notificacion(input.txtColor, "Ingrese un color");
        input.txtColor.focus();
        valid = false;
    }
    if (dimensiones === "") {
        notificacion(input.txtDimensiones, "Ingrese las dimensiones");
        input.txtDimensiones.focus();
        valid = false;
    }
    if (descripcion === "") {
        notificacion(input.txtDescripcion, "Ingrese una descripción");
        input.txtDescripcion.focus();
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

    let valid = true;
    let input = document.formArmazon;

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
    let input = document.formArmazon;

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
    let input = document.formArmazon;
    ;

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