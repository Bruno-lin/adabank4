import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Transfer {
    private static final Map<String, User> trans = new HashMap<>();

    public static void main(String[] args) throws Exception {
        File file = new File("transactions2.txt");
        int threadNums = Integer.parseInt("4");

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        CountDownLatch latch = new CountDownLatch(threadNums);

        for (int i = 0; i < threadNums; i++) {
            new Thread(() -> {
                while (true) {
                    String transaction = "";
                    try {
                        transaction = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (transaction == null) {
                        latch.countDown();
                        return;
                    }

                    String[] info = transaction.trim().split("\\s");
                    String remitterName = info[0];
                    String remitteeName = info[1];
                    int amount = Integer.parseInt(info[2]);

                    transfer(remitterName, remitteeName, amount);
                }
            }).start();
        }
        latch.await();

        for (User user : trans.values())
            System.out.println(user.toString());
    }

    /**
     * 转账
     */
    private static void transfer(String remitterName, String remitteeName, int amount) {
        if (trans.get(remitterName) == null) {
            trans.put(remitterName, new User(remitterName, 1000, 0));
        }
        if (trans.get(remitteeName) == null) {
            trans.put(remitteeName, new User(remitteeName, 1000, 0));
        }
        User remitter = trans.get(remitterName);
        User remittee = trans.get(remitteeName);

        remitter.setBalance(remitter.getBalance() - amount);
        remittee.setBalance(remittee.getBalance() + amount);

        remitter.setTransactions(remitter.getTransactions() + 1);
        remittee.setTransactions(remittee.getTransactions() + 1);
    }

}