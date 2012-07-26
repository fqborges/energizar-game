package org.game.energizar.game.datatypes;

import net.rim.device.api.ui.XYPoint;

/**
 * Direction � uma classe que representa uma dire��o poss�vel entre up ,right,
 * down, left e seus quatro intermedi�rios. Note que dire��o � um classe
 * imut�vel e os m�todos retornam uma nova dire��o.
 * 
 * @return a dire��o rotacionada 45 graus em sentido anti-hor�rio.
 */
public class Direction {

	public static final int VALUE_RIGHT = 0;
	public static final int VALUE_UP_RIGTH = 45;
	public static final int VALUE_UP = 90;
	public static final int VALUE_UP_LEFT = 135;
	public static final int VALUE_LEFT = 180;
	public static final int VALUE_DOWN_LEFT = 225;
	public static final int VALUE_DOWN = 270;
	public static final int VALUE_DOWN_RIGHT = 315;

	/**
	 * O valor em graus da dire��o conforme o c�rculo unit�rio trigonom�trico.
	 */
	public final int value;

	private Direction(int value) {
		this.value = value;
	}

	public static final Direction Right = new Direction(VALUE_RIGHT);
	public static final Direction UpRight = new Direction(VALUE_UP_RIGTH);
	public static final Direction Up = new Direction(VALUE_UP);
	public static final Direction UpLeft = new Direction(VALUE_UP_LEFT);
	public static final Direction Left = new Direction(VALUE_LEFT);
	public static final Direction DownLeft = new Direction(VALUE_DOWN_LEFT);
	public static final Direction Down = new Direction(VALUE_DOWN);
	public static final Direction DownRight = new Direction(VALUE_DOWN_RIGHT);

	public boolean equals(Object obj) {
		return (obj instanceof Direction)
				&& ((Direction) obj).value == this.value;
	}

	/**
	 * Retorna uma dire��o rotacionada 45 graus em sentido hor�rio. Note que
	 * dire��o � um classe imut�vel e o retorno deste m�todo � uma nova dire��o.
	 * 
	 * @return a dire��o rotacionada 45 graus em sentido hor�rio.
	 */
	public Direction RotateCW() {
		switch (value) {
		case VALUE_RIGHT:
			return Direction.DownRight;
		case VALUE_DOWN_RIGHT:
			return Direction.Down;
		case VALUE_DOWN:
			return Direction.DownLeft;
		case VALUE_DOWN_LEFT:
			return Direction.Left;
		case VALUE_LEFT:
			return Direction.UpLeft;
		case VALUE_UP_LEFT:
			return Direction.Up;
		case VALUE_UP:
			return Direction.UpRight;
		case VALUE_UP_RIGTH:
			return Direction.Right;

		default:
			throw new java.lang.IllegalStateException(
					"A dire��o n�o � uma dentre as poss�veis. Ver classe Direction.");
		}
	}

	/**
	 * Retorna uma dire��o rotacionada 45 graus em sentido anti-hor�rio. Note
	 * que dire��o � um classe imut�vel e o retorno deste m�todo � uma nova
	 * dire��o.
	 * 
	 * @return a dire��o rotacionada 45 graus em sentido anti-hor�rio.
	 */
	public Direction RotateCCW() {
		switch (value) {
		case VALUE_RIGHT:
			return Direction.UpRight;
		case VALUE_UP_RIGTH:
			return Direction.Up;
		case VALUE_UP:
			return Direction.UpLeft;
		case VALUE_UP_LEFT:
			return Direction.Left;
		case VALUE_LEFT:
			return Direction.DownLeft;
		case VALUE_DOWN_LEFT:
			return Direction.Down;
		case VALUE_DOWN:
			return Direction.DownRight;
		case VALUE_DOWN_RIGHT:
			return Direction.Right;

		default:
			throw new java.lang.IllegalStateException(
					"A dire��o n�o � uma dentre as poss�veis. Ver classe Direction.");
		}
	}

	public XYPoint toMoveIncrement() {

		int x = 0;
		int y = 0;

		switch (value) {

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
		switch (value) {
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
