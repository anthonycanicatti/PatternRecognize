import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by anthony on 11/26/16.
 */
public class PatternRecognize {

    public static void main(String[] args){
        String fileName = "img/8.jpg";
        File f = new File(fileName);
        try {
            BufferedImage image = ImageIO.read(f);
            System.out.println("Height: "+image.getHeight());
            System.out.println("Width: "+image.getWidth());

            int color = image.getRGB(1000,500);
            int red = (color & 0x00ff0000) >> 16;
            int green = (color & 0x0000ff00) >> 8;
            int blue = (color & 0x000000ff);
            System.out.println("Red value at (1000,500): "+red);
            System.out.println("Green value at (1000,500): "+green);
            System.out.println("Blue value at (1000,500): "+blue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
