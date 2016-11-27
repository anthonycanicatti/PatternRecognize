package corpus;

import java.awt.image.BufferedImage;

/**
 * Created by anthony on 11/26/16.
 */
public class ImageTransformer {

    private int imageHeight = -1, imageWidth = -1, gridSize = -1;
    private BufferedImage image;

    /**
     * corpus.ImageTransformer constructor
     * @param image image to recognize
     * @param gridSize image will be partitioned into a grid - gridSize is the number of row/columns
     */
    public ImageTransformer(BufferedImage image, int gridSize){
        this.image = image;
        cropImage();
        this.imageHeight = this.image.getHeight();
        this.imageWidth = this.image.getWidth();
        this.gridSize = gridSize;
    }

    /**
     * Crop surrounding whitespace from input image
     */
    private void cropImage(){
        boolean foundMark = false;
        int startX = 0, endX = 0, startXMarkings = 0, endXMarkings = 0;
        for(int i=0; i<image.getWidth(); i++){ // find starting x position from left of image
            if(containsMarking(i,i+1, 0, image.getHeight()))
                startXMarkings++;
            if(startXMarkings >= 5){
                startX = i;
                break;
            }
        }
        for(int i=image.getWidth(); i>0; i--){ // find ending x position from right of image
            if(containsMarking(i-1,i,0,image.getHeight()))
                endXMarkings++;
            if(endXMarkings >= 5){
                endX = i;
                break;
            }
        }
        int startY = 0, endY = 0, startYMarkings = 0, endYMarkings = 0;
        for(int j=0; j<image.getHeight(); j++){ // find starting y position from top of image
            if(containsMarking(0, image.getWidth(), j, j+1))
                startYMarkings++;
            if(startYMarkings >= 5){
                startY = j;
                break;
            }
        }
        for(int j=image.getHeight(); j>0; j--){ // find ending y position from bottom of image
            if(containsMarking(0, image.getWidth(), j-1,j))
                endYMarkings++;
            if(endYMarkings >= 5){
                endY = j;
                break;
            }
        }
        BufferedImage cropped = image.getSubimage(startX, startY, endX-startX, endY-startY);
        this.image = cropped;
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
     * @return a binary vector converted from a matrix with gridSize rows and columns
     */
    public int[] getInformationVector(){
        int[][] matrix = new int[gridSize][gridSize];
        int xBlockSize = imageWidth / gridSize;
        int yBlockSize = imageHeight / gridSize;
        for(int i=0; i<gridSize; i++) { // iterating rows of the image
            int startX = i * xBlockSize;
            int endX;
            // the dimensions may not line up exactly, so just make the last endX the remainder of the block
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
        return unravelMatrix(matrix);
    }

    /**
     * Unravel a two dimensional information matrix into a vector - row wise
     * @param matrix the information matrix to unravel
     * @return a vector of size (rows*columns) of info matrix unraveled
     */
    private int[] unravelMatrix(int[][] matrix){
        int[] infoVector = new int[matrix.length*matrix.length];
        int ivIndex = 0;
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix.length; j++){
                infoVector[ivIndex] = matrix[i][j];
                ivIndex++;
            }
        }
        return infoVector;
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
