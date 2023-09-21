import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTree {
    @Test
    @DisplayName("Test Tree")
    void testAddAndRemoveInTree() throws NoSuchAlgorithmException, IOException {
        // Add a blob entry
        String content = "Tree test content";
        // Write content to the testFile
        File tree = new File("treeTest.txt");
        tree.createNewFile();
        PrintWriter pw = new PrintWriter(tree);
        pw.print(content);
        pw.close();
        Tree test = new Tree("test");
        test.add("treeTest.txt");
        assertTrue(tree.exists());

        // Read the contents of the testFile and check if it contains the added blob
        // entry
        String fileContents = Blob.read("test");
        assertTrue(fileContents.contains("blob : " + Blob.sha1(content) + " : " + "treeTest.txt"));

        test.remove("treeTest.txt");
        String fileContents2 = Blob.read("test");
        assertEquals(fileContents2, "");
    }
}
