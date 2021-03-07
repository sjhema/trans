package com.primovision.lutransport.report;

//import java.awt.Color;
import java.text.DecimalFormat;

//import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.labels.PieSectionLabelGenerator;
//import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
//import org.jfree.chart.plot.Plot;

//import org.jfree.data.general.PieDataset;

//import ar.com.fdvs.dj.domain.chart.plot.Pie3DPlot;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class PieChartCustomizer implements JRChartCustomizer {
	 public void customize(JFreeChart chart, JRChart jrChart) {
		 PiePlot piePlot = (PiePlot)chart.getPlot(); // Pie3DPlot
		 piePlot.setSimpleLabels(Boolean.TRUE);
		 
		 /*PieDataset pieDataset =  piePlot.getDataset();
		 Comparable key1 =  pieDataset.getKey(0);
		 Comparable key2 =  pieDataset.getKey(1);
		 piePlot.setExplodePercent(key1, 0.10);
		 piePlot.setSectionPaint(key1, Color.green);
		 piePlot.setSectionPaint(key2, Color.red);*/

       PieSectionLabelGenerator sectionLabelGenerator = new StandardPieSectionLabelGenerator(
           "{1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%")); // {0}:
       piePlot.setLabelGenerator(sectionLabelGenerator);
       
       /*((StandardPieSectionLabelGenerator)piePlot3D.getLabelGenerator()).getPercentFormat().setMaximumFractionDigits(2);
       StandardPieSectionLabelGenerator labelGen = new StandardPieSectionLabelGenerator(
                        "{1} ({2})",
                        new DecimalFormat("#,##0.00"),
                        new DecimalFormat("0.00%"));*/
       
       /*
       JRFillPieDataset dataset = (JRFillPieDataset) jrChart.getDataset();<br />
       if (dataset.getLabelExpression() != null) {<br />
           String label = dataset.getLabelExpression().getText();<br />
           label = label.substring(1, label.length() - 1);<br />
           PiePlot piePlot = (PiePlot) jFreeChart.getPlot();<br />
           StandardPieSectionLabelGenerator labelGenerator = <br />
             new StandardPieSectionLabelGenerator(label);<br />
           piePlot.setLabelGenerator(labelGenerator);<br />
           piePlot.setLegendLabelGenerator(labelGenerator);<br />
         }
         */
       
       //piePlot.setToolTipGenerator(pieToolTipGenerator);
       
       //JFreeChart someChart = ChartFactory.createPieChart(
         //    "Header", dataset, true, true, false);
       //piePlot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
       
       //JRDesignChart jrPieChart = new JRDesignChart(jasperDesign,net.sf.jasperreports.engine.JRChart.CHART_TYPE_PIE);
	 }
	 
	 /*PieToolTipGenerator pieToolTipGenerator = new PieToolTipGenerator() {
	     public String generateToolTip(PieDataset dataset, Comparable key){
	         StringBuilder stringBuilder = new StringBuilder();
	         Number num = dataset.getValue(key);
	         stringBuilder.append(String.format("<html><p style='color:#0000ff;'>'%s'</p>", key));
	         stringBuilder.append(String.format(" '%d'", num));
	         stringBuilder.append("</html>");
	         return stringBuilder.toString();
	     }
	 };*/
}
