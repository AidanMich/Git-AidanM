import java.io.*;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Commit {
    private String treeSHA1;
    private String parentCommitSHA1;
    private String nextCommitSHA1;
    private String author;
    private String date;
    private String summary;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Commit tester = new Commit("randomstringofletters", "", "Aidan Michaelson", "Wow look at this awesome commit!");
        Commit test2 = new Commit("", "", "hahah not writing that",
                "This one tests if both the parent and next commits are null");
        Commit test3 = new Commit("parentSHAPlaceholder", "NextSHAPlaceholder", "Ronald McDonald",
                "What if the Commit has both a previous and next commit");
         tester.writeToFile("commit");
         test2.writeToFile("commit2");
         test3.writeToFile("commit3");

    }

    public Commit(String parentCommitSHA1, String nextCommit, String author, String summary)
            throws NoSuchAlgorithmException, IOException {
        this.nextCommitSHA1 = nextCommit;
        this.treeSHA1 = Commit.createTree();
        this.parentCommitSHA1 = parentCommitSHA1;
        this.author = author;
        this.date = getCurrentDate();
        this.summary = summary;
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
        String fileContents = parentCommitSHA1 + "\n" + nextCommitSHA1 + "\n" + author + "\n" + getDate() + "\n"
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
            writer.println(""); // Placeholder for the SHA1 of the next commit (blank initially)
            writer.println(author);
            writer.println(date);
            writer.println(summary);
        }
    }

    // Create a Tree and return its SHA1
    public static String createTree() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree("tree.txt");
        // Add entries to the tree, for example:
        // tree.add("file1.txt");
        // tree.add("file2.txt");
        // ...
        tree.generateBlob();
        return tree.sha1("tree.txt");
    }

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
}