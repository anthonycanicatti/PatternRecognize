import corpus.CorpusBuilder;

/**
 * Created by anthony on 11/26/16.
 */
public class PatternRecognize {

    public static void main(String[] args){
        CorpusBuilder cb = CorpusBuilder.createBuilder("img");
        cb.buildCorpus();
    }
}
