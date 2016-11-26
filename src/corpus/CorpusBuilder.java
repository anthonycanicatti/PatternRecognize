package corpus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by anthony on 11/26/16.
 * this class is a singleton
 */
public class CorpusBuilder {

    private static CorpusBuilder instance; // static CorpusBuilder instance (single)
    private static String corpusDirectory;

    /**
     * CorpusBuilder getter for singleton instance of CorpusBuilder class
     * @param corpusDirectory the directory containing all image files that make up the corpus
     * @return the singleton instance of the CorpusBuilder
     */
    public static CorpusBuilder createBuilder(String corpusDirectory){
        if(instance == null)
            return new CorpusBuilder(corpusDirectory);
        return instance;
    }

    /**
     * Private constructor for singleton instance of CorpusBuilder - to be executed exactly once
     * @param corpusDirectory the directory containing all image files that make up the corpus
     */
    private CorpusBuilder(String corpusDirectory){
        this.corpusDirectory = corpusDirectory;
    }

    /**
     * Build the corpus by obtaining all information matrices from all data files in given dir
     */
    public static void buildCorpus(){
        File[] files = new File(corpusDirectory).listFiles();
        HashMap<String, Integer[]> map = new HashMap<>(); // need to use wrapper class in map construct
        for(File f : files) {
            System.out.println("Obtaining: " + f.getAbsolutePath());
            try {
                BufferedImage image = ImageIO.read(f);
                ImageTransformer imageTransformer = new ImageTransformer(image, 20);
                int[] vector = imageTransformer.getInformationVector(); // convert primitive integer to wrapper integer
                Integer[] wrapperVector = new Integer[vector.length];
                for (int i = 0; i < vector.length; i++)
                    wrapperVector[i] = new Integer(vector[i]);
                String number = f.getName().substring(0, 1);
                map.put(number, wrapperVector);
            } catch (IOException e) {
                System.err.println("Error obtaining image from file.");
                continue;
            }
        }
        printMap(map);
    }

    /**
     * Print the map of number character, information vector pairs
     * @param map the map to print
     */
    public static void printMap(HashMap map){
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey()+": ");
            Integer[] wrapper = (Integer[]) pair.getValue();
            int[] vector = new int[wrapper.length];
            for(int i=0; i<wrapper.length; i++){
                vector[i] = (int)wrapper[i];
            }
            System.out.println(Arrays.toString(vector));
        }
    }
}
