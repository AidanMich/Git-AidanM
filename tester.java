import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Tester {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        Index.init();

        String file = "test.txt";
        Blob.blob(file);

        String file2 = "test2.txt";
        Blob.blob(file2);

        String file3 = "test3.txt";
        Index.add(file3);
        String file4 = "test4.txt";
        Index.add(file4);
        System.out.println(Blob.sha1(Blob.read(file3)));
        Index.remove(file3);

        String content = Blob.read("test.txt");
        String hash = Blob.sha1(content);
        System.out.println(hash);
        File testFile = new File("objects", hash);
        if (testFile.exists()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
