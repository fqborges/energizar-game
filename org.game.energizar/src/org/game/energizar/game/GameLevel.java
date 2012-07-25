package org.game.energizar.game;

import java.util.Vector;

import net.rim.device.api.ui.XYRect;

import org.game.energizar.util.StringUtil;

public class GameLevel {

	Vector _objects;
	boolean _isActive;
	private boolean _hasError;
	private String _errorMessage;
	private int _height;
	private int _width;
	private OBJ _currentObject = null;

	// cria um level do jogo
	public GameLevel() {

		// inicializa o menor estado válido
		_objects = new Vector();
		_isActive = true;
		_hasError = false;
		_errorMessage = null;

		// inicializa o restante a partir de uma string
		// com a descrição do level
		String sLevelData = "" + //
				// 2 3 4 5 6 7 8 9 0
				" , , , , , , , , , ;" + // 1
				" , , , , , , , , , ;" + // 2
				" , , , ,*, , , , , ;" + // 3
				" , , , , , , , , , ;" + // 4
				">, ,*, , , , , , , ;" + // 5
				" , , , , , , ,*, ,<;" + // 6
				" , , , , , , , , , ;" + // 7
				" , , , , , , , , , ;" + // 8
				" , , , , , , , , , ;" + // 9
				" , , , , , , , , , ;"; // 0

		char[][] parsedLevelData = parseLevelData(sLevelData);

		// seta altura e largura
		_height = parsedLevelData.length;
		_width = parsedLevelData[0].length;

		// percorre os dados para verificar se todas
		// linhas tem o mesmo tamanho
		for (int i = 1; i < parsedLevelData.length; i++) {
			char[] linha = parsedLevelData[i];
			// no caso de uma linha ter tamanho diferente
			// entra em estado de erro
			if (linha.length != _width) {
				this.setError("Os dados do level estão corrompidos: "
						+ "Todas linhas devem ter o mesmo tamanho, mas a linha "
						+ i + "não tem o mesmo tamanho da primeira linha.");
				return;
			}
		}

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
					this.objects().addElement(obj);
					if (obj.getTypeID() == OBJType.STARTPOINT) {
						this.setCurrentObject(obj);
					}
				}
			}
		}

	}

	private void setError(String message) {
		this._isActive = false;
		this._hasError = true;
		this._errorMessage = message;
	}

	private char[][] parseLevelData(String sLevelData) {

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

	public Vector objects() {
		return _objects;
	}

	public boolean isGameActive() {
		return _isActive;
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

	public OBJ getCurrentObject() {
		return _currentObject;
	}

	public void setCurrentObject(OBJ currentObject) {
		this._currentObject = currentObject;
	}

	public void endGame() {
		this._isActive = false;
	}

	public String getErrorMessage() {
		return _errorMessage;
	}

	public boolean getError() {
		return this._hasError;
	}

}
