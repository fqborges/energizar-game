package org.game.energizar.game.features;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;

import org.game.energizar.game.OBJ;
import org.game.energizar.sprites.Sprite;

public class SpriteDrawer {

	/**
	 * @param obj
	 * @param g
	 * @param screenSpriteWidth
	 * @param screenSpriteHeitght
	 * @param screenOffsetX
	 * @param screenOffsetY
	 */
	public static void drawObject(OBJ obj, Graphics g, int screenSpriteWidth,
			int screenSpriteHeitght, int screenOffsetX, int screenOffsetY) {

		// objetos que tenham posição
		// devem ter seu sprite desenhados em sua posição
		Sprite sprite = obj.getSprite();
		if (sprite != null && obj.getPos() != null) {
			XYPoint worldSpritePosition = obj.getPos();

			sprite.drawSprite(worldSpritePosition, screenSpriteWidth,
					screenSpriteHeitght, screenOffsetX, screenOffsetY, g);

		}

		// objetos que tenham caminho devem seu sprite
		// desenhados ao longo do caminho
		if (sprite != null && obj.getPath() != null) {
			for (int i = 0; i < obj.getPath().length(); i++) {
				XYPoint worldSpritePosition = obj.getPath().get(i);
				sprite.drawSprite(worldSpritePosition, screenSpriteWidth,
						screenSpriteHeitght, screenOffsetX, screenOffsetY, g);
			}
		}
	}
}