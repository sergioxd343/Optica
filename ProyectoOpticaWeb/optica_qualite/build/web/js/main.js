let moduloEmpleados = null;
let moduloClientes = null;
let moduloArmazones = null;
let moduloAccesorios = null;
let moduloSoluciones = null;
let moduloLentesContacto = null;
let moduloTratamientos = null;
let moduloMateriales = null;
let moduloTipoMicas = null;
let moduloVentaProductos = null;
let moduloVentaLenteContacto=null;
let home = null;

// Cargar página de Inicio
function cargarPrincipal() {
    fetch("home/vista_principal.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById("layoutSidenav_content").innerHTML = html;
            }
            );
}

// Cargar página de Clientes
function cargarModuloClientes() {
    fetch("modulos/modulo_cliente/vista_cliente.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_cliente/controlador_cliente.js").then(
                                function (controller) {
                                    moduloClientes = controller;
                                    moduloClientes.inicializar();
                                }
                        );
                    }
            );
}

// Cargar página de Empleados
function cargarModuloEmpleados() {
    fetch("modulos/modulo_empleado/vista_empleado.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_empleado/controlador_empleado.js')
                        .then(function (controller) {
                            moduloEmpleados = controller;
                            moduloEmpleados.inicializar();
                        });
            });
}

// Cargar página inicial para Productos
function cargarSeccionProductos() {
    fetch("modulos/modulo_producto/vista_producto.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                    }
            );
}

function cargarSeccionVentas(){
    fetch("modulos/modulo_ventas/vista_ventas.html")
            .then(response =>{
                return response.text();
            })
            .then(function (html){
                document.getElementById('layoutSidenav_content').innerHTML = html;
            });
}

// Cargar página de Accesorios
function cargarModuloAccesorios() {
    fetch("modulos/modulo_accesorio/vista_accesorio.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_accesorio/controlador_accesorio.js").then(
                                function (controller) {
                                    moduloAccesorios = controller;
                                    moduloAccesorios.inicializar();
                                }
                        );
                    }
            );
}

function cargarModuloVentaProductos(){
    fetch("modulos/modulo_venta_productos/vista_venta_productos.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_venta_productos/controlador_venta_productos.js").then(
                                function (controller) {
                                    moduloVentaProductos = controller;
                                    moduloVentaProductos.inicializar();
                                }
                        );
                    }
            );
}

function cargarModuloVentaLente(){
    fetch("modulos/modulo_venta_presupuesto_lc/vista_venta_presupuesto_lc.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_venta_presupuesto_lc/controlador_venta_presupuesto_lc.js").then(
                                function (controller) {
                                    moduloVentaLenteContacto = controller;
                                    moduloVentaLenteContacto.inicializar();
                                }
                        );
                    }
            );
}

// Cargar página de Armazones
function cargarModuloArmazones() {
    fetch("modulos/modulo_armazon/vista_armazon.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_armazon/controlador_armazon.js").then(
                                function (controller) {
                                    moduloArmazones = controller;
                                    moduloArmazones.inicializar();
                                }
                        );
                    }
            );
}

// Cargar página de Lentes de contacto
function cargarModuloLentesContacto() {
    fetch("modulos/modulo_lente_contacto/vista_lente_contacto.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("layoutSidenav_content").innerHTML = html;
                        import("../modulos/modulo_lente_contacto/controlador_lente_contacto.js").then(
                                function (controller) {
                                    moduloLentesContacto = controller;
                                    moduloLentesContacto.inicializar();
                                }
                        );
                    }
            );
}

// Cargar página de Soluciones
function cargarModuloSoluciones() {
    fetch("modulos/modulo_solucion/vista_solucion.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_solucion/controlador_solucion.js')
                        .then(obj => {
                            moduloSoluciones = obj;
                            moduloSoluciones.inicializar();
                        });
            });
}

// Cargar página de Tratamientos
function cargarModuloTratamiento() {
    fetch("modulos/modulo_tratamiento/vista_tratamiento.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_tratamiento/controlador_tratamiento.js')
                        .then(function (controller) {
                            moduloTratamientos = controller;
                            moduloTratamientos.inicializar();
                        });
            });
}

// Cargar página de Materiales
function cargarModuloMateriales() {
    fetch("modulos/modulo_material/vista_material.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_material/controlador_material.js')
                        .then(function (controller) {
                            moduloMateriales = controller;
                            moduloMateriales.inicializar();
                        });
            });
}
function cargarModuloMicas() {
    fetch("modulos/modulo_tipo_mica/vista_tipo_mica.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_tipo_mica/controlador_tipo_mica.js')
                        .then(obj => {
                            moduloTipoMicas = obj;
                            moduloTipoMicas.inicializar();
                        });
            });
}
// Cargar página de Tipos de micas
function cargarModuloMicass() {
    fetch("modulos/modulo_tipo_mica/vista_tipo_mica.html")
            .then(response => {
                return response.text();
            })
            .then(function (html)
            {
                document.getElementById('layoutSidenav_content').innerHTML = html;
                import('../modulos/modulo_tipo_mica/controlador_tipo_mica.js')
                        .then(function (controller) {
                            moduloTipoMicas = controller;
                            moduloTipoMicas.inicializar();
                        });
            });
}


// Páginas restantes o sin uso.
function cargarEnConstruccion() {
    fetch("vista_enConstruccion.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                    }
            );
}

function cargarSeccionPersonas() {
    fetch("secciones/seccionPersonas/personas.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                    }
            );
}

function cargarSeccionServicios() {
    fetch("secciones/seccionServicios/vista_servicios.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                    }
            );
}

function cargarSeccionTienda() {
    fetch("secciones/seccionTienda/vista_tienda.html")
            .then(
                    function (response) {
                        return response.text();
                    }
            )
            .then(
                    function (html) {
                        document.getElementById("contenedorPrincipal").innerHTML = html;
                    }
            );
}

