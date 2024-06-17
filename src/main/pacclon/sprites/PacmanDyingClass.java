package main.pacclon.sprites;

import java.awt.Color;
import java.awt.Graphics;

import main.pacclon.principal.Ventana;
import main.pacclon.settings.Settings;

public class PacmanDyingClass {
	
	public static void dyingAnima(Graphics g, Settings settings, int[] rgb, int x, int y, int contadorAnima) {
		
		int tileX = settings.laberinto.TILE_X;
		int tileY = settings.laberinto.TILE_Y;
		
		g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
		g.fillArc(x, y, tileX, tileY, 45 + contadorAnima * 36, 270);
		
		checkFinPacManDies(settings);
	}
	
	private static void checkFinPacManDies(Settings settings) {
		
		long checkMiliSec = System.currentTimeMillis();

		if (Ventana.getMiliSec() + settings.getDuracionPacManDying() <= checkMiliSec) {
			
			settings.estado.setPacmanDying(false);
			settings.estado.setEnJuego(true);
			settings.setReinstanciar_pacmanFantasmas(true);
		}
	}
}
