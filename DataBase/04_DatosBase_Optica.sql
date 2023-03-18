-- ---------------------------------------------------------------- --
-- Archivo: 04_DatosBase_Optica.sql                                 -- 
-- Version: 1.0                                                     --
-- Autor:   Miguel Angel Gil Rios   								--
-- Email:   angel.grios@gmail.com / mgil@utleon.edu.mx              --
-- Fecha de elaboracion: 29-12-2021                                 --
-- ---------------------------------------------------------------- --

USE optiqalumnos;

-- Insercion del Usuario Raiz (Administrador):
CALL insertarEmpleado('Administrador', '-', '-', 'O', '01/01/1901', -- Datos Personales
                      '', '', '', '', '', '', '', '', '','',
                      'Administrador', '', 'Administrador',         -- Datos de Seguridad
                      @out1, @out2, @out3, @out4, @out5); -- Parametros de salida
                      
CALL insertarEmpleado('Sergio', '-', '-', '1', '01/01/1901', -- Datos Personales
                      '', '', '', '','', '', '', '', '', '',
                      'Sergio', '', 'Administrador',         -- Datos de Seguridad
                      @out1, @out2, @out3, @out4, @out5); -- Parametros de salida
                      
                      
CALL insertarCliente('Juan', 'Pérez', 'González', 'M', '02/01/1990', 'Calle 1', '10', 'Colonia 1', '12345', 'Ciudad 1', 'Estado 1', '5555555555', '4444444444', 'juan@gmail.com', 'PERJ900102000', @ou1, @out2, @out3);
CALL insertarCliente('María', 'López', 'Sánchez', 'F', '03/05/1995', 'Calle 2', '20', 'Colonia 2', '54321', 'Ciudad 2', 'Estado 2', '6666666666', '7777777777', 'maria@gmail.com', 'LOSM950503000', @out1, @out2, @out3);
CALL insertarCliente('Pedro', 'García', 'Hernández', 'M', '12/12/1988', 'Calle 3', '30', 'Colonia 3', '67890', 'Ciudad 3', 'Estado 3', '8888888888', '9999999999', 'pedro@gmail.com', 'GAHP881212000', @out1, @out2, @out3);
CALL insertarCliente('Sofía', 'Martínez', 'Gutiérrez', 'F', '01/02/1992', 'Calle 4', '40', 'Colonia 4', '09876', 'Ciudad 4', 'Estado 4', '3333333333', '2222222222', 'sofia@gmail.com', 'MAGS920201000', @out1, @out2, @out3);
CALL insertarCliente('Carlos', 'Hernández', 'Jiménez', 'M', '11/07/1998', 'Calle 5', '50', 'Colonia 5', '45678', 'Ciudad 5', 'Estado 5', '4444444444', '5555555555', 'carlos@gmail.com', 'HEJC980711000', @out1, @out2, @out3);
CALL insertarCliente('Ana', 'Pérez', 'Rodríguez', 'F', '18/09/1991', 'Calle 6', '60', 'Colonia 6', '34567', 'Ciudad 6', 'Estado 6', '7777777777', '6666666666', 'ana@gmail.com', 'PERA910918000', @out1, @out2, @out3);
CALL insertarCliente('Luis', 'García', 'Pérez', 'M', '28/04/1997', 'Calle 7', '70', 'Colonia 7', '23456', 'Ciudad 7', 'Estado 7', '2222222222', '3333333333', 'luis@gmail.com', 'GAPL970428000', @out1, @out2, @out3);
                      
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);
CALL insertarSolucion(null,'HAJSH','Ojito',24.5,25.5,1, @out1,  @out2 , @out3);

INSERT INTO lente_contacto (idProducto, keratometria, fotografia, color)
VALUES
(1, 550, 'fotografia1.jpg', 'azul'),
(2, 600, 'fotografia2.jpg', 'verde'),
(3, 525, 'fotografia3.jpg', 'marrón'),
(4, 575, 'fotografia4.jpg', 'negro'),
(5, 500, 'fotografia5.jpg', 'rojo'),
(6, 625, 'fotografia6.jpg', 'rosado'),
(7, 550, 'fotografia7.jpg', 'amarillo'),
(8, 600, 'fotografia8.jpg', 'naranja'),
(9, 525, 'fotografia9.jpg', 'gris'),
(10, 575, 'fotografia10.jpg', 'blanco');

