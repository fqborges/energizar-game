package org.game.energizar;

import java.util.Random;
import java.util.Vector;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.FullScreen;

//Gameplay extends FullScreen only, it doesn't need the functionality of the
//MainScreen object.  Either are screens though, and can be pushed onto the
//screen stack
//Special Note: For those familiar with the concept double buffering, supposedly
//the rim UI automatically does this.  It seems to from what I can see, I
//notice no tearing or graphic anomalies.
//For those unfamiliar, if you blit (write) graphic data directly primary graphic
//buffer, it is out of sync with the refresh of the screen.  This can cause
//tearing or other weird graphic issues, as half of the screen is still being shown
//where the objects were, and the other half where the objects are now.  This
//is solved by first drawing to a back buffer, and then flipping the entire
//back buffer with the front during the screen's refresh.  In java there are
//specific game-oriented screen classes to do this, but supposedly the RIM
//libs do this automatically.
public class GamePlayScreen extends FullScreen {
	// These three objects are called from many places and are made public
	public static GFX gfx; // Static object for graphics control

	// TODO public static SND snd; // Static object for sound control
	public static Random rndGenerator; // Static object for random numbers

	Refresher _refresher; // Thread that continually refreshes the game
	EnemyAI _enemyai; // Thread when the enemies think
	Vector _objects; // A vector of all objects currently in play

	boolean _active; // Flag if our game is currently active or not (eg did we
						// lose)
	int _score; // Player's current score

	// Getters for active and score
	boolean getActive() {
		return _active;
	}

	int getScore() {
		return _score;
	}

	// Refresh is a type of thread that runs to refresh the game.
	// Its job is to make sure all the processing is called for each object,
	// update the background,
	// update score, check for end of game, etc. This is the main heartbeat.
	private class Refresher extends Thread {
		// When the object is created it starts itself as a thread
		Refresher() {
			start();
		}

