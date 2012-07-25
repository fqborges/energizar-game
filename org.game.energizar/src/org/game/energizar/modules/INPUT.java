package org.game.energizar.modules;

import net.rim.device.api.ui.Keypad;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;

public class INPUT {

	private static final int NUM_CHARS = Character.MAX_VALUE + 1;
	boolean[] charsDown = new boolean[NUM_CHARS];
	boolean[] charsJustDown = new boolean[NUM_CHARS];
	boolean[] charsJustUp = new boolean[NUM_CHARS];
	int moveDx = 0;
	int moveDy = 0;

	// Singleton field
	private static INPUT _instance = new INPUT();

	// singleton
	public static INPUT instance() {
		return INPUT._instance;
	}

	private INPUT() {
		for (int i = 0; i < NUM_CHARS; i++) {
			charsJustDown[i] = false;
			charsJustUp[i] = false;
			charsDown[i] = false;
		}
		moveDx = 0;
		moveDy = 0;
	}

	public void process(GameLevel gameData, int miliseconds) {

		// OBJ o = (OBJ) gameData.objects().elementAt(0);
		//
		// if (moveDx > 0) {
		// o.moveRight();
		// }
		// if (moveDx < 0) {
		// o.moveLeft();
		// }
		// if (moveDy > 0) {
		// o.moveDown();
		// }
		// if (moveDy < 0) {
		// o.moveUp();
		// }

		if (charsJustDown[Keypad.KEY_ESCAPE]) {
			gameData.endGame();
		}

		if (charsJustDown[Keypad.KEY_SPACE]) {
			OBJ o = gameData.getCurrentObject();

			if (o.getTypeID() == OBJ.JUNCTION) {
				o.shoot();
			}
			if (o.getTypeID() == OBJ.STARTPOINT) {
				o.shoot();
			}
		}

		// limpa as teclas relacionadas a eventos que ocorreram desde o ultimo
		// update
		clearKeys();
	}

	private void clearKeys() {
		for (int i = 0; i < NUM_CHARS; i++) {
			charsJustDown[i] = false;
			charsJustUp[i] = false;
		}
		moveDx = 0;
		moveDy = 0;
	}

	public boolean receiveKeyChar(char key, int status, int time) {
		return false;
	}

	public boolean receiveKeyDown(int keycode, int time) {
		char key = (char) Keypad.key(keycode);
		charsJustDown[key] = true;
		charsDown[key] = true;
		return true;
	}

	public boolean receiveKeyUp(int keycode, int time) {
		char key = (char) Keypad.key(keycode);
		charsJustUp[key] = true;
		charsDown[key] = false;
		return true;
	}

	public boolean receiveNavigationMovement(int dx, int dy, int status,
			int time) {

		this.moveDx += dx;
		this.moveDy += dy;
		return true;
	}

}
