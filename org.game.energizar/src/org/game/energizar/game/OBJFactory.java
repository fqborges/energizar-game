package org.game.energizar.game;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYPoint;

import org.game.energizar.sprites.Sprite;

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

	// cria um objeto do tipo 'junction'
	public OBJ createJunction(int x, int y) {

		final OBJ obj = new OBJ(OBJType.JUNCTION) {
			public void update(long gameTime, GameLevel gameData) {

				// update sprite
				int row = this.getJunctionState() == OBJ.OFF ? 0 : 1;
				int col = 0;
				Direction direction = Direction.Up;
				do {
					if (this.getDirection().equals(direction))
						break;
					col++;
					direction = direction.RotateCCW();
				} while (direction != Direction.Up);

				this.setSprite(ArtResource.instance().getSprite(row, col));

				// verify if state changed
				if (this.isJunctionStateChanged()) {
					this.clearJunctionStateChanged();

					switch (this.getJunctionState()) {
					case OBJ.ON:
						final OBJ thisRef = this;
						Timer timer = new Timer(20, new Runnable() {
							public void run() {
								if (thisRef.getJunctionState() != OBJ.ON) {
									return;
								}
								thisRef.setDirection(thisRef.getDirection()
										.RotateCW());
								thisRef.getTimer().reset();
							}
						});
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
		Sprite sprite = new Sprite(Bitmap.getBitmapResource("gameart.png"),
				0 * ArtResource.SPRITE_SIZE, 0 * ArtResource.SPRITE_SIZE,
				ArtResource.SPRITE_SIZE, ArtResource.SPRITE_SIZE);

		obj.setSprite(sprite);

		return obj;
	}

	public OBJ createStartPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.STARTPOINT);

		obj.setDirection(Direction.Right);
		obj.setPos(x, y);

		int row = 2;
		int col = 7;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
		obj.setSprite(sprite);

		return obj;
	}

	public OBJ createEndPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.ENDPOINT);

		obj.setPos(x, y);

		int row = 2;
		int col = 0;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
		obj.setSprite(sprite);

		return obj;
	}

	public OBJ createBullet(int x, int y, Direction direction) {
		final OBJ obj = new OBJ(OBJType.BULLET);

		obj.setPos(x, y);
		obj.setDirection(direction);

		int row = 2;
		int col = 2;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
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

		final OBJ obj = new OBJ(OBJType.CONNECTION) {
			public void update(long gameTime, GameLevel gameData) {
				OBJ source = this.getConnectionSourceObject();
				if (source != null) {
					this.setPos(source.getX(), source.getY());
				}
				OBJ target = this.getConnectionTargetObject();
				if (target != null) {
					this.setPos(target.getX(), target.getY());
				}

				if (source == target) {
					this.setSprite(ArtResource.instance().getSprite(2, 2));
				}

			}
		};

		Sprite sprite = ArtResource.instance().getSprite(2, 2);
		obj.setSprite(sprite);
		obj.setConnectionSourceObject(sourceObj);
		obj.setConnectionTargetObject(targetObj);

		return obj;
	}
}
