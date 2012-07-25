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

	public void process(GameLevel gameData, int miliseconds) {

		Vector objects = gameData.objects();
		// notifica os elementos de que o tempo passou
		for (Enumeration e = objects.elements(); e.hasMoreElements();) {
			OBJ o = (OBJ) e.nextElement();

			// notifica o timer do elemento
			if (o.getTimer() != null) {
				o.getTimer().tick();
			}

			// notifica um elemento
			o.update(miliseconds, gameData);

		}

		if (gameData.getCurrentObject() != null) {
			OBJ current = gameData.getCurrentObject();

			boolean isJunctionOrStartShooting = (current.getTypeID() == OBJ.JUNCTION || current
					.getTypeID() == OBJ.STARTPOINT) && current.isShooting();

			if (isJunctionOrStartShooting) {
				// verifica se ele pode acertar algum outro objeto
				current.getDirection();

				// cria um projetil ficticio no lugar do objeto
				XYPoint projetil = new XYPoint(current.getX(), current.getY());
				XYPoint increment = directionToMovimentIncrement(current);
				projetil.translate(increment);

				// area do cenario
				XYRect gameArea = new XYRect(0, 0, gameData.getWidth(),
						gameData.getHeigth());

				// navega pelo cenario até sair do cenario ou acertar alguém
				boolean bHit = false;
				OBJ oHit = null;
				while (gameArea.contains(projetil)) {
					for (Enumeration e = objects.elements(); e
							.hasMoreElements();) {
						OBJ o = (OBJ) e.nextElement();
						if (o.getX() == projetil.x && o.getY() == projetil.y) {
							bHit = true;
							oHit = o;
							break;
						}
					}
					projetil.translate(increment);
				}

				// se acertou alguém que é junction
				if (bHit && oHit.getTypeID() == OBJ.JUNCTION) {
					// avisa que está conectado
					current.notifyConect();

					//
					oHit.powerOn();

					//
					gameData.setCurrentObject(oHit);

				} else // se acertou alguém que é endpoint
				if (bHit && oHit.getTypeID() == OBJ.ENDPOINT) {
					gameData.endGame();
				} else {
					gameData.endGame();
				}

			}
		}

	}

	private XYPoint directionToMovimentIncrement(OBJ current) {

		int x = 0;
		int y = 0;

		switch (current.getDirection().value) {

		case Direction.VALUE_DOWN_RIGHT:
		case Direction.VALUE_RIGHT:
		case Direction.VALUE_UP_RIGTH:
			x = 1;
			break;
		case Direction.VALUE_DOWN_LEFT:
		case Direction.VALUE_LEFT:
		case Direction.VALUE_UP_LEFT:
			x = -1;
			break;
		default:
			x = 0;
			break;
		}
		switch (current.getDirection().value) {
		case Direction.VALUE_DOWN_RIGHT:
		case Direction.VALUE_DOWN:
		case Direction.VALUE_DOWN_LEFT:
			y = 1;
			break;
		case Direction.VALUE_UP_LEFT:
		case Direction.VALUE_UP:
		case Direction.VALUE_UP_RIGTH:
			y = -1;
			break;
		default:
			y = 0;
			break;
		}

		return new XYPoint(x, y);
	}
}
