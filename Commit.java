import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.nio.file.Path;

public class Commit {
    private String treeSHA1;
    private String parentCommitSHA1;
    private String nextCommitSHA1;
    private String author;
    private String date;
    private String summary;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Commit tester = new Commit("randomstringofletters", "Aidan Michaelson", "Wow look at this awesome commit!");
        Commit test2 = new Commit("", "hahah not writing that",
                "This one tests if both the parent and next commits are null");
        Commit test3 = new Commit("parentSHAPlaceholder", "Ronald McDonald",
                "What if the Commit has both a previous and next commit");
         tester.writeToFile("commit");
         test2.writeToFile("commit");
         test3.writeToFile("commit");

    }

    public Commit(String parentCommitSHA1, String author, String summary)
            throws NoSuchAlgorithmException, IOException {
        //this.nextCommitSHA1 = nextCommit;

        this.treeSHA1 = Commit.createTree();
        this.parentCommitSHA1 = parentCommitSHA1;
        this.author = author;
        this.date = getCurrentDate();
        this.summary = summary;

        File exists = new File ("commit");
        if (exists.exists ())
        {
            writeToFile2 ("commit");
            String headName = readFile ("commit");
         head (headName);
        }
        else {
        exists.createNewFile();
        // String filePath = "commit";
        // filePath.getPath ();
        // String headName = Files.readString (filePath);

        String headName = readFile ("commit");
        head (headName);
        }
    }
    

    public String getTreeSHA1() {
        return treeSHA1;
    }

    public String getParentCommitSHA1() {
        return parentCommitSHA1;
    }

    public String getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }

    public String generateSHA1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // String fileContents = treeSHA1 + "\n" + parentCommitSHA1 + "\n" + author +
        // "\n" + getDate() + "\n" + summary;
        // String parentCommitSHA1, String nextCommit, String author, String summary
        String fileContents = parentCommitSHA1 + "\n" + author + "\n" + getCurrentDate() + "\n"
                + summary;

        // MessageDigest digest = MessageDigest.getInstance("SHA-1");
        // byte[] hash = digest.digest(fileContents.getBytes("UTF-8"));

        // StringBuilder hexString = new StringBuilder();
        // for (byte b : hash) {
        // hexString.append(String.format("%02x", b));
        // }
        String hex = Blob.sha1(fileContents);
        return hex;
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(new Date());
    }

    // public void writeToFile() throws IOException, NoSuchAlgorithmException {
    // String sha1 = generateSHA1();
    // String filePath = "objects/" + sha1;

    // try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
    // writer.write(treeSHA1 + "\n");
    // if (parentCommitSHA1 != null) {
    // writer.write(parentCommitSHA1 + "\n");
    // }
    // if (nextCommitSHA1 != null) {
    // writer.write(nextCommitSHA1 + "\n");
    // }
    // writer.write(author + "\n");
    // writer.write(date + "\n");
    // writer.write(summary + "\n");
    // }
    // }

    public void writeToFile(String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println(treeSHA1);
            writer.println(parentCommitSHA1 != null ? parentCommitSHA1 : "");
            writer.println(""); // Placeholder for future commits
            writer.println(author);
            writer.println(date);
            writer.println(summary);
        }
    }

    public void writeToFile2(String filePath) throws IOException, NoSuchAlgorithmException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
             writer.println(treeSHA1);
             writer.println(parentCommitSHA1 != null ? parentCommitSHA1 : "");
            //writer.println(Blob.sha1 (Commit.readFile ("commit")));
             writer.println(author);
             writer.println(date);
             writer.println(summary);
        }
    }

    // Create a Tree and return its SHA1
    public static String createTree() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree("tree.txt");
        tree.generateBlob();
        return tree.sha1("tree.txt");
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    private void head(String headName) throws IOException, NoSuchAlgorithmException
    {
        headName = Blob.sha1 (headName);
        File head = new File("head");
        if (!head.exists())
        {
            head.createNewFile();
        }
        Files.write(head.toPath(), headName.getBytes());
    }

    public static String readFile (String fileName) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
	        stringBuilder.append(line);
	        stringBuilder.append(ls);
        }
        // delete the last new line separator
        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String content = stringBuilder.toString();
        return content;
    }
}