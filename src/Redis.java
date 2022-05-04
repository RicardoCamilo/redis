import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Redis {

    private static final String NO_TRANSACTION = "NO TRANSACTION";
    private Map<String, Integer> memory;
    private Integer openTransactions;
    private Map<String, Integer> valueFrequency;

    public Redis() {
        memory = new HashMap<>();
        openTransactions = 0;
    }

    private enum Command {
        SET, GET, UNSET, NUMEQUALTO, BEGIN, ROLLBACK, COMMIT, END
    }

    public void initTransaction(BufferedReader br, Map<String, Integer> prevState) throws IOException {
        String line;
        String[] tokens;

        Command command;
        String name;
        Integer value;

        loop:while ((line = br.readLine()) != null) {
            tokens = line.split("\\s");
            command = Command.valueOf(tokens[0]);
            switch (command) {
                case BEGIN:
                    Map<String, Integer> currentState = new HashMap<>(memory);
                    openTransactions++;
                    initTransaction(br, currentState);
                    break;
                case SET:
                    name = tokens[1];
                    value = Integer.parseInt(tokens[2]);
                    memory.put(name, value);
                    break;
                case GET:
                    name = tokens[1];
                    System.out.println(memory.getOrDefault(name, null));
                    break;
                case UNSET:
                    name = tokens[1];
                    memory.remove(name);
                    break;
                case NUMEQUALTO:
                    value = Integer.parseInt(tokens[1]);
                    System.out.println(countingVariables(value));
                    break;
                case ROLLBACK:
                    if (openTransactions == 0) {
                        System.out.println(NO_TRANSACTION);
                    } else {
                        memory = prevState;
                    }
                    openTransactions--;
                    break loop;
                case COMMIT:
                    if (openTransactions == 0) {
                        System.out.println(NO_TRANSACTION);
                    } else {
                        // close all the transactions;
                        openTransactions = 0;
                    }
                    break loop;
                case END:
                    break loop;
            }
        }
    }

    private Integer countingVariables(Integer value) {
        Integer count = 0;
        for (Map.Entry<String, Integer> e: memory.entrySet()) {
            if (e.getValue().equals(value)) {
                count++;
            }
        }
        return count;
    }
}
