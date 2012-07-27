package org.game.energizar.game;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.XYPoint;

import org.game.energizar.game.datatypes.Direction;
import org.game.energizar.game.datatypes.Path;
import org.game.energizar.game.features.SpriteProvider;

// OBJ is our root class for all objects, it defines behaviors and properties
// that are the same for ll objects, whether its the hero, enemies, or photons
public class OBJ {

	// todo objeto tem um id do tipo
	private char _typeID;

	/**
	 * Cria um objeto com o tipo informado.
	 */
	OBJ(char typeID) {
		this.setTypeID(typeID);
	}

	public long getTypeID() {
		return _typeID;
	}

	private void setTypeID(char typeID) {
		_typeID = typeID;
	}

	public static void nullify(OBJ object) {
		// nullify all object features
		object.setPos(null);
		object.setPath(null);
		object.setSpriteProvider(null);
		object.setDirection(null);
		object.setTimer(null);
		object.notifyShotHandled();
		object.setJunctionState(JUNCTION_STATE_NULL);
		object.setConnectionSourceObject(null);
		object.setConnectionTargetObject(null);

		// set object to null type
		object.setTypeID(OBJType.NULL);
	}

	// FEATURE update
	public void update(long gameTime, GameLevel gameData) {
	}

	// FEATURE Position
	private XYPoint _position; // posição

	public XYPoint getPos() {
		return _position;
	}

	public void setPos(XYPoint pos) {
		this._position = pos;
	}

	public void setPos(int x, int y) {
		this.setPos(new XYPoint(x, y));
	}

	// FEATURE Path
	private Path _path;

	public void setPath(Path path) {
		this._path = path;
	}

	public Path getPath() {
		return this._path;
	}

	// FEATURE SpriteProvider
	private SpriteProvider _spriteProvider;

	public SpriteProvider getSpriteProvider() {
		return _spriteProvider;
	}

	public void setSpriteProvider(SpriteProvider spriteProvider) {
		_spriteProvider = spriteProvider;
	}

	// FEATURE Direction
	private Direction _direction = null;

	public Direction getDirection() {
		return _direction;
	}

	void setDirection(Direction _direction) {
		this._direction = _direction;
	}

	// FEATURE Timer
	private Timer _timer;

	public Timer getTimer() {
		return _timer;
	}

	void setTimer(Timer _timer) {
		this._timer = _timer;
	}

	// FEATURE shooter
	protected final static int SHOOTING = 0;
	protected final static int IDLE = 1;
	private int _shootingState = IDLE;

	public void shoot() {
		this._shootingState = SHOOTING;
	}

	public boolean isShooting() {
		return (this._shootingState == SHOOTING);
	}

	public void notifyShotHandled() {
		this._shootingState = IDLE;
	}

	// FEATURE junction
	protected final static int JUNCTION_STATE_NULL = 0;
	protected final static int JUNCTION_OFF = 1;
	protected final static int JUNCTION_ON = 2;
	// protected final static int JUNCTION_CONNECTED = 3;

	private int _junctionState = JUNCTION_OFF;

	private void setJunctionState(int state) {
		this._junctionState = state;
	}

	public int getJunctionState() {
		return _junctionState;
	}

	public void junctionPowerOn() {
		this.setJunctionState(JUNCTION_ON);
	};

	// public void junctionNotifyDisconnected() {
	// this.setJunctionState(JUNCTION_ON);
	// }

	// public void junctionNotifyConected() {
	// this.setJunctionState(JUNCTION_CONNECTED);
	// };

	// Feature Connection
	private OBJ _connectionSourceObj;

	public void setConnectionSourceObject(OBJ sourceObj) {
		this._connectionSourceObj = sourceObj;
	}

	public OBJ getConnectionSourceObject() {
		return _connectionSourceObj;
	}

	private OBJ _connectionTargetObj;

	public void setConnectionTargetObject(OBJ targetOBj) {
		this._connectionTargetObj = targetOBj;
	}

	public OBJ getConnectionTargetObject() {
		return _connectionTargetObj;
	}

	// FEATURE Connected
	public static OBJ getAnyConnectedConnection(OBJ object, GameLevel gameLevel) {

		Vector objsInLevel = gameLevel.objects();

		for (Enumeration eachObject = objsInLevel.elements(); eachObject
				.hasMoreElements();) {
			OBJ connection = (OBJ) eachObject.nextElement();
			if (connection.getConnectionSourceObject() == object
					|| connection.getConnectionTargetObject() == object) {
				return connection;
			}
		}
		return null;
	}

	public static boolean isSourceOfAnyConnection(OBJ object,
			GameLevel gameLevel) {

		Vector objsInLevel = gameLevel.objects();

		for (Enumeration eachObject = objsInLevel.elements(); eachObject
				.hasMoreElements();) {
			OBJ connection = (OBJ) eachObject.nextElement();
			if (connection.getConnectionSourceObject() == object) {
				return true;
			}
		}
		return false;
	}
}
