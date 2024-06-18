package main.pacclon.interfaces;

import main.pacclon.principal.Ventana;
import main.pacclon.settings.Settings;

public interface TransicionesPreJuego {
	
	default void cambiarAestadoEnJuego(Settings settings) {
		
		if (!settings.estado.isPreparado()) return;
		
		long checkMiliSec = System.currentTimeMillis();
		
		// 4200ms = duracion 'musica preparado'...
		if (Ventana.getMiliSec() + 4200 <= checkMiliSec) {

			settings.estado.setPreparado(false);
			settings.estado.setEnJuego(true);
		}
	}
}
