package org.game.energizar.game;

public class Timer {

	private final int ticks;
	private int count;

	private final Runnable event;

	public Timer(int timeInTicks, Runnable event) {
		this.ticks = timeInTicks;
		this.count = 0;
		this.event = event;
	}

	public void tick() {
		this.count++;
		if (this.count > ticks) {
			if (this.event != null) {
				this.event.run();
			}
		}
	}

	public void reset() {
		this.count = 0;
	}
}
