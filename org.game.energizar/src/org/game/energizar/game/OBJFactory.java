package org.game.energizar.game;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYPoint;

import org.game.energizar.game.datatypes.Direction;
import org.game.energizar.game.datatypes.Path;
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
				obj.setPos(obj.getPos().x + move.x, obj.getPos().y + move.y);
			}
		}));
		return obj;
	}

	public OBJ createConnection(OBJ sourceObj, OBJ targetObj) {

		final OBJ obj = new OBJ(OBJType.CONNECTION) {
			public void update(long gameTime, GameLevel gameData) {
				OBJ source = this.getConnectionSourceObject();
				if (source != null && source.getPos() != null) {
					if (!source.getPos().equals(this.getPath().getFirst())) {
						this.getPath().addToBegining(source.getPos());
					}
				}

				OBJ target = this.getConnectionTargetObject();
				if (target != null && target.getPos() != null) {
					if (!target.getPos().equals(this.getPath().getLast())) {
						this.getPath().addToEnd(target.getPos());
					}
				}

			}
		};
		Sprite sprite = null;
		XYPoint srcPos = sourceObj.getPos();
		XYPoint tgtPos = targetObj.getPos();
		if (srcPos.equals(tgtPos)) {
			// ponto
			sprite = ArtResource.instance().getSprite(2, 2);
		} else if (srcPos.x == tgtPos.x) {
			// em pe
			sprite = ArtResource.instance().getSprite(3, 0);
		} else if (srcPos.y == tgtPos.y) {
			// deitado
			sprite = ArtResource.instance().getSprite(3, 1);
		} else {
			int coeficienteAngular = (tgtPos.y - srcPos.y)
					/ (tgtPos.x - srcPos.x);
			if (coeficienteAngular > 0) {
				// ascendente
				sprite = ArtResource.instance().getSprite(3, 2);
			} else { // coeficienteAngular < 0
				// descendente
				sprite = ArtResource.instance().getSprite(3, 3);
			}
		}

		obj.setSprite(sprite);

		obj.setConnectionSourceObject(sourceObj);
		obj.setConnectionTargetObject(targetObj);
		obj.setPath(new Path(srcPos, tgtPos));

		return obj;
	}
}
