package org.game.energizar.game;

public class Timer {

	private final int ticks;
	private int count;
	private boolean disabled;

	private final Runnable event;

	public Timer(int timeInTicks, Runnable event) {
		this.ticks = timeInTicks;
		this.count = 0;
		this.event = event;
		this.disabled = false;
	}

	public void tick() {
		if (this.disabled) {
			return;
		} else {
			this.count++;
			if (this.count > ticks) {
				if (this.event != null) {
					this.event.run();
				}
			}
		}
	}

	public void reset() {
		this.count = 0;
	}

	public void disable() {
		this.disabled = true;
	}
}
