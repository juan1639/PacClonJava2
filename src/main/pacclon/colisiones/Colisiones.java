package main.pacclon.colisiones;

public interface Colisiones {
	
	default Boolean checkColision(int[] args1, int[] args2) {
		
		//System.out.println(args1[0]);
		//System.out.println(args2[0]);
		
		int x1 = args1[0];
		int y1 = args1[1];
		int ancho1 = args1[2];
		int alto1 = args1[3];
		
		int x2 = args2[0];
		int y2 = args2[1];
		int ancho2 = args2[2];
		int alto2 = args2[3];
		
	 	return x1 < x2 + ancho2 &&
	 			x1 + ancho1 > x2 &&
	 			y1 < y2 + alto2 &&
	 			y1 + alto1 > y2;
	}
}
