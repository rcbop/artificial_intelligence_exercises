package br.fbv.rcbop.knn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

import br.fbv.rcbop.knn.controller.Facade;

public class MainScreen extends JFrame implements ActionListener, IConsoleListener {
	private static final long	serialVersionUID	= 2656701702991480497L;
	private static final int	DARK_RED			= 0xC70A00;
	private static final int	DARK_GREEN			= 0x0FA60F;

	// Layout Managers
	private GridLayout			matrixGridLayout;
	private GridLayout			scoreGridLayout;
	private BorderLayout		borderLayout;

	// Components
	private JButton				btnInitReading;
	private JComboBox<String>	comboCalcMode;
	private String[]			comboCalcModeModels	= { "EUCLIDES", "MINKOWSKI", "MANHATTAN" };
	private final String[]		lblAttributes		= { "X", "SET", "VER", "VIR" };
	private JTextField			txtFieldMikP;

	private JTextField			txtKField;
	private JTextArea			console;
	private JScrollPane			scrollPane;

	// Tabs Host
	private JTabbedPane			tabPanelMain;

	// Main Tab Panels
	private JPanel				panelTabMain;
	private JPanel				panelScoreArea;
	private JPanel				panelTopTab1;
	private JPanel				panelTab1Bottom;

	private JPanel				panelMatrixArea;
	private JPanel				panelMatrixAreaCenter;
	private JPanel				panelMatrixAreaLeft;
	private JPanel				panelMatrixAreaTop;
	private JPanel				panelScoreAreaTop;
	private JPanel				panelScoreAreaCenter;
	private JPanel				panelScoreAreaBottom;

	// Chart Tab Panels
	private TabPlotChartPie		tabPlotChart;

	// Matrix
	private JLabel[][]			txtMatrixOutuput;
	// Score
	private JLabel[][]			txtScoreArea;

	public MainScreen() {
		initLayouts();
		initGui();
		Facade.getInstance().setConsoleListener(this);
	}

	public void initLayouts() {
		matrixGridLayout = new GridLayout(4, 4, 2, 2);
		scoreGridLayout = new GridLayout(2, 3, 2, 2);
		borderLayout = new BorderLayout();
	}

	public void initGui() {
		tabPanelMain = new JTabbedPane(JTabbedPane.TOP);

		btnInitReading = new JButton("Iniciar Leitura");
		btnInitReading.addActionListener(this);

		assembleMatrixArea();

		assembleScoreArea();

		panelTabMain = new JPanel(new BorderLayout());
		panelTabMain.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		panelTopTab1 = new JPanel(new GridLayout(1, 2));
		panelTopTab1.add(panelMatrixArea);

		panelTopTab1.add(panelScoreArea);
		panelTabMain.add(panelTopTab1, BorderLayout.CENTER);
		console = new JTextArea(10, 150);
		console.setEditable(false);
		scrollPane = new JScrollPane(console);
		scrollPane.setBounds(10, 60, 600, 100);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		panelTab1Bottom = new JPanel(new BorderLayout());
		panelTab1Bottom.add(scrollPane, BorderLayout.CENTER);
		panelTab1Bottom.add(btnInitReading, BorderLayout.SOUTH);
		panelTabMain.add(panelTab1Bottom, BorderLayout.SOUTH);

		tabPanelMain.add("Aprendizado kNN ", panelTabMain);

		this.setSize(600, 420);
		this.setLayout(borderLayout);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(tabPanelMain);
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Aprendizagem Knn");
		// ImageIcon appIcon = Util.createImageIcon("images" + File.separator +
		// "icon-ai.png", "kNN Artificial Inteligence");
		// this.setIconImage(appIcon.getImage());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("images" + File.separator + "icon-ai.png"));

	}

