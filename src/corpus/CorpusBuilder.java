package corpus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by anthony on 11/26/16.
 * this class is a singleton
 */
public class CorpusBuilder {

    private static CorpusBuilder instance; // static CorpusBuilder instance (single)
    private static String corpusDirectory;
    private static int gridSize;

    /**
     * CorpusBuilder getter for singleton instance of CorpusBuilder class
     * @param corpusDirectory the directory containing all image files that make up the corpus
     * @return the singleton instance of the CorpusBuilder
     */
    public static CorpusBuilder createBuilder(String corpusDirectory, int gridSize){
        if(instance == null)
            return new CorpusBuilder(corpusDirectory, gridSize);
        return instance;
    }

    /**
     * Private constructor for singleton instance of CorpusBuilder - to be executed exactly once
     * @param corpusDirectory the directory containing all image files that make up the corpus
     */
    private CorpusBuilder(String corpusDirectory, int gridSize){
        this.corpusDirectory = corpusDirectory;
        this.gridSize = gridSize;
    }

    /**
     * Build the corpus by obtaining all information matrices from all data files in given dir
     */
    public static void buildCorpus(){
        File[] files = new File(corpusDirectory).listFiles();
        HashMap<String, int[]> masterMap = new HashMap<>(); // need to use wrapper class in map construct
        for(File f : files) {
            System.out.println("Obtaining: " + f.getAbsolutePath());
            try {
                BufferedImage image = ImageIO.read(f);
                ImageTransformer imageTransformer = new ImageTransformer(image, gridSize);
                int[] vector = imageTransformer.getInformationVector(); // convert primitive integer to wrapper integer
                String number = f.getName().substring(0, 1);
                masterMap.put(number, vector);
            } catch (IOException e) {
                System.err.println("Error obtaining image from file.");
                continue;
            }
        }

        HashMap<Integer, int[]> meanMap = getMeanMap(masterMap);
        writeDataToCsv(masterMap, "data.csv");
        //printMap(masterMap);
    }

    /**
     * Given the master map of all digits with their information vectors
     * compile all the same digits and their information vectors together
     * and compute each's mean vector, then store the mean vectors in a map
     * doesnt have to be a map - can be a list of size 10
     *
     * @param master the master map containing all digits and their info vectors
     * @return a map where the key is an integer and the value is its mean info vector
     */
    private static HashMap<Integer, int[]> getMeanMap(HashMap<String, int[]> master){
        HashMap<Integer, int[]> meanMap = new HashMap<>();
        ArrayList<ArrayList<int[]>> allLists = new ArrayList<>();
        for(int i=0; i<10; i++)
            allLists.add(new ArrayList<>()); // fill with empty arrays at first
        Iterator iterator = master.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry pair = (Map.Entry)iterator.next();
            String key = (String)pair.getKey();
            int[] infoVector = (int[])pair.getValue();
            int keyAsInt = Integer.parseInt(key);
            allLists.get(keyAsInt).add(infoVector); // each ArrayList contains all info vectors
        }

        // for each list of info vectors, compute the mean vector for each digit
        for(int i=0; i<10; i++){
            ArrayList<int[]> iInfoVectors = allLists.get(i);
            int[][] iInfoVectorsAsArr = new int[iInfoVectors.size()][gridSize];
            for(int j=0; j<iInfoVectors.size(); j++)
                iInfoVectorsAsArr[j] = iInfoVectors.get(j);
            int[] meanVector = getMeanVector(iInfoVectorsAsArr);
            meanMap.put(i, meanVector);
        }
        return meanMap;
    }

    /**
     * Obtain the mean vector for a particular digit
     * Amalgamates all training data - i.e all information vectors for a particular digit
     * into a single mean vector
     *
     * @param vectors a list of vectors which all correspond to the same digit
     * @return the mean vector for a particular digit - what new data will be tested against
     */
    private static int[] getMeanVector(int[][] vectors){
        int[] meanVector = new int[vectors[0].length];
        for(int i=0; i<vectors[0].length; i++){ // iterating column-wise to find meanVector element by element
            int numZeros = 0, numOnes = 0;
            for(int j=0; j<vectors.length; j++){ // iterating over single column for all vectors
                if(vectors[j][i] == 0)
                    numZeros++;
                else
                    numOnes++;
            }
            meanVector[i] = (numZeros > numOnes) ? 0 : 1;
        }
        return meanVector;
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

    /**
     * Write mean vector data out to CSV
     * this function is helpful if want to use data for any traditional data mining methods
     *
     * @param map the full map of all information vectors for all given data - not means
     * @param outFile the csv file to write data to
     */
    private static void writeDataToCsv(HashMap<String, int[]> map, String outFile){
        try (FileWriter writer = new FileWriter(outFile)) {
            for(int i=0; i<gridSize; i++){
                for(int j=0; j<gridSize; j++)
                    writer.write("a("+(i+1)+"x"+(j+1)+"),");
            }
            writer.write("class\n");
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry pair = (Map.Entry)iterator.next();
                String classLabel = (String)pair.getKey();
                int[] data = (int[])pair.getValue();
                String line = Arrays.toString(data).replace(" ","").replace("[","")
                        .replace("]","") + "," + classLabel + "\n";
                writer.write(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
