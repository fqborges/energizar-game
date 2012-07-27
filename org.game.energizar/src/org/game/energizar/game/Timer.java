package org.game.energizar.game;

public class Timer {

	private final int ticks;
	private int count;
	private boolean disabled;

	private Runnable event;

	public Timer(int timeInTicks) {
		this.ticks = timeInTicks;
		this.count = 0;
		this.disabled = false;
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
		if (this.disabled) {
			return;
		} else {
			this.count++;
			if (this.count > ticks) {
				run(gameLevel);
			}
		}
	}

	protected void run(GameLevel gameLevel) {
		if (this.event != null) {
			this.event.run();
		}
	}

	public void reset() {
		this.count = 0;
	}

	public void disable() {
		this.disabled = true;
	}
}
