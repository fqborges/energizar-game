package org.game.energizar;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.table.DataTemplate;
import net.rim.device.api.ui.component.table.RegionStyles;
import net.rim.device.api.ui.component.table.TableController;
import net.rim.device.api.ui.component.table.TableModel;
import net.rim.device.api.ui.component.table.TableView;
import net.rim.device.api.ui.component.table.TemplateColumnProperties;
import net.rim.device.api.ui.component.table.TemplateRowProperties;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import org.game.energizar.game.GameLevelRepository;
import org.game.energizar.game.GameLevelRepository.LevelDescriptor;

public class SelectLevelScreen extends MainScreen {

	private TableModel _tbModel;

	public SelectLevelScreen() {
		super(Manager.NO_VERTICAL_SCROLL);

		setTitle("Energizar");

		// adiciona os componentes na tela.
		add(new LabelField("Selecione um nível.", LabelField.FIELD_HCENTER));
		add(new SeparatorField());
		TableView tbView = criaTableView();
		this.add(tbView);

		atualizaDespesas();
	}

	private TableView criaTableView() {
		_tbModel = new TableModel();
		final TableView tbView = new TableView(_tbModel);
		TableController tbController = new TableController(_tbModel, tbView,
				TableController.ROW_FOCUS);

		tbView.setController(tbController);

		tbController.setCommand(new Command(new CommandHandler() {
			public void execute(ReadOnlyCommandMetadata metadata, Object context) {
				levelSelecionado(tbView);
			}
		}));

		final int ROWS = 1;
		final int COLS = 1;

		// Initialize the DataTemplate.
		DataTemplate dtTemplate = new DataTemplate(tbView, ROWS, COLS) {

			public Field[] getDataFields(int modelRowIndex) {
				TableModel theModel = (TableModel) getView().getModel();

				// obtem level da linha.
				Object[] rowData = (Object[]) theModel.getRow(modelRowIndex);
				LevelDescriptor level = (LevelDescriptor) rowData[0];
				String name = level.getName();

				// cria os campos
				LabelField lblName = new LabelField(name, Field.USE_ALL_WIDTH
						| DrawStyle.ELLIPSIS);

				// return new Field[] { bmfIcone, lblValor };
				return new Field[] { lblName };
			}
		};

		// Cria estilos
		Border borderBottom = BorderFactory.createSimpleBorder(new XYEdges(0,
				0, 1, 0), Border.STYLE_SOLID);

		RegionStyles styleName = new RegionStyles(borderBottom,
				Font.getDefault(), null, null, RegionStyles.ALIGN_LEFT,
				RegionStyles.ALIGN_MIDDLE);
		// col,row colsxrows
		// | 0,0 1x1 | 1,0 1x1 |
		dtTemplate.createRegion(new XYRect(0, 0, 1, 1), styleName);
		dtTemplate.setRowProperties(0, new TemplateRowProperties(60));
		dtTemplate.setColumnProperties(0,
				new TemplateColumnProperties(Display.getWidth()));
		dtTemplate.useFixedHeight(true);
		tbView.setDataTemplate(dtTemplate);

		return tbView;
	}

	private void atualizaDespesas() {
		// limpa a lista de despesas
		if (_tbModel.getNumberOfRows() > 0) {
			_tbModel.removeRowRangeAt(0, _tbModel.getNumberOfRows());
		}

		LevelDescriptor[] levels;
		try {
			levels = GameLevelRepository.instance().getGameLevels();
		} catch (final Exception e) {
			levels = new LevelDescriptor[] {};
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert(e.getMessage());
				}
			});
		}

		for (int i = 0; i < levels.length; i++) {
			_tbModel.addRow(new Object[] { levels[i] });
		}

		_tbModel.modelReset();
	}

	private void levelSelecionado(final TableView tbView) {
		int row = tbView.getRowNumberWithFocus();
		LevelDescriptor level = (LevelDescriptor) _tbModel.getElement(row, 0);
		UiApplication.getUiApplication().pushModalScreen(
				new GamePlayScreen(level));
	}
}
