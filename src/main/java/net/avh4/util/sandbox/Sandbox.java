package net.avh4.util.sandbox;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

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

	public void useResource(final String name) {
		final File file = new File(root, name);
		try {
			final URL source = Sandbox.class.getResource("/" + name);
			if (source == null) {
				throw new RuntimeException("Unable to load resource '" + name
						+ "'");
			}
			FileUtils.copyURLToFile(source, file);
		} catch (final IOException e) {
			throw new RuntimeException(
					"Unable to load resource '" + name + "'", e);
		}
	}
}
