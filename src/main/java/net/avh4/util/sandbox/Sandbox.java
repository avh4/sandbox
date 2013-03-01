package net.avh4.util.sandbox;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Sandbox {

    private final File root;

    public Sandbox() {
        try {
            root = createTempDirectory();
        } catch (final IOException e) {
            throw new RuntimeException("Could not create a sandbox", e);
        }
    }

    public File getRoot() {
        return root;
    }

    public File newFile(final String filename) {
        return new File(getRoot(), filename);
    }

    private static File createTempDirectory() throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: "
                    + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: "
                    + temp.getAbsolutePath());
        }

        return (temp);
    }

    public void useResource(final String resourceName) {
        useResource(resourceName, resourceName);
    }

    public void useResource(String resourceName, String filePath) {
        File file = new File(root, filePath);
        useResource(resourceName, file);
    }

    private void useResource(String resourceName, File file) {
        final URL source = getResourceUrl(resourceName);
        try {
            FileUtils.copyURLToFile(source, file);
        } catch (final IOException e) {
            throw new RuntimeException(
                    "Unable to load resource '" + resourceName + "'", e);
        }
    }

    private URL getResourceUrl(String resourceName) {
        final URL source = Sandbox.class.getResource("/" + resourceName);
        if (source == null) {
            throw new RuntimeException("Unable to load resource '" + resourceName
                    + "'");
        }
        return source;
    }
}
