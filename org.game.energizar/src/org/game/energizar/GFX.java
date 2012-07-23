package org.game.energizar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.*;
import java.util.Vector;

// The GFX class is our game's graphics engine with all the drawing routines
class GFX {
	int _backPos, _backSpeed; // Background position and speed
	Bitmap _backgroundBM; // The bitmap for the background
	Bitmap _healthBM; // The bitmap for the health meter
	Font _gameFont; // The font used for drawing score and lives

	GFX() {
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
	public int getBackPos() {
		return _backPos;
	}

	// Method that sets the bitmap for the background and sets the scroll speed
	public void initBackground(String passBackground, int passSpeed) {
		_backgroundBM = Bitmap.getBitmapResource(passBackground);
		_backPos = 0;
		_backSpeed = passSpeed;
	}

	// Primary function for the graphics engine, draws all the objects, text,
	// health, etc
	public void process(Graphics passGraphics, Vector passObjects,
			int passScore, int passLives) {
		// Draw our background at the correct position
		passGraphics.drawBitmap(0, 0, Display.getWidth(), Display.getHeight(),
				_backgroundBM, 0, _backPos);

		// Move our background.
		_backPos -= _backSpeed;

		// If we're at the beginning of our bitmap, reset to end
		if (_backPos < 0) {
			_backPos = _backgroundBM.getHeight() - _backSpeed;
		}

		// If we're within screenheight of the end of our bitmap, we need to
		// draw another copy to fill in the blank
		if (_backgroundBM.getHeight() - _backPos < Display.getHeight()
				+ _backSpeed) {
			passGraphics.drawBitmap(0, _backgroundBM.getHeight() - _backPos
					- _backSpeed, Display.getWidth(), Display.getHeight(),
					_backgroundBM, 0, 0);
		}

		// Now draw each of our objects to the screen
		for (int lcv = 0; lcv < passObjects.size(); lcv++) {
			passGraphics.drawBitmap(((OBJ) passObjects.elementAt(lcv)).getX(),
					((OBJ) passObjects.elementAt(lcv)).getY(),
					((OBJ) passObjects.elementAt(lcv)).getBitmap().getWidth(),
					((OBJ) passObjects.elementAt(lcv)).getBitmap().getHeight(),
					((OBJ) passObjects.elementAt(lcv)).getBitmap(), 0, 0);
		}

		// Draw score
		String zeroPad;

		// We want to pad the score with 0s
		zeroPad = "";
		if (passScore < 10000)
			zeroPad += "0";
		if (passScore < 1000)
			zeroPad += "0";
		if (passScore < 100)
			zeroPad += "0";
		if (passScore < 10)
			zeroPad += "0";

		// Draw score and lives in white with the game font
		passGraphics.setColor(0xFFFFFF);
		passGraphics.setFont(_gameFont);
		passGraphics.drawText("Score: " + zeroPad + passScore,
				Display.getWidth() - 93, 2);
		passGraphics.drawText("Lives: " + passLives, 7,
				Display.getHeight() - 20);

		// Draw health, with width dependent on hero's life
		if (((OBJ) passObjects.elementAt(0)).getLife() > 0)
			passGraphics.drawBitmap(7, 7, _healthBM.getWidth()
					* ((OBJ) passObjects.elementAt(0)).getLife() / 5,
					_healthBM.getHeight(), _healthBM, 0, 0);
	}

}
