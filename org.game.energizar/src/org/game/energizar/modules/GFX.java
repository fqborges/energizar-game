package org.game.energizar.modules;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
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

	// int _backPos, _backSpeed; // Background position and speed
	// Bitmap _backgroundBM; // The bitmap for the background
	Bitmap _healthBM; // The bitmap for the health meter
	Font _gameFont; // The font used for drawing score and lives

	private GFX() {
		// First see if we can get the BBCondensed font at size 16 for our game
		// font.
		// If not, we just go with the default, but all Blackberrys should have
		// this font.
		try {
			_gameFont = FontFamily.forName("BBCondensed").getFont(
					FontFamily.SCALABLE_FONT, 16);
		} catch (Exception e) {
			_gameFont = Font.getDefault();
		}

		// Load the health bitmap from health.png
		_healthBM = Bitmap.getBitmapResource("vida.png");
	}

	// Get current background position
	// public int getBackPos() {
	// return _backPos;
	// }

	// Method that sets the bitmap for the background and sets the scroll speed
	// public void initBackground(String passBackground, int passSpeed) {
	// _backgroundBM = Bitmap.getBitmapResource(passBackground);
	// _backPos = 0;
	// _backSpeed = passSpeed;
	// }

	// Primary function for the graphics engine, draws all the objects, text,
	// health, etc
	public void process(Graphics g, GameLevel gameData) {
		// // Draw our background at the correct position
		// passGraphics.drawBitmap(0, 0, Display.getWidth(),
		// Display.getHeight(),
		// _backgroundBM, 0, _backPos);

		// Move our background.
		// _backPos -= _backSpeed;

		// If we're at the beginning of our bitmap, reset to end
		// if (_backPos < 0) {
		// _backPos = _backgroundBM.getHeight() - _backSpeed;
		// }

		// If we're within screenheight of the end of our bitmap, we need to
		// draw another copy to fill in the blank
		// if (_backgroundBM.getHeight() - _backPos < Display.getHeight()
		// + _backSpeed) {
		// passGraphics.drawBitmap(0, _backgroundBM.getHeight() - _backPos
		// - _backSpeed, Display.getWidth(), Display.getHeight(),
		// _backgroundBM, 0, 0);
		// }

		// seta a cor de fundo como preta
		g.setColor(Color.BLACK);

		// Tamanho da área de desenho
		int canvasWidth = g.getClippingRect().width;
		int canvasHeigth = g.getClippingRect().height;

		// Calcula o tamanho, em pixels, de um espaço do level
		int blockWidth = canvasWidth / gameData.getWidth();
		int blockHeight = canvasHeigth / gameData.getHeigth();
		// normaliza para o menor tamanho
		if (blockWidth > blockHeight) {
			blockWidth = blockHeight;
		} else {
			blockHeight = blockWidth;
		}

		// calcula os deslocamentos para que possa centralizar a tela.
		// o deslocamento é metade do que sobra entre o level e o display.
		int xOffset = (canvasWidth - (blockWidth * gameData.getWidth())) / 2;
		int yOffset = (canvasHeigth - (blockHeight * gameData.getHeigth())) / 2;

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

			Bitmap spriteBitmap = obj.getSpriteBitmap();

			// se o objeto não tem um sprite não desenha ele
			if (spriteBitmap == null) {
				continue;
			}

			XYRect spriteRect = obj.getSpriteRect();

			spriteBitmap.scaleInto(spriteRect.x, spriteRect.y,
					spriteRect.width, spriteRect.height, block, 0, 0,
					blockWidth, blockHeight, Bitmap.FILTER_BILINEAR);

			g.drawBitmap(xOffset + obj.getX() * blockWidth,
					yOffset + obj.getY() * blockHeight, block.getWidth(),
					block.getHeight(), block, 0, 0);
		}

		// // Draw score
		// String zeroPad;
		//
		// // We want to pad the score with 0s
		// zeroPad = "";
		// if (passScore < 10000)
		// zeroPad += "0";
		// if (passScore < 1000)
		// zeroPad += "0";
		// if (passScore < 100)
		// zeroPad += "0";
		// if (passScore < 10)
		// zeroPad += "0";
		//
		// // Draw score and lives in white with the game font
		// passGraphics.setColor(0xFFFFFF);
		// passGraphics.setFont(_gameFont);
		// passGraphics.drawText("Score: " + zeroPad + passScore,
		// Display.getWidth() - 93, 2);
		// passGraphics.drawText("Lives: " + passLives, 7,
		// Display.getHeight() - 20);
		//
		// // Draw health, with width dependent on hero's life
		// if (((OBJ) passObjects.elementAt(0)).getLife() > 0)
		// passGraphics.drawBitmap(7, 7, _healthBM.getWidth()
		// * ((OBJ) passObjects.elementAt(0)).getLife() / 5,
		// _healthBM.getHeight(), _healthBM, 0, 0);
	}
}
