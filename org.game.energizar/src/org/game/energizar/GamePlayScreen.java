package org.game.energizar;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import net.rim.device.api.opengles.GLField;
import net.rim.device.api.opengles.GLUtils;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.FullScreen;

import org.game.energizar.game.GameLevel;
import org.game.energizar.game.GameLogic;
import org.game.energizar.modules.GFX_GL;
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

	private GameField _glField;

	// O construtor inicializa o jogo e dispara a thread que
	// executa o loop do jogo
	public GamePlayScreen() {

		// level corrente
		_gameLevel = new GameLevel();

		// inicializa a instância do gameloop
		_gameLoop = new GamePlayLoop();

		_glField = new GameField(GLField.VERSION_1_1);

		// _glField.setTargetFrameRate(20);
		this.add(_glField);

	}

	private final class GameField extends GLField {
		private GameField(int version) {
			super(version);
		}

		public void redraw() {
			this.invalidate();
		}

		protected void layout(int width, int height) {
			setExtent(Math.min(width, Display.getWidth()),
					Math.min(height, Display.getHeight()));
			GFX_GL.instance().layout(width, height);
		}

		protected void render(GL gl) {
			if (_gameLevel != null) {
				GFX_GL.instance().process(gl, _gameLevel);
			}
		}

		protected void update() {
			// processa as interações do usuário.
			// int miliseconds = 1000 / this.getTargetFrameRate();
			// INPUT.instance().process(_gameLevel, miliseconds);

			// aplica as regras do jogo
			// GameLogic.instance().process(_gameLevel, miliseconds);

			// if (!_gameLevel.isGameActive()) {
			// quitGame();
			// }
		}

		private void quitGame() {
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

		protected void initialize(GL gl) {
			GL11 gl11 = (GL11) gl;
			gl11.glClearColor(0, 0, 0, 1);
			gl11.glEnable(GL11.GL_TEXTURE_2D);
			gl11.glEnable(GL11.GL_BLEND);
			gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			int[] textureNameWorkspace = new int[1];
			gl11.glGenTextures(1, textureNameWorkspace, 0);
			int textureName = textureNameWorkspace[0];
			gl11.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D,
					GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D,
					GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);

			gl11.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_MODULATE);

			Bitmap bitmap = Bitmap.getBitmapResource("gameart.png");

			GLUtils.glTexImage2D(gl, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
					bitmap, null);
		}
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
				_glField.redraw();

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
	// protected void paint(Graphics graphics) {
	// GFX.instance().process(graphics, _gameLevel);
	// super.paint(graphics);
	// }

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