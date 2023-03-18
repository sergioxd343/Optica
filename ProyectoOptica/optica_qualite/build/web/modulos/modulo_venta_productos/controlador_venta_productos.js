let productoSeleccionado;
let productosEnCompra = [];
let productos;
let indexVenta = 0;
let empleado;
const header = document.getElementById("headerProductos");
console.log(header);
const tbl = document.getElementById('tablaProductos');
console.log(tbl);
export function inicializar() {
    refrescarTabla();
    setDetalleVisible(false);
    cargarEmpleado();
    mostrarCampoBusqueda();
    document.getElementById("btnClean").disabled = true;
    document.getElementById("btnSave").disabled = true;
    configureTableFilter(document.getElementById('txtBusqueda'),
            tbl);
}

export function mostrarCampoBusqueda()
{
    let busqueda = document.getElementById("txtBusqueda");
    if (busqueda.style.display != "none") {
        busqueda.style.display = "none";
        document.getElementById("btnBuscar").innerHTML = "<i class='fa-solid fa-magnifying-glass'></i>Buscar";
    } else {
        busqueda.style.display = "block";
        document.getElementById("btnBuscar").innerHTML = "<i class='fa-solid fa-magnifying-glass'></i>Cerrar";
    }
}

export function setDetalleVisible(valor) {
    const tblVenta = document.getElementById("ventaProductos");
    if (valor) {
        tblVenta.style.display = "block";
    } else {
        tblVenta.style.display = "none";
    }
}

export function cargarEmpleado()
{
    console.log(localStorage.getItem("empleado"));
    empleado = JSON.parse(localStorage.getItem("empleado"));
    console.log(empleado.idEmpleado);
    let select = document.getElementById("selectEmpleado");
    let options = select.getElementsByTagName('option');
    for (var i = 0; i < options.length; i++) {
        select.removeChild(options[i]);
        i--;
    }
    let contenido = "<option selected value='" + empleado.idEmpleado + "'>" + empleado.persona.nombre + "</option>";
    select.innerHTML = contenido;
}

export function seleccionarProducto(index) { //seleccionarProducto
    productoSeleccionado = productos[index];
    let enCarrito = false;
    productosEnCompra.forEach(function (producto, index) {
        if (producto == productoSeleccionado) {
            enCarrito = true;
            return;
        }
    });
    if (enCarrito) {
        Swal.fire('', 'Producto ya en carrito.', 'warning');
        setDetalleVisible(true);
        actualizarTablaVenta();
    } else {
        productosEnCompra.push(productoSeleccionado);
        setDetalleVisible(true);
        actualizarTablaVenta();
    }

}

export function agregarCompra()
{

    if (validarDescuento() && validarExistencias()) {
        let datos = null;
        let param = null;
        //let url = "api/empleado/getAll?token=" + currentUser.usuario.lastToken;}
        let venta = {
            idVenta: null,
            empleado: empleado,
            clave: ""
        };
        let ventaProducto = [];
        productosEnCompra.forEach(function (producto, index)
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
                        limpiar();
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
                    limpiar();
                    actualizarTablaVenta();
                    actualizarBotones();
                    productosEnCompra.splice(0, productosEnCompra.length);
                });
    }
}

export function eliminarProducto(index) { //seleccionarProducto
    productosEnCompra.splice(index, 1);
    console.log("Checar");
    let cantidad = [];
    let descuento = [];
    productosEnCompra.forEach(function (producto, index) {
        cantidad.push(document.getElementById("cantidadProducto" + index).value);
        descuento.push(document.getElementById("descuentoProducto" + index).value);
    });
    console.log(productosEnCompra);
    actualizarTablaVenta();
    productosEnCompra.forEach(function (producto, index) {
        document.getElementById("cantidadProducto" + index).value = cantidad[index];
        document.getElementById("descuentoProducto" + index).value = descuento[index];
    });
}

export function limpiar()
{
    console.log(productosEnCompra);
    productosEnCompra.splice(0, productosEnCompra.length);
    actualizarTablaVenta();
    refrescarTabla();
    
    actualizarBotones();
    actualizarTotalVenta();
}

/**
 * Habilita y deshabilita los botones de Guardar y Limpiar segun si se selecciono
 * @returns {undefined}
 */
export function actualizarBotones()
{
    if (document.getElementById("selectEmpleado").value >= 1 || productosEnCompra[0] != null)
    {
        document.getElementById("btnClean").disabled = false;
        if (document.getElementById("selectEmpleado").value >= 1 && productosEnCompra[0] != null) {
            document.getElementById("totalVenta").style.display = "block";
            document.getElementById("btnSave").disabled = false;
        }
    } else
    {
        document.getElementById("totalVenta").style.display = "none";
        document.getElementById("btnClean").disabled = true;
        document.getElementById("btnSave").disabled = true;
    }
}
/**
 * Agrega a la tabla de detalles de la compra un renglon, segun el producto que se haya elegido de la tabla
 * de productos
 * @param {type} indice
 * @returns {undefined}
 */
