package org.game.energizar.game;

import org.game.energizar.sprites.Sprite;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYPoint;

public class OBJFactory {

	// Singleton field
	private static OBJFactory _instance = new OBJFactory();

	// singleton
	public static OBJFactory instance() {
		return OBJFactory._instance;
	}

	// sigleton
	private OBJFactory() {
	}

	public OBJ createJunction(int x, int y) {
		return new JunctionFactory().createJunction(x, y);
	}

	public OBJ createStartPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.STARTPOINT);

		obj.setDirection(Direction.Right);
		obj.setPos(x, y);

		int row = 2;
		int col = 7;
		Sprite sprite = new Sprite(Bitmap.getBitmapResource("gameart.png"),
				col * 64, row * 64, 64, 64);
		obj.setSprite(sprite);

		return obj;
	}

	public OBJ createEndPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.ENDPOINT);

		obj.setPos(x, y);

		int row = 2;
		int col = 0;
		Sprite sprite = new Sprite(Bitmap.getBitmapResource("gameart.png"),
				col * 64, row * 64, 64, 64);
		obj.setSprite(sprite);

		return obj;
	}

	public OBJ createBullet(int x, int y, Direction direction) {
		final OBJ obj = new OBJ(OBJType.BULLET);

		obj.setPos(x, y);
		obj.setDirection(direction);

		int row = 2;
		int col = 2;
		Sprite sprite = new Sprite(Bitmap.getBitmapResource("gameart.png"),
				col * 64, row * 64, 64, 64);
		obj.setSprite(sprite);

		obj.setTimer(new Timer(5, new Runnable() {
			public void run() {
				XYPoint move = obj.getDirection().toMoveIncrement();

				obj.setPos(obj.getX() + move.x, obj.getY() + move.y);
			}
		}));
		return obj;
	}

	public OBJ createConnection(OBJ sourceObj, OBJ targetObj) {

		final OBJ obj = new OBJ(OBJType.CONNECTION);

		obj.setConnectionSourceObject(sourceObj);
		obj.setConnectionTargetObject(targetObj);

		return obj;
	}

	/**
	 * classe que é responsável por criar uma objeto do tipo junction.
	 */
	private class JunctionFactory {

		// cria um objeto do tipo 'junction'
		public OBJ createJunction(int x, int y) {

			final OBJ obj = new OBJ(OBJType.JUNCTION) {
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
			int row = 0;
			int col = 2;
			Sprite sprite = new Sprite(Bitmap.getBitmapResource("gameart.png"),
					col * 64, row * 64, 64, 64);
			obj.setSprite(sprite);

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

		private void setJunctionSpryteForDirection(final OBJ obj) {
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
			obj.getSprite().setSpriteRect(col * 64, row * 64, 64, 64);
		}
	}
}
