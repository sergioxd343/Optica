let armazonSeleccionado;
let DetalleVentaPresupuestoLentes = [];
let clientes;
let tiposMica;
let materiales;
let tratamientos;
let examenesVista;
let clienteSeleccionado;
let tratamientosSeleccionados = [];
let armazones;
const header = document.getElementById("headerLentesDeContacto");
console.log(header);
const tbl = document.getElementById('tablaClientes');
console.log(tbl);
console.log("Hola");
export function inicializar() {
    buscarCliente();
    buscarArmazon();
    buscarMaterial();
    buscarTipoMica();
    buscarTratamientos();
    mostrarCampoBusquedaClientes();
    document.getElementById("btnClean").disabled = true;
    document.getElementById("btnSave").disabled = true;

    document.getElementById("btnCleanPresupuesto").disabled = true;
    document.getElementById("btnSavePresupuesto").disabled = true;
    configureTableFilter(document.getElementById('txtBusquedaClientes'),
            tbl);
}

export function mostrarCampoBusquedaClientes()
{
    let busqueda = document.getElementById("txtBusquedaClientes");
    if (busqueda.style.display != "none") {
        busqueda.style.display = "none";
        document.getElementById("btnBuscarClientes").innerHTML = "<i class='fa-solid fa-magnifying-glass'></i>Buscar";
    } else {
        busqueda.style.display = "block";
        document.getElementById("btnBuscarClientes").innerHTML = "<i class='fa-solid fa-magnifying-glass'></i>Cerrar";
    }
}

export function setDetalleVisible() {
    const form = document.getElementById("areaContenido");
    let tbl = document.getElementById("tblPresupuestoLentes");
    if (form.style.display == "none") {
        form.style.display = "block";
        document.getElementById("letreroFormulario").innerHTML = "Cerrar.";
        tbl.style.display = "none";
    } else {
        form.style.display = "none";
        limpiarCamposLC();
        document.getElementById("letreroFormulario").innerHTML = "Agregar.";
        tbl.style.display = "block";
    }
}

export function buscarCliente()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    let filtro = document.getElementById("txtBusquedaClientes").value;
    datos = {
        filtro: filtro,
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
                if (data.errorperm != null) {
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
                console.log("cargando clientes...");
                console.log(data);
                cargarClientes(data);
            });
}

export function cargarClientes(data)
{
    let contenido = "";
    clientes = data;
    clientes.forEach(function (cliente, index) {
        let registro =
                "<tr>" +
                "<td>" + cliente.numeroUnico + "</td>" +
                "<td>" + cliente.persona.nombre + "</td>" +
                "<td>" + cliente.persona.apellidoPaterno + "</td>" +
                "<td>" + cliente.persona.apellidoMaterno + "</td>" +
                "<td>" + cliente.persona.telMovil + "</td>" +
                "<td>" + cliente.persona.email + "</td>" +
                "<td><a onclick='moduloVentaPresupuestoLentes.seleccionarCliente(" + index + ");'><i class='fa fa-plus-square'></i></a></td></tr>";
        contenido += registro;
    });
    document.getElementById("tblClientes").innerHTML = contenido;
}

export function cargarClienteSeleccionado()
{
    let registro =
            "<tr>" +
            "<td>" + clienteSeleccionado.numeroUnico + "</td>" +
            "<td>" + clienteSeleccionado.persona.nombre + "</td>" +
            "<td>" + clienteSeleccionado.persona.apellidoPaterno + "</td>" +
            "<td>" + clienteSeleccionado.persona.apellidoMaterno + "</td>" +
            "<td>" + clienteSeleccionado.persona.telMovil + "</td>" +
            "<td>" + clienteSeleccionado.persona.email + "</td>";
    document.getElementById("tblClienteSeleccionado").innerHTML = registro;
}

export function seleccionarCliente(index)
{
    if (clienteSeleccionado != null) {
        Swal.fire({
            title: 'Cliente ya seleccionado',
            text: "¿Quieres cambiar al cliente?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí, cambiarlo',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire(
                        'Seleccionado',
                        'El cliente se ha seleccionado',
                        'success'
                        );
                clienteSeleccionado = clientes[index];
                console.log(clienteSeleccionado);
                buscarEV();
                cargarClienteSeleccionado();
            }
        });
    } else
    {
        Swal.fire(
                'Seleccionado',
                'El cliente se ha seleccionado',
                'success'
                );
        clienteSeleccionado = clientes[index];
        console.log(clienteSeleccionado);
        buscarEV();
        cargarClienteSeleccionado();
    }
}

