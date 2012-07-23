package org.game.energizar;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

// OBJ is our root class for all objects, it defines behaviors and properties
// that are the same for all objects, whether its the hero, enemies, or photons
class OBJ {
	// Objects can have different states, these are general ones
	public static int STATE_NORMAL = 0; // Object is alive and functioning
										// normally
	public static int STATE_HIT = 100; // Object just got hit, flash red
	public static int STATE_DYING = 1000; // Object is dying, show an explosion

	int _posX, _posY, _velX, _velY; // All objects have position and velocity
	int _life, _value, _state; // All objects have life, point value, and a
								// current state
	OBJ _parent; // All objects can have a parent (e.g. a photon belongs to the
					// object that shot it)
	String _type; // Type is a string that stores what the object is for easy
					// reference
	Bitmap _bitmap; // Bitmap to be drawn for the object

	// Objects are initialized globally with a position
	OBJ(int passX, int passY) {
		_posX = passX;
		_posY = passY;
		_velX = 0;
		_velY = 0;
		_life = 1;
		_value = 0;
		_state = STATE_NORMAL;
		_type = "generic";

	}

	// Setters and getters
	public int getX() {
		return _posX;
	}

	public int getY() {
		return _posY;
	}

	public void setX(int passX) {
		_posX = passX;
	}

	public void setY(int passY) {
		_posY = passY;
	}

	public int getVelX() {
		return _velX;
	}

	public int getVelY() {
		return _velY;
	}

	public void setVelX(int passX) {
		_velX = passX;
	}

	public void setVelY(int passY) {
		_velY = passY;
	}

	public int getLife() {
		return _life;
	}

	public void setLife(int passLife) {
		_life = passLife;
	}

	public int getValue() {
		return _value;
	}

	public int getState() {
		return _state;
	}

	public void setState(int passState) {
		_state = passState;
	}

	public String getType() {
		return _type;
	}

	public Bitmap getBitmap() {
		return _bitmap;
	}

	public OBJ getParent() {
		return _parent;
	}

	// Process, think, and damager are all specific to the object, so these are
	// blank
	public void process() {
	}

	public void think(Vector passObjects) {
	}

	public void damage() {
	}

	// In our game, firing means shooting a photon and playing the zap tone,
	// this is the same
	// for all objects
	public void fire(Vector passObjects, OBJ passParent, int passVelocity) {
		Photon tempPhoton;

		// If the photon is going up, start it from the top of the object firing
		// it.
		// If its going down, start it from bottom of object firing it
		if (passVelocity > 0)
			tempPhoton = new Photon(passParent.getX(), passParent.getY()
					+ passParent.getBitmap().getHeight(), 0, passVelocity,
					passParent);
		else
			tempPhoton = new Photon(passParent.getX(), passParent.getY(), 0,
					passVelocity, passParent);

		// set X coordinate of photon to the middle of the object firing it
		tempPhoton.setX(tempPhoton.getX() + passParent.getBitmap().getWidth()
				/ 2);

		// Add the photon object to our object vector
		passObjects.addElement(tempPhoton);

		// Play a zap tone
		// TODO GamePlayScreen.snd.playSound();

	}

