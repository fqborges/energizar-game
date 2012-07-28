package org.game.energizar.game;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.XYPoint;

/**
 * Class to process game specific rules based on the state of game objects.
 * 
 * @author Filipe Borges <fqborges@gmail.com>
 * 
 */
public class GameLogic {

	// Singleton field
	private static GameLogic _instance = new GameLogic();

	// singleton
	public static GameLogic instance() {
		return GameLogic._instance;
	}

	private GameLogic() {
	}

	/**
	 * Update the game one game tick.
	 * 
	 * @param gameLevel
	 *            current game data
	 * @param milliseconds
	 *            milliseconds elapsed since last tick
	 */
	public void process(GameLevel gameLevel, int milliseconds) {

		// updates all objects and timer features
		updateTimersAndObjects(milliseconds, gameLevel);

		// process focused object
		processFocusedObject(gameLevel);

		// process the current state for every element and apply game rules
		for (Enumeration eachObject = gameLevel.objects().elements(); eachObject
				.hasMoreElements();) {
			OBJ object = (OBJ) eachObject.nextElement();

			// if object is a bullet
			if (object.getTypeID() == OBJType.BULLET) {
				applyBulletRules(object, gameLevel);
			}

		} // end - process the current state for every element and apply game
			// rules

		// cleanup all objects
		cleanupNullObjects(gameLevel);

		// verify conditions to end the game
		processEndConditions(gameLevel);
	}

	/**
	 * Update all timer features and object update features
	 * 
	 * @param milliseconds
	 *            milliseconds elapsed since last update
	 * @param gameLevel
	 *            current game data
	 */
	private void updateTimersAndObjects(int milliseconds, GameLevel gameLevel) {
		// objects in level
		Vector objsInLevel = gameLevel.objects();

		// notifica os elementos de que o tempo passou
		// e dá a cance de atualizarem seu estado
		for (Enumeration e = objsInLevel.elements(); e.hasMoreElements();) {
			OBJ obj = (OBJ) e.nextElement();

			// notifica o timer do elemento
			if (obj.getTimer() != null) {
				obj.getTimer().tick(milliseconds, gameLevel);
			}

			// notifica um elemento
			obj.update(milliseconds, gameLevel);

		}
	}

	/**
	 * Process rules for the focused object.
	 * 
	 * @param gameLevel
	 *            current game data
	 */
	private void processFocusedObject(GameLevel gameLevel) {

		// if there is a focused object
		if (gameLevel.getFocusedObject() != null) {
			OBJ currObj = gameLevel.getFocusedObject();

			// if it is shooting
			if (currObj.isShooting()) {

				// creates a bullet in front of it
				// that moves away from its direction
				XYPoint move = currObj.getDirection().toMoveIncrement();
				int posX = currObj.getPos().x + move.x;
				int posY = currObj.getPos().y + move.y;
				OBJ bullet = OBJFactory.instance().createBullet(posX, posY,
						currObj.getDirection());
				gameLevel.objects().addElement(bullet);

				// creates a connection to track bullet
				// and (possibly) connects to other object
				OBJ connection = OBJFactory.instance().createConnection(
						currObj, bullet);
				gameLevel.objects().addElement(connection);

				// TODO
				currObj.notifyShotHandled();

				// now there is no focused object
				gameLevel.setFocusedObject(null);
			}
		}
	}

	/**
	 * 
	 * @param gameLevel
	 */
	private void processEndConditions(GameLevel gameLevel) {
		// but only if the game has not ended yet
		if (!gameLevel.isEnded()) {
			// 1. if the game current object is the endpoint
			if (gameLevel.getFocusedObject() != null
					&& gameLevel.getFocusedObject().getTypeID() == OBJType.ENDPOINT) {

				// 2. if there is a powerable object unpowered you lose!
				for (Enumeration eachObject = gameLevel.objects().elements(); eachObject
						.hasMoreElements();) {

					// 2.1 foreach powerable object
					OBJ object = (OBJ) eachObject.nextElement();

					// 2.2 if this object is powered off
					if (object.getPoweredState() == OBJ.POWER_OFF) {
						// you lose the game!
						endTheGame(gameLevel, false);
						return;
					}
				}

				// 3. else you win the game!
				endTheGame(gameLevel, true);
				return;
			} else // case you have lost all tries to shot you lose
			if (gameLevel.getRemainingTries() <= 0) {
				// you lose the game!
				endTheGame(gameLevel, false);
			}
		}
	}

