let clienteSeleccionado;
let clientes;
let datepicker = null;

export function inicializar() {
    refrescarTabla();
    configureTableFilter(document.getElementById('txtBusqueda'),
            document.getElementById('tablaCliente'));
    document.getElementById("btnDelete").disabled = true;
}

export function setDetalleVisible() {
    const form = document.getElementById('areaContenido');
    const tbl = document.getElementById('tblClienteCompleta');
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

export function guardarCliente() {
    let datos = null;
    let params = null;

    let cliente = new Object();
    cliente.persona = new Object();

    if (document.getElementById("txtIdCliente").value.trim().length < 1)
    {
        cliente.idCliente = 0;
        cliente.persona.idPersona = 0;

    } else
    {
        cliente.idCliente = parseInt(document.getElementById("txtIdCliente").value);
        cliente.persona.idPersona = parseInt(document.getElementById("txtIdPersona").value);
    }

    //Persona datos personales
    cliente.persona.nombre = document.getElementById("txtNombre").value.toUpperCase();
    cliente.persona.apellidoPaterno = document.getElementById("txtApellidoPat").value.toUpperCase();
    cliente.persona.apellidoMaterno = document.getElementById("txtApellidoMat").value.toUpperCase();
    cliente.persona.genero = document.getElementById("txtGenero").value.toUpperCase();
    cliente.persona.rfc = document.getElementById("txtRFC").value.toUpperCase();
    // Formar fecha de nacimiento
    let dia = document.getElementById("txtDia").value;
    let mes = document.getElementById("txtMes").value;
    let anio = document.getElementById("txtAnio").value;
    let fecha = dia + "/" + mes + "/" + anio;
    document.getElementById("txtFechaNac").value = fecha;
    cliente.persona.fechaNacimiento = fecha;
    // Persona datos de domicilio
    cliente.persona.cp = document.getElementById("txtCodigoPostal").value;
    cliente.persona.estado = document.getElementById("txtEntidad").value.toUpperCase();
    cliente.persona.ciudad = document.getElementById("txtCiudad").value.toUpperCase();
    cliente.persona.colonia = document.getElementById("txtColonia").value.toUpperCase();
    cliente.persona.calle = document.getElementById("txtCalle").value.toUpperCase();
    cliente.persona.numero = document.getElementById("txtNumDom").value;
    // Persona datos de contacto
    cliente.persona.telCasa = document.getElementById("txtTelefonoCasa").value;
    cliente.persona.telMovil = document.getElementById("txtTelefonoMov").value;
    cliente.persona.email = document.getElementById("txtCorreo").value;
    // Cliente
    cliente.numeroUnico = document.getElementById("txtNUC").value;


    //params = "token=" + currentUser.usuario.lastToken + "&datosEmpleado=" + JSON.stringify(empleado);
    datos = {
        datosCliente: JSON.stringify(cliente)
    };
    //params = "datosEmpleado=" + JSON.stringify(empleado);
    params = new URLSearchParams(datos);
    if (validarvacios() === true) {
        if (validarRFC() === true) {
            if (validarTelefonoMovil() === true) {
                if (validarTelefonoCasa() === true) {
                    if (validarLongitudTelefono() === true) {
                        fetch("api/cliente/save",
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

                                    document.getElementById("txtIdCliente").value = data.idCliente;
                                    document.getElementById("txtIdPersona").value = data.persona.idPersona;
                                    document.getElementById("txtNUC").value = data.numeroUnico;

                                    Swal.fire('', 'Datos del cliente guardados correctamente.', 'success');
                                    clienteSeleccionado = data;
                                    refrescarTabla();
                                    limpiarCampos();
                                    document.getElementById('areaContenido').style.display = "none";
                                    document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                                    document.getElementById('tblClienteCompleta').style.display = "block";
                                });
                    }
                }
            }
        }
    }
}