export function buscarEV()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let filtro = clienteSeleccionado.idCliente;
    let rol = localStorage.getItem("rol");
    datos = {
        filtro: filtro,
        token: lastToken
    };
    console.log(datos);
    param = new URLSearchParams(datos);
    console.log(param);
    fetch("api/examenvista/getAll",
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
                if (data.errorperm != null) {
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
                console.log("cargando examenes vista...");
                console.log(data);
                cargarEV(data);
            });
}

export function cargarEV(data)
{
    examenesVista = data;
    console.log(examenesVista);
    let select = document.getElementById("selectEV");
    let options = select.getElementsByTagName('option');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let contenido = "<option value='seleccionarExamen' selected='' disabled=''>Selecciona el examen de vista</option>";
    examenesVista.forEach(function (ev, index) {
        let registro = "<option value='" + index + "'>" + ev.clave + "</option>";
        contenido += registro;
    });
    select.innerHTML = contenido;
}

export function buscarTipoMica()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    let datos;
    param = new URLSearchParams(datos);
    console.log(param);
    fetch("api/mica/getAll",
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
                if (data.errorperm != null) {
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
                console.log("cargando examenes vista...");
                console.log(data);
                cargarTipoMica(data);
            });
}

export function cargarTipoMica(data)
{
    tiposMica = data;
    console.log(tiposMica);
    let select = document.getElementById("selectMica");
    let options = select.getElementsByTagName('option');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let contenido = "<option value='seleccionarMica' selected='' disabled=''>Selecciona el tipo de mica</option>";
    tiposMica.forEach(function (tm, index) {
        let registro = "<option value='" + index + "'>" + tm.nombre + "</option>";
        contenido += registro;
    });
    select.innerHTML = contenido;
}

export function buscarTratamientos()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    let datos;
    param = new URLSearchParams(datos);
    console.log(param);
    fetch("api/tratamiento/getAll",
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
                if (data.errorperm != null) {
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
                console.log("cargando tratamientos...");
                console.log(data);
                cargarTratamientos(data);
            });
}

export function cargarTratamientos(data)
{
    tratamientos = data;
    console.log(tratamientos);
    let div = document.getElementById("divTratamientos");
    let contenido = "<div class='row'>";
    tratamientos.forEach(function (tt, index) {
        
        let registro =   "<div class='col-sm-3 elementoForm'>"
                        +"<input type='checkbox' class='form-check-input chbTratamiento' oncheck='moduloVentaPresupuestoLentes.actualizarBotonesPresupuesto();' value='"+index+"' id='chb"+tt.nombre+"'>"
                        +"<label class='form-check-label' for='chbTratamiento"+index+"' class='form-label'> "+ tt.nombre + "</label>"
                        +"</div>";
        
        contenido += registro;
    });
    contenido += "</div>";
    div.innerHTML = contenido;
}

export function buscarMaterial()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    let datos;
    param = new URLSearchParams(datos);
    console.log(param);
    fetch("api/material/getAll",
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
                if (data.errorperm != null) {
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
                console.log("cargando examenes vista...");
                console.log(data);
                cargarMaterial(data);
            });
}

export function cargarMaterial(data)
{
    materiales = data;
    console.log(tiposMica);
    let select = document.getElementById("selectMaterial");
    let options = select.getElementsByTagName('option');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let contenido = "<option value='seleccionarMaterial' selected='' disabled=''>Selecciona el material</option>";
    materiales.forEach(function (material, index) {
        let registro = "<option value='" + index + "'>" + material.nombre + "</option>";
        contenido += registro;
    });
    select.innerHTML = contenido;
}

export function buscarArmazon()
{
    let param = null;
    let lastToken = localStorage.getItem("lastToken");
    let rol = localStorage.getItem("rol");
    datos = {
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
                if (data.errorperm != null) {
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
                console.log("cargando armazones...");
                console.log(data);
                cargarArmazones(data);
            });
}

export function cargarArmazones(data)
{
    armazones = data;
    let select = document.getElementById("selectArmazon");
    let options = select.getElementsByTagName('option');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let contenido = "<option value='seleccionarArmazon' selected='' disabled=''>Selecciona el armazon</option>";
    armazones.forEach(function (armazon, index) {
        let registro = "<option value='" + index + "'>" + armazon.producto.nombre + "</option>";
        contenido += registro;
    });
    select.innerHTML = contenido;
}

