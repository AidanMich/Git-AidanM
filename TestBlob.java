import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestBlob {
    @Test
    @DisplayName("Test if Blobs are created correctly")
    void testBlob() throws Exception {
        // Run code
        File file = new File("junit-test.txt");
        file.createNewFile();

        PrintWriter pw = new PrintWriter(file);
        pw.print("Junit tester");
        pw.close();
        assertTrue(file.exists());

        Blob.blob("junit-test.txt");

        String content = Blob.read("junit-test.txt");
        String hash = Blob.sha1(content);

        File file1 = new File("objects", hash);
        assertTrue(file1.exists());
        assertEquals(Blob.read("junit-test.txt"), "Junit tester");
    }

    @Test
    void testGenerateBlob() throws NoSuchAlgorithmException, IOException {
        Tree testTree = new Tree("testTree");
        File testFile = new File("junittester.txt");

        // Add a tree entry
        testTree.add("tree: testdir");

        // Generate a blob for the tree
        testTree.generateBlob();

        // Check if a blob file with the same name as the tree exists
        File blobFile = new File("./objects/" + encryptPassword(readFile(testFile)));
        assertTrue(blobFile.exists());
    }

    public static String readFile(File file) {
        String currentString = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                char nextChar = (char) reader.read();
                currentString += nextChar;

            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("nah");
        }
        return currentString;

    }

    // TAKEN FROM STACK
    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    // TAKEN FROM STACK
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}