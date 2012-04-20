package husacct.validate.domain.validation.report;

import husacct.validate.domain.validation.iternal_tranfer_objects.ViolationsPerSeverity;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsImage {
	public void createStatisticsImage(String path, List<ViolationsPerSeverity> list) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JFreeChart chart = ChartFactory.createBarChart("Violations Chart", "severity", "violations", dataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		int index = 0;
		for(ViolationsPerSeverity violationPerSeverity : list) {
			dataset.addValue(violationPerSeverity.getAmount(), violationPerSeverity.getSeverity().getDefaultName(), violationPerSeverity.getSeverity().getDefaultName());
			GradientPaint paint = new GradientPaint(
					0.0f, 0.0f, Color.decode("#" + violationPerSeverity.getSeverity().getColor()),
					0.0f, 0.0f, new Color(0, 0, 20)
					);
			renderer.setSeriesPaint(index, paint);
			index++;
		}		
		try {
			ChartUtilities.saveChartAsPNG(new File(path), chart, 600, 200);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}