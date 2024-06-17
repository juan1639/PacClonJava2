package main.pacclon.sprites;

import java.awt.Color;
import java.awt.Graphics;

import main.pacclon.interfaces.IChooseRndDirection;
import main.pacclon.interfaces.ISpritesMethods;
import main.pacclon.principal.Ventana;
import main.pacclon.settings.Settings;

public class Fantasma implements ISpritesMethods, IChooseRndDirection {

	private static Boolean estanAzules = false;
	private Boolean estaComido = false;
	private Boolean colorIntermitente = false;

	// {direccionX, direccionY, direccion}
	// ( 1,0 = ri | -1,0 = le | 0,-1 up | 0,1 = do )
	private int[][] direcciones = { { 1, 0, 0 }, { -1, 0, 1 }, { 0, -1, 2 }, { 0, 1, 3 }, };

	// Posibles random direcciones (excluimos la direccion actual)
	private int[][] otraDireccionRND = { { 1, 2, 3 }, { 0, 2, 3 }, { 0, 1, 3 }, { 0, 1, 2 }, };

	// Puntos donde los fantasmas pueden cambiar de direccion
	private static int[][] ptosClave = { { 4, 1 }, { 14, 1 }, { 4, 4 }, { 6, 4 }, { 12, 4 }, { 14, 4 }, { 4, 8 },
			{ 6, 8 }, { 12, 8 }, { 14, 8 }, { 1, 11 }, { 4, 11 }, { 6, 11 }, { 12, 11 }, { 14, 11 }, { 17, 11 },
			{ 4, 13 }, { 9, 13 }, { 14, 13 } };

	private int x;
	private int y;
	private int tileX;
	private int tileY;
	private int id;
	private int dirPorDefecto = 0;
	private int direccion;
	private int[] velXY = { 0, 0 };
	private int vel;
	private Boolean avanzar;

	private Ventana ventana;
	private int[] coorPacMan;

	public Fantasma(int x, int y, int tileX, int tileY, int id, int dirPorDefecto, Ventana ventana) {

		this.tileX = tileX;
		this.tileY = tileY;
		this.x = x * this.tileX;
		this.y = y * this.tileY;

		this.dirPorDefecto = dirPorDefecto;
		this.velXY[0] = direcciones[this.dirPorDefecto][0];
		this.velXY[1] = direcciones[this.dirPorDefecto][1];
		this.direccion = this.dirPorDefecto;
		this.id = id;

		this.vel = (int) (this.tileY / DIVIDIR_TILE_ENTRE);
		this.avanzar = true;
		this.ventana = ventana;
	}

	@Override
	public void dibuja(Graphics g, int[][] matriz, Settings sett) {

		// Color rgb -> Fantasma0, Fantasma1...
		int[][] rgb = { { 9, 165, 205 }, { 225, 25, 9 }, { 245, 105, 135 }, { 55, 225, 9 } };

		// Color 'estanAzules'
		int[] rgbAzules = { 9, 75, 205 };

		coorPacMan = getPacManPosition();
		actualiza(matriz, sett);

		if (!estanAzules) {
			g.setColor(new Color(rgb[this.id][0], rgb[this.id][1], rgb[this.id][2]));

		} else {
			g.setColor(new Color(rgbAzules[0], rgbAzules[1], rgbAzules[2]));
			if (checkIntermitentes(sett))
				colorIntermitente(g);
		}

		if (!estaComido) {
			g.fillArc(this.x + 1, this.y, this.tileX - 2, this.tileY, 0, 360);
			g.fillRect(this.x + 1, this.y + (int) (this.tileY / 2), this.tileX - 2, (int) (this.tileY / 2) - 1);
			drawSoloOjos(g);

		} else {
			drawSoloOjos(g);
		}

		// drawPtosClave(g);
	}

	private void drawSoloOjos(Graphics g) {

		int radioOjo = 17;
		int radioPupila = 8;

		int ojoX = (int) (this.x + 3);
		int ojoY = (int) (this.tileY / 4);
		int centroX = (int) (this.tileX / 2);

		// mover Pupilas para que miren hacia la direccion en la que van...
		int cuantoMover = 5;
		int vX = this.velXY[0] * cuantoMover;
		int vY = this.velXY[1] * cuantoMover;

		// Ojos (color blanco)
		g.setColor(new Color(241, 241, 241));
		g.fillArc(ojoX, this.y + ojoY, radioOjo, radioOjo, 0, 360);
		g.fillArc(ojoX + centroX, this.y + ojoY, radioOjo, radioOjo, 0, 360);

		// Pupilas (color negro)
		int pupilaX = ojoX + (int) (radioOjo / 2) - (int) (radioPupila / 2);
		int pupilaY = this.y + ojoY + (int) (radioOjo / 2) - (int) (radioPupila / 2);

		g.setColor(new Color(9, 9, 9));
		g.fillArc(pupilaX + vX, pupilaY + vY, radioPupila, radioPupila, 0, 360);
		g.fillArc(pupilaX + centroX + vX, pupilaY + vY, radioPupila, radioPupila, 0, 360);
	}

