import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Password {

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
    public static String decrypt(String digits, String encoded) {
        int digit = Integer.parseInt(digits);

        String Num = "9".repeat(digit);
        int maxNum = Integer.parseInt(Num);

        for (int i = 0; i <= maxNum; i++) {
            byte[] bytes = stringToBytes(String.valueOf(i));
            String str = bytesToHexString(Objects.requireNonNull(bytes));
            if (str.equals(encoded)) return String.valueOf(i);
        }
        return "failed";
    }


    public static void main(String[] args) {

        if (args.length == 1) {
            String password = args[0];
            byte[] bytes = stringToBytes(password);
            String str = bytesToHexString(Objects.requireNonNull(bytes));
            System.out.println(str);
        } else if (args.length == 2){
            String decoded = decrypt(args[0], args[1]);
            System.out.println(decoded);
        }
    }
}
