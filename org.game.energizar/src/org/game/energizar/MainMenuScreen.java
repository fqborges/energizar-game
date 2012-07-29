package org.game.energizar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MainMenuScreen extends MainScreen {

	/**
	 * Creates a new MyScreen object
	 */
	public MainMenuScreen() {
		super(Manager.USE_ALL_WIDTH | Manager.NO_VERTICAL_SCROLL);

		// set centered title
		setTitle(new LabelField("Energizar", LabelField.FIELD_HCENTER));

		// Blackberry: How to center fields on screen?
		// http://stackoverflow.com/questions/8707608/horizontally-centering-fields-in-a-vertical-field-manager
		//
		// A MainScreen has a VerticalFieldManager to lay out its
		// fields from top to bottom, the style bits here tell it
		// to span the whole width of the screen and not to scroll

		// Create a new HorizontalFieldManager which is centered horizontally
		// and spans the whole height of the containing VerticalFieldManager
		HorizontalFieldManager hfm = new HorizontalFieldManager(
				Field.FIELD_HCENTER | Manager.USE_ALL_HEIGHT);

		// Add the HorizontalFieldManager to the VerticalFieldManager
		add(hfm);

		// now add field with style FIELD_VCENTER to center on screen
		VerticalFieldManager vfm = new VerticalFieldManager(Field.FIELD_VCENTER);

		// set main bacground to BLACK
		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.BLACK));

		// set main image
		Bitmap bmpBG = Bitmap.getBitmapResource("mainbg.png");
		BitmapField bmfMainImage = new BitmapField(bmpBG, Field.FIELD_HCENTER);
		bmfMainImage.setMargin(32, 0, 32, 0);
		vfm.add(bmfMainImage);

		// button Start to open SelectLevelScreen
		ButtonField btnStartgame = new ButtonField("Iniciar!",
				Field.FIELD_HCENTER);
		btnStartgame.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				// open select level screen
				UiApplication.getUiApplication().pushScreen(
						new SelectLevelScreen());
			}
		});
		vfm.add(btnStartgame);
		// create a label and chage font and color
		LabelField cc = new LabelField("arte baseada em:\n"
				+ "http://opengameart.org/content/grayscale-icons",
				Field.FOCUSABLE | LabelField.HCENTER) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.GRAY);
				super.paint(graphics);
			}
		};
		Font font = cc.getFont();
		font = font.derive(font.getStyle(), (int) (font.getHeight() * 0.75f));
		cc.setFont(font);
		vfm.add(cc);

		// add vfm to hfm to center it on screen
		hfm.add(vfm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * never prompt user
	 * 
	 * @see net.rim.device.api.ui.container.MainScreen#onSavePrompt()
	 */
	protected boolean onSavePrompt() {
		return true;
	}
}
