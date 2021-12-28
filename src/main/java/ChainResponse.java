import java.util.ArrayList;
import java.util.List;

public class ChainResponse {

    List<Block> chain = new ArrayList<>();
    int length;

    public ChainResponse(List<Block> chain, int length) {
        this.chain = chain;
        this.length = length;
    }
}