	/**
	 * Apply game rules for bullets.
	 * 
	 * @param bullet
	 *            the bullet
	 * @param gameLevel
	 *            current game data
	 */
	private void applyBulletRules(OBJ bullet, GameLevel gameLevel) {
		//
		Vector objsInLevel = gameLevel.objects();

		// verify if the bullet hits another object
		boolean bHit = false;
		OBJ theHitObject = null;
		for (Enumeration eachOtherObject = objsInLevel.elements(); eachOtherObject
				.hasMoreElements();) {
			OBJ otherObj = (OBJ) eachOtherObject.nextElement();

			// ignore hitting itself
			if (otherObj == bullet)
				continue;

			// cannot hit objects without a position
			if (otherObj.getPos() == null) {
				continue;
			}

			// if the two objects are in same position there is a hit
			if (otherObj.getPos().equals(bullet.getPos())) {
				bHit = true;
				theHitObject = otherObj;
				break;
			}
		}

		// if it hits an object
		if (bHit) {
			// if it hit a unpowered powerable object
			if (theHitObject.getPoweredState() == OBJ.POWER_OFF) {
				OBJ powerable = theHitObject;

				// powers on and focus the junction
				powerable.poweredPowerOn();
				gameLevel.setFocusedObject(powerable);

				// gets the bullet connection
				OBJ connection = OBJ.getFirstFoundConnectedConnection(bullet,
						gameLevel);
				if (connection != null) {
					// it now target the hit object
					connection.setConnectionTargetObject(powerable);
				}
				// destroy the bullet
				OBJ.nullify(bullet);
			} else // if the bullet leaves the level
			if (!gameLevel.getGameArea().contains(bullet.getPos())) {
				// it is lost
				handleLostBullet(bullet, gameLevel);
			}

		} else
		// if the bullet leaves the level
		if (!gameLevel.getGameArea().contains(bullet.getPos())) {
			// it is lost
			handleLostBullet(bullet, gameLevel);
		}

	}

	/**
	 * Handles an lost bullet. Any connection attached to the bullet is
	 * destroyed and the shooter is focused again.
	 * 
	 * @param bullet
	 *            the bullet
	 * @param gameLevel
	 *            current game data
	 */
	private void handleLostBullet(OBJ bullet, GameLevel gameLevel) {
		// gets the bullet connection
		OBJ connection = OBJ
				.getFirstFoundConnectedConnection(bullet, gameLevel);
		if (connection != null) {
			// focus the original shooter again
			OBJ shooter = connection.getConnectionSourceObject();
			gameLevel.setFocusedObject(shooter);
			// destroy the connection
			OBJ.nullify(connection);
		}// destroy the bullet
		OBJ.nullify(bullet);

		// lost this try
		gameLevel.loseTry();
	}

	/**
	 * Removes from game all NULL objects.
	 * 
	 * @param gameLevel
	 *            current game data
	 */
	private void cleanupNullObjects(GameLevel gameLevel) {

		Vector objsInLevel = gameLevel.objects();
		Vector objsToDelete = new Vector();

		// finds all null objects
		for (Enumeration eachObject = objsInLevel.elements(); eachObject
				.hasMoreElements();) {
			OBJ object = (OBJ) eachObject.nextElement();
			if (object.getTypeID() == OBJType.NULL) {
				objsToDelete.addElement(object);
			}
		}
		// deletes all null objects
		for (Enumeration eToBeDeleted = objsToDelete.elements(); eToBeDeleted
				.hasMoreElements();) {
			gameLevel.objects().removeElement((OBJ) eToBeDeleted.nextElement());
		}
	}

	/**
	 * 
	 * @param gameLevel
	 * @param won
	 */
	private void endTheGame(GameLevel gameLevel, boolean won) {
		// end the game
		gameLevel.end(won);

		// setup a timer to deactivate game
		OBJ timerToEnd = new OBJ(OBJType.TIMER);
		timerToEnd.setTimer(new Timer(20) {
			protected void run(GameLevel gameLevel) {
				gameLevel.deactivateGame();
			}
		});
		// and add it to game objects
		gameLevel.objects().addElement(timerToEnd);

		// no more focused object
		gameLevel.setFocusedObject(null);
	}
}
