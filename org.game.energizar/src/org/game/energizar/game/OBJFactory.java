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

	public OBJ createJunction() {

		return new OBJ(20, 20, Bitmap.getBitmapResource("explosao.png"));
	}

}
