
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class TestTree {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILE_CONTENT = "This is a test file content.";
    private Tree tree;

    

    @Test
    @DisplayName("Testing the tree constructor to make a tree file")
    public void testTreeConstructor() throws IOException {
        Tree tree = new Tree(); // should create a Tree file

        File treeFile = new File("Tree");
        assertTrue(treeFile.exists());

    }

    @Test
    @DisplayName("Testing if I can write to the tree")
    public void testWriteToTree() throws Exception {
        Tree tree = new Tree(); // should create a Tree file

        File treeFile = new File("Tree");
        assertTrue(treeFile.exists());

        // programatically create Files
        File testFile = new File(TEST_FILE);
        testFile.createNewFile();

        String fileContents = Blob.read(TEST_FILE);
        String fileSha = Blob.sha1(fileContents);

        String newLine = "blob : " + fileSha + " : " + TEST_FILE;
        tree.add(TEST_FILE); // exception

        String treeContents = Blob.read("Tree");

        assertEquals(newLine, treeContents);

    }

    @Test
    public void testAddRemoveAndGetSha() throws Exception {
        // Test the add, remove, and getSha methods
        Tree tree = new Tree();

        // Add a blob and a tree entry
        tree.add(TEST_FILE);
        tree.add("tree : subdir-hash : subdir");

        // Check if the tree file contains the added entries
        File treeFile = new File("Tree");
        assertTrue(treeFile.exists() && treeFile.isFile());

        try (Scanner scanner = new Scanner(treeFile)) {
            boolean foundBlob = false;
            boolean foundTree = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("blob : ") && line.contains(TEST_FILE)) {
                    foundBlob = true;
                } else if (line.contains("tree : subdir-hash : subdir")) {
                    foundTree = true;
                }
            }
            assertTrue(foundBlob);
            assertTrue(foundTree);
        }

        // Remove the blob entry and check if it's removed
        tree.remove(TEST_FILE);
        try (Scanner scanner = new Scanner(treeFile)) {
            boolean foundBlob = false;
            boolean foundTree = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("blob : ") && line.contains(TEST_FILE)) {
                    foundBlob = true;
                } else if (line.contains("tree : subdir-hash : subdir")) {
                    foundTree = true;
                }
            }
            assertFalse(foundBlob);
            assertTrue(foundTree);
        }

    }

    
}