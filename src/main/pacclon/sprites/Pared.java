package main.pacclon.sprites;

import java.awt.Color;
import java.awt.Graphics;

public class Pared {

	private int x;
	private int y;
	private int tileX;
	private int tileY;

	public Pared(int x, int y, int tileX, int tileY) {

		this.tileX = tileX;
		this.tileY = tileY;
		this.x = x * this.tileX;
		this.y = y * this.tileY;
	}

	public void dibuja(Graphics g) {

		//int[] rgb = {143, 127, 9, 163, 150, 12};
		//int[] rgb = {155, 100, 9, 185, 113, 55};
		
		// Color Paredes --> 0,1,2 masClaro --> 3,4,5 masOscuro 
		int[] rgb = {157, 157, 98, 128, 128, 82};
		
		g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
		g.fillRect(this.x, this.y, this.tileX - 1, this.tileY - 1);

		g.setColor(new Color(rgb[3], rgb[4], rgb[5]));
		g.fillRect(this.x, this.y, this.tileX - 3, this.tileY - 3);
	}
}