	// Collision detection routine using an AABB test (Axis Align Bounding Box).
	// This
	// is a quick and easy test great for games with simple squarish sprites
	// which simply
	// looks to see if the bounding boxes overlap in any way.
	public static void collisionDetect(Vector passObjects) {
		OBJ tempObject1, tempObject2; // temporarily points to the two objects
										// being tested
		boolean intersect, check; // flags during testing

		// Loop through all objects in our vector
		for (int lcv = 0; lcv < passObjects.size(); lcv++) {
			// Set tempObject1 to the current object
			tempObject1 = (OBJ) passObjects.elementAt(lcv);

			// Now loop from the current object to the end of the vector
			for (int lcv2 = lcv; lcv2 < passObjects.size(); lcv2++) {
				// Set tempObject2 to the current object of the nested loop
				tempObject2 = (OBJ) passObjects.elementAt(lcv2);

				// See if we need to check for collision (e.g. some objects dont
				// matter if
				// they collide, enemy with enemy or fire with fire for example)

				// Assume we dont need to check
				check = false;

				// Hero and enemy would be something to check for
				if (tempObject1.getType() == "hero"
						&& tempObject2.getType().startsWith("enemy"))
					check = true;

				// Hero and enemy fired photons would be something to check for
				if (tempObject1.getType() == "hero"
						&& tempObject2.getType().startsWith("fire")
						&& tempObject2.getParent().getType()
								.startsWith("enemy"))
					check = true;

				// Enemy and hero fired photons would be something ot check for
				if (tempObject1.getType().startsWith("enemy")
						&& tempObject2.getType().startsWith("fire")
						&& tempObject2.getParent().getType() == "hero")
					check = true;

				// If our check flag is set to true, and the state of the
				// objects is normal
				// (e.g. an object in a hit or exploded state can't collide with
				// something),
				// then lets check for the actual collision
				if (check && tempObject1.getState() == 0
						&& tempObject2.getState() == 0) {

					// We assume the two objects collided
					intersect = true;

					// Left and Right sides of bounding box check
					if (!(Math.abs((tempObject1.getX() + tempObject1
							.getBitmap().getWidth() / 2)
							- (tempObject2.getX() + tempObject2.getBitmap()
									.getWidth() / 2)) <= tempObject1
							.getBitmap().getWidth()
							/ 2
							+ tempObject2.getBitmap().getWidth() / 2))
						intersect = false;

					// Top and Bottom sides of bounding box check
					if (!(Math.abs((tempObject1.getY() + tempObject1
							.getBitmap().getHeight() / 2)
							- (tempObject2.getY() + tempObject2.getBitmap()
									.getHeight() / 2)) <= tempObject1
							.getBitmap().getHeight()
							/ 2
							+ tempObject2.getBitmap().getHeight() / 2))
						intersect = false;

					// If the objects collided, damage each one.
					if (intersect) {
						tempObject1.damage();
						tempObject2.damage();
					}
				}
			}
		}
	}

	// Clean up objects that have died or are way off screen.
	public static int cleanObjects(Vector passObjects) {
		OBJ tempObject; // Temporary points to object we're checking
		boolean delFlag; // Flag if we should get rid of it or not
		int scoreAdd; // Aggregate points to add to user's score

		// Start out with no points added
		scoreAdd = 0;

		// Loop through all objects in our vector
		for (int lcv = 0; lcv < passObjects.size(); lcv++) {
			// Set tempObject to current object
			tempObject = (OBJ) passObjects.elementAt(lcv);

			// Assume we're not deleting it
			delFlag = false;

			// Check the object's state. If its been dying for 10 refreshes, its
			// time to get rid of it.
			if (tempObject.getState() > STATE_DYING + 10) {
				// In the case of our hero, the player has lives, so if the hero
				// dies, we need to check to see if any lives are left before
				// quitting the game
				if (tempObject.getType() == "hero") {
					// If there are lives left...
					if (((Hero) tempObject).getLives() > 0) {
						// Decrement the number of lives left, set state,
						// bitmap,
						// and position back to normal
						((Hero) tempObject).setLives(((Hero) tempObject)
								.getLives() - 1);
						tempObject.setLife(5);
						tempObject.setState(STATE_NORMAL);
						tempObject.setX(Display.getWidth() / 2);
						tempObject.setY(Display.getHeight() - 50);
						tempObject._bitmap = Bitmap
								.getBitmapResource("heroi.png");
					} else {
						// The player is out of lives, lets destroy the hero
						// (which will
						// end the game)
						delFlag = true;
					}
				} else {
					// Enemies only have 1 life, so they are set to be deleted,
					// and we
					// add their value to the total score the player got this
					// cleanup.
					delFlag = true;
					scoreAdd += tempObject.getValue();
				}
			}

			// If the object is a photon...
			if (tempObject.getType() == "firephoton" && delFlag == false) {
				// Delete if off left side of screen
				if (tempObject.getX() + tempObject.getBitmap().getWidth() < 0)
					delFlag = true;

				// Delete if off right side of screen
				if (tempObject.getX() > Display.getWidth())
					delFlag = true;

				// Delete if off top of screen
				if (tempObject.getY() + tempObject.getBitmap().getHeight() < 0)
					delFlag = true;

				// Delete if off bottom of screen
				if (tempObject.getY() > Display.getHeight())
					delFlag = true;
			}

			// We need to check for enemies that are way off screen.
			// Normally enemies will swarm around here, but there are
			// also kamikaze enemies that will aim toward the hero,
			// and if miss, keep going forever
			if (tempObject.getType() == "enemydrone" && delFlag == false) {
				// Check each of the four sides of the screen, if an enemy is
				// past any of them plus 100 pixels its considered lost. No
				// points are scored for these enemies though.
				if (tempObject.getX() + tempObject.getBitmap().getWidth() < -100)
					delFlag = true;

				if (tempObject.getX() > Display.getWidth() + 100)
					delFlag = true;

				if (tempObject.getY() + tempObject.getBitmap().getHeight() < -100)
					delFlag = true;

				if (tempObject.getY() > Display.getHeight() + 100)
					delFlag = true;
			}

			// If the delete flag is true
			if (delFlag) {
				// Remove the object from the vector
				passObjects.removeElementAt(lcv);

				// Set our temporary object to null
				tempObject = null;

				// If this was our hero object (eg location 0), then we return a
				// -1
				// to communicate this back to our processing routine
				if (lcv == 0) {
					return (-1);
				}
			}
		}

		// If our hero is still alive, we return to the number of points scored
		return scoreAdd;
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
			_velX = 0;
		}