export function agregarVentaPresupuestoLentes()
{

    if (validarDescuento() && validarExistencias()) {
        let datos = null;
        let param = null;
        //let url = "api/empleado/getAll?token=" + currentUser.usuario.lastToken;}
        let empleadoVenta = JSON.parse(localStorage.getItem("empleado"));
        console.log(empleadoVenta);
        let venta = {
            idVenta: null,
            empleado: empleadoVenta,
            clave: ""
        };
        let ventaProducto = [];
        armazones.forEach(function (producto, index)
        {
            let ventaProduct = new Object();
            ventaProduct.producto = producto;
            ventaProduct.precioUnitario = producto.precioVenta;
            ventaProduct.cantidad = document.getElementById("cantidadProducto" + index + "").value;
            ventaProduct.descuento = document.getElementById("descuentoProducto" + index + "").value;
            ventaProducto.push(ventaProduct);
        });
        let datosVP =
                {
                    venta: venta,
                    listaProductos: ventaProducto
                };
        let lastToken = localStorage.getItem("lastToken");
        datos = {
            datosVP: JSON.stringify(datosVP),
            token: lastToken
        };
        param = new URLSearchParams(datos);
        console.log(datos);
        console.log(param);
        fetch("api/venta_producto/createVentaP",
                {
                    method: "POST",
                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body: param
                })
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
                        limpiarCampos();
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
                    if (data.response != null) {
                        Swal.fire('Realizado', data.response, 'success');
                    }
                    //alert('cargando tabla despues del fetch...');
                    buscarEmpleado();
                    limpiar();
                    actualizarTablaVenta();
                    actualizarBotones();
                    armazones.splice(0, armazones.length);
                });
    }
}

export function eliminarPresupuesto(index) { //seleccionarProducto
    DetalleVentaPresupuestoLentes.splice(index, 1);
    console.log("Checar");
    console.log(DetalleVentaPresupuestoLentes);
    actualizarTablaLC();
}

export function guardarPresupuesto()
{
    let alturaOblea = document.getElementById("txtAlturaOblea").value;
    let tipoMica = tiposMica[document.getElementById("selectMica").value];
    let listaTratamientos = document.getElementsByName("chbTratamiento");
    for (var tratamiento in listaTratamientos) {
        if (tratamiento.checked) {
            tratamientosSeleccionados.push(tratamientos[tratamiento.value]);        }
    }
    let material = materiales[document.getElementById("selectMaterial").value];
    
    let presupuesto =
            {
                idPresupuesto: 0,
                clave: "",
                examenVista: examenesVista[document.getElementById("selectEV").value]
            };
    let presupuestoLentes =
            {
                idPresupuestoLentes: 0,
                tipoMica: tipoMica,
                material: material,
                tratamientos: tratamientosSeleccionados,
                alturaOblea: alturaOblea,
                armazon: armazonSeleccionado,
                presupuesto: presupuesto
            };
    DetalleVentaPresupuestoLentes.push(presupuestoLentes);
    actualizarTablaPresupuesto();
    limpiarCamposPresupuesto();

}

export function actualizarTablaPresupuesto()
{
    let contenido = "";
    if (DetalleVentaPresupuestoLentes[0] != null) {
        DetalleVentaPresupuestoLentes.forEach(function (presupuesto, index) {
            let registro =
                    "<tr>" +
                    "<td>" + presupuesto.armazon.producto.nombre + "</td>" +
                    "<td>" + presupuesto.tipoMica.nombre + "</td>" +
                    "<td> $" + presupuesto.producto.precioVenta + "</td>" +
                    "<td> <input type='number' id='cantidadPresupuesto" + index + "' value='1' name='cantidadPresupuesto" + index + "' oninput='moduloVentaPresupuestoLentes.actualizarTotalVenta();'  min='1' max='" + presupuesto.producto.existencias + "'></td>" +
                    "<td> <input type='number' id='descuentoPresupuesto" + index + "' value='0' name='descuentoPresupuesto" + index + "' oninput='moduloVentaPresupuestoLentes.actualizarTotalVenta();' max='100'</td>" +
                    "<td><a onclick='moduloVentaPresupuestoLentes.eliminarPresupuesto(" + index + ");'><i class='fa-sharp fa-solid fa-trash'></i></a></td></tr>";
            contenido += registro;
        });
        console.log(armazones);
        document.getElementById("tblVentaPresupuestoLentes").innerHTML = contenido;
        document.getElementById("totalVenta").style.display = "block";
        actualizarBotones();
        actualizarTotalVenta();
        setDetalleVisible(false);
    } else
    {
        document.getElementById("totalVenta").style.display = "none";
        setDetalleVisible(false);
    }

}

