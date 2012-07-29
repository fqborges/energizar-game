package org.game.energizar.modules;

import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;
import org.game.energizar.game.OBJType;

public class INPUT {

	private static final int NUM_CHARS = Character.MAX_VALUE + 1;
	boolean[] charsDown = new boolean[NUM_CHARS];
	boolean[] charsJustDown = new boolean[NUM_CHARS];
	boolean[] charsJustUp = new boolean[NUM_CHARS];
	int moveDx = 0;
	int moveDy = 0;
	private boolean click = false;

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

	public void process(GameLevel gameData, int milliseconds) {

		if (charsJustDown[Keypad.KEY_ESCAPE]) {
			gameData.deactivateGame();
		}

		if (charsJustDown[Keypad.KEY_SPACE] || click) {
			OBJ o = gameData.getFocusedObject();

			if (o != null) {
				if (o.getTypeID() == OBJType.JUNCTION) {
					o.shoot();
				}
				if (o.getTypeID() == OBJType.STARTPOINT) {
					o.shoot();
				}
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
		click = false;
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

	public boolean receiveNavigationClick(int status, int time) {
		this.click = true;
		return true;
	}

	/**
	 * @param message
	 * @return
	 */
	public boolean receiveTouchEvent(TouchEvent message) {
		switch (message.getEvent()) {
		case TouchEvent.DOWN:
			this.click = true;
			break;
		default:
			break;
		}
		return true;
	}

}
