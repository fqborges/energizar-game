package org.game.energizar.game.features;

import org.game.energizar.sprites.Sprite;

/**
 * Provides sprites for an Object.
 * 
 * @author Filipe
 * 
 */
public class SimpleSpriteProvider extends SpriteProvider {
	private Sprite _sprite;

	public SimpleSpriteProvider(Sprite sprite) {
		this._sprite = sprite;
	}

	public Sprite getSprite() {
		return this._sprite;
	}

}
