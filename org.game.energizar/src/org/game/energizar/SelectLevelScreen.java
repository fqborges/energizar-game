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

				// obtem a despesa da linha.
				Object[] rowData = (Object[]) theModel.getRow(modelRowIndex);
				Object despesa = rowData[0];
				String valor = (String) despesa;

				// cria os campos
				Bitmap bmpOrig = Bitmap.getBitmapResource("explosao.png");
				Bitmap bmpIcone = new Bitmap(48, 48);
				bmpIcone.createAlpha();
				bmpOrig.scaleInto(bmpIcone, Bitmap.FILTER_BILINEAR);
				BitmapField bmfIcone = new BitmapField(bmpIcone);

				LabelField lblValor = new LabelField(valor, Field.USE_ALL_WIDTH
						| DrawStyle.ELLIPSIS);

				// return new Field[] { bmfIcone, lblValor };
				return new Field[] { lblValor };
			}
		};

		// Cria estilos
		Border borderBottom = BorderFactory.createSimpleBorder(new XYEdges(0,
				0, 1, 0), Border.STYLE_SOLID);

		// RegionStyles iconeStyle = new RegionStyles(borderBottom, null, null,
		// null, RegionStyles.ALIGN_CENTER, RegionStyles.ALIGN_MIDDLE);
		RegionStyles valorStyle = new RegionStyles(borderBottom,
				Font.getDefault(), null, null, RegionStyles.ALIGN_LEFT,
				RegionStyles.ALIGN_MIDDLE);
		// col,row colsxrows
		// | 0,0 1x1 | 1,0 1x1 |
		dtTemplate.createRegion(new XYRect(0, 0, 1, 1), valorStyle);
		// dtTemplate.createRegion(new XYRect(1, 0, 1, 1), valorStyle);
		dtTemplate.setRowProperties(0, new TemplateRowProperties(60));
		// dtTemplate.setColumnProperties(0, new TemplateColumnProperties(60));
		// dtTemplate.setColumnProperties(1,
		// new TemplateColumnProperties(Display.getWidth() - 60));
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

		String[] levels;
		try {
			levels = GameLevelRepository.instance().getGameLevels();
		} catch (final Exception e) {
			levels = new String[] {};
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert(e.getMessage());
				}
			});
		}

		for (int i = 0; i < levels.length; i++) {
			// String level = levels[i];
			_tbModel.addRow(new Object[] { "Level " + (i + 1) });
		}

		_tbModel.modelReset();
	}

	private void levelSelecionado(final TableView tbView) {
		int row = tbView.getRowNumberWithFocus();
		String[] levels = GameLevelRepository.instance().getGameLevels();
		// Object d = _tbModel.getElement(row, 0);
		UiApplication.getUiApplication().pushModalScreen(
				new GamePlayScreen(levels[row]));
	}

}
