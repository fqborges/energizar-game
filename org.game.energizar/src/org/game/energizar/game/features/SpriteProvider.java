package org.game.energizar.game.features;

import org.game.energizar.sprites.Sprite;

public abstract class SpriteProvider {

	public SpriteProvider() {
		super();
	}

	public abstract Sprite getSprite();

	public Sprite[] getSprites() {
		return new Sprite[] { this.getSprite() };
	}

}