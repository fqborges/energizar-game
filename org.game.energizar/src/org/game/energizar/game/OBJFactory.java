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

		final OBJ obj = new OBJ(OBJ.JUNCTION) {
			public void update(long gameTime, GameLevel gameData) {
				if (this.isJunctionStateChanged()) {
					this.clearJunctionStateChanged();

					switch (this.getJunctionState()) {
					case OBJ.ON:
						setJunctionSpryteForDirection(this);
						Timer timer = createJunctionTimer(this);
						this.setTimer(timer);
						break;
					case OBJ.CONNECTED:
					case OBJ.OFF:
						if (this.getTimer() != null) {
							this.getTimer().disable();
							this.setTimer(null);
						}
						break;
					default:
						break;
					}
				}
			}
		};

		obj.setPos(x, y);
		obj.setDirection(Direction.Left);
		// sprite apagado olhando para esquerda
		obj.setSpriteBitmap(Bitmap.getBitmapResource("gameart.png"));
		obj.setSpriteRect(2 * 64, 0 * 64, 64, 64);
		return obj;
	}

	private Timer createJunctionTimer(final OBJ obj) {
		Timer timer = new Timer(10, new Runnable() {
			public void run() {
				if (obj.getJunctionState() != OBJ.ON) {
					return;
				}

				obj.setDirection(obj.getDirection().RotateCW());
				setJunctionSpryteForDirection(obj);
				obj.getTimer().reset();
			}
		});
		return timer;
	}

	private static void setJunctionSpryteForDirection(final OBJ obj) {
		int row = 1;
		int col = 0;
		switch (obj.getDirection().value) {
		case Direction.VALUE_UP:
			col = 0;
			break;
		case Direction.VALUE_UP_LEFT:
			col = 1;
			break;
		case Direction.VALUE_LEFT:
			col = 2;
			break;
		case Direction.VALUE_DOWN_LEFT:
			col = 3;
			break;
		case Direction.VALUE_DOWN:
			col = 4;
			break;
		case Direction.VALUE_DOWN_RIGHT:
			col = 5;
			break;
		case Direction.VALUE_RIGHT:
			col = 6;
			break;
		case Direction.VALUE_UP_RIGTH:
			col = 7;
			break;
		default:
			break;
		}
		obj.setSpriteRect(col * 64, row * 64, 64, 64);
	}

	public OBJ createStartPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJ.STARTPOINT);
		obj.setSpriteBitmap(Bitmap.getBitmapResource("gameart.png"));
		obj.setDirection(Direction.Right);
		obj.setSpriteRect(6 * 64, 1 * 64, 64, 64);
		obj.setPos(x, y);
		return obj;
	}

	public OBJ createEndPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJ.ENDPOINT);
		obj.setSpriteBitmap(Bitmap.getBitmapResource("gameart.png"));
		obj.setSpriteRect(0 * 64, 2 * 64, 64, 64);
		obj.setPos(x, y);
		return obj;
	}
}
