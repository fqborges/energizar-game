package org.game.energizar.game;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.XYPoint;
import net.rim.device.api.ui.XYRect;

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

		// objects to be deleted
		Vector objsToBeDeleted = new Vector();

		// objects in level
		Vector objsInLevel = gameLevel.objects();

		// notifica os elementos de que o tempo passou
		// e dá a cance de atualizarem seu estado
		for (Enumeration e = objsInLevel.elements(); e.hasMoreElements();) {
			OBJ obj = (OBJ) e.nextElement();

			// notifica o timer do elemento
			if (obj.getTimer() != null) {
				obj.getTimer().tick();
			}

			// notifica um elemento
			obj.update(miliseconds, gameLevel);

		}

		// verifica se há um elemento com foco
		if (gameLevel.getCurrentObject() != null) {
			OBJ currObj = gameLevel.getCurrentObject();

			// se o elemento está atirando
			if (currObj.isShooting()) {

				// cria um projétil na frente do objeto e que
				// se move na direção do objeto direção
				XYPoint move = currObj.getDirection().toMoveIncrement();
				int posX = currObj.getX() + move.x;
				int posY = currObj.getY() + move.y;
				OBJ bullet = OBJFactory.instance().createBullet(posX, posY,
						currObj.getDirection());
				gameLevel.objects().addElement(bullet);

				// cria uma conexao entre o objeto e o projetil
				OBJ connection = OBJFactory.instance().createConnection(
						currObj, bullet);
				gameLevel.objects().addElement(connection);
				currObj.notifyConected();

				// agora nenhum elemento tem foco
				gameLevel.setCurrentObject(null);
			}
		}

		// process the current state for every element and apply game rules
		for (Enumeration eachObject = objsInLevel.elements(); eachObject
				.hasMoreElements();) {
			OBJ object = (OBJ) eachObject.nextElement();

			// if object is a bullet
			if (object.getTypeID() == OBJType.BULLET) {

				// verify if the bullet hits another object
				boolean bHit = false;
				OBJ oHit = null;
				for (Enumeration eachOtherObject = objsInLevel.elements(); eachOtherObject
						.hasMoreElements();) {
					OBJ otherObj = (OBJ) eachOtherObject.nextElement();

					// ignore hitting itself
					if (otherObj == object)
						continue;

					// if the two objects are in same position there is a hit
					if (otherObj.getX() == object.getX()
							&& otherObj.getY() == object.getY()) {
						bHit = true;
						oHit = otherObj;
						break;
					}
				}

				// if it hits a junction
				if (bHit && oHit.getTypeID() == OBJType.JUNCTION) {

					// search for the connection targeting this object
					OBJ connection = null;
					for (Enumeration eachOtherObject = objsInLevel.elements(); eachOtherObject
							.hasMoreElements();) {
						OBJ other = (OBJ) eachObject.nextElement();
						if (other.getTypeID() == OBJType.CONNECTION
								&& other.getConnectionTargetObject() == object) {
							connection = other;
							break;
						}

					}

					// if a connection was found
					if (connection != null) {
						// it will target the hit object
						connection.setConnectionTargetObject(oHit);
					}

					oHit.powerOn();
					gameLevel.setCurrentObject(oHit);
					objsToBeDeleted.addElement(object);
					continue;

				} else // if it hits an endpoint
				if (bHit && oHit.getTypeID() == OBJType.ENDPOINT) {
					gameLevel.endGame();
					continue;
				}

				// verify if the bullet leaves the level
				if (!gameLevel.getGameArea().contains(object.getX(),
						object.getY())) {
					objsToBeDeleted.addElement(object);
					continue;
				}

			}

		} // end - process the current state for every element and apply game
			// rules

		// remove do jogo todos elementos que foram marcados para exclusao
		for (Enumeration eToBeDeleted = objsToBeDeleted.elements(); eToBeDeleted
				.hasMoreElements();) {
			gameLevel.objects().removeElement((OBJ) eToBeDeleted.nextElement());
		}
	}
}
