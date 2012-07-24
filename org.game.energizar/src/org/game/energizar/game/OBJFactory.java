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

		final OBJ obj = new OBJ() {
			public Bitmap getBitmap() {
				switch (this.getDirection().value) {
				case Direction.VALUE_UP:
					return Bitmap.getBitmapResource("heroi.png");
				case Direction.VALUE_DOWN:
					return Bitmap.getBitmapResource("inimigo.png");
				default:
					return super.getBitmap();
				}
			}
		};
		obj.setPos(x, y);
		obj.setBitmap(Bitmap.getBitmapResource("explosao.png"));
		obj.setDirection(Direction.Left);

		obj.setTimer(new Timer(10, new Runnable() {
			public void run() {
				obj.setDirection(obj.getDirection().RotateCW());
				obj.getTimer().reset();
			}
		}));

		return obj;
	}
}
