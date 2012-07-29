package org.game.energizar.game;

import org.game.energizar.sprites.Sprite;

import net.rim.device.api.system.Bitmap;

public class ArtResource {
	// Singleton field
	private static ArtResource _instance = new ArtResource();

	// singleton
	public static ArtResource instance() {
		return ArtResource._instance;
	}

	private final Bitmap _bitmap;
	static final int SPRITE_SIZE = 64;

	private ArtResource() {
		this._bitmap = Bitmap.getBitmapResource("gameart.png");
	}

	public Sprite getSprite(int row, int col) {
		Sprite sprite = new Sprite(_bitmap, col * ArtResource.SPRITE_SIZE, row
				* ArtResource.SPRITE_SIZE, ArtResource.SPRITE_SIZE,
				ArtResource.SPRITE_SIZE);
		return sprite;
	}

	/**
	 * @return
	 */
	public Sprite getErrorSprite() { 
		return getSprite(7, 7);
	}

}