		if (_posY < 0) {
			_posY = 0;
			_velY = 0;
		}

		if (_posX > Display.getWidth() - _bitmap.getWidth()) {
			_posX = Display.getWidth() - _bitmap.getWidth();
			_velX = 0;
		}

		if (_posY > Display.getHeight() - _bitmap.getHeight()) {
			_posY = Display.getHeight() - _bitmap.getHeight();
			_velY = 0;
		}

	}

}

// Hero object
class Hero extends OBJ {
	int _lives; // The hero (the player) has multiple lives, different from
				// other objects

	Hero(int passX, int passY) {
		super(passX, passY);

		// Set bitmap to herogame
		_bitmap = Bitmap.getBitmapResource("heroi.png");

		// Set coordinates, velocity, lives, and type identifier
		_velX = 0;
		_velY = 0;
		_life = 5;
		_lives = 2;
		_type = "hero";
	}

	// Hero processing
	public void process() {
		// The hero has a max velocity of 10 in any direction
		if (_velX > 10)
			_velX = 10;

		if (_velX < -10)
			_velX = -10;

		if (_velY > 10)
			_velY = 10;

		if (_velY < -10)
			_velY = -10;

		// Movement is simply adding velocity to position
		_posX += _velX;
		_posY += _velY;

		// If the current life of the hero is less than 1, we check the state of
		// hero
		if (_life < 1) {

			// If the hero is currently in a normal state, it is put into a
			// dying state
			if (_state == STATE_NORMAL) {
				// Set hero bitmap to an explosion
				_bitmap = Bitmap.getBitmapResource("explosao.png");

				// Set state to dying
				_state = STATE_DYING;

				// Vibrate the phone
				// TODO GamePlayScreen.snd.vibrate(180);
			}

		}

		// If the hero is in an abnormal state (hit or dying), there is
		// additional processing
		// that must happen
		if (_state > STATE_NORMAL) {
			// First, we increment the state by 1. Non normal states are
			// temporary and are checked
			// for terminate by seeing if the initial state value plus a certain
			// number of
			// refreshes has been reached. It allows the object to be in a
			// abnormal state for 10
			// refreshes, 3 refreshes, however many necessary, and then continue
			// onto
			// some other state, either back to normal or deleted.
			_state++;

			// If the ship has been in the hit state for more than 3 refreshes,
			// set it back to normal
			if ((_state > STATE_HIT + 3) && (_state < STATE_DYING)) {
				_state = STATE_NORMAL;
				_bitmap = Bitmap.getBitmapResource("heroidano.png");
			}
		}

		// Bound our hero to the screen
		boundToScreen();
	}

	// Our hero's damage method
	public void damage() {
		// Decrease life (not lives) by 1
		_life--;

		// If life is still above 0, change our hero to the hit state
		if (_life > 0) {
			_bitmap = Bitmap.getBitmapResource("heroi.png");
			_state = STATE_HIT;
		}
	}

	// Our hero's fire method for when it fires a photon
	public void fire(Vector passObjects) {
		// Call the parents fire method, with a velocity of -20
		super.fire(passObjects, this, -20);
	}

	// Lives setter and getter
	public int getLives() {
		return _lives;
	}

	public void setLives(int passLives) {
		_lives = passLives;
	}

}

// Photon class
class Photon extends OBJ {

