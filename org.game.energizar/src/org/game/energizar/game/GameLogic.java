package org.game.energizar.game;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.XYPoint;

public class GameLogic {

	// Singleton field
	private static GameLogic _instance = new GameLogic();

	// singleton
	public static GameLogic instance() {
		return GameLogic._instance;
	}

	private GameLogic() {
	}

	public void process(GameLevel gameLevel, int miliseconds) {

		// updates all objects and timer features
		updateTimersAndObjects(miliseconds, gameLevel);

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

		// verify game state
		if (gameLevel.getRemainingTries() <= 0) {
			gameLevel.endGame();
		}

	}

	/**
	 * @param miliseconds
	 * @param gameLevel
	 */
	private void updateTimersAndObjects(int miliseconds, GameLevel gameLevel) {
		// objects in level
		Vector objsInLevel = gameLevel.objects();

		// notifica os elementos de que o tempo passou
		// e dá a cance de atualizarem seu estado
		for (Enumeration e = objsInLevel.elements(); e.hasMoreElements();) {
			OBJ obj = (OBJ) e.nextElement();

			// notifica o timer do elemento
			if (obj.getTimer() != null) {
				obj.getTimer().tick(miliseconds, gameLevel);
			}

			// notifica um elemento
			obj.update(miliseconds, gameLevel);

		}
	}

	/**
	 * @param gameLevel
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
				// currObj.setConnectedConnection(connection);
				// bullet.setConnectedConnection(connection);
				gameLevel.objects().addElement(connection);

				// TODO
				// currObj.junctionNotifyConected();
				currObj.notifyShotHandled();

				// now there is no focused object
				gameLevel.setFocusedObject(null);
			}
		}
	}

	/**
	 * Removes from game all NULL objects.
	 * 
	 * @param gameLevel
	 * @param objsInLevel
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
	 * @param bullet
	 * @param gameLevel
	 * @param objsToBeDeleted
	 * @param objsInLevel
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

			// cant hit objetcts without a position
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

		// if it hits a junction
		if (bHit && theHitObject.getTypeID() == OBJType.JUNCTION) {

			OBJ junction = theHitObject;
			// gets the object connection
			OBJ connection = OBJ.getAnyConnectedConnection(bullet, gameLevel);
			if (connection != null) {
				// it now target the hit object
				connection.setConnectionTargetObject(junction);
			}

			// powers on and focus the junction
			junction.junctionPowerOn();
			gameLevel.setFocusedObject(junction);

			// destroy the bullet
			OBJ.nullify(bullet);

		} else // if it hits an endpoint
		if (bHit && theHitObject.getTypeID() == OBJType.ENDPOINT) {
			// end the game
			gameLevel.endGame();
		} else {
			// if the bullet leaves the level
			if (!gameLevel.getGameArea().contains(bullet.getPos())) {
				// gets the bullet connection
				OBJ connection = OBJ.getAnyConnectedConnection(bullet,
						gameLevel);
				if (connection != null) {
					// focus the original shooter again
					OBJ shooter = connection.getConnectionSourceObject();
					gameLevel.setFocusedObject(shooter);
					// destroy the connection
					OBJ.nullify(connection);
				}// destroy the bullet
				OBJ.nullify(bullet);

				gameLevel.lostShot();
			}
		}
	}
}
