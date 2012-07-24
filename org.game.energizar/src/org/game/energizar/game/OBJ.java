package org.game.energizar.game;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

// OBJ is our root class for all objects, it defines behaviors and properties
// that are the same for all objects, whether its the hero, enemies, or photons
public class OBJ {

	int _posX, _posY; // All objects have position and velocity

	Bitmap _bitmap; // Bitmap to be drawn for the object

	// Objects are initialized globally with a position
	OBJ(int x, int y, Bitmap bitmap) {
		this._posX = x;
		this._posY = y;
		this._bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return _bitmap;
	}

	public int getX() {
		return _posX;
	}

	public int getY() {
		return _posY;
	}

	// A quick method simply to ensure screen bound objects don't go off screen.
	// For now this is just our hero, but there may be other objects that
	// function
	// like this.
	public void boundToScreen() {
		// If the coordinates are off screen in any direction, correct the
		// coordinate and
		// set that velocity to 0
		if (_posX < 0) {
			_posX = 0;
		}

		if (_posY < 0) {
			_posY = 0;
		}

		if (_posX > Display.getWidth() - _bitmap.getWidth()) {
			_posX = Display.getWidth() - _bitmap.getWidth();
		}

		if (_posY > Display.getHeight() - _bitmap.getHeight()) {
			_posY = Display.getHeight() - _bitmap.getHeight();
		}

	}

	public void moveUp() {
		this._posY -= 10;
		boundToScreen();
	}

	public void moveDown() {
		this._posY += 10;
		boundToScreen();
	}

	public void moveLeft() {
		this._posX -= 10;
		boundToScreen();
	}

	public void moveRight() {
		this._posX += 10;
		boundToScreen();
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