		// This method defines what this thread does every time it runs.
		public void run() {
			// Temporary variable that stores the score value of all
			// the objects that were just cleaned (destroyed/removed)
			// return a negative number if we died
			int cleanReturn;

			// This thread runs while the game is active
			while (_active) {

				// Level population/processing, responsible for creating new
				// enemies
				processLevel();

				// Perform physics by calling each object's process command.
				// Objects
				// are unique and control their own physics (e.g. a photon blast
				// can move
				// faster than our hero ship), so we loop through all our
				// objects
				// and call the process method on each.
				for (int lcv = 0; lcv < _objects.size(); lcv++) {
					((OBJ) _objects.elementAt(lcv)).process();
				}

				// Collision detection of objects. If they collide, this method
				// will call the damage method of each object, which might lead
				// to no life for the object
				OBJ.collisionDetect(_objects);

				// Clean up stuff that's gone (e.g. life of 0 and in
				// destroyable state, eg explosion graphic), quit if we were
				// destroyed
				cleanReturn = OBJ.cleanObjects(_objects);

				// If we didn't die, add the score, redraw, etc.
				if (cleanReturn >= 0) {
					_score += cleanReturn;

					// Now that all our processing is done, tell the graphics
					// engine to
					// redraw the screen through a call to invalidate
					// (invalidates the screen
					// and automatically causes a redraw)
					invalidate();
				} else {
					// if we died, mark active as false.
					_active = false;
				}

				try {
					// Attempt to sleep for 50 ms
					Thread.sleep(50);

				} catch (InterruptedException e) {
					// Do nothing if we couldn't sleep, we don't care about
					// exactly perfect
					// timing.
				}
			}

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(
							GamePlayScreen.this);
					;
				}
			});
		}

	}

	// We have a separate thread that takes care of enemy AI. This allows us to
	// control how
	// quickly the enemies think outside of our main refresh thread. That way we
	// can make
	// dumb enemies that think much slower than the action happening around
	// them, or smart
	// enemies that think as fast.
	private class EnemyAI extends Thread {
		EnemyAI() {
			start();

		}

		public void run() {
			// Just make sure the thread doesn't accidentally run when the game
			// is over
			while (_active) {
				// Loop through all the objects and call the think method, which
				// controls the actions of that object. Technically, this call's
				// the hero's think method as well, but Hero doesn't have the
				// think method overridden from the parent's, which is just
				// blank,
				// so the hero does no automatic thinking.
				for (int lcv = 0; lcv < _objects.size(); lcv++) {
					if (_active)
						((OBJ) _objects.elementAt(lcv)).think(_objects);
				}

				try {
					// Enemies think 5 times a second
					Thread.sleep(1000 / 5);

				} catch (InterruptedException e) {
					// Do nothing, again we don't care if timing isn't exact
				}
			}

		}

	}

	public GamePlayScreen() {
		// TODO snd = new SND(); // Create sound engine
		gfx = new GFX(); // Create graphics engine

		rndGenerator = new Random(); // Create random number generator

		_active = true; // Mark the game as active
		_score = 0; // Start with a score of 0

		// Set our background to stars.png with a speed of 3 pixels per refresh
		gfx.initBackground("espaco.png", 3);

		// Start our music playing. Something odd I noticed with my Blackberry
		// 8830,
		// which may either be a bug or some functionality I'm missing from the
		// sound routines, is the volume starts out very quiet the first time
		// you
		// start playing. If you stop the music playing and play it again, the
		// volume
		// is fine. This might not be true for all Blackberrys, but it doesn't
		// hurt
		// anything to start, stop, and start again, so that's what we've done
		// here,
		// just to solve the volume bug/misundertsanding.

		// TODO snd.playMusic("music.mid");
		// TODO snd.stopMusic();
		// TODO snd.playMusic("music.mid");

		// Create a new vector to hold all the active objects (hero, enemies,
		// photons, etc)
		_objects = new Vector();

		// Our hero will always be the very first object in the vector.
		_objects.addElement(new Hero(Display.getWidth() / 2, Display
				.getHeight() - 50));

		// Create the refresher and enemy AI last, we want to make sure all our
		// objects are setup first
		// so the threads don't make use of uninitialized objects
		_refresher = new Refresher();
		_enemyai = new EnemyAI();

	}

	// Process level is responsible for new computer generated events in the
	// game. In our
	// case, the only one really is creating new enemies.
	public void processLevel() {
		OBJ tempObject;

		// Throw a new enemy in every 25 pixels. This can be made more complex
		// if desired
		// of course
		if (gfx.getBackPos() % 25 == 0) {
			// Create a new enemy drone with the X coordinate somewhere between
			// the two edges of the
			// screen
			tempObject = new EnemyDrone(rndGenerator.nextInt()
					% Display.getWidth(), 0);

			// Set the Y coordinate to above the screen, so it comes in from the
			// top
			tempObject.setY(tempObject.getY()
					- tempObject.getBitmap().getHeight() + 3);

			// Add the enemy to the object vector
			_objects.addElement(tempObject);
		}
	}

	// This method is called when the invalidate method is called from the
	// refresh thread.
	// We have it passing the graphics object over to our graphics engine so our
	// custom graphics routines can take care of any drawing necessary.
	protected void paint(Graphics graphics) {
		gfx.process(graphics, _objects, _score,
				((Hero) _objects.elementAt(0)).getLives());
	}

	// The keyChar method is called by the event handler when a key is pressed.
	public boolean keyChar(char key, int status, int time) {

		boolean retVal = false;

		switch (key) {
		// If escape is pressed, we set the game to inactive (quit)
		case Characters.ESCAPE:
			_active = false;
			retVal = true;
			break;

		// If the spacebar is pressed, we call the fire method of our
		// hero object, causing him to fire a photon
		case Characters.SPACE:
			((Hero) _objects.elementAt(0)).fire(_objects);

			retVal = true;
			break;

		default:
			break;
		}

		return retVal;
	}

	// The navigationMovement method is called by the event handler when the
	// trackball is used.
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		// If the trackball was scrolled in the horizontal direction, we add
		// that amount to
		// our hero's X velocity
		if (dx != 0)
			((OBJ) _objects.elementAt(0)).setVelX(dx / Math.abs(dx) * 5
					+ ((OBJ) _objects.elementAt(0)).getVelX());

		// If the trackball was scrolled in the vertical direction, we add that
		// amount to
		// our hero's Y velocity
		if (dy != 0)
			((OBJ) _objects.elementAt(0)).setVelY(dy / Math.abs(dy) * 5
					+ ((OBJ) _objects.elementAt(0)).getVelY());

		return true;
	}

}