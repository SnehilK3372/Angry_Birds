package io.github.some_example_name;

import java.io.*;

public class AppendingObjectOutputStream extends ObjectOutputStream implements Serializable {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();  // Skips the header
    }
}
