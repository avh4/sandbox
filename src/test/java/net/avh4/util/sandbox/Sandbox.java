package net.avh4.util.sandbox;

import java.io.File;
import java.io.IOException;

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

}
