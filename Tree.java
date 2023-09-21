import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    File tree;

    public Tree(String fileName) throws IOException {
        tree = new File(fileName);
        tree.createNewFile();
    }

    public void add(String input) throws NoSuchAlgorithmException, IOException {
        PrintWriter pw = new PrintWriter(tree);
        if (input.substring(0, 4).equals("tree")) {
            pw.print("\n + tree : " + input);
        } else {
            pw.print("\n + blob : " + Blob.sha1(Blob.read(input)) + " : " + input);
        }
        pw.close();
    }

    public void remove(String fileName) throws NoSuchAlgorithmException, IOException {
        String entryToDelete1 = "blob : " + Blob.sha1(Blob.read(fileName)) + fileName;
        String entryToDelete2 = "tree : " + fileName;

        try {
            List<String> indexContents = new ArrayList<>();
            BufferedReader lineReader = new BufferedReader(new FileReader(tree));
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (!line.equals(entryToDelete1) && !line.equals(entryToDelete2)) {
                    indexContents.add(line);
                }
            }
            lineReader.close();

            try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(tree))) {
                for (String contents : indexContents) {
                    indexWriter.write(contents);
                    indexWriter.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void generateBlob() throws NoSuchAlgorithmException, IOException {
        Blob.blob(tree.getName());
    }
}
