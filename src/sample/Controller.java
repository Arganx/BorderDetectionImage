package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class Controller {

    @FXML
    private Button fileSelect;
    @FXML
    private Label path;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField textField;
    @FXML
    private TextField blureText;
    @FXML
    private TextField intervals;
    @FXML
    private CheckBox checkBox;
    @FXML
    private CheckBox invertion;
    @FXML
    private ColorPicker colorPicker;

    private final FileChooser fileChooser=new FileChooser();
    private static BufferedImage img;
    private static Double[][] lum;
    private static List<Integer> list = new ArrayList<>();
    private static int numberOfIntervals;

    public static int getNumberOfIntervals() {
        return numberOfIntervals;
    }

    public static List<Integer> getList() {
        return list;
    }

    public static BufferedImage getImg() {
        return img;
    }

    public static Double[][] getLum() {
        return lum;
    }

    private void show(Double[][] array)
    {
        for(int i=0;i<array.length ;i++)
        {
            for (int j = 0; j < array[i].length; j++)
            {
                System.out.printf("%.2f ",array[i][j]);
            }
            System.out.println();
        }
    }

    private void lumToBin(Double[][] img,int level)
    {
        for(int i=0;i<img.length ;i++)
        {
            for(int j=0;j<img[i].length;j++)
            {
                if(img[i][j]>level)
                {
                    img[i][j]=1.0;
                }
                else
                {
                    img[i][j] = 0.0;
                }
            }
        }
    }

    private void dilate(Double[][] img)
    {
        Double[][] copy= new Double[img.length][img[0].length];

        for(int i=0;i<img.length;i++)
        {
            for(int j=0;j<img[0].length;j++)
            {
                copy[i][j]=img[i][j];
            }
        }

        for(int i=1;i<img.length-1 ;i++)
        {
            for (int j = 1; j < img[i].length-1; j++)
            {
                if(copy[i][j]==1.0)
                {
                    img[i][j+1]=1.0;
                    img[i][j-1]=1.0;
                    img[i+1][j]=1.0;
                    img[i-1][j]=1.0;

                }
            }
        }

        for(int i=1;i<img.length-1;i++)
        {
            if(copy[i][0]==1.0)
            {
                img[i][1]=1.0;
                img[i+1][0]=1.0;
                img[i-1][0]=1.0;
            }
        }

        for(int i=1;i<img.length-1;i++)
        {
            if(copy[i][img[0].length-1]==1.0)
            {
                img[i][img[0].length-2]=1.0;
                img[i+1][img[0].length-1]=1.0;
                img[i-1][img[0].length-1]=1.0;
            }
        }

        for(int j=1;j<img[0].length-1;j++)
        {
            if(copy[0][j]==1.0)
            {
                img[0][j+1]=1.0;
                img[0][j-1]=1.0;
                img[1][j]=1.0;
            }
        }

        for(int j=1;j<img[0].length-1;j++)
        {
            if(copy[img.length-1][j]==1.0)
            {
                img[img.length-1][j+1]=1.0;
                img[img.length-1][j-1]=1.0;
                img[img.length-2][j]=1.0;
            }
        }

        if(copy[0][0]==1.0)
        {
            img[1][0]=1.0;
            img[0][1]=1.0;
        }
        if(copy[img.length-1][0]==1.0)
        {
            img[img.length-2][0]=1.0;
            img[img.length-1][1]=1.0;
        }
        if(copy[0][img[0].length-1]==1.0)
        {
            img[0][img[0].length-2]=1.0;
            img[1][img[0].length-1]=1.0;
        }
        if(copy[img.length-1][img[0].length-1]==1.0)
        {
            img[img.length-1][img[0].length-2]=1.0;
            img[img.length-2][img[0].length-1]=1.0;
        }

    }

    private void erode(Double[][] img)
    {
        Double[][] copy= new Double[img.length][img[0].length];

        for(int i=0;i<img.length;i++)
        {
            for(int j=0;j<img[0].length;j++)
            {
                copy[i][j]=img[i][j];
            }
        }

        for(int i=1;i<img.length-1 ;i++)
        {
            for (int j = 1; j < img[i].length-1; j++)
            {
                if(copy[i][j]==0.0)
                {
                    img[i][j+1]=0.0;
                    img[i][j-1]=0.0;
                    img[i+1][j]=0.0;
                    img[i-1][j]=0.0;

                }
            }
        }

        for(int i=1;i<img.length-1;i++)
        {
            if(copy[i][0]==0.0)
            {
                img[i][1]=0.0;
                img[i+1][0]=0.0;
                img[i-1][0]=0.0;
            }
        }

        for(int i=1;i<img.length-1;i++)
        {
            if(copy[i][img[0].length-1]==0.0)
            {
                img[i][img[0].length-2]=0.0;
                img[i+1][img[0].length-1]=0.0;
                img[i-1][img[0].length-1]=0.0;
            }
        }

        for(int j=1;j<img[0].length-1;j++)
        {
            if(copy[0][j]==0.0)
            {
                img[0][j+1]=0.0;
                img[0][j-1]=0.0;
                img[1][j]=0.0;
            }
        }

        for(int j=1;j<img[0].length-1;j++)
        {
            if(copy[img.length-1][j]==0.0)
            {
                img[img.length-1][j+1]=0.0;
                img[img.length-1][j-1]=0.0;
                img[img.length-2][j]=0.0;
            }
        }

        if(copy[0][0]==0.0)
        {
            img[1][0]=0.0;
            img[0][1]=0.0;
        }
        if(copy[img.length-1][0]==0.0)
        {
            img[img.length-2][0]=0.0;
            img[img.length-1][1]=0.0;
        }
        if(copy[0][img[0].length-1]==0.0)
        {
            img[0][img[0].length-2]=0.0;
            img[1][img[0].length-1]=0.0;
        }
        if(copy[img.length-1][img[0].length-1]==0.0)
        {
            img[img.length-1][img[0].length-2]=0.0;
            img[img.length-2][img[0].length-1]=0.0;
        }
    }

    private Double[][] copyTable(Double[][] tab)
    {
        Double[][] copy = new Double[tab.length][tab[0].length];

        for(int i=0;i<tab.length;i++)
        {
            for(int j=0;j<tab[0].length;j++)
            {
                copy[i][j]=tab[i][j];
            }
        }
        return  copy;
    }

    private Double[][] multiplyMatrix(Double[][] m1,Double[][] m2)
    {
        if(m1.length==m2.length && m1[0].length == m2[0].length)
        {
            Double[][] result = new Double[m1.length][m1[0].length];
            for(int i=0;i<m1.length;i++)
            {
                for(int j=0;j<m1[0].length;j++)
                {
                    result[i][j]=m1[i][j]*m2[i][j];
                }
            }
            return  result;

        }
        System.out.println("Error in Matrix subtraction, matrixes must be the same size");
        return null;
    }

    private Double sum(Double[][] img)
    {
        Double sum =0.0;
        for(int i=0;i<img.length;i++)
        {
            for(int j=0;j<img[0].length;j++)
            {
                sum+=img[i][j];
            }
        }
        return sum;
    }

    private Double[][] getObject(Double[][] img,int x,int y)
    {
        Double[][] result = new Double[img.length][img[0].length];
        for(int i=0;i<result.length;i++)
        {
            for(int j=0;j<result[0].length;j++)
            {
                result[i][j]=0.0;
            }
        }
        result[x][y] = 1.0;
        Double sum1=sum(img);
        Double sum2;
        while(true)
        {
            dilate(result);
            result=multiplyMatrix(result,img);
            sum2=sum(result);
            if(sum2.equals(sum1))
            {
                return  result;
            }
            sum1=sum2;
        }

    }

    private Double[][] subtractMatrix(Double[][] m1,Double[][] m2)
    {
        if(m1.length==m2.length && m1[0].length == m2[0].length)
        {
            Double[][] result = new Double[m1.length][m1[0].length];
            for(int i=0;i<m1.length;i++)
            {
                for(int j=0;j<m1[0].length;j++)
                {
                    result[i][j]=m1[i][j]-m2[i][j];
                }
            }
            return  result;

        }
        System.out.println("Error in Matrix subtraction, matrixes must be the same size");
        return null;
    }

    private void invertBin(Double[][] bin)
    {
        for(int i=0;i<bin.length;i++)
        {
            for(int j=0;j<bin[0].length;j++)
            {
                if(bin[i][j]==1.0)
                {
                    bin[i][j]=0.0;
                }
                else
                {
                    bin[i][j]=1.0;
                }
            }
        }
    }

    @FXML
    public void initialize() {

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        blureText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        intervals.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    public void choosingFile(ActionEvent event)
    {
        int level = 200;
        int blure=0;
        boolean invert=false;
        numberOfIntervals = 2;
        boolean countBorderLength=false;
        if(checkBox.isSelected())
        {
            countBorderLength=true;
        }
        if(invertion.isSelected())
        {
            invert=true;
        }
        if(!textField.getText().equals(""))
        {
            level = Integer.parseInt(textField.getText());
        }
        if(!blureText.getText().equals(""))
        {
            blure= Integer.parseInt(blureText.getText());
        }
        if(!intervals.getText().equals(""))
        {
            numberOfIntervals= Integer.parseInt(intervals.getText());
        }


        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile!=null)
        {

            try {
                String type = Files.probeContentType(selectedFile.toPath());
                System.out.println("Loaded file type is: " + type);
                if(!type.equals("image/png") && !type.equals("image/jpeg"))
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING,"File format must be png or jpeg");
                    alert.setTitle("Wrong file format");
                    alert.show();
                    selectedFile=null;
                    path.setText("Selected path:");
                    return;
                }
                else
                {
                    path.setText("Selected path: " + selectedFile.getAbsolutePath());
                    Image image = new Image(selectedFile.toURI().toString());

                    imageView.setImage(image);
                    img = null;

                    try {
                        img = ImageIO.read(selectedFile);
                        Integer[][] r = new Integer[img.getWidth()][img.getHeight()];
                        Integer[][] g = new Integer[img.getWidth()][img.getHeight()];
                        Integer[][] b = new Integer[img.getWidth()][img.getHeight()];
                        lum = new Double[img.getWidth()][img.getHeight()];
                        for(int i=0;i<img.getHeight();i++)
                        {
                            for(int j=0;j<img.getWidth();j++)
                            {
                                Color c = new Color(img.getRGB(j,i));
                                r[j][i] = c.getRed();
                                g[j][i] = c.getGreen();
                                b[j][i] = c.getBlue();
                                lum[j][i] = 0.2126* (double)r[j][i] + 0.7152*(double)g[j][i] + 0.0722*(double)b[j][i];
                                //System.out.printf("%.2f ",lum[i][j]);
                                //c = new Color(r[i][j],0,0);
                                //img.setRGB(i,j,c.getRGB());

                            }
                            //System.out.println();
                        }
                        lumToBin(lum,level);
                        if(invert)
                        {
                            invertBin(lum);
                        }
                        for(int k=0;k<blure;k++)
                        {
                            erode(lum);
                        }
                        for(int k=0;k<blure*2;k++)
                        {
                            dilate(lum);
                        }
                        for(int k=0;k<blure;k++)
                        {
                            erode(lum);
                        }
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("image_view.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("Picture Viewer");
                            stage.setScene(new Scene(root, 1440, 800));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int borderLength=0;
                        Double[][] copy;
                        if(countBorderLength) {
                            Double[][] obj;
                            copy = copyTable(lum);
                            Double[][] copyObj;
                            int counter = 0;
                            for (int i = 0; i < copy.length; i++) {
                                for (int j = 0; j < copy[0].length; j++) {
                                    if (copy[i][j] == 1.0) {
                                        System.out.println("Copy " + i + " " + j + " = " + copy[i][j]);
                                        obj = getObject(copy, i, j);

                                        copyObj = copyTable(obj);
                                        copy = subtractMatrix(copy, obj);
                                        dilate(copyObj);
                                        copyObj = subtractMatrix(copyObj, obj);

                                        System.out.println("For object " + (counter + 1) + " border length is: " + sum(copyObj));
                                        borderLength+=sum(copyObj);
                                        list.add(sum(copyObj).intValue());
                                        counter++;
                                    }
                                }
                            }
                            System.out.println("Number of objects in the picture is: " + counter);
                            System.out.println("Sum of border lengths in the picture is "+borderLength);
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("barChart.fxml"));
                                Stage stage = new Stage();
                                stage.setTitle("Chart");
                                stage.setScene(new Scene(root, 1440, 800));
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        copy = copyTable(lum);
                        dilate(lum);
                        lum = subtractMatrix(lum,copy);
                        for(int i=0;i<img.getHeight();i++)
                        {
                            for (int j = 0; j < img.getWidth(); j++)
                            {
                                if(lum[j][i]==1.0)
                                {
                                    Color c = new Color((float)colorPicker.getValue().getRed(),(float)colorPicker.getValue().getGreen(),(float)colorPicker.getValue().getBlue(),(float)colorPicker.getValue().getOpacity());
                                    img.setRGB(j,i,c.getRGB());
                                }
                                /*else
                                {
                                    Color c = Color.BLACK;
                                    img.setRGB(j,i,c.getRGB());
                                }*/
                            }
                        }
                        image=SwingFXUtils.toFXImage(img, null);
                        imageView.setImage(image);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not read file type");
            }
        }
        else
        {
            System.out.println("File is not selected");
        }
    }

}
