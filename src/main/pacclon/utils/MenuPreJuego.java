package main.pacclon.utils;

import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import main.pacclon.principal.Ventana;
import main.pacclon.settings.Settings;
import main.pacclon.audio.Sonidos;

public class MenuPreJuego {
	
	private Boolean existeOptionPane = false;

	public void preJuegoDialog(Settings settings, int newGame, int gameoverPane, Ventana ventana, String txt,
			String tit, Sonidos sonido, Timer timer) {
		
		if (!settings.estado.isPreJuego() && !settings.estado.isGameOver()) return;
		
		if (settings.estado.isPreJuego() && !existeOptionPane) {
			
			existeOptionPane = true;
			newGame = JOptionPane.showConfirmDialog(ventana, txt, tit, JOptionPane.CLOSED_OPTION);

			if (newGame == JOptionPane.OK_OPTION) {
				cambiarAestadoPreparado(settings, ventana, sonido, timer);
			}

		} else if (settings.estado.isGameOver()) {

			gameoverPane = JOptionPane.showConfirmDialog(ventana, "Volver a jugar?");

			if (gameoverPane == JOptionPane.NO_OPTION || gameoverPane == JOptionPane.CANCEL_OPTION) {

				Toolkit.getDefaultToolkit().beep();
				System.exit(0);

			} else if (gameoverPane == JOptionPane.YES_OPTION) {

				// reset_rejugar();
			}
		}
	}

	public void cambiarAestadoPreparado(Settings settings, Ventana ventana, Sonidos sonido, Timer timer) {

		settings.estado.setPreJuego(false);
		settings.estado.setPreparado(true);
		
		existeOptionPane = false;

		Toolkit.getDefaultToolkit().beep();
		sonido.detenerSonido();
		sonido.cargarAudio(settings.urlaudio.getPreparado());
		sonido.playSonido();

		ventana.instanciarTxtPreparado();

		timer.stop();
		timer = new Timer(4200, ventana);
		timer.start();
		timer.setRepeats(false);
	}
}
