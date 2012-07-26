package org.game.energizar.sprites;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.XYRect;

public class Sprite {

	private Bitmap _bitmap = null; // bitmap que representa o objeto
	private XYRect _spriteRect = null;

	public Sprite(Bitmap bitmap, int x, int y, int width, int heigth) {
		this.setBitmap(bitmap);
		this.setRect(x, y, width, heigth);
	}

	public Sprite(Sprite prototype) {
		this.setBitmap(prototype.getBitmap());
		this._spriteRect = prototype.getRect();
	}

	public Bitmap getBitmap() {
		return _bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this._bitmap = bitmap;
		this.setRect(0, 0, _bitmap.getWidth(), _bitmap.getHeight());
	}

	public XYRect getRect() {
		return new XYRect(_spriteRect);
	}

	public void setRect(int x, int y, int width, int height) {
		this._spriteRect = new XYRect(x, y, width, height);
	}

	public void drawToBuffer(Bitmap buffer, int x, int y, int width,
			int height, int filter) {
		this._bitmap.scaleInto(_spriteRect.x, _spriteRect.y, _spriteRect.width,
				_spriteRect.height, buffer, x, y, width, height,
				Bitmap.FILTER_BILINEAR);
	}

}
