package net.avh4.util.sandbox;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class SandboxTest {

	@SuppressWarnings("rawtypes")
	private static final Collection NO_FILES = new ArrayList<File>();

	@Test
	public void testIsEmpty() {
		final Sandbox emptySandbox = new Sandbox();
		assertThat(emptySandbox.getRoot(), isEmptyDirectory());
		assertEmptyDirectory(emptySandbox.getRoot());
	}

	@Test
	public void testSandboxesAreIndependent() throws Exception {
		final Sandbox s1 = new Sandbox();
		final Sandbox s2 = new Sandbox();
		final File f = s1.newFile("file.txt");
		FileUtils.writeLines(f, Arrays.asList("test"));
		assertThat(s1.getRoot(), not(isEmptyDirectory()));
		assertThat(s2.getRoot(), isEmptyDirectory());
	}

	@Test
	public void testCreateFilesFromResources() throws Exception {
		final Sandbox s = new Sandbox();
		s.useResource("file_in_jar.txt");

		final String fileContents = FileUtils.readFileToString(s
				.newFile("file_in_jar.txt"));
		assertThat(fileContents, is("MAGIC NUMBER 3849"));
	}

	@Test
	public void testCorrectErrorMessageWhenResourceDoesntExist() {
		try {
			final Sandbox s = new Sandbox();
			s.useResource("doesnt_exist.txt");
		} catch (final NullPointerException e) {
			fail("Should not throw NullPointerException");
		} catch (final RuntimeException e) {
			// pass
		}
	}

	private Matcher<? super File> isEmptyDirectory() {
		return new TypeSafeMatcher<File>() {

			@Override
			public void describeTo(final Description description) {
				description.appendText("an empty directory");
			}

			@Override
			protected boolean matchesSafely(final File item) {
				@SuppressWarnings("rawtypes")
				final Collection files = FileUtils.listFiles(item, null, true);
				return files.equals(NO_FILES);
			}
		};
	}

	private static void assertEmptyDirectory(final File dir) {
		assertThat(FileUtils.listFiles(dir, null, true), is(NO_FILES));
	}

}
