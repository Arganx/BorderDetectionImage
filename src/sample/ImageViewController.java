package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by qwerty on 23-Nov-18.
 */
public class ImageViewController {

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        Double[][] lum = Controller.getLum();
        BufferedImage original = Controller.getImg();
        BufferedImage img = new BufferedImage(original.getWidth(),original.getHeight(),original.getType());
        for(int i=0;i<img.getHeight();i++)
        {
            for (int j = 0; j < img.getWidth(); j++)
            {
                if(lum[j][i]==1.0)
                {
                    Color c = Color.WHITE;
                    img.setRGB(j,i,c.getRGB());
                }
                else
                {
                    Color c = Color.BLACK;
                    img.setRGB(j,i,c.getRGB());
                }
            }
        }

        Image image= SwingFXUtils.toFXImage(img, null);
        imageView.setImage(image);
    }


}
