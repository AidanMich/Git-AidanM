import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.PrintWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestIndex {
    @Test
    @DisplayName("Test Index")
    void testCreateBlob() throws Exception {
        Index.init();
        File testFile = new File("junittester.txt");
        testFile.createNewFile();
        PrintWriter pw = new PrintWriter(testFile);
        pw.print("Junit tester");
        pw.close();
        assertTrue(testFile.exists());

        Index.add("junittester.txt");

        // Check blob exists in the objects folder
        File file_junit1 = new File("objects", Blob.sha1(Blob.read("junittester.txt")));

        assertTrue(file_junit1.exists());
        // Read file contents
        String fileContents = Blob.read("junittester.txt");
        String hash = Blob.sha1(fileContents);

        String indexContents = Blob.read("Index");
        assertEquals("junittester.txt: " + hash + "\n", indexContents);

        Index.remove("junittester.txt");
        String fileContents2 = Blob.read("junittester.txt");
        String hash2 = Blob.sha1(fileContents);

        String indexContents2 = Blob.read("Index");
        assertEquals("", indexContents2);
    }
}
