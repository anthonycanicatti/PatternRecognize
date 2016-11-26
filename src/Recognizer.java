import java.awt.image.BufferedImage;

/**
 * Created by anthony on 11/26/16.
 */
public class Recognizer {

    private int imageHeight = -1, imageWidth = -1, gridSize = -1;
    private BufferedImage image;

    /**
     * Recognizer constructor
     * @param image image to recognize
     * @param gridSize image will be partitioned into a grid - gridSize is the number of row/columns
     */
    public Recognizer(BufferedImage image, int gridSize){
        this.image = image;
        this.imageHeight = image.getHeight();
        this.imageWidth = image.getWidth();
        this.gridSize = gridSize;
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

    /**
     * Partition the image file into a matrix with gridSize rows and columns
     * for each block in the matrix, iterate over each pixel searching for black markings
     * if a marking is found, enter a 1 in the image's corresponding binary matrix
     * @return a binary matrix with gridSize rows and columns, where 1 represents a marking
     */
    public int[][] getMatrix(){
        int[][] matrix = new int[gridSize][gridSize];
        int xBlockSize = imageWidth / gridSize;
        int yBlockSize = imageHeight / gridSize;
        for(int i=0; i<gridSize; i++) { // iterating rows of the image
            int startX = i * xBlockSize;
            int endX;
            if (i != gridSize - 1)
                endX = (i + 1) * xBlockSize;
            else
                endX = imageWidth;
            for (int j = 0; j < gridSize; j++) { // iterating columns of the image
                int startY = j * yBlockSize;
                int endY;
                if (j != gridSize - 1)
                    endY = (j + 1) * yBlockSize;
                else
                    endY = imageHeight;
                matrix[j][i] = containsMarking(startX, endX, startY, endY) ? 1 : 0;
            }
        }
        printMatrix(matrix);
        return matrix;
    }

    /**
     * Prints an image matrix
     * @param matrix the matrix to print
     */
    public static void printMatrix(int[][] matrix){
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++)
                System.out.print(matrix[i][j]+"\t\t");
            System.out.println();
        }
    }
}
