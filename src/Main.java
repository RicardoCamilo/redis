import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        Redis redis = new Redis();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        redis.initTransaction(br, new HashMap<>());
    }

}
