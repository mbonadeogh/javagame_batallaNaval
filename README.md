# javagame_batallaNaval

Juego tradicional de Batalla Naval
**********************************

El tablero es configurable, por defecto este es de 10 columnas por 10 filas.
La cantidad de barcos es configurable y dependera tambien del tamaño del tablero.

Uso: java -jar BatallaNaval.jar [-c<ancho>] [-f<alto>] [-t<CantTiposDeBarcos>] [-s]
Nota: los corchetes '[' y ']' indican que el parámetro es opcional.

Donde:
-c<ancho>: Cantidad de columnas de la cuadricula
-f<alto>: Cantidad de filas de la cuadricula
-t<CantTiposDeBarcos>: Cantidad de tipos de barcos distintos (y máximo tamaño de barco). Ej: Si es 4, habra 4 barcos de 1, 3 de 2, 2 de 3 y 1 de 4
-s: Saca sonido de fondo.

Ejemplo de llamada:
java -jar BatallaNaval.jar -c12 -f12 -t5
(este crea una grilla de 12x12 celdas, con 1 barco de 5 celdas, 2 de 4, 3 de 3 , 4 de 2 y 5 barcos de 1 celda)

IMPORTANTE: Ningun barco puede ser adyacente a otro barco, ni tener celdas con vertices coincidentes con otro barco.
