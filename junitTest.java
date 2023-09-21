import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class junitTest {
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

        assertTrue("Blob file to add not found", file_junit1.exists());
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

    @Test
    void testGenerateBlob() throws NoSuchAlgorithmException, IOException {
        Tree testTree = new Tree("testTree");
        File testFile = new File("junittester.txt");

        // Add a tree entry
        testTree.add("tree: testdir");

        // Generate a blob for the tree
        testTree.generateBlob();

        // Check if a blob file with the same name as the tree exists
        File blobFile = new File(testFile.getName() + ".blob");
        assertTrue(blobFile.exists());
    }
}
