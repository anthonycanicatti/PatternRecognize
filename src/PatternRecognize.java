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
            Recognizer r = new Recognizer(image, 20);
            int[][] matrix = r.getMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
