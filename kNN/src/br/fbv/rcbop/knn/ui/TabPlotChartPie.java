package br.fbv.rcbop.knn.ui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class TabPlotChartPie extends JPanel {

	private static final long	serialVersionUID	= -2123162266924249960L;
	private int[][]	confusionMatrix;

	public TabPlotChartPie(LayoutManager layout, int[][] confusionMatrix) {
		super(layout);
		this.confusionMatrix = confusionMatrix;

		PieDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset, "Iris previstas com sucesso");
		ChartPanel chartPanel = new ChartPanel(chart);
		this.add(chartPanel);
	}

	private PieDataset createDataset() {
		DefaultPieDataset result = new DefaultPieDataset();
		result.setValue("Setosa", confusionMatrix[0][0]);
		result.setValue("Versicolor", confusionMatrix[1][1]);
		result.setValue("Virgínica", confusionMatrix[2][2]);
		return result;

	}

	private JFreeChart createChart(PieDataset dataset, String title) {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2} ({1})"));
		return chart;

	}

}
