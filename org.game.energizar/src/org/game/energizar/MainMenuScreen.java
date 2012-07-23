package org.game.energizar;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MainMenuScreen extends MainScreen {

	// Botão para iniciar o jogo
	private ButtonField btnStartgame = new ButtonField("Iniciar!",
			Field.FIELD_HCENTER);

	// Label título do jogo
	LabelField title = new LabelField("Energizar", LabelField.FIELD_HCENTER);

	/**
	 * Creates a new MyScreen object
	 */
	public MainMenuScreen() {

		btnStartgame.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				UiApplication.getUiApplication().pushScreen(
						new GamePlayScreen());
			}
		});

		setTitle(title);

		this.add(btnStartgame);

	}
}
