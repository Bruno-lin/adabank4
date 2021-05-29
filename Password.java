import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class Password {

    static String result;

    /**
     * 将一个byte数组转换成十六进制编码的字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int b : bytes) {
            int val = b;
            val = val & 0xff;                   // 只取最后一个字节
            if (val < 16) builder.append('0');  // 前导0
            builder.append(Integer.toString(val, 16));
        }
        return builder.toString();
    }

    /**
     * 计算哈希值，返回一个byte数组
     */
    public static byte[] stringToBytes(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密算法
     */
    public static void decrypt(String digits, String encoded, int threadNums) {
        int digit = Integer.parseInt(digits);

        char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        List<char[]> lists = new ArrayList<>();
        int range = alphabet.length / threadNums;

        if (threadNums != 1) {
            for (int i = 0; i < threadNums - 1; i++) {
                lists.add(Arrays.copyOfRange(alphabet, i * range, (1 + i) * range));
            }
        }
        lists.add(Arrays.copyOfRange(alphabet, (threadNums - 1) * range, alphabet.length));

        CountDownLatch latch = new CountDownLatch(threadNums);
        for (char[] chars : lists) {
            new Thread(() ->
            {
                for (char c : chars) {
                    findPassword(alphabet, new StringBuilder(String.valueOf(c)), digit, 1, encoded);
                }
                latch.countDown();
            }
            ).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暴力破解过程
     */
    public static void findPassword(char[] alphabet, StringBuilder sb,
                                    int digit, int index, String encoded) {
        if (digit == 1) {
            if (isMatch(sb, encoded)) {
                result = sb.toString();
            }
        }

        if (index < digit) {
            for (char c : alphabet) {
                sb.append(c);
                if (isMatch(sb, encoded)) {
                    result = sb.toString();
                }
                findPassword(alphabet, sb, digit, index + 1, encoded);

                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    //判断是否匹配
    private static boolean isMatch(StringBuilder sb, String encoded) {
        byte[] bytes = stringToBytes(sb.toString());
        String str = bytesToHexString(Objects.requireNonNull(bytes));
        return str.equals(encoded);
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String password = args[0];
            byte[] bytes = stringToBytes(password);
            String str = bytesToHexString(Objects.requireNonNull(bytes));
            System.out.println(str);
        } else if (args.length == 2) {
            decrypt(args[0], args[1], 1);
            print();
        } else if (args.length == 3) {
            decrypt(args[0], args[1], Integer.parseInt(args[2]));
            print();
        }
    }

    //打印
    private static void print() {
        if (result != null) {
            System.out.println("found: " + result);
        } else
            System.out.println("failed");
    }
}