export function agregar(indice)
{
    let renglon =
            "<tr>" +
            "<td>" + producto.codigoBarras + "</td>" +
            "<td>" + producto.nombre + "</td>" +
            "<td>" + producto.precioVenta + "</td>" +
            "<td> <input type='number' id='cantidadProducto" + indexVenta + "' value='1' name='cantidadProducto" + index + "' min='1' max='" + producto.existencias + "'></td>" +
            "<td> <input type = 'number' id='descuentoProducto" + indexVenta + "' value='0' name='descuentoProducto" + index + "'></td>" +
            "<td><a onclick='moduloVentaProductos.eliminarProducto(" + indexVenta + ");'><i class='fa-sharp fa-solid fa-trash'></i></a></td></tr>";
}
/**
 * Actualiza la tabla de los detalles de la compra, segun lo que se haya agregado de los productos existentes y activos
 * registrados en la optica
 * @returns {undefined}
 */
export function actualizarTablaVenta()
{
    let contenido = "";
    if (productosEnCompra[0] != null) {
        productosEnCompra.forEach(function (producto, index) {
            let registro =
                    "<tr>" +
                    "<td>" + producto.codigoBarras + "</td>" +
                    "<td>" + producto.nombre + "</td>" +
                    "<td> $" + producto.precioVenta + "</td>" +
                    "<td> <input type='number' id='cantidadProducto" + index + "' value='1' name='cantidadProducto" + index + "' oninput='moduloVentaProductos.actualizarTotalVenta();'  min='1' max='" + producto.existencias + "'></td>" +
                    "<td> <input type = 'number' id='descuentoProducto" + index + "' value='0' name='descuentoProducto" + index + "' oninput='moduloVentaProductos.actualizarTotalVenta();'></td>" +
                    "<td><a onclick='moduloVentaProductos.eliminarProducto(" + index + ");'><i class='fa-sharp fa-solid fa-trash'></i></a></td></tr>";
            contenido += registro;
        });
        console.log(productosEnCompra);
        document.getElementById("tblVentaProductos").innerHTML = contenido;
        document.getElementById("totalVenta").style.display = "block";
        actualizarBotones();
        actualizarTotalVenta();
        setDetalleVisible(true);
    } else
    {
        document.getElementById("totalVenta").style.display = "none";
        setDetalleVisible(false);
    }

}

export function refrescarTabla() {
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
        let totalProductos = 0;
        productosEnCompra.forEach(function (producto, index) {
            totalProductos += parseInt(document.getElementById("cantidadProducto" + index + "").value);
            subtotalVenta += (producto.precioVenta * document.getElementById("cantidadProducto" + index + "").value);
            descuento += parseFloat(document.getElementById("descuentoProducto" + index + "").value);
        });
        totalVenta += subtotalVenta - descuento;
        console.log(totalVenta);
        document.getElementById("totalProductos").value = totalProductos;
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
    productos = data;
    productos.forEach(function (producto, index) {
        let registro =
                "<tr>" +
                "<td>" + producto.codigoBarras + "</td>" +
                "<td>" + producto.nombre + "</td>" +
                "<td> $" + producto.precioVenta + "</td>" +
                "<td>" + producto.existencias + "</td>" +
                "<td><a onclick='moduloVentaProductos.seleccionarProducto(" + index + ");'><i class='fa fa-plus-square'></i></a></td></tr>";
        contenido += registro;
    });
    console.log(productos);
    document.getElementById("tblProductos").innerHTML = contenido;
}

export function validarExistencias() {
    let valid = true;
    productosEnCompra.forEach(function (producto, index) {
        let id = document.getElementById("cantidadProducto" + index + "");
        let existencias = parseInt(id.value);
        if (!Number.isInteger(existencias)) {
            notificacion(id, "Ingrese una cantidad válida");
            id.focus();
            valid = false;
        } else
        if (existencias > producto.existencias) {
            notificacion(id, "La cantidad supera a las existencias");
            id.focus();
            valid = false;
        }

    });
    return valid;
}


export function validarDescuento() {
    let valid = true;
    productosEnCompra.forEach(function (producto, index) {
        let id = document.getElementById("descuentoProducto" + index + "");
        let descuento = parseFloat(id.value);
        if (Number.isNaN(descuento)) {
            notificacion(id, "Ingrese una cantidad válida");
            id.focus();
            valid = false;
        } else
        if (descuento > ((producto.precioVenta*document.getElementById("cantidadProducto" + index).value)/2)) {
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