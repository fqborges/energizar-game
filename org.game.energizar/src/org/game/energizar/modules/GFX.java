package org.game.energizar.modules;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;
import org.game.energizar.game.features.SpriteDrawer;

// The GFX class is our game's graphics engine with all the drawing routines
public class GFX {

	// Singleton field
	private static GFX _instance = new GFX();

	// singleton
	public static GFX instance() {
		return GFX._instance;
	}

	private GFX() {
	}

	public void process(Graphics g, GameLevel gameData) {

		// seta a cor de fundo como preta
		g.setColor(Color.BLACK);

		// Tamanho da área de desenho
		int canvasWidth = g.getClippingRect().width;
		int canvasHeight = g.getClippingRect().height;

		// Calcula o tamanho, em pixels, de um espaço do level
		int blockWidth = canvasWidth / gameData.getWidth();
		int blockHeight = canvasHeight / gameData.getHeigth();
		// normaliza para o menor tamanho
		if (blockWidth > blockHeight) {
			blockWidth = blockHeight;
		} else {
			blockHeight = blockWidth;
		}

		// calcula os deslocamentos para que possa centralizar a tela.
		// o deslocamento é metade do que sobra entre o level e o display.
		int xOffset = (canvasWidth - (blockWidth * gameData.getWidth())) / 2;
		int yOffset = (canvasHeight - (blockHeight * gameData.getHeigth())) / 2;

		// desenha as bordas do level
		g.fillRect(xOffset, yOffset, blockWidth * gameData.getWidth(),
				blockHeight * gameData.getHeigth());

		// dados importantes para desenhar na tela
		int screenSpriteWidth = blockWidth;
		int screenSpriteHeitght = blockHeight;
		int screenOffsetX = xOffset;
		int screenOffsetY = yOffset;

		// Desenha todos objetos na tela
		Vector objects = gameData.objects();
		for (int lcv = 0; lcv < objects.size(); lcv++) {
			OBJ obj = (OBJ) objects.elementAt(lcv);

			SpriteDrawer.drawObject(obj, g, screenSpriteWidth,
					screenSpriteHeitght, screenOffsetX, screenOffsetY);

		}

	}
}