export function eliminarCliente() {
    let id = null;
    let param = null;
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Eliminarás el cliente seleccionado",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            let idCliente = parseInt(document.getElementById("txtIdCliente").value);
            id = {
                id: JSON.stringify(idCliente)
            };
            param = new URLSearchParams(id);
            fetch("api/cliente/delete",
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
                        'El cliente ha sido eliminado',
                        'success'
                        );
                limpiarCampos();
                document.getElementById('areaContenido').style.display = "none";
                document.getElementById('letreroFormulario').innerHTML = "Abrir formulario.";
                document.getElementById('tblClienteCompleta').style.display = "block";
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
    fetch("api/cliente/getAll",
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
    clientes = data;
    clientes.forEach(function (cliente) {
        let registro =
                "<tr>" +
                "<td>" + cliente.numeroUnico + "</td>" +
                "<td>" + cliente.persona.nombre + "</td>" +
                "<td>" + cliente.persona.apellidoPaterno + "</td>" +
                "<td>" + cliente.persona.apellidoMaterno + "</td>" +
                "<td>" + cliente.persona.telMovil + "</td>" +
                "<td>" + cliente.persona.email + "</td>" +
                "<td><a onclick='moduloClientes.mostrarDetalle(" + cliente.idCliente + ");'><i class='fa-solid fa-pen-to-square'></i></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblClientes").innerHTML = contenido;

}

export function mostrarDetalle(idCliente) {
    document.getElementById("btnDelete").disabled = false;
    document.getElementById('areaContenido').style.display = "block";
    document.getElementById('letreroFormulario').innerHTML = "Cerrar formulario.";
    document.getElementById('tblClienteCompleta').style.display = "none";

    for (var i = 0; i < clientes.length; i++) {
        if (idCliente === clientes[i].idCliente) {
            document.getElementById("txtNombre").value = clientes[i].persona.nombre;
            document.getElementById("txtApellidoPat").value = clientes[i].persona.apellidoPaterno;
            document.getElementById("txtApellidoMat").value = clientes[i].persona.apellidoMaterno;
            document.getElementById("txtGenero").value = clientes[i].persona.genero;
            // Mostrar la fecha en los select
            document.getElementById("txtFechaNac").value = clientes[i].persona.fechaNacimiento;
            let fecha = document.getElementById("txtFechaNac").value;
            let selectDia = fecha.substring(0, 2);
            let selectMes = fecha.substring(3, 5);
            let selectAnio = fecha.substring(6);
            asignarFecha(selectDia, selectMes, selectAnio);
            // Datos de domicilio
            document.getElementById("txtCalle").value = clientes[i].persona.calle;
            document.getElementById("txtNumDom").value = clientes[i].persona.numero;
            // Mostra la colonia
            let select = document.getElementById("txtColonia");
            let option = document.createElement('option');
            option.value = clientes[i].persona.colonia;
            option.text = clientes[i].persona.colonia;
            option.selected = true;
            option.disabled = false;
            select.appendChild(option);
            document.getElementById("txtCodigoPostal").value = clientes[i].persona.cp;
            document.getElementById("txtCiudad").value = clientes[i].persona.ciudad;
            document.getElementById("txtEntidad").value = clientes[i].persona.estado;
            // Datos de contacto
            document.getElementById("txtTelefonoCasa").value = clientes[i].persona.telCasa;
            document.getElementById("txtTelefonoMov").value = clientes[i].persona.telMovil;
            document.getElementById("txtRFC").value = clientes[i].persona.rfc;
            document.getElementById("txtCorreo").value = clientes[i].persona.email;
            // Datos de identificación
            document.getElementById("txtIdCliente").value = clientes[i].idCliente;
            document.getElementById("txtIdPersona").value = clientes[i].persona.idPersona;
            document.getElementById("txtNUC").value = clientes[i].numeroUnico;
        }
    }
    document.getElementById("btnDelete").classList.remove("disabled");
}

export function limpiarCampos() {
    document.getElementById("txtIdCliente").value = "";
    document.getElementById("txtIdPersona").value = "";

    document.getElementById("txtNombre").value = "";
    document.getElementById("txtApellidoPat").value = "";
    document.getElementById("txtApellidoMat").value = "";
    document.getElementById("txtGenero").value = "Escoge una opcion";
    document.getElementById("txtRFC").value = "";
    document.getElementById("txtFechaNac").value = "";
    document.getElementById("txtAnio").value = "Año";
    document.getElementById("txtMes").value = "Mes";
    limpiarFecha();

    document.getElementById("txtCalle").value = "";
    document.getElementById("txtNumDom").value = "";
    let select = document.getElementById("txtColonia");
    let options = select.getElementsByTagName('OPTION');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let option = document.createElement('option');
    option.value = "Selecciona una colonia";
    option.text = "Selecciona una colonia";
    option.selected = true;
    option.disabled = true;
    select.appendChild(option);

    document.getElementById("txtCodigoPostal").value = "";
    document.getElementById("txtCiudad").value = "";
    document.getElementById("txtEntidad").value = "";

    document.getElementById("txtTelefonoCasa").value = "";
    document.getElementById("txtTelefonoMov").value = "";
    document.getElementById("txtCorreo").value = "";

    document.getElementById("txtNUC").value = "";

    document.getElementById("txtNombre").focus();
    document.getElementById("btnDelete").disabled = true;
}

export function asignarDia() {
    limpiarFecha();
    let mes = document.getElementById("txtMes").value.toUpperCase();
    let anio = document.getElementById("txtAnio").value;
    let selectDia = document.getElementById("txtDia");
    if (mes === "01" || mes === "03" || mes === "05" || mes === "07" || mes === "08" || mes === "10" || mes === "12") {
        for (var i = 0; i < 31; i++) {
            let option = document.createElement('option');
            if (i === 0 || i === 1 || i === 2 || i === 3 || i === 4 || i === 5 || i === 6 || i === 7 || i === 8) {
                option.value = "0" + (i + 1);
                option.text = i + 1;
                console.log(option.value);
            } else {
                option.value = i + 1;
                option.text = i + 1;
            }
            selectDia.appendChild(option);
        }
    } else if (mes === "04" || mes === "06" || mes === "09" || mes === "11") {
        for (var i = 0; i < 30; i++) {
            let option = document.createElement('option');
            if (i === 0 || i === 1 || i === 2 || i === 3 || i === 4 || i === 5 || i === 6 || i === 7 || i === 8) {
                option.value = "0" + (i + 1);
                option.text = i + 1;
                console.log(option.value);
            } else {
                option.value = i + 1;
                option.text = i + 1;
            }
            selectDia.appendChild(option);
        }
    } else {
        if (anio === "1932" || anio === "1936" || anio === "1940" || anio === "1944" ||
                anio === "1948" || anio === "1952" || anio === "1956" || anio === "1960" ||
                anio === "1964" || anio === "1968" || anio === "1972" || anio === "1976" ||
                anio === "1980" || anio === "1984" || anio === "1988" || anio === "1992" ||
                anio === "1996" || anio === "2000" || anio === "2004" || anio === "2008" ||
                anio === "2012" || anio === "2016" || anio === "2020") {
            for (var i = 0; i < 29; i++) {
                let option = document.createElement('option');
                if (i === 0 || i === 1 || i === 2 || i === 3 || i === 4 || i === 5 || i === 6 || i === 7 || i === 8) {
                    option.value = "0" + (i + 1);
                    option.text = i + 1;
                    console.log(option.value);
                } else {
                    option.value = i + 1;
                    option.text = i + 1;
                }
                selectDia.appendChild(option);
            }
        } else {
            for (var i = 0; i < 28; i++) {
                let option = document.createElement('option');
                if (i === 0 || i === 1 || i === 2 || i === 3 || i === 4 || i === 5 || i === 6 || i === 7 || i === 8) {
                    option.value = "0" + (i + 1);
                    option.text = i + 1;
                    console.log(option.value);
                } else {
                    option.value = i + 1;
                    option.text = i + 1;
                }
                selectDia.appendChild(option);
            }
        }
    }
}

export function limpiarFecha() {
    let selectDia = document.getElementById("txtDia");
    let options = selectDia.getElementsByTagName('OPTION');
    for (var i = 0; i < options.length; i++) {
        selectDia.removeChild(options[i]);
        i--;
    }
    let option = document.createElement('option');
    option.value = "dia";
    option.text = "Día";
    option.selected = true;
    option.disabled = true;
    selectDia.appendChild(option);
}

export function asignarFecha(dia, mes, anio) {
    limpiarFecha();
    document.getElementById("txtAnio").value = anio;
    document.getElementById("txtMes").value = mes;
    let selectDia = document.getElementById("txtDia");
    let option = document.createElement('option');
    option.value = dia;
    option.text = dia;
    option.selected = true;
    selectDia.appendChild(option);
}

// Validaciones

export function validarvacios() {
    // Datos personales
    let nombre = document.getElementById("txtNombre").value;
    let apePaterno = document.getElementById("txtApellidoPat").value;
    let genero = document.getElementById("txtGenero").value;
    let rfc = document.getElementById("txtRFC").value;
    let dia = document.getElementById("txtDia").value;
    let mes = document.getElementById("txtMes").value;
    let anio = document.getElementById("txtAnio").value;
    // Datos de domicilio
    let calle = document.getElementById("txtCalle").value;
    let numero = document.getElementById("txtNumDom").value;
    let colonia = document.getElementById("txtColonia").value;
    let codigoPostal = document.getElementById("txtCodigoPostal").value;
    let ciudad = document.getElementById("txtCiudad").value;
    let entidad = document.getElementById("txtEntidad").value;
    // Datos de contacto
    let telefonoMovil = document.getElementById("txtTelefonoMov").value;
    let correo = document.getElementById("txtCorreo").value;

    // Bandera
    let valid = true;
    let input = document.formCliente;

    // If datos personales
    if (nombre === "") {
        notificacion(input.txtNombre, "Ingrese un nombre");
        input.txtNombre.focus();
        valid = false;
    }
    if (apePaterno === "") {
        notificacion(input.txtApellidoPat, "Ingrese un apellido paterno");
        input.txtApellidoPat.focus();
        valid = false;
    }
    if (genero === "Escoge una opción") {
        notificacion(input.txtGenero, "Escoga una opción");
        input.txtGenero.focus();
        valid = false;
    }
    if (rfc === "") {
        notificacion(input.txtRFC, "Ingrese una RFC");
        input.txtRFC.focus();
        valid = false;
    }
    if (dia === "Día" || mes === "Mes" || anio === "Año") {
        notificacion(input.txtMes, "Ingrese una fecha de nacimiento");
        input.txtFechaNac.focus();
        valid = false;
    }

    // If datos de domicilio
    if (calle === "") {
        notificacion(input.txtCalle, "Ingrese un nombre de calle");
        input.txtCalle.focus();
        valid = false;
    }
    if (numero === "") {
        notificacion(input.txtNumDom, "Ingrese un número de domicilio");
        input.txtNumDom.focus();
        valid = false;
    }
    if (colonia === "Selecciona una colonia") {
        notificacion(input.txtColonia, "Ingrese una colonia");
        input.txtColonia.focus();
        valid = false;
    }
    if (codigoPostal === "") {
        notificacion(input.txtCodigoPostal, "Ingrese un código postal");
        input.txtCodigoPostal.focus();
        valid = false;
    }
    if (ciudad === "") {
        notificacion(input.txtCiudad, "Ingrese una ciudad");
        input.txtCiudad.focus();
        valid = false;
    }
    if (entidad === "") {
        notificacion(input.txtEntidad, "Ingrese un estado");
        input.txtEntidad.focus();
        valid = false;
    }

    // If datos de contacto
    if (telefonoMovil === "") {
        notificacion(input.txtTelefonoMov, "Ingrese un número de teléfono");
        input.txtTelefonoMov.focus();
        valid = false;
    }
    if (correo === "") {
        notificacion(input.txtCorreo, "Ingrese un correo");
        input.txtCorreo.focus();
        valid = false;
    }
    return valid;
}

export function validarRFC() {
    let rfc = document.getElementById("txtRFC").value;
    let input = document.formCliente;
    let valid = true;
    if (rfc.length !== 13) {
        notificacion(input.txtRFC, "Ingresa una RFC valida (13 dígitos)");
        input.txtRFC.focus();
        valid = false;
    }
    return valid;
}

export function validarTelefonoMovil() {
    let telefonoMovil = document.getElementById("txtTelefonoMov").value;
    let input = document.formCliente;
    let valid = true;
    if (isNaN(telefonoMovil)) {
        notificacion(input.txtTelefonoMov, "Ingresa un número de teléfono válido");
        input.txtTelefonoMov.focus();
        valid = false;
    }
    return valid;
}

export function validarTelefonoCasa() {
    let telefonoCasa = document.getElementById("txtTelefonoCasa").value;
    let input = document.formCliente;
    let valid = true;
    if (telefonoCasa !== "") {
        if (isNaN(telefonoCasa)) {
            notificacion(input.txtTelefonoCasa, "Ingresa un número de teléfono válido");
            input.txtTelefonoCasa.focus();
            valid = false;
        }
    }
    return valid;
}

export function validarLongitudTelefono() {
    let telefonoMovil = document.getElementById("txtTelefonoMov").value;
    let telefonoCasa = document.getElementById("txtTelefonoCasa").value;
    let input = document.formCliente;
    let valid = true;
    if (telefonoMovil.length !== 10) {
        notificacion(input.txtTelefonoMov, "Ingresa un número de teléfono válido: 10 dígitos");
        input.txtTelefonoMov.focus();
        valid = false;
    }
    if (telefonoCasa !== "") {
        if (telefonoCasa.length !== 10) {
            notificacion(input.txtTelefonoCasa, "Ingresa un número de teléfono válido: 10 dígitos");
            input.txtTelefonoCasa.focus();
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

// Servicio api rest copomex mailBox

export function obtenerDatosDomicilio() {
    const token_glasscode6 = "45a670f1-500e-460d-869d-17314d297e40";
    //const token = "11ef7121-0baa-4ee9-8499-eb1c8c3d2f57";
    /*const token_glasscode6 = "45a670f1-500e-460d-869d-17314d297e40";
     const token_glasscode7 = "1732fdc8-6159-4679-8c46-792dfd94908f";
     const token_chilisquis = "ea71b1f5-41f9-4862-8f77-522d32784254";
     const token_glasscode8 = "39d9ac49-5774-4a3a-98d7-c431412cafec";
     const token_glasscode9 = "f68f5b61-1ac4-43ee-83eb-31726b60c2f6";
     const token_glasscode11 = "3ea0351d-ecfc-421d-a0c6-c40b6387fca7";
     const token_glasscode10 = "f2c9fd82-318c-4839-981f-641b944354dd";
     const token_glasscode4 = "be5c8d47-9403-4deb-8ca0-a1efb0330dd1";
     const token_glasscode6 = "45a670f1-500e-460d-869d-17314d297e40";
     const token_glasscode7 = "1732fdc8-6159-4679-8c46-792dfd94908f";
     const token_chilisquis = "ea71b1f5-41f9-4862-8f77-522d32784254";
     const token_glasscode8 = "39d9ac49-5774-4a3a-98d7-c431412cafec";
     const token_glasscode9 = "f68f5b61-1ac4-43ee-83eb-31726b60c2f6";*/
    let select = document.getElementById("txtColonia");
    let options = select.getElementsByTagName('OPTION');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let option = document.createElement('option');
    option.value = "Selecciona una colonia";
    option.text = "Selecciona una colonia";
    option.selected = true;
    option.disabled = true;
    select.appendChild(option);
    let input = document.formCliente;
    let codigoPostal = document.getElementById("txtCodigoPostal").value;
    if (codigoPostal !== "") {
        if (codigoPostal.length < 5) {
            notificacion(input.txtCodigoPostal, "Ingresa un codigo postal de 5 dígitos");
            document.getElementById("txtCodigoPostal").value = "";
        } else {
            if (isNaN(codigoPostal)) {
                document.getElementById("txtCodigoPostal").value = "";
                notificacion(input.txtCodigoPostal, "Ingresa un codigo postal válido");
            } else {
                codigoPostal = parseInt(codigoPostal);
                let url = 'https://api.copomex.com/query/info_cp/' + codigoPostal + '?type=simplified&token=' + token_glasscode6;
                fetch(url)
                        .then(datosNaturales => {
                            return datosNaturales.json();
                        })
                        .then(datos => {
                            let entidad = datos.response["estado"];
                            let ciudad = datos.response["municipio"];
                            document.getElementById("txtEntidad").value = entidad;
                            document.getElementById("txtCiudad").value = ciudad;
                            let colonias = datos.response["asentamiento"];
                            let select = document.getElementById("txtColonia");
                            colonias.forEach(colonia => {
                                let opt = document.createElement('option');
                                opt.value = colonia;
                                opt.text = colonia;
                                select.appendChild(opt);
                            });
                        });
            }
        }
    } else {
        notificacion(input.txtCodigoPostal, "Ingresa un codigo postal");
    }
}

export function validarCorreo() {
    const acces_key = "b63aeffb0c8f45d1b06c3a721fec1669";
    let correo = document.getElementById("txtCorreo").value;
    let url = "";
    let input = document.formCliente;
    if (correo !== "") {
        url = "https://emailvalidation.abstractapi.com/v1/?api_key=" + acces_key + "&email=" + correo;
        fetch(url)
                .then(datosNaturales => {
                    return datosNaturales.json();
                })
                .then(datos => {
                    let correo = datos.email;
                    let formatoValido = datos.is_valid_format["value"];
                    let encotrado = datos.is_mx_found["value"];
                    let smtp = datos.is_smtp_valid["value"];
                    if (formatoValido === true) {
                        notificacion(input.txtCorreo, "Correo válido");
                    } else {
                        document.getElementById("txtCorreo").value = "";
                        notificacion(input.txtCorreo, "Correo inválido");
                    }
                });
    } else {
        notificacion(input.txtCorreo, "Ingresa un correo");
    }
}