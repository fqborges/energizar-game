package org.game.energizar.sprites;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;
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

	private void setBitmap(Bitmap bitmap) {
		this._bitmap = bitmap;
		this.setRect(0, 0, _bitmap.getWidth(), _bitmap.getHeight());
	}

	public XYRect getRect() {
		return new XYRect(_spriteRect);
	}

	private void setRect(int x, int y, int width, int height) {
		this._spriteRect = new XYRect(x, y, width, height);
	}

	/**
	 * @param g
	 * @param worldSpritePosition
	 * @param screenSpriteWidth
	 * @param screenSpriteHeitght
	 * @param screenOffsetX
	 * @param screenOffsetY
	 * @param sprite
	 */
	public void drawSprite(Graphics g, XYPoint worldSpritePosition,
			int screenSpriteWidth, int screenSpriteHeitght, int screenOffsetX,
			int screenOffsetY) {
		XYPoint screenSpritePosition = Sprite.translateToScreenCoordinates(
				worldSpritePosition, screenSpriteWidth, screenSpriteHeitght,
				screenOffsetX, screenOffsetY);

		Sprite.drawToScreen(this, g, screenSpritePosition, screenSpriteWidth,
				screenSpriteHeitght);
	}

	/**
	 * @param worldSpritePosition
	 * @param screenSpriteWidth
	 * @param screenSpriteHeitght
	 * @param screenOffsetX
	 * @param screenOffsetY
	 * @return
	 */
	public static XYPoint translateToScreenCoordinates(
			XYPoint worldSpritePosition, int screenSpriteWidth,
			int screenSpriteHeitght, int screenOffsetX, int screenOffsetY) {
		int posicaoXSpriteEmCoordeadasDeTela = screenOffsetX
				+ worldSpritePosition.x * screenSpriteWidth;
		int posicaoYSpriteEmCoordeadasDeTela = screenOffsetY
				+ worldSpritePosition.y * screenSpriteHeitght;

		XYPoint posicaoSpriteCoordenadasTela = new XYPoint(
				posicaoXSpriteEmCoordeadasDeTela,
				posicaoYSpriteEmCoordeadasDeTela);
		return posicaoSpriteCoordenadasTela;
	}

	/**
	 * @param sprite
	 * @param g
	 * @param screenSpritePosition
	 * @param screenSpriteWidth
	 * @param screenSpriteHeitght
	 */
	public static void drawToScreen(Sprite sprite, Graphics g,
			XYPoint screenSpritePosition, int screenSpriteWidth,
			int screenSpriteHeitght) {
		// cria um bitmap para ser o buffer dos blocos
		Bitmap block = new Bitmap(screenSpriteWidth, screenSpriteHeitght);
		block.createAlpha();

		// escala a sprite no buffer
		sprite.getBitmap().scaleInto(sprite.getRect().x, sprite.getRect().y,
				sprite.getRect().width, sprite.getRect().height, block, 0, 0,
				screenSpriteWidth, screenSpriteHeitght, Bitmap.FILTER_BILINEAR);

		g.drawBitmap(screenSpritePosition.x, screenSpritePosition.y,
				screenSpriteWidth, screenSpriteHeitght, block, 0, 0);
	}

}
