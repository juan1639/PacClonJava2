package main.pacclon.audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sonidos {

	private Clip clip;
	private float bajar_volumen_audios = -10.0f;

	public Sonidos() {
	}

	public void cargarAudio(String ruta) {

		try {

			File sonido_intermision = new File(ruta);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sonido_intermision);

			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(bajar_volumen_audios);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public void playSonido() {
		
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void detenerSonido() {
		
		if (clip != null && clip.isRunning()) {
			clip.stop();
		}
	}
	
	
	public void playSonidoLoop() {
		
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
			clip.loop(clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public Boolean isRunning() {
		return clip.isRunning();
	}
	
	public Clip getClip() {
		return clip;
	}
}