CALL insertarArmazon('Armazon1', 'Marca1', 10.0, 20.0, 100, 'Modelo1', 'Negro', '100x50x20', 'Descripción del armazón 1', 'ruta/a/la/fotografia1.png', @out1, @out2, @out3);
CALL insertarArmazon('Armazon2', 'Marca2', 20.0, 30.0, 200, 'Modelo2', 'Rojo', '90x45x18', 'Descripción del armazón 2', 'ruta/a/la/fotografia2.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon3', 'Marca3', 30.0, 40.0, 300, 'Modelo3', 'Verde', '80x40x16', 'Descripción del armazón 3', 'ruta/a/la/fotografia3.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon4', 'Marca4', 40.0, 50.0, 400, 'Modelo4', 'Azul', '70x35x14', 'Descripción del armazón 4', 'ruta/a/la/fotografia4.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon5', 'Marca5', 50.0, 60.0, 500, 'Modelo5', 'Amarillo', '60x30x12', 'Descripción del armazón 5', 'ruta/a/la/fotografia5.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon6', 'Marca6', 60.0, 70.0, 600, 'Modelo6', 'Naranja', '50x25x10', 'Descripción del armazón 6', 'ruta/a/la/fotografia6.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon7', 'Marca7', 70.0, 80.0, 700, 'Modelo7', 'Morado', '40x20x8', 'Descripción del armazón 7', 'ruta/a/la/fotografia7.png', @out1, @out2, @out2);
CALL insertarArmazon('Armazon8', 'Marca8', 80.0, 90.0, 800, 'Modelo8', 'Café', '30x15x6', 'Descripción del armazón 8', 'ruta/a/la/fotografia8.png', @out1, @out2, @out2);


CALL insertarExamenVista(0.75, 1.00, -0.50, -0.75, 95, 110, '10/10', 1, 1, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(-1.25, -1.00, 0, 0, 0, 0, '12/12', 2, 1, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.50, 0.50, -0.25, -0.25, 175, 170, '8/10', 1, 2, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(1.25, 1.50, -0.75, -1.00, 80, 90, '10/12', 2, 2, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(-1.50, -1.75, 0, 0, 0, 0, '10/10', 1, 3, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.25, 0.25, -0.25, -0.25, 80, 80, '10/10', 2, 3, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(-2.00, -2.25, 0, 0, 0, 0, '10/10', 1, 4, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.50, 0.50, -0.50, -0.50, 80, 85, '10/10', 2, 4, @out1, @out2, @out3, @out4);


CALL insertarExamenVista(-2.50, -2.25, 0, 0, 0, 0, '10/10', 1, 5, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.70, 0.50, -0.50, -0.50, 80, 85, '10/10', 2, 5, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(-1.00, -2.25, 0, 0, 0, 0, '10/10', 1, 6, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.50, 0.50, -0.50, -0.50, 80, 85, '10/10', 2, 6, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(-2.00, -2.25, 0, 0, 0, 0, '10/10', 1, 7, @out1, @out2, @out3, @out4);
CALL insertarExamenVista(0.50, 0.50, -0.50, -0.50, 80, 85, '10/10', 2, 7, @out1, @out2, @out3, @out4);

select * from examen_vista;

-- Insercion de Catalogos Base
INSERT INTO tipo_mica (nombre, precioCompra, precioVenta)
VALUES 
('Bifocal delgado', 15.00, 30.00),
('Monofocal delgado', 10.00, 20.00),
('Progresivo delgado', 25.00, 50.00),
('Bifocal solar', 20.00, 40.00),
('Monofocal solar', 12.00, 24.00),
('Progresivo solar', 30.00, 60.00),
('Bifocal luz azul', 18.00, 36.00),
('Monofocal luz azul', 11.00, 22.00),
('Progresivo luz azul', 27.00, 54.00),
('Monofocal botella', 8.00, 16.00);

INSERT INTO material (nombre, precioCompra, precioVenta)
VALUES 
('Cristal Transparente', 5.00, 10.00),
('Cristal anti', 7.00, 14.00),
('Polarizado', 10.00, 20.00),
('Policarbonato', 8.00, 16.00),
('Plastico', 3.00, 6.00),
('PlasticoPolarizado', 5.00, 10.00),
('PlasticoCarbonato', 6.00, 12.00),
('Cristalpolarizado', 12.00, 24.00),
('Cristalcarbonato', 9.00, 18.00),
('Normal', 4.00, 8.00);

                    
INSERT INTO tratamiento(nombre, precioCompra, precioVenta)
VALUES
('Antireflejante premium', 50.00, 100.00),
('Anti blue', 30.00, 60.00),
('Tinte', 20.00, 40.00),
('Polarizado', 60.00, 120.00),
('Comun', 10.00, 20.00),
('Doble tinte', 25.00, 50.00),
('Entintado rojo', 15.00, 30.00),
('Entintado azul', 15.00, 30.00),
('Entintado morado', 15.00, 30.00),
('Entintado rosa', 15.00, 30.00);	
				
select * from venta;
select * from presupuesto;
select * from  presupuesto_lentescontacto;
select * from venta_presupuesto;

select * from examen_vista;
SELECT * FROM v_lentes_contacto;
insert into presupuesto(idExamenVista,clave) values(2,"bsndksn");

INSERT INTO presupuesto_lentescontacto(idExamenVista, idLenteContacto, idPresupuestoLentesContacto, clave)VALUES (2,6,15,'OQ-PSTLC-1679039080885');