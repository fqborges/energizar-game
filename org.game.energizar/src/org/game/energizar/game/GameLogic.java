package org.game.energizar.game;

import java.util.Enumeration;
import java.util.Vector;

public class GameLogic {

	// Singleton field
	private static GameLogic _instance = new GameLogic();

	// singleton
	public static GameLogic instance() {
		return GameLogic._instance;
	}

	private GameLogic() {
	}

	public void process(GameLevel _gameData) {

		Vector objects = _gameData.objects();
		for (Enumeration e = objects.elements(); e.hasMoreElements();) {
			OBJ o = (OBJ) e.nextElement();

			if (o.getTimer() != null) {
				o.getTimer().tick();
			}

		}

	}
}
