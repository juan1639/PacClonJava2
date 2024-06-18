package main.pacclon.interfaces;

import main.pacclon.principal.Ventana;
import main.pacclon.settings.Settings;

public interface TransicionesPreJuego {
	
	default void cambiarAestadoEnJuego(Settings settings) {
		
		if (!settings.estado.isPreparado()) return;
		
		long checkMiliSec = System.currentTimeMillis();
		
		// JUEGO COMIENZA CUANDO TERMINA 'Musica PREPARADO'...
		if (Ventana.getMiliSec() + settings.DURACION_MUSICA_PREPARADO <= checkMiliSec) {

			settings.estado.setPreparado(false);
			settings.estado.setEnJuego(true);
		}
	}
}
