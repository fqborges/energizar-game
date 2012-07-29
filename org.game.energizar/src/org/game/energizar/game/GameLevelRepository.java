/**
 * 
 */
package org.game.energizar.game;

import java.util.Vector;

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

	private Vector _levels;

	// private for singleton
	private GameLevelRepository() {
		Vector levelList = new Vector();
		LevelDescriptor level = null;
		//
		// create a level
		level = new LevelDescriptor("Level Um", LEVEL_DATA_1,
				"O objetivo é levar energia até a casinha.\n\n"
						+ "Para disparar energia use <espaco>.");
		levelList.addElement(level);// create a level
		level = new LevelDescriptor("Level Dois", LEVEL_DATA_2,
				"Um ponto de conexão de energia movimenta-se quando energizado!\n"
						+ "Tome cuidado para não errar o disparo.");
		levelList.addElement(level);
		// create a level
		level = new LevelDescriptor(
				"Level Três",
				LEVEL_DATA_3,
				"Para levar energia até o fim você poderá ter que passar por diversos pontos.\n"
						+ "Lembre-se, não erre os disparos!");
		levelList.addElement(level);
		// create a level
		level = new LevelDescriptor("Level Quatro", LEVEL_DATA_4,
				"Você deve energizar todos pontos antes de terminar.");
		levelList.addElement(level);
		// create a level
		level = new LevelDescriptor("Level Cinco", LEVEL_DATA_5,
				"Isto pode ficar um pouco mais complicado.");
		levelList.addElement(level);
		// create a level
		level = new LevelDescriptor("Level Seis", LEVEL_DATA_6, null);
		levelList.addElement(level);
		//
		this._levels = levelList;
	}

	public LevelDescriptor[] getGameLevels() {
		LevelDescriptor[] arrLevels = new LevelDescriptor[_levels.size()];
		_levels.copyInto(arrLevels);
		return arrLevels;
	}

	/**
	 * Data for Level Two
	 */
	private static final String LEVEL_DATA_1 = "" + //
			// 2 3 4 5 6 7 8
			"#,#,#,#,#,#,#,#;" + // 1
			"#, , , , , ,#,#;" + // 2
			"#, , , , , , ,#;" + // 3
			">, , , , , , ,<;" + // 4
			"#, , , , , , ,#;" + // 5
			"#,#, , , , , ,#;" + // 6
			"#,#,#,#,#,#,#,#;"; // 7

	/**
	 * Data for Level Two
	 */
	private static final String LEVEL_DATA_2 = "" + //
			// 2 3 4 5 6 7 8 9 0
			"#,#,#,#,#,#,#,#,#;" + // 1
			"#, , , , , , ,#,#;" + // 2
			"#, , , , , , , ,#;" + // 3
			">, , , ,*, , , ,<;" + // 4
			"#, , , , , , , ,#;" + // 5
			"#,#, , , , , ,#,#;" + // 6
			"#, ,#, , , ,#, ,#;" + // 7
			"#,#,#,#,#,#,#,#,#;"; // 8

	/**
	 * Data for Level Two
	 */
	private static final String LEVEL_DATA_3 = "" + //
			// 2 3 4 5 6 7 8 9 0
			"#,#,#,#,#,#,#,#,#,#;" + // 1
			"#, , , , , , , , ,#;" + // 2
			"#, , , ,*, , , , ,#;" + // 3
			"#, , , , , , , , ,#;" + // 4
			">, ,*, , , , , , ,#;" + // 5
			"#, , , , , , ,*, ,<;" + // 6
			"#, , , , , , , , ,#;" + // 7
			"#,#,#,#,#,#,#,#,#,#;" + // 8
			"#, , , , , , , , ,#;" + // 9
			"#,#,#,#,#,#,#,#,#,#;"; // 0

	/**
	 * Data for Level Three
	 */
	private static final String LEVEL_DATA_4 = "" + //
			// 2 3 4 5 6 7 8 9 0
			"#,#,#,#,#,#,#,#,#,#;" + // 1
			"#, , , , , , , , ,#;" + // 2
			"#,#,#,#,#,#,#,#,#,#;" + // 3
			"#, , , , , , , , ,#;" + // 4
			">, , ,*, , ,*, , ,<;" + // 5
			"#, , , , , , , , , ;" + // 6
			"#,#, , , , , , , ,#;" + // 7
			"#, ,#, , , ,*, , ,#;" + // 8
			"#, , ,#, , , , , ,#;" + // 9
			"#,#,#,#,#,#,#,#,#,#;"; // 0

	/**
	 * Data for Level Four
	 */
	private static final String LEVEL_DATA_5 = "" + //
			// 2 3 4 5 6 7 8 9 0
			"#,#,#,#,#,#,#,#,#,#;" + // 1
			"#, , , , , , , , ,#;" + // 2
			"#,*, , , ,*, ,*, ,#;" + // 3
			"#, ,', , , ,*, , , ;" + // 4
			">, , ,*, , , , , ,<;" + // 5
			"#, , , , , ,*, , , ;" + // 6
			"#,#, ,*, ,*, ,*, ,#;" + // 7
			"#,#, , , , , , , ,#;" + // 8
			"#,#,#,#,#,#,#,#,#,#;" + // 9
			"#,#,#,#,#,#,#,#,#,#;"; // 0

	/**
	 * Data for Level Five
	 */
	private static final String LEVEL_DATA_6 = "" + //
			// 2 3 4 5 6 7 8 9 0
			" , ,#,#,#,#,#,#, , ;" + // 1
			" ,#, ,*, , ,*, ,#, ;" + // 2
			"#, , , , , , , , ,#;" + // 3
			"#,*, ,#, , ,#, ,*,#;" + // 4
			"#, , , , , , , , ,#;" + // 5
			" , , , , , , , , , ;" + // 6
			" ,*, , ,#,#, , ,*, ;" + // 7
			" , , , , , , , , , ;" + // 8
			">, , ,*, , ,*, , ,<;" + // 9
			"#,#,#,#,#,#,#,#,#,#"; // / 0

	public class LevelDescriptor {
		int _id;
		String _name;
		String _data;
		String _dica;

		public LevelDescriptor(String name, String data, String dica) {
			this._name = name;
			this._data = data;
			this._dica = dica;
		}

		public String getName() {
			return _name;
		}

		public String getData() {
			return _data;
		}

		public String getTip() {
			return _dica;
		}
	}
}
