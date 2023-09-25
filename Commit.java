import java.io.*;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Commit {
    private String treeSHA1;
    private String parentCommitSHA1;
    private String author;
    private String date;
    private String summary;

    public Commit(String treeSHA1, String parentCommitSHA1, String author, String summary) {
        this.treeSHA1 = treeSHA1;
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
        String fileContents = treeSHA1 + "\n" + parentCommitSHA1 + "\n" + author + "\n" + date + "\n" + summary + "\n";
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(fileContents.getBytes("UTF-8"));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        return sdf.format(new Date());
    }

    public void writeToFile() throws IOException, NoSuchAlgorithmException {
        String sha1 = generateSHA1();
        String filePath = "objects/" + sha1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(treeSHA1 + "\n");
            if (parentCommitSHA1 != null) {
                writer.write(parentCommitSHA1 + "\n");
            }
            writer.write(author + "\n");
            writer.write(date + "\n");
            writer.write(summary + "\n");
        }
    }

    // Create a Tree and return its SHA1
    public String createTree() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree("tree.txt");
        // Add entries to the tree, for example:
        // tree.add("file1.txt");
        // tree.add("file2.txt");
        // ...
        tree.generateBlob();
        return tree.getSHA1("tree.txt");
    }

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
}