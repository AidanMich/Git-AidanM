//step 1.
//read a file
//hash the contents of said file
//create a new file called that hash, with the original files contents.
//put that new file into a objects folder.

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class Blob {
    public static void blob(String fileName) throws IOException, NoSuchAlgorithmException {
        try {
            String content = read(fileName);
            String hash = sha1(content);

            String folderPath = "objects";
            String newFileName = hash;

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String filePath = folderPath + File.separator + newFileName;

            File newFile = new File(filePath);

            PrintWriter pw = new PrintWriter(newFile);
            pw.print(content);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String read(String fileName) throws IOException {
        StringBuilder str = new StringBuilder("");
        BufferedReader ar = new BufferedReader(new FileReader(fileName));
        while (ar.ready()) {
            str.append((char) ar.read());
        }
        ar.close();
        return str.toString();
    }

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // System.out.println("Hello " + sha1("Hello"));
        blob("test.txt");

        String file2 = "test2.txt";
        blob(file2);
    }
}
