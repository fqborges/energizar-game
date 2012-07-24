package org.game.energizar.modules;

import org.game.energizar.game.GameData;
import org.game.energizar.game.OBJFactory;

public class INPUT {
	// Singleton field
	private static INPUT _instance = new INPUT();

	// singleton
	public static INPUT instance() {
		return INPUT._instance;
	}

	private INPUT() {
	}

	public void process(GameData _gameData) {
		// TODO Auto-generated method stub

	}

	public boolean receiveKeyChar(char key, int status, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean receiveNavigationMovement(int dx, int dy, int status,
			int time) {
		// TODO Auto-generated method stub
		return false;
	}

}