	// Initializes like hero object, only has photon.png as a bitmap, and starts
	// with a program specified velocity
	Photon(int passX, int passY, int passVelX, int passVelY, OBJ passParent) {
		super(passX, passY);
		_bitmap = Bitmap.getBitmapResource("tiro.png");
		_velX = passVelX;
		_velY = passVelY;
		_type = "firephoton";
		_parent = passParent;
	}

	// Photon processing is simple, we simply move the object in accordance to
	// its
	// velocity
	public void process() {

		// Position = Position + Velocity
		_posX += _velX;
		_posY += _velY;

	}

	// When a photon is damaged, it simply dies and disappears instantly.
	// This is accomplished by setting the state to STATE_DYING + 11. Since
	// we put objects in the dying state for 10 frames before delition, this
	// immediate deletes it.
	public void damage() {
		_life = 0;
		_state = STATE_DYING + 11;
	}

}

// Our enemy class
class EnemyDrone extends OBJ {
	int _AIRoutine; // AI routine stores what kind of enemy this is, normal or
					// kamikaze

	// Enemy initialization is like other objects, except we randomly choose
	// what
	// kind of AI routine it should use
	EnemyDrone(int passX, int passY) {
		super(passX, passY);
		_bitmap = Bitmap.getBitmapResource("inimigo.png");
		_value = 50;
		_type = "enemydrone";

		// Statistically, 3 out of 5 enemies are normal, 2 are kamikaze
		if (GamePlayScreen.rndGenerator.nextInt() % 10 < 6)
			_AIRoutine = 0;
		else
			_AIRoutine = 1;

	}

	// Enemy processing is identical to hero processing, except enemies don't
	// have a hit state, since they have one life (not to be confused with
	// lives),
	// one hit kills them, hence theres no need for a hit state
	public void process() {
		_posX += _velX;
		_posY += _velY;

		if (_life < 1) {
			if (_state == STATE_NORMAL) {
				_bitmap = Bitmap.getBitmapResource("explosao.png");
				_state = STATE_DYING;
				// TODO GamePlayScreen.snd.vibrate(180);
			}

			_state++;
		}

	}

	// The think method is where the individual enemy AI takes place
	public void think(Vector passObjects) {

		// If they've blown up, they can no longer think
		if (_life < 1)
			return;

		// We start off with a velocity of 0 in both directions
		_velX = 0;
		_velY = 0;

		// Grab a handle on the hero object so we know how to direct our enemies
		Hero tempHero = (Hero) passObjects.elementAt(0);

		// If we're in normal AI mode
		if (_AIRoutine == 0) {
			// If hero is to our right, set velocity to right
			if (_posX + _bitmap.getWidth() / 2 < tempHero.getX()
					+ tempHero.getBitmap().getWidth() / 2)
				_velX = 5;

			// If hero is to our left, set velocity to our left
			if (_posX + _bitmap.getWidth() / 2 > tempHero.getX()
					+ tempHero.getBitmap().getWidth() / 2)
				_velX = -5;

			// Enemies try to stay 40 pixels above hero
			if (_posY + _bitmap.getHeight() < tempHero.getY() - 40)
				_velY = 5;

			// If enemy is below hero, they move up
			if (_posY > tempHero.getY() + tempHero.getBitmap().getHeight())
				_velY = -5;

			// Add a little bit of random movement in
			_velX += GamePlayScreen.rndGenerator.nextInt() % 4 - 2;
			_velY += GamePlayScreen.rndGenerator.nextInt() % 4 - 2;

			// Random firing, fire 1 in 7 times thinking
			if (GamePlayScreen.rndGenerator.nextInt() % 4 == 1) {
				fire(passObjects);
			}
		} else {
			// Kamikaze AI is the same for as above for horizontal processing.
			if (_posX + _bitmap.getWidth() / 2 < tempHero.getX()
					+ tempHero.getBitmap().getWidth() / 2)
				_velX = 5;

			if (_posX + _bitmap.getWidth() / 2 > tempHero.getX()
					+ tempHero.getBitmap().getWidth() / 2)
				_velX = -5;

			// For vertical though, the enemy drone is always going downward at
			// a faster rate
			_velY = 8;
		}
	}

	// If enemy is damaged, their life is decreased
	public void damage() {
		_life--;
	}

	// An enemy firing calls the Object's fire method with a downward direction
	public void fire(Vector passObjects) {
		super.fire(passObjects, this, 20);
	}

}
