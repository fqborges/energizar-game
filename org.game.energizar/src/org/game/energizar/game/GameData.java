package org.game.energizar.game;

import java.util.Vector;

public class GameData {

	Vector _objects;
	OBJ _current;
	boolean _isActive;

	public GameData() {
		_objects = new Vector();
		_current = null;
		_isActive = true;
	}

	public Vector objects() {
		return _objects;
	}

	public void setCurrentObject(OBJ object) {
		this._current = object;
	}

	public boolean isGameActive() {
		return _isActive;
	}

}
