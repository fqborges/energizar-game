package org.game.energizar.game;

import java.util.Vector;

import net.rim.device.api.ui.XYPoint;

import org.game.energizar.game.datatypes.Direction;
import org.game.energizar.game.datatypes.Path;
import org.game.energizar.game.features.SimpleSpriteProvider;
import org.game.energizar.game.features.SpriteProvider;
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

			private int _lastJunctionState;
			private boolean _lastIsSourceState;
			private boolean _firstTime = true;

			public void update(long gameTime, final GameLevel gameData) {

				// verify if state changed
				if (stateChanged(gameData)) {
					// if now it is power on and disconnected
					if (this.getJunctionState() == OBJ.JUNCTION_ON
							&& !OBJ.isSourceOfAnyConnection(this, gameData)) {
						final OBJ thisRef = this;
						this.setTimer(new Timer(10) {
							protected void run(GameLevel gameData) {
								// if junction is off or is connected then
								// disarm
								if (thisRef.getJunctionState() == OBJ.JUNCTION_OFF
										|| OBJ.isSourceOfAnyConnection(thisRef,
												gameData)) {
									// disarm and unset clock
									thisRef.getTimer().disable();
									thisRef.setTimer(null);
								} else {
									// else rotate the object CCW and
									// run again
									thisRef.setDirection(thisRef.getDirection()
											.RotateCW());
									thisRef.getTimer().reset();
								}
							}
						});
					}
				}
			}

			/**
			 * @return
			 */
			private boolean stateChanged(GameLevel gameData) {
				boolean isSourceState = OBJ.isSourceOfAnyConnection(this,
						gameData);

				// if state changed or first time it runs
				if (this.getJunctionState() != _lastJunctionState
						|| _lastIsSourceState != isSourceState || _firstTime) {
					// copy state for next run
					_lastJunctionState = this.getJunctionState();
					_lastIsSourceState = isSourceState;
					// clear first time guard
					_firstTime = false;
					return true;
				} else {
					return false;
				}
			}
		};

		obj.setPos(x, y);
		obj.setDirection(Direction.Left);

		SpriteProvider spriteProvider = new SpriteProvider() {
			public Sprite getSprite() {
				// row defined by junction state
				int row = obj.getJunctionState() == OBJ.JUNCTION_OFF ? 0 : 1;
				// col defined by direction
				int col = 0;
				Direction direction = Direction.Up;
				do {
					if (obj.getDirection().equals(direction))
						break;
					col++;
					direction = direction.RotateCCW();
				} while (direction != Direction.Up);

				return ArtResource.instance().getSprite(row, col);
			}
		};
		obj.setSpriteProvider(spriteProvider);

		return obj;
	}

	public OBJ createStartPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.STARTPOINT);

		obj.setDirection(Direction.Right);
		obj.setPos(x, y);

		int row = 2;
		int col = 7;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
		obj.setSpriteProvider(new SimpleSpriteProvider(sprite));

		return obj;
	}

	public OBJ createEndPoint(int x, int y) {
		final OBJ obj = new OBJ(OBJType.ENDPOINT);

		obj.setPos(x, y);

		int row = 2;
		int col = 0;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
		obj.setSpriteProvider(new SimpleSpriteProvider(sprite));

		return obj;
	}

	public OBJ createBullet(int x, int y, Direction direction) {
		final OBJ obj = new OBJ(OBJType.BULLET);

		obj.setPos(x, y);
		obj.setDirection(direction);

		int row = 2;
		int col = 2;
		Sprite sprite = ArtResource.instance().getSprite(row, col);
		obj.setSpriteProvider(new SimpleSpriteProvider(sprite));

		obj.setTimer(new Timer(4) {
			protected void run(GameLevel gameLevel) {
				XYPoint move = obj.getDirection().toMoveIncrement();
				obj.setPos(obj.getPos().x + move.x, obj.getPos().y + move.y);
			}
		});
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
		XYPoint srcPos = sourceObj.getPos();
		XYPoint tgtPos = targetObj.getPos();

		obj.setSpriteProvider(new SpriteProvider() {

			private Sprite[] _sprites = null;
			private XYPoint _first = null;
			private XYPoint _last = null;

			public Sprite getSprite() {
				Sprite[] sprites = this.getSprites();
				if (sprites != null && sprites.length > 0) {
					return sprites[0];
				} else
					return null;
			}

			public Sprite[] getSprites() {
				if (pathChanged()) {
					_sprites = this.createSprites(obj);
				}
				return _sprites;
			}

			private boolean pathChanged() {
				if (obj.getPath().getFirst() != _first
						|| obj.getPath().getLast() != _last) {
					return true;
				}
				return false;
			}

			private Sprite[] createSprites(final OBJ obj) {

				XYPoint srcPos = obj.getPath().getFirst();
				XYPoint tgtPos = obj.getPath().getLast();

				// where we get sprites
				ArtResource art = ArtResource.instance();

				Sprite segmentSprite = null;
				Sprite beginingSprite = art.getSprite(2, 2);
				Sprite endSprite = art.getSprite(2, 2);
				if (srcPos.equals(tgtPos)) {
					// ponto
					segmentSprite = art.getSprite(2, 2);
				} else if (srcPos.x == tgtPos.x) {
					// em pe
					segmentSprite = art.getSprite(3, 0);
				} else if (srcPos.y == tgtPos.y) {
					// deitado
					segmentSprite = art.getSprite(3, 1);
				} else {
					int coeficienteAngular = (tgtPos.y - srcPos.y)
							/ (tgtPos.x - srcPos.x);
					if (coeficienteAngular > 0) {
						// descendente
						segmentSprite = art.getSprite(3, 2);
						if (tgtPos.x > srcPos.x) {
							beginingSprite = art.getSprite(3, 6);
							endSprite = art.getSprite(3, 4);
						} else {
							beginingSprite = art.getSprite(3, 4);
							endSprite = art.getSprite(3, 6);
						}
					} else { // coeficienteAngular < 0
						// ascendente
						segmentSprite = art.getSprite(3, 3);
						if (tgtPos.x > srcPos.x) {
							beginingSprite = art.getSprite(3, 7);
							endSprite = art.getSprite(3, 5);
						} else {
							beginingSprite = art.getSprite(3, 5);
							endSprite = art.getSprite(3, 7);
						}
					}
				}

				Vector sprites = new Vector();
				sprites.addElement(beginingSprite);
				for (int i = 0; i < obj.getPath().length() - 2; i++) {
					sprites.addElement(segmentSprite);
				}
				sprites.addElement(endSprite);

				Sprite[] retorno = new Sprite[obj.getPath().length()];
				sprites.copyInto(retorno);
				return retorno;
			}
		});

		obj.setConnectionSourceObject(sourceObj);
		obj.setConnectionTargetObject(targetObj);
		obj.setPath(new Path(srcPos, tgtPos));

		return obj;
	}
}
