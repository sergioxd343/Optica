var login;
var usuarios = [];
function validarSesion() {
    // Crear variables para hacer el json
    let datos = null;
    let params = null;
    let nombre = document.getElementById("txtUsuario").value;
    let contrasenia = document.getElementById("txtPassword").value;
    datos = {
        usuario: nombre,
        password: contrasenia
    };
    params = new URLSearchParams(datos);
    console.log(datos);

    fetch("api/log/in",
            {
                method: "POST",
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                body: params
            })
            .then(response => {
                return response.json();
            })
            .then(function (data) {
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
                if (data.usuario.nombre != null) {
                    if (data.usuario.contrasenia != null) {
                        let empleado = data;
                        console.log("----------");
                        console.log(empleado);
                        localStorage.setItem("lastToken", empleado.usuario.lastToken);
                        alert("consola");
                        localStorage.setItem("empleado", JSON.stringify(empleado));
                        console.log(localStorage.getItem("empleado"));
                        window.location = "home.html";
                    }


                } else {
                    Swal.fire('', 'Usuario y/o contraseña incorrectos.', 'warning');
                    return;
                }

            });
}

function cerrarSesion() {
    let emp = new Object();
    emp = JSON.parse(localStorage.getItem("empleado"));
    console.log(emp);
    datos = {
        empleado: JSON.stringify(emp)
    };
    params = new URLSearchParams(datos);
    console.log(datos);
    fetch("api/log/out",
            {
                method: "POST",
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                body: params
            })
            .then(response => {
                return response.json();
            })
            .then(function (data) {
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
                if (data.response != null) {
                    Swal.fire({
                        title: 'Sesion cerrada correctamente',
                        text: data.response,
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Salir'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            localStorage.clear();
                            window.location = "index.html";
                        }
                    });
                } else {
                    Swal.fire('', data.exception, 'warning');
                    return;
                }
            });
}

function validarToken()
{
    let validado;
    if (localStorage.getItem("lastToken") != null) {
        let lastToken = localStorage.getItem("lastToken");
        datos = {
            token: lastToken
        };
        params = new URLSearchParams(datos);
        console.log(datos);
        fetch("api/log/verify",
                {
                    method: "POST",
                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
                    body: params
                })
                .then(response => {
                    return response.json();
                })
                .then(function (data) {
                    console.log(data);
                    if (data.exception != null)
                    {
                        Swal.fire('', 'Error interno del servidor. Intente nuevamente mas tarde.', 'error');
                        window.location = "index.html";
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
                    if (data.success != null) {
                        console.log(data.success);
                        return;
                    }
                    if (data.errorec != null) {
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
                    }
                });
    } else
    {
        localStorage.clear();
        Swal.fire({
            title: 'No has iniciado sesion',
            text: 'Debes iniciar sesion',
            icon: 'error',
            confirmButtonColor: '#3085d6',
            confirmButtonText: 'Iniciar sesion'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location = "index.html";
            }
        });
    }

}
