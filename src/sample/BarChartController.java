package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerty on 24-Nov-18.
 */
public class BarChartController {

    @FXML
    private BarChart<String,Integer> barChart;

    private void createChart(List<Integer> list, double numberOfInterval)
    {
        list.sort((o1, o2) -> {
            if(o1>o2)
            {
                return 1;
            }
            else if(o2>o1)
            {
                return -1;
            }
            return 0;
        });

        int max = list.get(list.size()-1);
        Integer min = list.get(0);
        int spacing = (int)Math.ceil((double)(max-min)/numberOfInterval);

        List<String> names = new ArrayList<String>();
        Integer tmp = min;
        List<Integer> inInterval = new ArrayList<Integer>();
        int sum=0;
        for(int i=0;i<numberOfInterval-1;i++)
        {
            for(int j=0;j<list.size();j++)
            {
                if(list.get(j)>=tmp && list.get(j)<=tmp+spacing)
                {
                    sum++;
                }
            }
            inInterval.add(sum);
            sum=0;
            names.add(tmp.toString() + " - " + (tmp+spacing));
            tmp+=spacing;
        }
        names.add(tmp.toString() + " - " + max);
        for(int j=0;j<list.size();j++)
        {
            if(list.get(j)>=tmp && list.get(j)<=max)
            {
                sum++;
            }
        }
        inInterval.add(sum);
        sum=0;

        barChart.setTitle("Border length distribution");

        barChart.getXAxis().setLabel("Interval");
        barChart.getYAxis().setLabel("Number of objects");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Borders of objects");


        for(int i=0;i<numberOfInterval;i++)
        {
            series1.getData().add(new XYChart.Data<>(names.get(i),inInterval.get(i)));
        }
        barChart.getData().addAll(series1);
    }

    @FXML
    public void initialize()
    {
            List<Integer> list = Controller.getList();
            Double numberOfInterval = (double)Controller.getNumberOfIntervals();
            createChart(list,numberOfInterval);
            list.clear();

    }
}
