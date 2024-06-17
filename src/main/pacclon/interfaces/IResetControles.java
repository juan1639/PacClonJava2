package main.pacclon.interfaces;

import main.pacclon.settings.Settings;

public interface IResetControles {
	
	default void resetControles(Boolean bool, Settings settings) {
		
		settings.controles.setIzquierda(bool);
		settings.controles.setDerecha(bool);
		settings.controles.setArriba(bool);
		settings.controles.setAbajo(bool);
	}
	
	default void resetEstados(Boolean bool, Settings settings) {
		
		settings.estado.setPreJuego(bool);
		settings.estado.setPreparado(bool);
		settings.estado.setEnJuego(bool);
		settings.estado.setNivelSuperado(bool);
		settings.estado.setGameOver(bool);
	}
}
