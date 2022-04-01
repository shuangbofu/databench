package org.example.databench.common.utils;

import java.io.*;

public class FileUtils {

    public static String loadResourceFile(String path) throws IOException {
        InputStream stream = FileUtils.class.getClassLoader()
                .getResourceAsStream(path);
        if (stream != null) {
            return new String(stream.readAllBytes());
        }
        throw new IOException("Stream is null");
    }

    public static void writeStringToFile(File file, String data, String encoding) throws IOException {
        FileOutputStream out = null;
        try {
            out = openOutputStream(file);
            write(data, out, encoding);
        } finally {
            closeQuietly(out);
        }

    }

    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, (String) null);
    }

    public static void write(String data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes());
        }
    }

    public static void write(String data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            if (encoding == null) {
                write(data, output);
            } else {
                output.write(data.getBytes(encoding));
            }
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException var2) {
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IOException("File '" + file + "' could not be created");
            }
        }
        return new FileOutputStream(file);
    }

}
