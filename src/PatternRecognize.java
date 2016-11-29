import corpus.CorpusBuilder;

/**
 * Created by anthony on 11/26/16.
 */
public class PatternRecognize {

    public static void main(String[] args){
        System.out.println("Building corpus...");
        CorpusBuilder cb = CorpusBuilder.createBuilder("img", 20);
        cb.buildCorpus();
    }
}
