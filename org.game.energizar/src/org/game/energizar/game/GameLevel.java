package org.game.energizar.game;

import java.util.Vector;

import net.rim.device.api.ui.XYRect;

import org.game.energizar.util.StringUtil;

public class GameLevel {

	Vector _objects;
	boolean _gameActive;
	private boolean _hasError;
	private String _errorMessage;
	private int _height;
	private int _width;
	private OBJ _currentObject = null;
	private int _tries;
	private boolean _ended;
	private boolean _won;

	/**
	 * Creates a new level based on a level data describing this level.
	 * 
	 * @param levelData
	 */
	public GameLevel(String levelData) {

		// inicializa o menor estado válido
		_objects = new Vector();
		_gameActive = true;
		_hasError = false;
		_errorMessage = null;

		// level starts with only 1 tries to make it easy
		_ended = false;
		_won = false;
		_tries = 1;

		// 1. parse level data
		char[][] parsedLevelData = parseLevelData(levelData);

		// 2. validate
		String errorMsg = GameLevel.validateLevelData(parsedLevelData);
		if (errorMsg != null) {
			this.setError(errorMsg);
			return;
		}

		// 3. setup game level
		_height = parsedLevelData.length;
		_width = parsedLevelData[0].length;
		createLevelObjects(parsedLevelData);

	}

	/**
	 * 
	 * @param sLevelData
	 * @return matrix of char describing the level
	 */
	private static char[][] parseLevelData(String sLevelData) {

		char[][] parsedLevel;

		// linhas do level
		String[] linhas = StringUtil.split(sLevelData, ";");

		// cria o array de linhas
		parsedLevel = new char[linhas.length][];

		for (int i = 0; i < linhas.length; i++) {
			// casas na linha
			String[] places = StringUtil.split(linhas[i], ",");

			// cria a linha
			parsedLevel[i] = new char[places.length];

			for (int j = 0; j < places.length; j++) {
				String place = places[j].trim();
				if (place.equals("")) {
					parsedLevel[i][j] = ' ';
				} else {
					parsedLevel[i][j] = place.charAt(0);
				}
			}
		}

		return parsedLevel;
	}

	/**
	 * @param parsedLevelData
	 * @return
	 */
	private static String validateLevelData(char[][] parsedLevelData) {
		// * for now the only validation is all lines must have same width

		// get width of first line
		int width = parsedLevelData[0].length;

		// percorre os dados para verificar se todas
		// linhas tem o mesmo tamanho
		for (int i = 1; i < parsedLevelData.length; i++) {
			char[] linha = parsedLevelData[i];
			// no caso de uma linha ter tamanho diferente
			// entra em estado de erro
			if (linha.length != width) {
				String message = "Os dados do level estão corrompidos: "
						+ "Todas linhas devem ter o mesmo tamanho, mas a linha "
						+ i + "não tem o mesmo tamanho da primeira linha.";
				return message;
			}
		}
		// no errors, no message
		return null;
	}

	private void createLevelObjects(char[][] parsedLevelData) {
		// varre os dados e cria um objeto para cada
		// elemento encontrado
		for (int i = 0; i < parsedLevelData.length; i++) {
			char[] linha = parsedLevelData[i];
			for (int j = 0; j < linha.length; j++) {
				char c = linha[j];
				int posX = j;
				int posY = i;

				OBJ obj = null;
				switch (c) {
				case '*':
					obj = OBJFactory.instance().createJunction(posX, posY);
					break;
				case '>':
					obj = OBJFactory.instance().createStartPoint(posX, posY);
					break;
				case '<':
					obj = OBJFactory.instance().createEndPoint(posX, posY);
					break;
				}
				if (obj != null) {
					Vector objects = this.objects();
					objects.addElement(obj);
					if (obj.getTypeID() == OBJType.STARTPOINT) {
						this.setFocusedObject(obj);
					}
				}
			}
		}
	}

	public Vector objects() {
		return _objects;
	}

	public boolean isGameActive() {
		return _gameActive;
	}

	public void deactivateGame() {
		this._gameActive = false;
	}

	public int getWidth() {
		return _width;
	}

	public int getHeigth() {
		return _height;
	}

	public XYRect getGameArea() {
		return new XYRect(0, 0, this._width, this._height);
	}

	public OBJ getFocusedObject() {
		return _currentObject;
	}

	public void setFocusedObject(OBJ currentObject) {
		this._currentObject = currentObject;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	private void setError(String message) {
		this._gameActive = false;
		this._hasError = true;
		this._errorMessage = message;
	}

	public boolean hasError() {
		return this._hasError;
	}

	public int getRemainingTries() {
		return this._tries;
	}

	public void loseTry() {
		this._tries -= 1;
	}

	/**
	 * end this level
	 * 
	 * @param won
	 *            the game was won.
	 */
	public void end(boolean won) {
		this._ended = true;
		this._won = won;
	}

	/**
	 * @return if this level has ended
	 */
	public boolean isEnded() {
		return this._ended;
	}

	/**
	 * @return
	 */
	public boolean wasWon() {
		return _ended && _won;
	}

	/**
	 * @return
	 */
	public boolean wasLost() {
		return _ended && !_won;
	}

}
