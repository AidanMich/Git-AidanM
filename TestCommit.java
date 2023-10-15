import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class TestCommit {
    private static final String treeSHA1 = "8c411a89ed6d846f064ed0decdba3a857f0d1667";
    private static final String parrentCommit = "f924e482dd33576fd0de90b6376f1671b08b5f52";
    private static final String testAuthor = "hello hello";
    private static final String testSummary = "oblia di obla da";

    @Before
    public void setUp() throws Exception {
        // Create a test directory for the objects
        File objectsDir = new File("objects");
        if (!objectsDir.exists()) {
            objectsDir.mkdirs();
        }
    }

    @After
    public void tearDown() throws Exception {
        // Delete the test directory for the objects
        File objectsDir = new File("objects");
        if (objectsDir.exists()) {
            deleteDirectory(objectsDir);
        }
    }

    @Test
    public void testWriteToFile() throws Exception {
        Commit commit = new Commit(treeSHA1, testAuthor, testSummary);
        String filePath = "objects/" + commit.generateSHA1();

        commit.writeToFile(filePath);

        File commitFile = new File(filePath);
        assertTrue(commitFile.exists() && commitFile.isFile());

        // Verify the content of the written file
        try (BufferedReader reader = new BufferedReader(new FileReader(commitFile))) {
            //assertEquals(treeSHA1, reader.readLine());
            assertEquals(parrentCommit, reader.readLine());
            assertEquals("", reader.readLine());
            assertEquals(testAuthor, reader.readLine());
            assertNotNull(reader.readLine()); // Date format varies
            assertEquals(testSummary, reader.readLine());
        }
    }

    @Test
    public void testGenerateSHA1() throws Exception {
        Commit commit = new Commit(treeSHA1, testAuthor, testSummary);
        String expectedSHA1 = Blob
                .sha1(treeSHA1 + "\n" + testAuthor + "\n" +
                        commit.getCurrentDate()
                        + "\n" + testSummary);

        assertEquals(expectedSHA1, commit.generateSHA1());
    }

    @Test
    public void testGetDate() throws Exception {
        Commit commit = new Commit(treeSHA1, testAuthor, testSummary);
        assertNotNull(commit.getDate());
    }

    @Test
    public void testCreateTree() throws Exception {
        Commit commit = new Commit(treeSHA1, testAuthor, testSummary);
        String treeSHA1 = commit.createTree();
        assertNotNull(treeSHA1);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testTwoCommits() throws IOException, NoSuchAlgorithmException {
        
        String parent = "";
        String author1 = "Aidan";
        String text1 = "First commit";

        Commit parentCommit = new Commit(parent, author1, text1);

        String author2 = "Delaney";
        String text2 = "Second commit";

        String str = Blob.sha1 (Commit.readFile ("commit"));
        Commit commit = new Commit("objects/" + str, author2, text2);
        //------------------
        assertNotNull(commit);
        String expected = commit.getTreeSHA1() + "\n" + "objects/" + str + "\n\n" + author2 + "\n" + commit.getCurrentDate() + "\n" + text2;
        String actual = Commit.readFile("commit");
        assertEquals(expected, actual);
    
    }
}