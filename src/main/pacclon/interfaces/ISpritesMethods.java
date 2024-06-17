package main.pacclon.interfaces;

import main.pacclon.settings.Settings;

public interface ISpritesMethods extends ISpritesDraw {
	
	// Para calcular la velocidad -> divide TILE / DIVIDIR_TILE_ENTRE
	// Ejemplo -> ( 50 / 10 = 5px es la Velocidad) 
	static int DIVIDIR_TILE_ENTRE = 10;
	
	void actualiza(int[][] matriz, Settings sett);
	
	default Boolean checkColisionLaberintoVelXY(int x, int y, int[] velXY, int[][] matriz, Settings sett) {
		
		// PRIMERO VERIFICAMOS QUE EL VALOR ESTE DENTRO DEL ARRAY...
		// ...(evitando ERROR 'Index out of range'
		if (x + velXY[0] < 0 || x + velXY[0] >= sett.laberinto.matriz[0].length) return false;
		
		if (matriz[y + velXY[1]][x + velXY[0]] == sett.laberinto.PARED) {
			return true;
		}

		return false;
	}
	
	default int escapatorias(int x, int laberintoLength, int tileX, int direccion) {
		
		if (x > laberintoLength * tileX && direccion == 0) {
			return -(tileX);
        }

        if (x < -(tileX) && direccion == 1) {
        	return (laberintoLength - 1) * tileX;
        }
        
        return x;
	}
}
