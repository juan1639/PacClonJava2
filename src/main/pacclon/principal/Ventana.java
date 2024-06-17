package main.pacclon.principal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import main.pacclon.colisiones.Colisiones;
import main.pacclon.interfaces.IResetControles;
import main.pacclon.marcadores.Marcadores;
import main.pacclon.settings.Settings;
import main.pacclon.audio.Sonidos;
import main.pacclon.sprites.Fantasma;
import main.pacclon.sprites.PacMan;
import main.pacclon.sprites.Pared;
import main.pacclon.sprites.Puntitos;
import main.pacclon.utils.AreaTexto;
import main.pacclon.utils.MenuPreJuego;

public class Ventana extends JPanel implements ActionListener, IResetControles, Colisiones {
	
	private static final long serialVersionUID = 5143709932637106557L;
	
	private Settings settings;
	private Sonidos sonido;
	private Instancias instancias;

	private int newGame;
	private int gameoverPane;
	
	private AreaTexto txtPreparado;
	private MenuPreJuego preJuegoPane = new MenuPreJuego();
	
	public ArrayList<Pared> pared;
	public ArrayList<Puntitos> puntitos;
	
	public PacMan pacman;
	private Fantasma[] fantasma;

	private Marcadores marcador;
	private Marcadores nivel;
	private Marcadores hi;
	
	private Timer timer;
	private static long miliSec;
	private Boolean playerStart = false;
	
	public Ventana() {

		inicializa();
	}

	private void inicializa() {

		settings = Settings.getInstancia();
		sonido = new Sonidos();
		
		addKeyListener(new Controles());

		int[] rgb = settings.getColorFondos();
		setBackground(new Color(rgb[3], rgb[4], rgb[5]));
		setFocusable(true);
		setPreferredSize(new Dimension(settings.laberinto.RES_X, settings.laberinto.RES_Y));

		comenzar();
	}
	
	private void comenzar() {
		
		instancias = new Instancias(settings);
		settings.setReinstanciar_pacmanFantasmas(false);
		
		pared = instancias.instanciarParedesLaberinto();
		puntitos = instancias.instanciarPuntitosLaberinto();
		pacman = instancias.instanciarPacman();
		fantasma = instancias.instanciarFantasmas(this);
		marcador = instancias.instanciarMarcador();
		nivel = instancias.instanciarNivel();
		hi = instancias.instanciarHiScore();
		//instanciarTxtPreparado();
		
		sonido.cargarAudio(settings.urlaudio.getIntermision());
		sonido.playSonido();

		timer = new Timer((int) (1000 / settings.FPS), this);
		timer.start();
		timer.setRepeats(true);
	}
	
	public void instanciarTxtPreparado() {
		
		final int CX = (int) (settings.laberinto.RES_X / 2);
		final int CY = (int) (settings.laberinto.RES_Y / 2);
		txtPreparado = instancias.instanciarAreaTexto(90, CX, CY, " Preparado...", new int[] {115, 225, 9});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		renderiza(g);
	}

	private void renderiza(Graphics g) {

		int[][] matriz = settings.laberinto.matriz;

		for (Pared tile : pared) {
			tile.dibuja(g);
		}
		
		for (Puntitos tile : puntitos) {
			tile.dibuja(g, matriz, settings);
		}
		
		if (getPacman() != null) {
			getPacman().dibuja(g, matriz, settings);
		}
		
		for (int i = 0; i < settings.NUMERO_FANTASMAS; i ++) {
			
			if (fantasma[i] != null) {
				fantasma[i].dibuja(g, matriz, settings);
			}
		}

		marcador.dibuja(g, settings.getPuntos());
		nivel.dibuja(g, settings.getNivel());
		hi.dibuja(g, settings.getHiScore());
		
		if (txtPreparado != null) txtPreparado.dibuja(g);

		Toolkit.getDefaultToolkit().sync();
	}
	
	public void cambiarAestadoEnJuego() {

		if (!settings.estado.isPreparado()) return;

		settings.estado.setPreparado(false);
		settings.estado.setEnJuego(true);
		playerStart = true;
	}
	
