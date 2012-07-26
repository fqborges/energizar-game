package org.game.energizar.modules;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import net.rim.device.api.ui.XYRect;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.OBJ;

// The GFX class is our game's graphics engine with all the drawing routines
public class GFX_GL {

	// Singleton field
	private static GFX_GL _instance = new GFX_GL();
	private int _canvasWidth;
	private int _canvasHeight;

	// singleton
	public static GFX_GL instance() {
		return GFX_GL._instance;
	}

	private GFX_GL() {
	}

	public void process(GL gl, GameLevel gameData) {

		// seta a cor de fundo como preta
		GL11 gl11 = (GL11) gl;
		gl11.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Calcula o tamanho, em pixels, de um espaço do level
		int blockWidth = _canvasWidth / gameData.getWidth();
		int blockHeight = _canvasHeight / gameData.getHeigth();
		// normaliza para o menor tamanho
		if (blockWidth > blockHeight) {
			blockWidth = blockHeight;
		} else {
			blockHeight = blockWidth;
		}

		// calcula os deslocamentos para que possa centralizar a tela.
		// o deslocamento é metade do que sobra entre o level e o display.
		int xOffset = (_canvasWidth - (blockWidth * gameData.getWidth())) / 2;
		int yOffset = (_canvasHeight - (blockHeight * gameData.getHeigth())) / 2;

		// desenha as bordas do level
		// gl.fillRect(xOffset, yOffset, blockWidth * gameData.getWidth(),
		// blockHeight * gameData.getHeigth());

		// cria um bitmap para ser o buffer dos blocos
		// Bitmap block = new Bitmap(blockWidth, blockHeight);
		// block.createAlpha();

		// Desenha todos objetos na tela
		Vector objects = gameData.objects();
		for (int lcv = 0; lcv < objects.size(); lcv++) {
			OBJ obj = (OBJ) objects.elementAt(lcv);

			// se o objeto não tem um sprite não desenha ele
			if (obj.getSprite() == null) {
				continue;
			}
			// Bitmap spriteBitmap = obj.getSprite().getBitmap();
			XYRect spriteRect = obj.getSprite().getRect();

			// obj.getSprite().drawToBuffer(block, 0, 0, blockWidth,
			// blockHeight,
			// Bitmap.FILTER_BOX);

			// Set the crop parameter
			float[] params = new float[] { spriteRect.x, spriteRect.y,
					spriteRect.width, spriteRect.height };

			gl11.glTexParameterfv(GL11.GL_TEXTURE_2D,
					GL11Ext.GL_TEXTURE_CROP_RECT_OES, params, 0);

			int sx = xOffset + obj.getX() * blockWidth;
			int sy = yOffset + obj.getY() * blockHeight;
			int swidth = blockWidth;
			int sheigth = blockHeight;

			((GL11Ext) gl11).glDrawTexiOES(sx, _canvasHeight - sy, 0, swidth,
					-sheigth);

			//
			// ((GL11Ext) gl11).glDrawTexiOES(xOffset + obj.getX() * blockWidth,
			// yOffset + obj.getY() * blockHeight, 0, blockWidth,
			// blockHeight);

			// spriteBitmap.scaleInto(spriteRect.x, spriteRect.y,
			// spriteRect.width, spriteRect.height, block, 0, 0,
			// blockWidth, blockHeight, Bitmap.FILTER_BILINEAR);

			// gl.drawBitmap(xOffset + obj.getX() * blockWidth,
			// yOffset + obj.getY() * blockHeight, blockWidth,
			// blockHeight, block, 0, 0);
		}

	}

	public void layout(int width, int height) {
		_canvasWidth = width;
		_canvasHeight = height;

	}
}
