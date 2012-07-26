package org.game.energizar.modules;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;

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

		// cria um bitmap para ser o buffer dos blocos
		Bitmap block = new Bitmap(blockWidth, blockHeight);
		block.createAlpha();

		// Desenha todos objetos na tela
		Vector objects = gameData.objects();
		for (int lcv = 0; lcv < objects.size(); lcv++) {
			OBJ obj = (OBJ) objects.elementAt(lcv);

			// se o objeto não tem um sprite não desenha ele
			if (obj.getSprite() == null) {
				continue;
			} else {
				// Bitmap spriteBitmap = obj.getSprite().getBitmap();
				// XYRect spriteRect = obj.getSprite().getRect();

				obj.getSprite().drawToBuffer(block, 0, 0, blockWidth,
						blockHeight, Bitmap.FILTER_BOX);
				g.drawBitmap(xOffset + obj.getX() * blockWidth,
						yOffset + obj.getY() * blockHeight, blockWidth,
						blockHeight, block, 0, 0);
			}
			// spriteBitmap.scaleInto(spriteRect.x, spriteRect.y,
			// spriteRect.width, spriteRect.height, block, 0, 0,
			// blockWidth, blockHeight, Bitmap.FILTER_BILINEAR);

		}

	}
}
