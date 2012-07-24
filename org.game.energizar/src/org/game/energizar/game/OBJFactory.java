package org.game.energizar.game;

import net.rim.device.api.system.Bitmap;

public class OBJFactory {

	// Singleton field
	private static OBJFactory _instance = new OBJFactory();

	// singleton
	public static OBJFactory instance() {
		return OBJFactory._instance;
	}

	private OBJFactory() {
	}

	// cria um objeto do tipo 'junction'
	public OBJ createJunction(int x, int y) {
		return new OBJ(x, y, Bitmap.getBitmapResource("explosao.png"));
	}

}