	private void assembleScoreArea() {
		panelScoreArea = new JPanel(new BorderLayout());
		panelScoreAreaTop = new JPanel(new FlowLayout());
		panelScoreAreaTop.add(new JLabel("PONTUAÇÃO"));
		panelScoreAreaCenter = new JPanel(scoreGridLayout);
		initScorePanel();

		panelScoreAreaBottom = new JPanel(new FlowLayout());

		panelScoreAreaBottom.add(new JLabel("k"));
		txtKField = new JTextField(2);
		panelScoreAreaBottom.add(txtKField);

		panelScoreAreaBottom.add(new JLabel("Distância: "));
		comboCalcMode = new JComboBox<String>(comboCalcModeModels);
		comboCalcMode.addActionListener(this);
		panelScoreAreaBottom.add(comboCalcMode);

		panelScoreAreaBottom.add(new JLabel("p"));

		txtFieldMikP = new JTextField(2);
		txtFieldMikP.setEnabled(false);
		panelScoreAreaBottom.add(txtFieldMikP);

		panelScoreArea.add(panelScoreAreaTop, BorderLayout.NORTH);
		panelScoreArea.add(panelScoreAreaCenter, BorderLayout.CENTER);
		panelScoreArea.add(panelScoreAreaBottom, BorderLayout.SOUTH);

		panelScoreArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	private void assembleMatrixArea() {
		panelMatrixArea = new JPanel(new BorderLayout());
		panelMatrixAreaCenter = new JPanel(matrixGridLayout);
		initConfusionMatrix();

		panelMatrixAreaLeft = new JPanel(new GridBagLayout());
		VerticalJLabel vLabel = new VerticalJLabel("Resultado Real");
		panelMatrixAreaLeft.add(vLabel, new GridBagConstraints());

		panelMatrixAreaTop = new JPanel(new GridLayout(2, 1, 2, 2));
		JLabel label = new JLabel("MATRIZ DE CONFUSÃO");
		JLabel label2 = new JLabel("Resultado Previsto");
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);
		label2.setVerticalAlignment(JLabel.CENTER);
		label2.setHorizontalAlignment(JLabel.CENTER);
		panelMatrixAreaTop.add(label);
		panelMatrixAreaTop.add(label2);

		panelMatrixArea.add(panelMatrixAreaCenter, BorderLayout.CENTER);
		panelMatrixArea.add(panelMatrixAreaTop, BorderLayout.NORTH);
		panelMatrixArea.add(panelMatrixAreaLeft, BorderLayout.WEST);
		panelMatrixArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}

	private void initConfusionMatrix() {
		txtMatrixOutuput = new JLabel[4][4];
		JLabel label;
		for (int i = 0; i < txtMatrixOutuput.length; i++) {
			for (int j = 0; j < txtMatrixOutuput[i].length; j++) {
				if (i == 0) {
					label = txtMatrixOutuput[i][j] = new JLabel(lblAttributes[j]);
				} else if (i != 0 && j == 0) {
					label = txtMatrixOutuput[i][j] = new JLabel(lblAttributes[i]);
				} else {
					label = txtMatrixOutuput[i][j] = new JLabel("0");
					if (i == j) {
						label.setForeground(new Color(DARK_GREEN));
					} else {
						label.setForeground(new Color(DARK_RED));
					}
				}
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				panelMatrixAreaCenter.add(txtMatrixOutuput[i][j]);
			}
		}
	}

