package kmeansga;

import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class FitnessGraph extends ApplicationFrame {
    
    ArrayList<Integer> bestFitness = new ArrayList();

    public FitnessGraph(String title, String chartTitle,ArrayList<Integer> bestFitness) {
        super(title);
        this.bestFitness = bestFitness;
        JFreeChart lineChart = ChartFactory.createLineChart(
        chartTitle,
        "Generation","Fitness",
        createDataset(),
        PlotOrientation.VERTICAL,
        true,true,false
    );

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }

    private DefaultCategoryDataset createDataset( ) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        for(int i = 0; i < bestFitness.size(); i++){
            dataset.addValue(bestFitness.get(i), "Fitness", "" + i + "");
        }
        return dataset;
    }
}