	public void setPlayerStart() {
		
		if (!playerStart) return;
		
		playerStart = false;
		txtPreparado = null;
		
		timer = new Timer((int) (1000 / settings.FPS), this);
		timer.start();
		timer.setRepeats(true);
	}
	
	public Boolean checkIsNivelSuperado() {
		
		int contador = settings.laberinto.getContadorPuntitos();
		int contadorGordos = settings.laberinto.getContadorPuntitosGordos();
		
		if (contador <= 0 && contadorGordos <= 0) {
			
			System.out.println("Nivel superado!");
			resetEstados(false, settings);
			settings.estado.setNivelSuperado(true);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		preJuegoPane.preJuegoDialog(settings, newGame, gameoverPane, this,
				"Comenzar nueva partida...", "COMENZAR", sonido, timer);
		
		setPlayerStart();
		cambiarAestadoEnJuego();

		if (settings.estado.isEnJuego()) {
			// check SI Re-instanciarPacMan&Fantasmas
			checkReInstanciarPacmanYFantasmas();
			
			checkColisionesVsFantasmas();
			
			checkIsNivelSuperado();
			Fantasma.checkFinFantasmasAzules(settings);
		}
		
		repaint();
	}

	public void checkColisionesVsFantasmas() {
		// Check COLISIONES (Si son null... No checkear al no existir)
		if (getPacman() != null && fantasma[0] != null) {
			
			if (Fantasma.getEstanAzules()) {
				// Si estan "azules"...
				for (int i = 0; i < settings.NUMERO_FANTASMAS; i ++) {
					
					if (checkColision(pacman.getCoorDim(), fantasma[i].getCoorDim())) {
						fantasma[i].setEstaComido(true);
					}
				}
				
			} else {
				//Boolean prueba = checkColision(pacman.getCoorDim(), fantasma[0].getCoorDim());
				// Si estan "normales"...
				for (int i = 0; i < settings.NUMERO_FANTASMAS; i ++) {
					
					if (checkColision(pacman.getCoorDim(), fantasma[i].getCoorDim())) {
						
						System.out.println("colision");
						resetEstados(false, settings);
						settings.estado.setPacmanDying(true);
						setMiliSec(System.currentTimeMillis());
						
						sonido.cargarAudio(settings.urlaudio.getPacmandies());
						sonido.playSonido();
						break;
					}
				}
			}
		}
	}

	public void checkReInstanciarPacmanYFantasmas() {
		
		if (settings.getReinstanciar_pacmanFantasmas()) {
			
			for (int i = 0; i < settings.NUMERO_FANTASMAS; i ++) {
				fantasma[i] = null;
			}
			
			fantasma = instancias.instanciarFantasmas(this);
			
			resetControles(false, settings);
			setPacman(null);
			setPacman(instancias.instanciarPacman());
			settings.setReinstanciar_pacmanFantasmas(false);
		}
	}
	
	private class Controles extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (settings.estado.isEnJuego()) {

				if (key == KeyEvent.VK_LEFT) {
					resetControles(false, settings);
					settings.controles.setIzquierda(true);
				}

				if (key == KeyEvent.VK_RIGHT) {
					resetControles(false, settings);
					settings.controles.setDerecha(true);
				}

				if (key == KeyEvent.VK_UP) {
					resetControles(false, settings);
					settings.controles.setArriba(true);
				}

				if (key == KeyEvent.VK_DOWN) {
					resetControles(false, settings);
					settings.controles.setAbajo(true);
				}
			}

			if (key == KeyEvent.VK_ESCAPE) {
				Toolkit.getDefaultToolkit().beep();
				System.exit(0);
			}
		}
	}
	
	// Getters & Setters
	public PacMan getPacman() {
		return pacman;
	}

	public void setPacman(PacMan pacman) {
		this.pacman = pacman;
	}

	public static long getMiliSec() {
		return miliSec;
	}

	public static void setMiliSec(long miliSec) {
		Ventana.miliSec = miliSec;
	}
}