	private void initScorePanel() {
		txtScoreArea = new JLabel[2][3];
		for (int i = 0; i < txtScoreArea.length; i++) {
			for (int j = 0; j < txtScoreArea[i].length; j++) {
				JLabel label = txtScoreArea[i][j] = new JLabel("0");
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				panelScoreAreaCenter.add(txtScoreArea[i][j]);
			}
		}
		txtScoreArea[0][0].setText("Acertos");
		txtScoreArea[0][1].setText("Erros");
		txtScoreArea[0][2].setText("Eficiencia");
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainScreen main = new MainScreen();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == comboCalcMode) {
			if (comboCalcMode.getSelectedIndex() != 1) {
				txtFieldMikP.setEnabled(false);
			} else {
				txtFieldMikP.setEnabled(true);
			}
		} else if (event.getSource() == btnInitReading) {
			int distMode = comboCalcMode.getSelectedIndex() + 1;
			int p = 0;
			boolean valid = false;
			if (distMode == Facade.MINKOWSKI && txtFieldMikP.getText() != null && !txtFieldMikP.getText().equals("")) {
				p = Integer.valueOf(txtFieldMikP.getText());
				if (p <= 0) {
					showNegativePMsg();
				} else {
					valid = true;
				}
			} else {
				if (distMode == Facade.MINKOWSKI) {
					showNegativePMsg();
				}
			}
			int k = 0;
			if (txtKField.getText() != null && !txtKField.getText().equals("")) {
				k = Integer.valueOf(txtKField.getText());
				if (k > 0) {
					if (k < getTestSize()) {
						if (distMode != Facade.MINKOWSKI || valid) {
							doAlgorithm(distMode, p, k);
						}
					} else {
						showKTooLargeMsg();
					}
				} else {
					showNegativeKMsg();
				}
			} else {
				showNullKMsg();
			}
		}

	}

	private void doAlgorithm(int distMode, int p, int k) {
		try {
			btnInitReading.setEnabled(false);
			int[][] confusionValues = Facade.getInstance().initAlgorithmAndCalculateClass(k, distMode, p);
			fillConfusionMatrix(confusionValues);
			fillStatistics();
			plotChart(confusionValues);

			btnInitReading.setEnabled(true);
		} catch (IOException e) {
			showIOErrorMsg();
			System.exit(0);
			e.printStackTrace();
		}
	}

	private void plotChart(int[][] confusionMatrix) {
		tabPlotChart = new TabPlotChartPie(new BorderLayout(), confusionMatrix);

		tabPanelMain.add("Gráficos", tabPlotChart);
	}

	private void fillStatistics() {
		txtScoreArea[1][0].setText(Facade.getInstance().getSuccess() + "");
		txtScoreArea[1][0].setForeground(new Color(DARK_GREEN));
		txtScoreArea[1][1].setText(Facade.getInstance().getFailure() + "");
		txtScoreArea[1][1].setForeground(new Color(DARK_RED));
		double rate = Facade.getInstance().getSuccessRate();
		if (rate >= 50) {
			txtScoreArea[1][2].setForeground(new Color(DARK_GREEN));
		} else {
			txtScoreArea[1][2].setForeground(new Color(DARK_RED));
		}
		txtScoreArea[1][2].setText(rate + "%");

	}

	private void fillConfusionMatrix(int[][] confusionValues) {
		for (int i = 0; i < confusionValues.length; i++) {
			for (int j = 0; j < confusionValues[i].length; j++) {
				txtMatrixOutuput[i + 1][j + 1].setText(confusionValues[i][j] + "");
			}
		}
	}

	@Override
	public void updateLine() {
		console.append(getOutput());
	}

	@Override
	public String getOutput() {
		return Facade.getInstance().getOutput();
	}

	public int getTestSize() {
		int size = 0;
		try {
			size = Facade.getInstance().getTestSize();
		} catch (IOException e) {
			showIOErrorMsg();
			System.exit(0);
			e.printStackTrace();
		}
		return size;
	}
	
	private void showKTooLargeMsg() {
		JOptionPane.showMessageDialog(this, "O k deve ser menor do que " + getTestSize()
				+ " (tamanho da base de teste)", "ERRO", JOptionPane.ERROR_MESSAGE);
	}

	private void showNegativePMsg() {
		JOptionPane.showMessageDialog(this, "Forneça um P positivo e diferente de zero", "ERRO",
				JOptionPane.ERROR_MESSAGE);
	}

	private void showNullKMsg() {
		JOptionPane.showMessageDialog(this, "Forneça um K", "ERRO", JOptionPane.ERROR_MESSAGE);
	}

	private void showNegativeKMsg() {
		JOptionPane.showMessageDialog(this, "Forneça um K positivo e diferente de zero", "ERRO",
				JOptionPane.ERROR_MESSAGE);
	}

	public void showIOErrorMsg() {
		JOptionPane.showMessageDialog(this, "Erro de leitura da base de dados, programa finalizado", "ERRO",
				JOptionPane.ERROR_MESSAGE);
	}

}