export function limpiarCamposPresupuesto()
{
    document.getElementById("selectArmazon").value = "seleccionarArmazon";
    document.getElementById("selectMaterial").value = "seleccionarMaterial";
    document.getElementById("selectMica").value = "seleccionarMica";
    let tratamientos = document.getElementsByClassName("chbTratamiento");
    console.log(tratamientos);
    for (var tratamiento in tratamientos) {
        if (tratamiento.checked) {
           tratamiento.checked = false; 
        }
    }
    
    document.getElementById("txtAlturaOblea").value = 0;

    document.getElementById("btnSavePresupuesto").disabled = true;
    document.getElementById("btnCleanPresupuesto").disabled = true;
}

export function limpiar()
{
    console.log(armazones);
    armazones.splice(0, armazones.length);
    actualizarTablaVenta();
    clienteSeleccionado = null;
    cargarClienteSeleccionado();
    refrescarTabla();
    buscarCliente();
    actualizarBotones();
    actualizarTotalVenta();
}

/**
 * Habilita y deshabilita los botones de Guardar y Limpiar segun si se selecciono
 * @returns {undefined}
 */
export function actualizarBotones()
{
    if (clienteSeleccionado != null || DetalleVentaPresupuestoLentes[0] != null || document.getElementById("selectEV").value != "seleccionaExamen")
    {
        document.getElementById("btnClean").disabled = false;
        if (clienteSeleccionado != null && armazones[0] != null && document.getElementById("selectEV").value != "seleccionaExamen") {
            document.getElementById("btnSave").disabled = false;
        }
    } else
    {
        document.getElementById("btnClean").disabled = true;
        document.getElementById("btnSave").disabled = true;
    }
}

export function actualizarBotonesPresupuesto()
{
    let materialSeleccionada = document.getElementById("selectMaterial").value;
    let armazonSelected = document.getElementById("selectArmazon").value;
    let micaSeleccionada = document.getElementById("selectMica").value;
    let alturaOblea = parseInt(document.getElementById("txtAlturaOblea").value);
    let tratamientos = document.getElementsByClassName("chbTratamiento");
    let selected;
    for (var tratamiento in tratamientos) {
        if(tratamiento.checked){
            selected = true;
            return;
        }
    }
    if (materialSeleccionada != null || armazonSelected != null || micaSeleccionada != null || selected)
    {
        document.getElementById("btnCleanPresupuesto").disabled = false;
        if (materialSeleccionada != null && armazonSelected != null && micaSeleccionada != null && clienteSeleccionado != null && document.getElementById("selectEV").value != "seleccionaExamen" && selected) {
            document.getElementById("btnSavePresupuesto").disabled = false;
        }
    } else
    {
        document.getElementById("btnCleanPresupuesto").disabled = true;
        document.getElementById("btnSavePresupuesto").disabled = true;
    }
}
/**
 * Agrega a la tabla de detalles de la compra un renglon, segun el producto que se haya elegido de la tabla
 * de productos
 * @param {type} indice
 * @returns {undefined}
 */
/**
 * Actualiza la tabla de los detalles de la compra, segun lo que se haya agregado de los productos existentes y activos
 * registrados en la optica
 * @returns {undefined}
 */

export function actualizarTotalVenta()
{
    if (validarDescuento() && validarExistencias()) {
        let totalVenta = 0;
        let subtotalVenta = 0;
        let descuento = 0;
        let totalLC = 0;
        let porcentaje;
        armazones.forEach(function (lc, index) {
            porcentaje = 0;
            totalLC += parseInt(document.getElementById("cantidadLC" + index + "").value);
            subtotalVenta += (lc.producto.precioVenta * document.getElementById("cantidadLC" + index + "").value);
            if (document.getElementById("descuentoLC" + index).value != 0) {
                porcentaje = parseFloat((document.getElementById("descuentoLC" + index).value)/100);
            }
            porcentaje = parseFloat((document.getElementById("descuentoLC" + index).value)/100);
            console.log(porcentaje);
            if (porcentaje != 0) {
                descuento += (lc.producto.precioVenta * document.getElementById("cantidadLC" + index + "").value) * porcentaje;
            }
            
        });
        totalVenta += subtotalVenta - descuento;
        console.log(totalVenta);
        document.getElementById("totalLC").value = totalLC;
        document.getElementById("subtotalCosto").value = "$ " + subtotalVenta;
        document.getElementById("descuentoCosto").value = descuento;
        document.getElementById("totalCosto").value = "$ " + totalVenta;
        console.log(totalVenta);
    } else {
        document.getElementById("totalLC").value = "Checa los valores de la tabla";
        document.getElementById("subtotalCosto").value = "Checa los valores de la tabla";
        document.getElementById("descuentoCosto").value = "Checa los valores de la tabla";
        document.getElementById("totalCosto").value = "Checa los valores de la tabla";
        document.getElementById("totalCosto").value = "Checa los valores de la tabla";
    }

}

