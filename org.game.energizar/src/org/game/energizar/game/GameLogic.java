package org.game.energizar.game;

public class GameLogic {

	// Singleton field
	private static GameLogic _instance = new GameLogic();

	// singleton
	public static GameLogic instance() {
		return GameLogic._instance;
	}

	private GameLogic() {
	}

	public void process(GameLevel _gameData) {
		// TODO Auto-generated method stub

	}

}
