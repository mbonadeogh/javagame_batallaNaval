package com.mygames.batallanaval;
import java.util.LinkedList;
import java.util.Iterator;

public class Flotas {
	double flotaPropia[][];
	double flotaEnemiga[][];
	LinkedList<TipoBarco> listaBarcos;
	int altoTablero, anchoTablero, totalBarcos;
	static int FILA = 0;
	static int COLUMNA = 1;
	static double AGUA = 0.0;
	
	/**************************************************************************/
	/* Construye los tableros -propio(computadora)- y -enemigo(humano)-       */
	/* Agrega los barcos de la flota de la computadora                        */
	/**************************************************************************/
	public Flotas(int alto, int ancho, int lenBarcoMax){
		int cantBarcosLen = 0;
		altoTablero = alto;
		anchoTablero = ancho;
		flotaPropia = (new matrix()).rellena(altoTablero, anchoTablero, AGUA);
		flotaEnemiga = (new matrix()).rellena(altoTablero, anchoTablero, AGUA);
		listaBarcos = new LinkedList<TipoBarco>();
		for (int len=lenBarcoMax; len > 0; len--) {
			cantBarcosLen++;
			totalBarcos+=cantBarcosLen;
			addBarcos(len, cantBarcosLen);
		}
/* 		matrix.printMat(flotaPropia);
		Iterator it = listaBarcos.iterator();
		while (it.hasNext()) {
			printMatInt(((TipoBarco)it.next()).coordenadas);
		} */
	}
	static void printMatInt (int c[][]) { // Solo imprime la Matriz de forma "legible".
		for (int fila=0; fila < c.length; fila++) {
			for (int col=0; col < c[0].length; col++) {
				System.out.print("C("+(fila+1)+","+(col+1)+")="+c[fila][col]+"  ");
			}
			System.out.println();
		}		
	}	
	public int getTotalBarcos() {
		return totalBarcos;
	}
	public double[][] getFlotaPropia() {
		return flotaPropia;
	}
	public double[][] getFlotaEnemiga() {
		return flotaEnemiga;
	}	
	public LinkedList<TipoBarco> getListaBarcosPropios() {
		return listaBarcos;
	}
	
	/************************************************************************************/
	/* Pone "cantBarcos" barcos de tamaño "lenBarco" en el tablero propio -computadora- */
	/************************************************************************************/
	public void addBarcos(int lenBarco, int cantBarcos) {
		for (int iCuenta = 1; iCuenta <= cantBarcos; iCuenta++) {
			int orientacion, fInicio, cInicio;
			do {
				orientacion = (Math.random()>0.5?FILA:COLUMNA);
				fInicio = (int)(Math.random()*altoTablero);
				cInicio = (int)(Math.random()*anchoTablero);						
			} while (!ubicacionValida(fInicio, cInicio, orientacion, lenBarco));
		}
	}
	