	@Override
	public void actualiza(int[][] matriz, Settings sett) {

		if (!sett.estado.isEnJuego())
			return;

		// 999 = default (NO perseguir)
		int perseguir = 999;

		if (this.x % this.tileX == 0 && this.y % this.tileY == 0) {

			int x = (int) (this.x / this.tileX);
			int y = (int) (this.y / this.tileY);

			for (int i = 0; i < ptosClave.length; i++) {

				int pClaveX = ptosClave[i][0];
				int pClaveY = ptosClave[i][1];

				if (x == pClaveX && y == pClaveY) {

					perseguir = generarRND(100);

					if (perseguir < calcularMaxPercPerseguir(sett)) {

						this.direccion = perseguirApacMan(this.x, this.y, coorPacMan[0], coorPacMan[1]);
						cambiarVelXY();
					}
				}
			}

			Boolean colisionVelXY = checkColisionLaberintoVelXY(x, y, this.velXY, matriz, sett);

			if (!colisionVelXY) {

				this.avanzar = true;

			} else {
				// rango 100%
				if (generarRND(100) < calcularMaxPercPerseguir(sett)) {

					this.direccion = perseguirApacMan(this.x, this.y, coorPacMan[0], coorPacMan[1]);

				} else {

					this.direccion = elegirOtraDireccionRND(otraDireccionRND, this.direccion);
				}

				cambiarVelXY();
			}
		}

		if (this.avanzar) {

			this.x += this.velXY[0] * this.vel;
			this.y += this.velXY[1] * this.vel;

			// Escapatorias (derecha izquierda)
			this.x = escapatorias(this.x, sett.laberinto.matriz[0].length, this.tileX, this.direccion);
		}
	}

	public int calcularMaxPercPerseguir(Settings sett) {

		int maxPercPerseguir = MIN_PERC_PERSEGUIR + sett.getNivel() * 4;
		if (maxPercPerseguir > MAX_PERC_PERSEGUIR)
			maxPercPerseguir = MAX_PERC_PERSEGUIR;

		return maxPercPerseguir;
	}

	public void cambiarVelXY() {

		this.velXY[0] = direcciones[this.direccion][0];
		this.velXY[1] = direcciones[this.direccion][1];
		this.avanzar = false;
	}

	public int[] getPacManPosition() {
		if (ventana.getPacman() == null) return new int[] {0, 0};
		
		int[] coorPac = { ventana.getPacman().getX(), ventana.getPacman().getY() };

		return coorPac;
	}

	public int generarRND(int rango) {
		return (int) (Math.random() * rango);
	}

	/*
	 * private void drawPtosClave(Graphics g) {
	 * 
	 * for (int i = 0; i < ptosClave.length; i++) { g.setColor(new Color(255, 50,
	 * 0)); g.fillArc(ptosClave[i][0] * this.tileX, ptosClave[i][1] * tileY,
	 * this.tileX, this.tileY, 0, 360); } }
	 */
	
	public void checkFantasmasResetCoor(Settings sett) {

		if (!sett.estado.isPacmanDying())
			return;
		
		int tiempo = sett.getDuracionPacManDying() - 400;
		long checkMiliSec = System.currentTimeMillis();
		
		if (Ventana.getMiliSec() + tiempo <= checkMiliSec) {
			
			setX(sett.getIniSprites()[this.id + 1][0] * this.tileX);
			setY(sett.getIniSprites()[this.id + 1][1] * this.tileY);
		}
	}
	
	public static void checkFinFantasmasAzules(Settings sett) {

		if (!getEstanAzules())
			return;

		long checkMiliSec = System.currentTimeMillis();

		if (Ventana.getMiliSec() + calcularDuracionAzulesNivel(sett) <= checkMiliSec) {

			Fantasma.setEstanAzules(false);
			if (PacMan.mientrasAzules.isRunning())
				PacMan.mientrasAzules.detenerSonido();
		}
	}

	private Boolean checkIntermitentes(Settings sett) {

		long checkMiliSec = System.currentTimeMillis();
		long intermitentes = (int) (calcularDuracionAzulesNivel(sett) / 2);

		if (Ventana.getMiliSec() + intermitentes <= checkMiliSec)
			return true;

		return false;
	}

	private void colorIntermitente(Graphics g) {

		if (colorIntermitente) {

			colorIntermitente = false;
			g.setColor(new Color(9, 225, 215));

		} else {

			colorIntermitente = true;
		}
	}

	public static int calcularDuracionAzulesNivel(Settings sett) {

		// Si nivel > 8 entonces ya duracion minima de 2900ms
		if (sett.getNivel() > 8)
			return 2900;

		int duracion = sett.getDuracionFantasmasAzulesNivel()[sett.getNivel()];
		// System.out.println(duracion);

		return duracion;
	}

	public int[] getCoorDim() {

		return new int[] { this.getX(), this.getY(), this.getTileX(), this.getTileY() };
	}

	public static Boolean getEstanAzules() {
		return estanAzules;
	}

	public static void setEstanAzules(Boolean estanAzules) {
		Fantasma.estanAzules = estanAzules;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public Boolean getEstaComido() {
		return estaComido;
	}

	public void setEstaComido(Boolean estaComido) {
		this.estaComido = estaComido;
	}
}
