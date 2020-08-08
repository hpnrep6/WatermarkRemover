import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new SelectionGui();
    }

    public static void saveImage(BufferedImage image, String directory, String name) {
        try{
            File u = new File(directory + File.separator + name + ".png");
            ImageIO.write(image,"png",u);
        }catch(IOException e){}
    }


}
