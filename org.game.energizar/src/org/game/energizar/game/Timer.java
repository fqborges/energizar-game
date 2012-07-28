package org.game.energizar.game;

public class Timer {

	private final int _time;
	private int _count;
	private boolean _disabled;

	public Timer(int timeInMilliseconds) {
		this._time = timeInMilliseconds;
		this._count = 0;
		this._disabled = false;
	}

	/**
	 * Notifies this timer that time has passed.
	 * 
	 * @param milliseconds
	 *            milliseconds since last run
	 * @param gameLevel
	 *            game data
	 * 
	 */
	public void tick(int milliseconds, GameLevel gameLevel) {
		if (this._disabled) {
			return;
		} else {
			int lastCount = this._count;
			this._count += milliseconds;
			boolean trigger = (lastCount < _time) && (this._count >= _time);
			if (trigger) {
				run(gameLevel);
			}
		}
	}

	protected void run(GameLevel gameLevel) {
	}

	public void reset() {
		this._count = 0;
	}

	public void disable() {
		this._disabled = true;
	}
}
