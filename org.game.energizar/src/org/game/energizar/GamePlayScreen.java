package org.game.energizar;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.FullScreen;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.GameLogic;
import org.game.energizar.modules.GFX;
import org.game.energizar.modules.INPUT;

//Código obtido originalmente de http://www.toniwestbrook.com/archives/69
//
// Comentários originais relevantes ao desenvolvimento de jogos para
// Blackberry foram mantidos.
//
//Gameplay extends FullScreen only, it doesn't need the functionality of the
//MainScreen object.  Either are screens though, and can be pushed onto the
//screen stack
//Special Note: For those familiar with the concept double buffering, supposedly
//the rim UI automatically does this.  It seems to from what I can see, I
//notice no tearing or graphic anomalies.
//
public class GamePlayScreen extends FullScreen {

	// dados persistentes do jogo
	private GameLevel _gameLevel;

	// instância do game loop
	GamePlayLoop _gameLoop;

	// O construtor inicializa o jogo e dispara a thread que
	// executa o loop do jogo
	public GamePlayScreen() {

		// level corrente
		_gameLevel = new GameLevel();

		// inicializa a instância do gameloop
		_gameLoop = new GamePlayLoop();

	}

	// Refresh is a type of thread that runs to refresh the game.
	// Its job is to make sure all the processing is called for each object,
	// update the background,
	// update score, check for end of game, etc. This is the main heartbeat.
	private class GamePlayLoop extends Thread {
		private static final int MILISECONDS_PER_TICK = 50;

		// When the object is created it starts itself as a thread
		GamePlayLoop() {
			start();
		}

		// This method defines what this thread does every time it runs.
		public void run() {
			// This thread runs while the game is active
			while (_gameLevel.isGameActive()) {

				// processa as interações do usuário.
				INPUT.instance().process(_gameLevel, MILISECONDS_PER_TICK);

				// aplica as regras do jogo
				GameLogic.instance().process(_gameLevel, MILISECONDS_PER_TICK);

				// causa o redesenho da tela.
				// gera uma execução do método paint
				// // que por sua vez chama o GFX
				invalidate();

				try {
					// Attempt to sleep for 50 ms
					Thread.sleep(MILISECONDS_PER_TICK);

				} catch (InterruptedException e) {
					// Do nothing if we couldn't sleep, we don't care about
					// exactly perfect
					// timing.
				}
			}

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(
							GamePlayScreen.this);
					if (_gameLevel.getError()) {
						Dialog.alert(_gameLevel.getErrorMessage());
					} else {
						Dialog.alert("Game Over");
					}
				}
			});
		}
	}

	// This method is called when the invalidate method is called from the
	// refresh thread.
	// We have it passing the graphics object over to our graphics engine so our
	// custom graphics routines can take care of any drawing necessary.
	protected void paint(Graphics graphics) {
		GFX.instance().process(graphics, _gameLevel);
		super.paint(graphics);
	}

	// The keyChar method is called by the event handler when a key is pressed.
	public boolean keyChar(char key, int status, int time) {
		return INPUT.instance().receiveKeyChar(key, status, time);

	}

	// The keyChar method is called by the event handler when a key is pressed.
	protected boolean keyDown(int keycode, int time) {
		return INPUT.instance().receiveKeyDown(keycode, time);
	}

	// The keyChar method is called by the event handler when a key is pressed.
	protected boolean keyUp(int keycode, int time) {
		return INPUT.instance().receiveKeyUp(keycode, time);
	}

	// The navigationMovement method is called by the event handler when the
	// trackball is used.
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		return INPUT.instance().receiveNavigationMovement(dx, dy, status, time);
	}

}