	/******************************************************************************************************************************/
	/* Valida si puede poner un barco de longitud "lenBarco" en la posicion "filInicio"/"colInicio" con orientacion "orientacion" */
	/* si puede ponerlo, lo pone en el tablero y retorna "true" si no puede retorna "false".                                      */
	/******************************************************************************************************************************/
	public boolean ubicacionValida(int filInicio, int colInicio, int orientacion, int lenBarco) {
		boolean valida = true;
		if(orientacion==FILA) {
			if ((colInicio+lenBarco) > flotaPropia[0].length) { // Saco el =
				for (int col=colInicio ;col > (colInicio-lenBarco) && valida; col--) {
					valida = (flotaPropia[filInicio][col] == AGUA); // El lugar para el barco no tiene celdas ocupadas en la fila
					if (valida && (filInicio-1) >= 0) {
						valida = (flotaPropia[filInicio-1][col] == AGUA); // Fila superior adyacente no tiene celdas ocupadas
					}
					if (valida && (filInicio+1) < flotaPropia.length) {
						valida = (flotaPropia[filInicio+1][col] == AGUA); // Fila inferior adyacente no tiene celdas ocupadas
					}						
				}
				if (valida && (colInicio+1) < flotaPropia[0].length && (filInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio+1] == AGUA); // Celda en angulo superior derecho no esta ocupada
				}
				if (valida && (colInicio+1) < flotaPropia[0].length && (filInicio+1) < flotaPropia.length) {
					valida = (flotaPropia[filInicio+1][colInicio+1] == AGUA); // Celda en angulo inferior derecho no esta ocupada
				}
				if (valida && (filInicio-1) >= 0 && (colInicio-lenBarco) >= 0) {
					//System.out.println("colInicio: "+colInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio-1][colInicio-lenBarco] == AGUA); // Celda en angulo superior izquierdo no esta ocupada
				}
				if (valida && (filInicio+1) < flotaPropia.length && (colInicio-lenBarco) >= 0) {
					//System.out.println("colInicio: "+colInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio+1][colInicio-lenBarco] == AGUA); // Celda en angulo inferior izquierdo no esta ocupada
				}
				if (valida && (colInicio+1) < flotaPropia[0].length) {
					valida = (flotaPropia[filInicio][colInicio+1] == AGUA); // Celda a la derecha no esta ocupada
				}
				if (valida && (colInicio-lenBarco) >= 0) {
					//System.out.println("colInicio: "+colInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio][colInicio-lenBarco] == AGUA); // Celda a la izquierda no esta ocupada
				}
				if (valida) {
					TipoBarco tb = new TipoBarco(lenBarco);
					for (int col=colInicio ;col > (colInicio-lenBarco); col--) { // Pone barco en su lugar
						flotaPropia[filInicio][col] = lenBarco;
						tb.coordenadas[0][colInicio-col]=filInicio;
						tb.coordenadas[1][colInicio-col]=col;
					}
					listaBarcos.add(tb);
				}
			} else { // (colInicio+lenBarco) <= flotaPropia[0].length
				for (int col=colInicio ;col < (colInicio+lenBarco) && valida; col++) {
					valida = (flotaPropia[filInicio][col] == AGUA); // El lugar para el barco no tiene celdas ocupadas en la fila 
					if (valida && (filInicio-1) >= 0) {
						valida = (flotaPropia[filInicio-1][col] == AGUA); // Fila superior adyacente no tiene celdas ocupadas
					}
					if (valida && (filInicio+1) < flotaPropia.length) {
						valida = (flotaPropia[filInicio+1][col] == AGUA); // Fila inferior adyacente no tiene celdas ocupadas
					}						
				}
				if (valida && (colInicio+lenBarco) < flotaPropia[0].length && (filInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio+lenBarco] == AGUA); // Celda en angulo superior derecho no esta ocupada
				}
				if (valida && (colInicio+lenBarco) < flotaPropia[0].length && (filInicio+1) < flotaPropia.length) {
					valida = (flotaPropia[filInicio+1][colInicio+lenBarco] == AGUA); // Celda en angulo inferior derecho no esta ocupada
				}
				if (valida && (filInicio-1) >= 0 && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio-1] == AGUA); // Celda en angulo superior izquierdo no esta ocupada
				}
				if (valida && (filInicio+1) < flotaPropia.length && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio+1][colInicio-1] == AGUA); // Celda en angulo inferior izquierdo no esta ocupada
				}
				if (valida && (colInicio+lenBarco) < flotaPropia[0].length) {
					valida = (flotaPropia[filInicio][colInicio+lenBarco] == AGUA); // Celda a la derecha no esta ocupada
				}
				if (valida && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio][colInicio-1] == AGUA); // Celda a la izquierda no esta ocupada
				}
				if (valida) {
					TipoBarco tb = new TipoBarco(lenBarco);					
					for (int col=colInicio ;col < (colInicio+lenBarco); col++) { // Pone barco en su lugar
						flotaPropia[filInicio][col] = lenBarco;
						tb.coordenadas[0][col-colInicio]=filInicio;
						tb.coordenadas[1][col-colInicio]=col;
					}
					listaBarcos.add(tb);				
				}					
			}
		} else if (orientacion == COLUMNA) {
			if ((filInicio+lenBarco) > flotaPropia.length) { // Saco el =
				for (int fil=filInicio ;fil > (filInicio-lenBarco) && valida; fil--) {
					valida = (flotaPropia[fil][colInicio] == AGUA); // El lugar para el barco no tiene celdas ocupadas en la columna
					if (valida && (colInicio-1) >= 0) {
						valida = (flotaPropia[fil][colInicio-1] == AGUA); // Columna izquierda adyacente no tiene celdas ocupadas
					}
					if (valida && (colInicio+1) < flotaPropia[0].length) {
						valida = (flotaPropia[fil][colInicio+1] == AGUA); // Columna derecha adyacente no tiene celdas ocupadas
					}						
				}
				if (valida && (filInicio+1) < flotaPropia.length && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio+1][colInicio-1] == AGUA); // Celda en angulo inferior izquierdo no esta ocupada
				}
				if (valida && (filInicio+1) < flotaPropia.length && (colInicio+1) < flotaPropia[0].length) {
					valida = (flotaPropia[filInicio+1][colInicio+1] == AGUA); // Celda en angulo inferior derecho no esta ocupada
				}
				if (valida && (colInicio-1) >= 0 && (filInicio-lenBarco) >= 0) {
					//System.out.println("filInicio: "+filInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio-lenBarco][colInicio-1] == AGUA); // Celda en angulo superior izquierdo no esta ocupada
				}
				if (valida && (colInicio+1) < flotaPropia[0].length && (filInicio-lenBarco) >= 0) {
					//System.out.println("filInicio: "+filInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio-lenBarco][colInicio+1] == AGUA); // Celda en angulo superior derecho no esta ocupada
				}
				if (valida && (filInicio+1) < flotaPropia.length) {
					valida = (flotaPropia[filInicio+1][colInicio] == AGUA); // Celda debajo no esta ocupada
				}
				if (valida && (filInicio-lenBarco) >= 0) {
					//System.out.println("filInicio: "+filInicio+", lenBarco: "+lenBarco);
					valida = (flotaPropia[filInicio-lenBarco][colInicio] == AGUA); // Celda de arriba no esta ocupada
				}
				if (valida) {
					TipoBarco tb = new TipoBarco(lenBarco);
					for (int fil=filInicio ;fil > (filInicio-lenBarco); fil--) { // Pone barco en su lugar
						flotaPropia[fil][colInicio] = lenBarco;
						tb.coordenadas[0][filInicio-fil]=fil;
						tb.coordenadas[1][filInicio-fil]=colInicio;
					}
					listaBarcos.add(tb);
				}
			} else { // (filInicio+lenBarco) <= flotaPropia.length
				for (int fil=filInicio ;fil < (filInicio+lenBarco) && valida; fil++) {
					valida = (flotaPropia[fil][colInicio] == AGUA); // El lugar para el barco no tiene celdas ocupadas en la columna
					if (valida && (colInicio-1) >= 0) {
						valida = (flotaPropia[fil][colInicio-1] == AGUA); // Columna izquierda adyacente no tiene celdas ocupadas
					}
					if (valida && (colInicio+1) < flotaPropia[0].length) {
						valida = (flotaPropia[fil][colInicio+1] == AGUA); // Columna derecha adyacente no tiene celdas ocupadas
					}						
				}
				if (valida && (filInicio+lenBarco) < flotaPropia.length && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio+lenBarco][colInicio-1] == AGUA); // Celda en angulo inferior izquierdo no esta ocupada
				}
				if (valida && (filInicio+lenBarco) < flotaPropia.length && (colInicio+1) < flotaPropia[0].length) {
					valida = (flotaPropia[filInicio+lenBarco][colInicio+1] == AGUA); // Celda en angulo inferior derecho no esta ocupada
				}
				if (valida && (filInicio-1) >= 0 && (colInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio-1] == AGUA); // Celda en angulo superior izquierdo no esta ocupada
				}
				if (valida && (colInicio+1) < flotaPropia[0].length && (filInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio+1] == AGUA); // Celda en angulo superior derecho no esta ocupada
				}
				if (valida && (filInicio+lenBarco) < flotaPropia.length) {
					valida = (flotaPropia[filInicio+lenBarco][colInicio] == AGUA); // Celda debajo no esta ocupada
				}
				if (valida && (filInicio-1) >= 0) {
					valida = (flotaPropia[filInicio-1][colInicio] == AGUA); // Celda de arriba no esta ocupada
				}
				if (valida) {
					TipoBarco tb = new TipoBarco(lenBarco);
					for (int fil=filInicio ;fil < (filInicio+lenBarco); fil++) { // Pone barco en su lugar
						flotaPropia[fil][colInicio] = lenBarco;
						tb.coordenadas[0][fil-filInicio]=fil;
						tb.coordenadas[1][fil-filInicio]=colInicio;
					}
					listaBarcos.add(tb);
				}					
			}
		}
		return valida;
	}

	public static void main(String[] args) {
		new Flotas(10,10,4);
	}
}