/*export function refrescarTabla() {
 let datos = null;
 let param = null;
 //let url = "api/empleado/getAll?token=" + currentUser.usuario.lastToken;}
 let filtro = document.getElementById("txtBusqueda").value;
 let lastToken = localStorage.getItem("lastToken");
 datos = {
 filtro: filtro,
 token: lastToken
 };
 param = new URLSearchParams(datos);
 fetch("api/venta_producto/getAll",
 {
 method: "POST",
 headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
 body: param
 })
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
 
 export function actualizarTotalVenta()
 {
 if (validarDescuento() && validarExistencias()) {
 let totalVenta = 0;
 let subtotalVenta = 0;
 let descuento = 0;
 let totalPresupuesto = 0;
 lentesDeContactoEnCompra.forEach(function (lc, index) {
 totalPresupuesto += parseInt(document.getElementById("cantidadLC" + index + "").value);
 subtotalVenta += (lc.precioVenta * document.getElementById("cantidadLC" + index + "").value);
 descuento += parseFloat(document.getElementById("descuentoLC" + index + "").value);
 });
 totalVenta += subtotalVenta - descuento;
 console.log(totalVenta);
 document.getElementById("totalPresupuesto").value = totalPresupuesto;
 document.getElementById("subtotalCosto").value = "$ " + subtotalVenta;
 document.getElementById("descuentoCosto").value = "$ " + descuento;
 document.getElementById("totalCosto").value = "$ " + totalVenta;
 
 } else {
 
 console.log(totalVenta);
 document.getElementById("totalCosto").value = "Checa los valores de la tabla";
 }
 
 }
 
 export function cargarTabla(data) { // Carga en la tabla los datos que se envien
 let contenido = "";
 lentesDeContacto = data;
 lentesDeContacto.forEach(function (lc, index) {
 let registro =
 "<tr>" +
 "<td>" + lc.codigoBarras + "</td>" +
 "<td>" + lc.nombre + "</td>" +
 "<td> $" + lc.precioVenta + "</td>" +
 "<td>" + lc.existencias + "</td>" +
 "<td><a onclick='moduloVentaProductos.seleccionarProducto(" + index + ");'><i class='fa fa-plus-square'></i></a></td></tr>";
 contenido += registro;
 });
 console.log(productos);
 document.getElementById("tblProductos").innerHTML = contenido;
 }
 */

export function validarExistencias() {
    let valid = true;
    armazones.forEach(function (lc, index) {
        let id = document.getElementById("cantidadLC" + index + "");
        let existencias = parseInt(id.value);
        if (!Number.isInteger(existencias)) {
            notificacion(id, "Ingrese una cantidad válida");
            id.focus();
            valid = false;
        } else
        if (existencias > lc.producto.existencias) {
            notificacion(id, "La cantidad supera a las existencias");
            id.focus();
            valid = false;
        }

    });
    return valid;
}


export function validarDescuento() {
    let valid = true;
    DetalleVentaPresupuestoLentes.forEach(function (presupuesto, index) {
        let id = document.getElementById("descuentoPresupuesto" + index);
        let descuento = parseFloat(id.value);
        let total = presupuesto.armazon.producto.precioVenta + presupuesto.tipoMica.precioVenta + presupuesto.material.precioVenta;
        
        if (Number.isNaN(descuento)) {
            notificacion(id, "Ingrese una cantidad válida");
            id.focus();
            valid = false;
        } else
        if (descuento > ((presupuesto.armazon.producto.precioVenta * document.getElementById("cantidadLC" + index).value) / 2)) {
            notificacion(id, "El descuento supera al maximo descuento (50%)");
            id.focus();
            valid = false;
        }
    });
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