package io.github.some_example_name;

import java.io.*;

public class LevelManager implements Serializable {
    private static final String LEVEL_FOLDER = "Levels";

    public static void saveLevelOne(LevelOne level) throws IOException {
        File folder = new File(LEVEL_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdir();
            if (!created) {
                throw new IOException("Failed to create directory: " + LEVEL_FOLDER);
            }
        }

        String fileName = LEVEL_FOLDER + "/order_1.dat";
        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file, true); // true-> append mode
             ObjectOutputStream oos = file.length() == 0 ? new ObjectOutputStream(new BufferedOutputStream(fos)): new AppendingObjectOutputStream(new BufferedOutputStream(fos)))
        {
            oos.writeObject(level);
        }
        catch (IOException e){
            System.out.println("Error while saving order: " + e.getMessage());
            throw e;
        }
    }
}
