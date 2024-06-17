package main.pacclon.marcadores;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Marcadores {

	private int id;
	private int size;
	private int x;
	private int y;
	private String txt;

	public Marcadores(int id, int size, int x, int y, String txt) {

		this.id = id;
		this.size = size;
		this.x = x;
		this.y = y;
		this.txt = txt;
	}

	public void dibuja(Graphics g, int valor) {

		int id = this.id;
		int[] rgb = {235, 235, 135, 225, 225, 9, 235, 200, 9};

		Font fuente = new Font("Helvetica", Font.BOLD, this.size);

		String textoScore = this.txt + valor;

		g.setFont(fuente);
		g.setColor(new Color(rgb[id * 3], rgb[id * 3 + 1], rgb[id * 3 + 2]));
		g.drawString(textoScore, this.x, this.y - 4);
	}

	public static int checkNuevoRecord(int marcador, int hi) {

		if (marcador > hi) {
			return marcador;
		}

		return hi;
	}
}
