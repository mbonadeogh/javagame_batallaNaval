package com.mygames.batallanaval;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.util.LinkedList;
import java.util.Iterator;

import java.applet.Applet;
import java.applet.AudioClip;


 public class BatallaNaval {
	//public final static Object obj = new Object();	 
	static int turno = 1;
	static int aguasH, tocadosH, hundidosH;
	static int aguasC, tocadosC, hundidosC;
	static JLabel jAguasC, jTocadosC, jHundidosC;
	static JLabel jAguasH, jTocadosH, jHundidosH;
	static JPanel estadisticas;
	double[][] flotaEnemiga;
	JFrame  marcoJuego = new JFrame();
	int totalBarcos;
	public BatallaNaval(int alto, int ancho, int cantBarcos) {
		try {
			Thread.sleep(1000);
		}
		catch(InterruptedException e) {
		}		 
		Flotas flotas = new Flotas(alto, ancho, cantBarcos);
		double[][] flota = flotas.getFlotaPropia();
		flotaEnemiga = flotas.getFlotaEnemiga();
		totalBarcos = flotas.getTotalBarcos();
		int[] barcosActivos = {totalBarcos};
        marcoJuego.setLayout(new BorderLayout());
		marcoJuego.setTitle("******** BATALLA NAVAL ********(©MCB)        -Puede seleccionar la coordenada a atacar con el mouse o indicarla en los campos de ingreso debajo-");
		JPanel filaLetras = new JPanel();
		filaLetras.setLayout(new GridLayout(1,ancho));
		char letraBase = 'A';
		for (int i=0; i<ancho;i++) {
			Label lbl = new Label(String.valueOf((char)(letraBase+i)), Label.RIGHT);
			lbl.setBackground(Color.GRAY);
			filaLetras.add(lbl);
		}
		JPanel columnaNumeros = new JPanel();
		columnaNumeros.setLayout(new GridLayout(alto,1));	
		for (int j=0; j<alto;j++) {
			Label lbl = new Label(""+(j+1), Label.RIGHT);
			lbl.setBackground(Color.GRAY);
			columnaNumeros.add(lbl);
		}		
		JPanel ingresoCoordenadas = new JPanel(); // Panel para el ingreso de coordenadas manual.(ataque a barcos de la computadora)
		ingresoCoordenadas.setLayout(new FlowLayout(FlowLayout.CENTER));
		Label etiquetaInformativa = new Label("Aquí puede ingresar las coordenadas de su ataque -", Label.LEFT);
		Label etiquetaIngresoColumna = new Label("Ingrese la letra de la columna:", Label.RIGHT);
		TextField campoIngresoColumna = new TextField(1);
		campoIngresoColumna.setBackground(new Color(135, 206, 235));
		Label separador = new Label("Luego Presione------->>");
		Label etiquetaIngresoFila = new Label("Ingrese el numero de la fila:", Label.RIGHT);
		TextField campoIngresoFila = new TextField(2);
		campoIngresoFila.setBackground(new Color(135, 206, 235));
		Button disparoBtn = new Button("Disparar!");
		disparoBtn.setBackground(Color.ORANGE);
		LinkedList<JButton> listaCoordenadas = new LinkedList<JButton>(); // Lista con referencias a todos los botones de coordenadas.
		disparoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String disCol = "", disFil = "";
				if (e.getSource() == disparoBtn) {
					if ((disCol=campoIngresoColumna.getText().trim()).isEmpty() || (disFil=campoIngresoFila.getText().trim()).isEmpty()) {
						JDialog d = new JDialog(marcoJuego, "Error: Para DISPARAR debe ingresar todas las coordenadas", true);
						d.setLayout( new FlowLayout() );  
						Button b = new Button ("OK");  
						b.addActionListener ( new ActionListener()  {  
							public void actionPerformed( ActionEvent e )  {  
								d.setVisible(false);  
							}
						});  
						d.add( new Label ("Presione botón OK para continuar."));  
						d.add(b);   						
						d.setSize(400, 100);
						d.setVisible(true);
					} else {
						int valFil = -1;
						disCol = disCol.toUpperCase();
						try {
							valFil = Integer.parseInt(disFil);
						} catch (NumberFormatException nfe) {
						}
						if (disCol.length() != 1 || (disCol.compareTo("A") < 0 || disCol.compareTo(String.valueOf((char)(letraBase+ancho-1))) > 0) || 
							valFil < 1 || valFil > alto) {
							new MyDialog(marcoJuego, 400, 100, "Error: Las coordenadas ingresadas no son válidas", true, false);
						} else { // Todo OK! datos de ingreso validados.
							// Verificar que las coordenadas ya no hayan sido ingresadas.
							if (flota[valFil-1][disCol.charAt(0)-letraBase] < 0.0) {
								new MyDialog(marcoJuego, 400, 100, "Error: La coordenada ingresada ya fue atacada", true, false);
							} else {
								Iterator it = listaCoordenadas.iterator();
								while (it.hasNext()) {
									JButton btn = (JButton)it.next();
									if (btn.getText().equalsIgnoreCase("A_"+valFil+","+(disCol.charAt(0)-letraBase+1))) { // Si es el botón cuyo label corresponde a la fila y columna del disparo.
										System.out.println("A_"+valFil+","+(disCol.charAt(0)-letraBase+1));
										btn.doClick(); // Simula el click del mouse sobre este botón para generar el evento que tratara el ActionListener asociado a este botón.
										break;
									}
								}
							}
						}
					}
				}
			}
		});
		ingresoCoordenadas.add(etiquetaInformativa);
		ingresoCoordenadas.add(etiquetaIngresoColumna);
		ingresoCoordenadas.add(campoIngresoColumna);
		ingresoCoordenadas.add(etiquetaIngresoFila);
		ingresoCoordenadas.add(campoIngresoFila);
		ingresoCoordenadas.add(separador);
		ingresoCoordenadas.add(disparoBtn);
		// Armado del panel de estadísticas
		estadisticas = new JPanel();
		estadisticas.setBackground(Color.CYAN);
		estadisticas.setLayout(new GridLayout(3,4));
		estadisticas.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
		JLabel jlEst = new JLabel("Estadistica-Tiros", SwingConstants.CENTER);
		jlEst.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jlEst);
		JLabel jlFall = new JLabel("FALLADOS", SwingConstants.CENTER);
		jlFall.setOpaque(true);
		jlFall.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jlFall.setBackground(Color.lightGray);
		estadisticas.add(jlFall);
		JLabel jlToc = new JLabel("TOCADOS", SwingConstants.CENTER);
		jlToc.setOpaque(true);
		jlToc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jlToc.setBackground(Color.lightGray);
		estadisticas.add(jlToc);
		JLabel jlHun = new JLabel("HUNDIDOS", SwingConstants.CENTER);
		jlHun.setOpaque(true);
		jlHun.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jlHun.setBackground(Color.lightGray);
		estadisticas.add(jlHun);
		JLabel jlBHU = new JLabel("BARCOS HUMANO", SwingConstants.CENTER);
		jlBHU.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jlBHU);
		jAguasH = new JLabel(""+aguasH, SwingConstants.CENTER);
		jAguasH.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jAguasH);
		jTocadosH = new JLabel(""+tocadosH, SwingConstants.CENTER);
		jTocadosH.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jTocadosH);
		jHundidosH = new JLabel(""+hundidosH, SwingConstants.CENTER);
		jHundidosH.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jHundidosH);
		JLabel jlBCO = new JLabel("BARCOS COMPUTADOR", SwingConstants.CENTER);
		jlBCO.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jlBCO);
		jAguasC = new JLabel(""+aguasC, SwingConstants.CENTER);
		jAguasC.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jAguasC);
		jTocadosC = new JLabel(""+tocadosC, SwingConstants.CENTER);
		jTocadosC.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jTocadosC);
		jHundidosC = new JLabel(""+hundidosC, SwingConstants.CENTER);
		jHundidosC.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		estadisticas.add(jHundidosC);
		JPanel pie = new JPanel();
		pie.setLayout(new GridLayout(2,1));
		pie.add(ingresoCoordenadas);
		pie.add(estadisticas);
		/************************************************/
		/* Arma cuadricula completa del juego.          */
		/************************************************/
		JPanel tablero = new JPanel();
		tablero.setLayout(new GridLayout(alto,ancho));
		marcoJuego.add(filaLetras,BorderLayout.NORTH);
		marcoJuego.add(columnaNumeros,BorderLayout.WEST);
		marcoJuego.add(tablero,BorderLayout.CENTER);
		marcoJuego.add(pie,BorderLayout.SOUTH);
		/************************************************/
		LinkedList<TipoBarco> listaBarcosMios = flotas.getListaBarcosPropios(); // Lista con tipos de barcos propios y sus coordenadas, para facilitar la contabilidad.
		// Relleno de tablero con botones, cada uno con su action listener
		for (int i = 1; i <= alto; i++) {
			for (int j = 1; j <= ancho; j++) {
				JButton btn = new JButton("A_"+i+","+j);
				btn.setForeground(Color.BLACK);
				btn.setBackground(Color.BLACK);
				tablero.add(btn);
				listaCoordenadas.add(btn);
				btn.addActionListener(new ActionListener() { // Cada botón de coordenadas tiene su ActionListener.
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() == btn) {
							double impacto;	
							String label = btn.getText();
							String[] s1 = label.split("_");
							String[] s2 = s1[1].split(",");
							System.out.println("Fila: "+s2[0]+", Columna: "+s2[1]);
							int fil = Integer.parseInt(s2[0]) - 1;
							int col = Integer.parseInt(s2[1]) - 1;
							String tiroH = String.valueOf((char)('A'+col))+(fil+1);
							System.out.println(label+" Button is pressed");
							if ((impacto = flota[fil][col]) == Flotas.AGUA) {
								Sound.AGUA.play();
								flota[fil][col] = -100.0;
								btn.setBackground(Color.CYAN);		
								btn.setText("AGUA");
								System.out.println(tiroH+" - aguA - Tablero Compu");
								aguasC++;
							} else {
								flota[fil][col] = -impacto;
								for (int i = 0; i < listaBarcosMios.size(); i++) {
									TipoBarco tb = listaBarcosMios.get(i);
									if(tb.lenBarco==(int)impacto) {
										boolean huboDaño = false;
										for (int j=0; j < tb.lenBarco; j++) {
											if (tb.coordenadas[0][j]==fil && tb.coordenadas[1][j]==col) {
												tb.cuentaImpactos++;
												huboDaño = true;
											}
										}
										if (huboDaño){
											if (tb.lenBarco == tb.cuentaImpactos) {
												Sound.HUNDIDO.play();
												if (tb.lenBarco == 1) {
													btn.setBackground(Color.MAGENTA);
												} else {
													btn.setBackground(Color.RED);
												}
												tocadosC++;
												hundidosC++;
												btn.setText("HUNDIDO");
												System.out.println(tiroH+" - hundidO - Tablero Compu");
												barcosActivos[0]--;
											} else {
												Sound.TOCADO.play();
												btn.setBackground(Color.PINK);
												tocadosC++;
												btn.setText("TOCADO");
												System.out.println(tiroH+" - tocadO - Tablero Compu");
/* 												try {
													Image img = ImageIO.read(getClass().getResource("bullet_hole.jpg"));
													btn.setIcon(new ImageIcon(img));
												} catch (Exception ex) {
												}	 */											
											}
											listaBarcosMios.set(i,tb);
											break;
										}
									}
								}
							}
							// Repinta estadísticas de barcos de la computadora
							jAguasC.setText(""+aguasC);
							jAguasC.repaint();
							jTocadosC.setText(""+tocadosC);
							jTocadosC.repaint();
							jHundidosC.setText(""+hundidosC);
							jHundidosC.repaint();													
							if (barcosActivos[0] == 0) { // Gana el adversario humano.
								new MyDialog(marcoJuego, 400, 100, "Felicitaciones!!! Acaba de ganar el juego", true, true);
							}							
							btn.setEnabled(false); // Deshabilita botón impactado.
							turno = 2;
/* 							synchronized (obj) {
								obj.notify();
							} */
						}
					}
				});
			}
		}
		marcoJuego.pack();
		marcoJuego.setVisible(true);
		marcoJuego.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
	public int getTotalBarcos() {
		return totalBarcos;
	}
	public int getFilaTiro(String tiro) {
		return (Integer.parseInt(tiro.substring(1))-1);
	}
	public int getColumnaTiro(String tiro){
		return ((int)(tiro.charAt(0)-'A'));
	}
	public class MyDialog {
		public MyDialog(JFrame marcoJuego, int ancho, int alto, String info, boolean modal, boolean endGame) {
			JDialog d = new JDialog(marcoJuego, info, modal);
			d.setLayout( new FlowLayout() );  
			Button b = new Button ("OK");  
			b.addActionListener ( new ActionListener()  {  
				public void actionPerformed( ActionEvent e )  {  
					d.setVisible(false);
					if (endGame) {
						marcoJuego.setVisible(false);
						marcoJuego.dispose();
						System.exit(0);
					}
				}
			});  
			d.add( new Label ("Presione botón OK para continuar."));  
			d.add(b);   						
			d.setSize(400, 100);
			d.setVisible(true);			 
		}
	}
	// Clase con todos los sonidos utilizados en el juego
	public class Sound {
		public static final AudioClip FONDO = Applet.newAudioClip(Sound.class.getResource("fondo.wav"));
		public static final AudioClip AGUA = Applet.newAudioClip(Sound.class.getResource("agua.wav"));
		public static final AudioClip TOCADO = Applet.newAudioClip(Sound.class.getResource("tocado.wav"));
		public static final AudioClip HUNDIDO = Applet.newAudioClip(Sound.class.getResource("hundido.wav"));
		public static final AudioClip GAMEOVER = Applet.newAudioClip(Sound.class.getResource("gameover.wav"));
	}	
	
	public static void main(String[] args) {
		int alto = 10;
		int ancho = 10;
		boolean sonido_fondo = true;
		int cantTipoBarcos = 4;		
		if (args.length!=0) {
			for (String str: args) {
				if (str.indexOf("-c")!=-1) { // columnas: A,B,C...
					ancho = Integer.parseInt(str.substring(2));
					continue;
				}
				if (str.indexOf("-f")!=-1) { // filas: 1,2,3,...
					alto = Integer.parseInt(str.substring(2));
					continue;
				}
				if (str.indexOf("-s")!=-1) { // Saca el sonido de fondo.
					sonido_fondo = false;
					continue;
				}				
				if (str.indexOf("-t")!=-1) { // Cantidad de Tipos diferentes de barcos
					int valRecomendado = 0;
					cantTipoBarcos = Integer.parseInt(str.substring(2));
					if (cantTipoBarcos > alto || cantTipoBarcos > ancho || cantTipoBarcos == 0) {
						System.out.println("El valor para <CantTiposDeBarcos>, no puede superar ni al 'ancho' ni al 'alto'");
						return;
					} else if (cantTipoBarcos > (valRecomendado=((ancho+alto)/4))) {
						System.out.println("El valor máximo recomendado para <CantTiposDeBarcos>, acorde al 'ancho' y 'alto' es: "+valRecomendado);
						return;
					}
					continue;
				}
				System.out.println("\nUso: java -jar BatallaNaval.jar [-c<ancho>] [-f<alto>] [-t<CantTiposDeBarcos>] [-s]");
				System.out.println("Nota: los corchetes '[' y ']' indican que el parámetro es opcional.\n");
				System.out.println("Donde:");
				System.out.println("-c<ancho>: Cantidad de columnas de la cuadricula");
				System.out.println("-f<alto>: Cantidad de filas de la cuadricula");
				System.out.println("-t<CantTiposDeBarcos>: Cantidad de tipos de barcos distintos (y máximo tamaño de barco). Ej: Si es 4, habra 4 barcos de 1, 3 de 2, 2 de 3 y 1 de 4");				
				System.out.println("-s: Saca sonido de fondo.\n");
				System.out.println("Ejemplo de llamada:");
				System.out.println("java -jar BatallaNaval.jar -c12 -f12 -t5");
				System.out.println("(este crea una grilla de 12x12 celdas, con 1 barco de 5 celdas, 2 de 4, 3 de 3 , 4 de 2 y 5 barcos de 1 celda)\n");
				System.out.println("IMPORTANTE: Ningun barco puede ser adyacente a otro barco, ni tener celdas con vertices coincidentes con otro barco.");
				return;
			}			
		}
		if (sonido_fondo) {
			Sound.FONDO.loop();			
		}
		final double AGUA = -100.0;
		final double TOCADO = -1.0;
		final double HUNDIDO = -2.0;
		final double INEXPLORADO = 0.0;
		BatallaNaval bn = new BatallaNaval(alto, ancho, cantTipoBarcos);
		boolean respuestaPendiente = false;
		String primerTocado = null;
		String ultimoTocado = null;
		String ultimoTiro =  null;
		int fila = -1, columna = -1;
		int filaTocado = -1, columnaTocado = -1;
		int cuentaHundidos = 0;
		boolean eliminarAguaBtn = false;
		double resultadoUltimoTiro = INEXPLORADO;
		
		// Ciclo infinito del juego hasta que alguno gana.
		while (true) {
/* 			if (cuentaHundidos == bn.getTotalBarcos()) { // Gana la computadora
				Sound.GAMEOVER.play();
				bn.new MyDialog(bn.marcoJuego, 400, 100, "Lo lamento, Acaba de perder el juego!!!", true, true);
			} */
/* 			try {
				synchronized (obj) {
					obj.wait();
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} */
			if (turno ==1) { // Tiro adversario humano (sobre barcos computadora)
				if (cuentaHundidos == bn.getTotalBarcos()) { // Gana la computadora
					Sound.GAMEOVER.play();
					bn.new MyDialog(bn.marcoJuego, 400, 100, "Lo lamento, Acaba de perder el juego!!!", true, true);
				}				
				try {
					Thread.sleep(700);
				} catch (InterruptedException ie) {
				} 
			} else if (turno ==2) { // Tiro computadora (sobre barcos humano)
				if (!respuestaPendiente) {
					if (ultimoTocado == null) {
						do {
							fila = (int)(Math.random()*alto);
							columna = (int)(Math.random()*ancho);
						} while (bn.flotaEnemiga[fila][columna] != INEXPLORADO);
					} else {
						int count = 0;
						do {
							fila = filaTocado = bn.getFilaTiro(ultimoTocado); //Integer.parseInt(ultimoTocado.substring(1))-1;
							columna = columnaTocado = bn.getColumnaTiro(ultimoTocado); //(int)(ultimoTocado.charAt(0)-'A');
							System.out.println("filaUltimoTocado: "+filaTocado+", columnaUltimoTocado: "+columnaTocado+" -Tablero Enemigo Humano-");
							if (filaTocado > 0 ) {
								if (bn.flotaEnemiga[filaTocado-1][columnaTocado] == INEXPLORADO) {
									fila = filaTocado-1;
									if (resultadoUltimoTiro == AGUA && columna == bn.getColumnaTiro(ultimoTiro) && !primerTocado.equalsIgnoreCase(ultimoTocado)) {
										eliminarAguaBtn = true;
									}										
									System.out.println("Entro a 1 : fila = filaTocado-1");
								} else if (filaTocado < (alto-1) && bn.flotaEnemiga[filaTocado+1][columnaTocado] == INEXPLORADO) {
									fila = filaTocado+1;
									if (resultadoUltimoTiro == AGUA && columna == bn.getColumnaTiro(ultimoTiro) && !primerTocado.equalsIgnoreCase(ultimoTocado)) {
										eliminarAguaBtn = true;
									}									
									System.out.println("Entro a 2 : fila = filaTocado+1, Columna:"+columna+", bn.getColumnaTiro(ultimoTiro):"+bn.getColumnaTiro(ultimoTiro));
								} else if (columnaTocado > 0 && bn.flotaEnemiga[filaTocado][columnaTocado-1] == INEXPLORADO) {
									columna = columnaTocado-1;
									if (resultadoUltimoTiro == AGUA && fila == bn.getFilaTiro(ultimoTiro) && !primerTocado.equalsIgnoreCase(ultimoTocado)) {
										eliminarAguaBtn = true;
									}									
									System.out.println("Entro a 3 : columna = columnaTocado-1");									
								} else if (columnaTocado < (ancho-1) && bn.flotaEnemiga[filaTocado][columnaTocado+1] == INEXPLORADO) {
									columna = columnaTocado+1;
									if (resultadoUltimoTiro == AGUA && fila == bn.getFilaTiro(ultimoTiro)) {
										eliminarAguaBtn = true;
									}
									System.out.println("Entro a 4 : columna = columnaTocado+1");
								}
							} else { // filaTocado es 0
								if (bn.flotaEnemiga[filaTocado+1][columnaTocado] == INEXPLORADO) {
									fila = filaTocado+1;
									System.out.println("Entro a 5 : fila = filaTocado+1");
								} else if (columnaTocado > 0 && bn.flotaEnemiga[filaTocado][columnaTocado-1] == INEXPLORADO) {
									columna = columnaTocado-1;
									System.out.println("Entro a 6 : columna = columnaTocado-1");
								} else if (columnaTocado < (ancho-1) && bn.flotaEnemiga[filaTocado][columnaTocado+1] == INEXPLORADO) {
									columna = columnaTocado+1;
									if (resultadoUltimoTiro == AGUA && fila == bn.getFilaTiro(ultimoTiro) && !primerTocado.equalsIgnoreCase(ultimoTocado)) {
										eliminarAguaBtn = true;
									}									
									System.out.println("Entro a 7 : columna = columnaTocado+1");
								}
							}
							if (columna == columnaTocado && fila == filaTocado) {
								System.out.println("Cambio sentido B");
								ultimoTocado = primerTocado;
								eliminarAguaBtn = true;
								count++;
							}
						} while (columna == columnaTocado && fila == filaTocado && count <= 1);
						if (count > 1) {
							ultimoTocado = primerTocado = null;
							eliminarAguaBtn = false;
							do {
								fila = (int)(Math.random()*alto);
								columna = (int)(Math.random()*ancho);
							} while (bn.flotaEnemiga[fila][columna] != INEXPLORADO);							
						}
					}
				}
				
				String tiro = String.valueOf((char)('A'+columna))+(fila+1);
				String tiro_msg = "<html>Mi disparo fue: <ul>"
				+ "<b>"+tiro+"</b><br></html>";
				JLabel tiro_label = new JLabel(tiro_msg);
				tiro_label.setFont(new Font("serif", Font.PLAIN, 16));
				Object[] options = {"Agua",
									"Tocado",
									"Hundido"};
				Object[] options1 = {"Tocado",
									"Hundido"};
				int n = JOptionPane.showOptionDialog(bn.marcoJuego,
					tiro_label, //""+tiro+" <-Cual es el resultado de mi disparo...",
					"RESPONDA",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					(eliminarAguaBtn?options1:options),
					(eliminarAguaBtn?options1[0]:options[0]));
				if (n == JOptionPane.CLOSED_OPTION) {
					respuestaPendiente = true;
					continue;
				}
				if (eliminarAguaBtn) {
					n++;
				}
				respuestaPendiente = false;
				System.out.println(tiro+" - "+(String)options[n]+" - Tablero Enemigo Humano");
				if (n==0) {
					Sound.AGUA.play();
					bn.flotaEnemiga[fila][columna] = AGUA;
					if (ultimoTocado != null && !primerTocado.equalsIgnoreCase(ultimoTocado)) {
						System.out.println("Cambio sentido A");
						ultimoTocado = primerTocado;
						eliminarAguaBtn = true;
					}
					aguasH++;					
				} else if (n==1) {
					Sound.TOCADO.play();
					bn.flotaEnemiga[fila][columna] = TOCADO;
					if (ultimoTocado == null) {
						primerTocado = tiro;
					}
					ultimoTocado = tiro;
					if (resultadoUltimoTiro == AGUA && !primerTocado.equalsIgnoreCase(ultimoTocado) && 
						(bn.getFilaTiro(ultimoTocado)==bn.getFilaTiro(ultimoTiro) || bn.getColumnaTiro(ultimoTocado)==bn.getColumnaTiro(ultimoTiro))) {
						eliminarAguaBtn = true;
					}
					tocadosH++;
				} else if (n==2) {
					Sound.HUNDIDO.play();
					bn.flotaEnemiga[fila][columna] = HUNDIDO;
					cuentaHundidos++;
					primerTocado = ultimoTocado = null;
					eliminarAguaBtn = false;
					tocadosH++;
					hundidosH++;
				}
				// Repinta estadísticas de barcos del adversario humano.
				jAguasH.setText(""+aguasH);
				jAguasH.repaint();
				jTocadosH.setText(""+tocadosH);
				jTocadosH.repaint();
				jHundidosH.setText(""+hundidosH);
				jHundidosH.repaint();				
				ultimoTiro = tiro;
				resultadoUltimoTiro = bn.flotaEnemiga[fila][columna];
				// Descarta coordenadas enemigas (celdas) a las que ya no es necesario acceder / disparar.
				if (n==1 || n==2) {
					if (fila>0) {
						if (columna>0) {
							bn.flotaEnemiga[fila-1][columna-1] = AGUA;
							if (n==2) {
								if(bn.flotaEnemiga[fila][columna-1] == INEXPLORADO) {
									bn.flotaEnemiga[fila][columna-1] = AGUA;
								}
								if(bn.flotaEnemiga[fila-1][columna] == INEXPLORADO) {
									bn.flotaEnemiga[fila-1][columna] = AGUA;
								}
							}
						}
						if (columna < (ancho-1)) {
							bn.flotaEnemiga[fila-1][columna+1] = AGUA;
							if (n==2) {
								if(bn.flotaEnemiga[fila][columna+1] == INEXPLORADO) {
									bn.flotaEnemiga[fila][columna+1] = AGUA;
								}
								if(bn.flotaEnemiga[fila-1][columna] == INEXPLORADO) {
									bn.flotaEnemiga[fila-1][columna] = AGUA;
								}								
							}
						}
						if (fila < (alto-1)) {
							if (columna > 0) {
								bn.flotaEnemiga[fila+1][columna-1] = AGUA;
							} 
							if (columna < (ancho-1)) {
								bn.flotaEnemiga[fila+1][columna+1] = AGUA;
							}
							if (n==2) {
								if(bn.flotaEnemiga[fila+1][columna] == INEXPLORADO) {
									bn.flotaEnemiga[fila+1][columna] = AGUA;
								}
								if(bn.flotaEnemiga[fila-1][columna] == INEXPLORADO) {
									bn.flotaEnemiga[fila-1][columna] = AGUA;
								}									
							}
						}
						if (n==2) {
							if(bn.flotaEnemiga[fila-1][columna] == INEXPLORADO) {
								bn.flotaEnemiga[fila-1][columna] = AGUA;
							}							
						}
					} else { // Fila es 0 aquí
						if (columna > 0) {
							bn.flotaEnemiga[fila+1][columna-1] = AGUA;
							if (n==2) {
								if(bn.flotaEnemiga[fila][columna-1] == INEXPLORADO) {
									bn.flotaEnemiga[fila][columna-1] = AGUA;
								}
							}							
						} 
						if (columna < (ancho-1)) {
							bn.flotaEnemiga[fila+1][columna+1] = AGUA;
							if (n==2) {
								if(bn.flotaEnemiga[fila][columna+1] == INEXPLORADO) {
									bn.flotaEnemiga[fila][columna+1] = AGUA;
								}							
							}							
						}
						if (n==2) {
							if(bn.flotaEnemiga[fila+1][columna] == INEXPLORADO) {
								bn.flotaEnemiga[fila+1][columna] = AGUA;
							}							
						}						
					}
					
				}
				// matrix.printMat(bn.flotaEnemiga);
				turno = 1;
			 }
		 }
	 }
 }