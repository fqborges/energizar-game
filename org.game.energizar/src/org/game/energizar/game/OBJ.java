package org.game.energizar.game;

import org.game.energizar.sprites.Sprite;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYRect;

// OBJ is our root class for all objects, it defines behaviors and properties
// that are the same for all objects, whether its the hero, enemies, or photons
public class OBJ {

	// todo objeto tem um id do tipo
	private char _typeID;

	/**
	 * Cria um objeto com o tipo informado.
	 */
	OBJ(char typeID) {
		this._typeID = typeID;
	}

	public long getTypeID() {
		return _typeID;
	}

	// FEATURE update
	public void update(long gameTime, GameLevel gameData) {
	}

	// FEATURE posição
	private int _posX = -1, _posY = -1; // posição

	public int getX() {
		return _posX;
	}

	public int getY() {
		return _posY;
	}

	public void setPos(int x, int y) {
		this._posX = x;
		this._posY = y;
	}

	// FEATURE Sprite
	private Sprite _sprite;

	public Sprite getSprite() {
		return _sprite;
	}

	public void setSprite(Sprite sprite) {
		this._sprite = sprite;
	}

	// FEATURE Direção
	private Direction _direction = null;

	public Direction getDirection() {
		return _direction;
	}

	void setDirection(Direction _direction) {
		this._direction = _direction;
	}

	// FEATURE Timer
	private Timer _timer;

	public Timer getTimer() {
		return _timer;
	}

	void setTimer(Timer _timer) {
		this._timer = _timer;
	}

	// FEATURE shooter
	protected final static int SHOOTING = 0;
	protected final static int IDLE = 1;
	private int _shootingState = IDLE;

	public void shoot() {
		this._shootingState = SHOOTING;
	}

	public boolean isShooting() {
		return (this._shootingState == SHOOTING);
	}

	public void notifyShotHandled() {
		this._shootingState = IDLE;
	}

	// FEATURE junction
	protected final static int OFF = 0;
	protected final static int ON = 1;
	protected final static int CONNECTED = 2;

	private int _junctionState = OFF;
	private boolean _junctionStateChanged = false;

	private void setJunctionState(int state) {
		if (this._junctionState != state) {
			this._junctionState = state;
			_junctionStateChanged = true;
		}
	}

	public int getJunctionState() {
		return _junctionState;
	}

	public boolean isJunctionStateChanged() {
		return _junctionStateChanged;
	}

	public void clearJunctionStateChanged() {
		this._junctionStateChanged = false;
	}

	public void powerOn() {
		this.setJunctionState(ON);
	};

	public void notifyConected() {
		this.setJunctionState(CONNECTED);
	};

	// Feature Connection
	private OBJ _connectionSourceObj;

	public void setConnectionSourceObject(OBJ sourceObj) {
		this._connectionSourceObj = sourceObj;
	}

	public OBJ getConnectionSourceObject() {
		return _connectionSourceObj;
	}

	private OBJ _connectionTargetObj;

	public void setConnectionTargetObject(OBJ targetOBj) {
		this._connectionTargetObj = targetOBj;
	}

	public OBJ getConnectionTargetObject() {
		return _connectionTargetObj;
	}

	// // Collision detection routine using an AABB test (Axis Align Bounding
	// Box).
	// // This
	// // is a quick and easy test great for games with simple squarish sprites
	// // which simply
	// // looks to see if the bounding boxes overlap in any way.
	// public static void collisionDetect(Vector passObjects) {
	// OBJ tempObject1, tempObject2; // temporarily points to the two objects
	// // being tested
	// boolean intersect, check; // flags during testing
	//
	// // Loop through all objects in our vector
	// for (int lcv = 0; lcv < passObjects.size(); lcv++) {
	// // Set tempObject1 to the current object
	// tempObject1 = (OBJ) passObjects.elementAt(lcv);
	//
	// // Now loop from the current object to the end of the vector
	// for (int lcv2 = lcv; lcv2 < passObjects.size(); lcv2++) {
	// // Set tempObject2 to the current object of the nested loop
	// tempObject2 = (OBJ) passObjects.elementAt(lcv2);
	//
	// // See if we need to check for collision (e.g. some objects dont
	// // matter if
	// // they collide, enemy with enemy or fire with fire for example)
	//
	// // Assume we dont need to check
	// check = false;
	//
	// // Hero and enemy would be something to check for
	// if (tempObject1.getType() == "hero"
	// && tempObject2.getType().startsWith("enemy"))
	// check = true;
	//
	// // Hero and enemy fired photons would be something to check for
	// if (tempObject1.getType() == "hero"
	// && tempObject2.getType().startsWith("fire")
	// && tempObject2.getParent().getType()
	// .startsWith("enemy"))
	// check = true;
	//
	// // Enemy and hero fired photons would be something ot check for
	// if (tempObject1.getType().startsWith("enemy")
	// && tempObject2.getType().startsWith("fire")
	// && tempObject2.getParent().getType() == "hero")
	// check = true;
	//
	// // If our check flag is set to true, and the state of the
	// // objects is normal
	// // (e.g. an object in a hit or exploded state can't collide with
	// // something),
	// // then lets check for the actual collision
	// if (check && tempObject1.getState() == 0
	// && tempObject2.getState() == 0) {
	//
	// // We assume the two objects collided
	// intersect = true;
	//
	// // Left and Right sides of bounding box check
	// if (!(Math.abs((tempObject1.getX() + tempObject1
	// .getBitmap().getWidth() / 2)
	// - (tempObject2.getX() + tempObject2.getBitmap()
	// .getWidth() / 2)) <= tempObject1
	// .getBitmap().getWidth()
	// / 2
	// + tempObject2.getBitmap().getWidth() / 2))
	// intersect = false;
	//
	// // Top and Bottom sides of bounding box check
	// if (!(Math.abs((tempObject1.getY() + tempObject1
	// .getBitmap().getHeight() / 2)
	// - (tempObject2.getY() + tempObject2.getBitmap()
	// .getHeight() / 2)) <= tempObject1
	// .getBitmap().getHeight()
	// / 2
	// + tempObject2.getBitmap().getHeight() / 2))
	// intersect = false;
	//
	// // If the objects collided, damage each one.
	// if (intersect) {
	//
	// }
	// }
	// }
	// }
	// }

}
