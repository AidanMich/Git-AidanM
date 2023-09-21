//create a new directory called objects 
//create a new file called index
//be able to add blobs which as before puts them into objects
//but also creates a new file with the name of the txt file and the hash
//be able to remove the file associated with that txt and remove it from objects

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Index {

    public static void init() throws FileNotFoundException {
        File theDir = new File("objects");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        File file = new File("Index");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void add(String fileName) throws NoSuchAlgorithmException, IOException {
        Blob.blob(fileName);
        File file = new File("Index");

        // add hash file to object
        // add txt name and hash to index.
        PrintWriter pw = new PrintWriter(file);
        pw.print(fileName + ": " + Blob.sha1(Blob.read(fileName)) + "\n");
        pw.close();
    }

    public static void remove(String fileName) throws IOException, NoSuchAlgorithmException {
        String objectsFolderPath = "objects";

        String content = Blob.read(fileName);
        String hash = Blob.sha1(content);
        String entryToDelete = "fileName: " + hash;

        try {
            List<String> indexContents = new ArrayList<>();
            BufferedReader indexReader = new BufferedReader(new FileReader("Index"));
            PrintWriter pw = new PrintWriter("Index");
            String line;
            while ((line = indexReader.readLine()) != null) {
                if (!line.equals(entryToDelete)) {
                    indexContents.add(line);
                }
            }
            indexReader.close();
            pw.close();

            BufferedWriter indexWriter = new BufferedWriter(new FileWriter("Index"));
            for (String contents : indexContents) {
                indexWriter.write(content);
                indexWriter.newLine();
            }
            indexWriter.close();

            File fileToDelete = new File(hash);
            fileToDelete.delete();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        init();
        String file3 = "test3.txt";
        add(file3);
        String file4 = "test4.txt";
        add(file4);
        System.out.println(Blob.sha1(Blob.read(file3)));
        remove(file3);
    }
}
