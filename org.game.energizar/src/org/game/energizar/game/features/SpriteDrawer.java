package org.game.energizar.game.features;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;

import org.game.energizar.game.OBJ;
import org.game.energizar.sprites.Sprite;

/**
 * Feature for make an object drawable by using sprites.
 * <p>
 * For now this feature is static and depends on the object feature
 * <code>SpriteProvider<code>
 * 
 * @author Filipe Borges <fqborges@gmail.com>
 */
public class SpriteDrawer {

	/**
	 * @param object
	 *            the object to draw
	 * @param g
	 *            an graphics to paint the object to
	 * @param screenSpriteWidth
	 *            the width of one sprite in screen coordinates
	 * @param screenSpriteHeitght
	 *            the height of one sprite in screen coordinates
	 * @param screenOffsetX
	 *            the offset x of the screen origin
	 * @param screenOffsetY
	 *            the offset y of the screen origin
	 */
	public static void drawObject(OBJ object, Graphics g,
			int screenSpriteWidth, int screenSpriteHeitght, int screenOffsetX,
			int screenOffsetY) {

		if (object.getSpriteProvider() == null) {
			return;
		}

		// objetos que tenham posição
		// devem ter seu sprite desenhados em sua posição
		Sprite sprite = object.getSpriteProvider().getSprite();
		if (sprite != null && object.getPos() != null) {
			XYPoint worldSpritePosition = object.getPos();

			sprite.drawSprite(g, worldSpritePosition, screenSpriteWidth,
					screenSpriteHeitght, screenOffsetX, screenOffsetY);

		}

		// objetos que tenham caminho devem seu sprite
		// desenhados ao longo do caminho
		Sprite[] sprites = object.getSpriteProvider().getSprites();
		if (sprites != null && object.getPath() != null
				&& sprites.length == object.getPath().length()) {
			// para cada ponto do caminho...
			for (int i = 0; i < object.getPath().length(); i++) {
				// desenha a sprite referente ao ponto
				XYPoint worldSpritePosition = object.getPath().get(i);
				sprites[i].drawSprite(g, worldSpritePosition,
						screenSpriteWidth, screenSpriteHeitght, screenOffsetX,
						screenOffsetY);
			}

		}
	}
}