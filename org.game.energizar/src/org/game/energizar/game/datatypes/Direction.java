package org.game.energizar.game.datatypes;

import net.rim.device.api.ui.XYPoint;

/**
 * Direction é uma classe que representa uma direção possível entre up ,right,
 * down, left e seus quatro intermediários. Note que direção é um classe
 * imutável e os métodos retornam uma nova direção.
 * 
 * @return a direção rotacionada 45 graus em sentido anti-horário.
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
	 * O valor em graus da direção conforme o círculo unitário trigonométrico.
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
	 * Retorna uma direção rotacionada 45 graus em sentido horário. Note que
	 * direção é um classe imutável e o retorno deste método é uma nova direção.
	 * 
	 * @return a direção rotacionada 45 graus em sentido horário.
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
					"A direção não é uma dentre as possíveis. Ver classe Direction.");
		}
	}

	/**
	 * Retorna uma direção rotacionada 45 graus em sentido anti-horário. Note
	 * que direção é um classe imutável e o retorno deste método é uma nova
	 * direção.
	 * 
	 * @return a direção rotacionada 45 graus em sentido anti-horário.
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
					"A direção não é uma dentre as possíveis. Ver classe Direction.");
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
