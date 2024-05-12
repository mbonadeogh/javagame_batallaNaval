package com.mygames.batallanaval;
public class TipoBarco {
	int lenBarco;
	int cuentaImpactos;
	int [][] coordenadas;
	public TipoBarco(int len) {
		lenBarco = len;
		coordenadas = new int[2][len];
	}
}