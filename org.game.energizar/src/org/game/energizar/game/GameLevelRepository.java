/**
 * 
 */
package org.game.energizar.game;

/**
 * @author Filipe Borges <fqborges@gmail.com>
 * 
 */
public class GameLevelRepository {

	// Singleton field
	private static GameLevelRepository _instance = new GameLevelRepository();

	// singleton
	public static GameLevelRepository instance() {
		return GameLevelRepository._instance;
	}

	// private for singleton
	private GameLevelRepository() {
	}

	public String[] getGameLevels() {
		return new String[] { LEVEL_1, LEVEL_2, LEVEL_3 };
	}

	/**
	 * Data for Level One
	 */
	public static final String LEVEL_1 = "" + //
			// 2 3 4 5 6 7 8 9 0
			" , , , , , , , , , ;" + // 1
			" , , , , , , , , , ;" + // 2
			" , , , , , , , , , ;" + // 3
			" , , , , , , , , , ;" + // 4
			">, , ,*, , ,*, , ,<;" + // 5
			" , , , , , , , , , ;" + // 6
			" , , , , , , , , , ;" + // 7
			" , , , , , , , , , ;" + // 8
			" , , , , , , , , , ;" + // 9
			" , , , , , , , , , ;"; // 0

	/**
	 * Data for Level Two
	 */
	public static final String LEVEL_2 = "" + //
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

	/**
	 * Data for Level Two
	 */
	public static final String LEVEL_3 = "" + //
			// 2 3 4 5 6 7 8 9 0
			" , , , , , , , , , ;" + // 1
			" , , , , , , , , , ;" + // 2
			" , , , , , , , , , ;" + // 3
			" , , , , , , , , , ;" + // 4
			">, , ,*, , ,*, , ,<;" + // 5
			" , , , , , , , , , ;" + // 6
			" , , , , , , , , , ;" + // 7
			" , , , , , ,*, , , ;" + // 8
			" , , , , , , , , , ;" + // 9
			" , , , , , , , , , ;"; // 0

}
