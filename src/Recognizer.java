import java.awt.image.BufferedImage;

/**
 * Created by anthony on 11/26/16.
 */
public class Recognizer {

    private int imageHeight = -1, imageWidth = -1;
    private BufferedImage image;

    /**
     * Recognizer constructor
     * @param image image to recognize
     */
    public Recognizer(BufferedImage image){
        this.image = image;
        this.imageHeight = image.getHeight();
        this.imageWidth = image.getWidth();
    }

    /**
     * Given a partition of the BufferedImage, iterate over each pixel and look for a black marking
     * as soon as a black marking is found can safely return true
     *
     * @param startX the starting x value of the partition
     * @param endX the ending x value of the partition
     * @param startY the starting y value of the partition
     * @param endY the ending y value of the partition
     * @return whether the partition contains a marking
     */
    private boolean containsMarking(int startX, int endX, int startY, int endY){
        boolean foundMark = false;
        for(int i=startX; i<endX; i++){
            for(int j=startY; j<endY; j++){
                int color = image.getRGB(i,j);
                int red = (color & 0x00ff0000) >> 16;
                int green = (color & 0x0000ff00) >> 8;
                int blue = (color & 0x000000ff);
                if(red < 100 && green < 100 && blue < 100){
                    foundMark = true;
                    break;
                }
            }
        }
        return foundMark;
    }

}
