package org.game.energizar.game;

import net.rim.device.api.system.Bitmap;

public class ArtResource {
	// Singleton field
	private static ArtResource _instance = new ArtResource();

	// singleton
	public static ArtResource instance() {
		return ArtResource._instance;
	}

	private final Bitmap _bitmap;

	private ArtResource() {
		this._bitmap = Bitmap.getBitmapResource("gameart.png");
	}

	public Bitmap get() {
		return _bitmap;
	}
}
