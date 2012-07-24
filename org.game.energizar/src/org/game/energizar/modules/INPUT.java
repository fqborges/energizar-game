package org.game.energizar.modules;

import java.util.Enumeration;
import java.util.Vector;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;

public class INPUT {

	Vector _commands = new Vector();

	// Singleton field
	private static INPUT _instance = new INPUT();

	// singleton
	public static INPUT instance() {
		return INPUT._instance;
	}

	private INPUT() {
	}

	public void process(GameLevel gameData) {
		for (Enumeration e = _commands.elements(); e.hasMoreElements();) {
			ProcessGameData p = (ProcessGameData) e.nextElement();
			p.process(gameData);
		}

		_commands.removeAllElements();
	}

	public boolean receiveKeyChar(char key, int status, int time) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean receiveNavigationMovement(int dx, int dy, int status,
			int time) {

		if (dx > 0) {
			// move right
			_commands.addElement(new ProcessGameData() {
				void process(GameLevel gameData) {
					OBJ o = (OBJ) gameData.objects().elementAt(0);
					o.moveRight();
				}
			});
		} else if (dx < 0) {
			// move left
			_commands.addElement(new ProcessGameData() {
				void process(GameLevel gameData) {
					OBJ o = (OBJ) gameData.objects().elementAt(0);
					o.moveLeft();
				}
			});
		}

		if (dy > 0) {
			// move down
			_commands.addElement(new ProcessGameData() {
				void process(GameLevel gameData) {
					OBJ o = (OBJ) gameData.objects().elementAt(0);
					o.moveDown();
				}
			});
		} else if (dy < 0) {
			// move up
			_commands.addElement(new ProcessGameData() {
				void process(GameLevel gameData) {
					OBJ o = (OBJ) gameData.objects().elementAt(0);
					o.moveUp();
				}
			});
		}

		return false;
	}

	private abstract class ProcessGameData {
		abstract void process(GameLevel gameData);
	}
}
