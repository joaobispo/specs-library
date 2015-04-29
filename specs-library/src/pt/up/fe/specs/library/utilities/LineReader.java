/*
 * Copyright 2010 Ancora Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.library.utilities;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import pt.up.fe.specs.library.Log;

/**
 * Reads lines from a File, one by one.
 * 
 * @author Joao Bispo
 */
public class LineReader implements Iterable<String>, Closeable {

	/**
	 * INSTANCE VARIABLES
	 */
	private final BufferedReader reader;
	private int currentLineIndex;
	private String nextLine;
	private final Optional<String> name;

	private boolean fileEnded;

	/**
	 * Default CharSet used in file operations.
	 */
	public static final String DEFAULT_CHAR_SET = "UTF-8";

	/**
	 * Private constructor for static creator method.
	 * 
	 * @param reader
	 */
	private LineReader(BufferedReader reader, String filename) {
		this.reader = reader;
		this.name = Optional.ofNullable(filename);

		this.currentLineIndex = 0;
		fileEnded = false;

		this.nextLine = nextLineHelper();
		// this.currentLineIndex = 1;
		// this.currentLine =reader.readLine();

	}

	/**
	 * Builds a LineReader from the given file. If the object could not be
	 * created, returns null.
	 * 
	 * <p>
	 * Creating a LineReader involves operations which can lead to failure in
	 * creation of the object. That is why a public static method is used
	 * instead of a constructor.
	 * 
	 * @return a LineReader If the object could not be created, or if there is
	 *         any problem, throws a RuntimeException.
	 */
	// Cannot close resource, since the stream must remain open after LineReader
	// is created.
	public static LineReader createLineReader(File file) {

		// @SuppressWarnings("resource")
		// FileInputStream stream = null;
		try {
			// try (FileInputStream stream = new FileInputStream(file)) {
			FileInputStream stream = new FileInputStream(file);
			// return createLineReader(stream, file.getName());
			return createLineReader(stream, file.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Problem while using LineReader backed by a file", e);
		}
	}

	/**
	 * Builds a LineReader from the given String. If the object could not be
	 * created, returns null.
	 * 
	 * @param string
	 * @return
	 */
	public static LineReader createLineReader(String string) {
		return createLineReader(string, null);
	}

	/**
	 * Builds a LineReader from the given String. If the object could not be
	 * created, returns null.
	 * 
	 * @param string
	 * @param filename
	 * @return a LineReader If the object could not be created, returns null.
	 */
	public static LineReader createLineReader(String string, String filename) {
		StringReader reader = new StringReader(string);
		BufferedReader newReader = new BufferedReader(reader);
		return new LineReader(newReader, filename);
	}

	/**
	 * @deprecated
	 * @param inputStream
	 * @return
	 */
	/*
	 * public static LineReader createLineReader(InputStream inputStream) {
	 * return createLineReader(inputStream, null); }
	 */

	/**
	 * Builds a LineReader from the given InputStream. If the object could not
	 * be created, returns null.
	 * 
	 * <p>
	 * Creating a LineReader involves operations which can lead to failure in
	 * creation of the object. That is why a public static method is used
	 * instead of a constructor.
	 * 
	 * @return a LineReader If the object could not be created, returns null.
	 */
	public static LineReader createLineReader(InputStream inputStream, String name) {

		InputStreamReader streamReader = null;

		try {
			// Try to read the contents of the file into the StringBuilder
			streamReader = new InputStreamReader(inputStream, DEFAULT_CHAR_SET);
			BufferedReader newReader = new BufferedReader(streamReader);
			return new LineReader(newReader, name);
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(LineReader.class.getName()).log(Level.WARNING,
					"UnsupportedEncodingException: " + ex.getMessage());
		}

		return null;
	}

	public int getLastLineIndex() {
		return currentLineIndex;
	}

	public Optional<String> getFilename() {
		return name;
	}

	/**
	 * @return the next line in the file, or null if the end of the stream has
	 *         been reached.
	 */
	public String nextLine() {
		if (nextLine != null) {
			this.currentLineIndex++;
		}

		String currentLine = nextLine;

		nextLine = nextLineHelper();

		return currentLine;
	}

	public boolean hasNextLine() {
		return nextLine != null;
	}

	private String nextLineHelper() {
		// If file already ended, return null
		if (fileEnded) {
			return null;
		}

		try {
			// Read next line
			String line = reader.readLine();

			// If we got to the end of the stream mark it, and close reader.
			if (line == null) {
				fileEnded = true;
				reader.close();
			}

			return line;
		} catch (IOException ex) {
			Log.warn("Could not read line.", ex);
			// Logger.getLogger(LineReader.class.getName()).log(Level.WARNING,
			// "IOException: " + ex.getMessage());
			return null;
		}
	}

	/**
	 * @return the next line which is not empty, or null if the end of the
	 *         stream has been reached.
	 */
	public String nextNonEmptyLine() {
		boolean foundAnswer = false;
		while (!foundAnswer) {
			String line = nextLine();

			if (line == null) {
				return line;
			}

			if (line.length() > 0) {
				return line;
			}

		}

		return null;
	}

	public static List<String> readLines(File file) {
		return readLines(LineReader.createLineReader(file));
	}

	public static List<String> readLines(String string) {
		return readLines(LineReader.createLineReader(string));
	}

	private static List<String> readLines(LineReader lineReader) {
		List<String> lines = new ArrayList<>();
		// LineReader lineReader = LineReader.createLineReader(file);
		String line = null;
		while ((line = lineReader.nextLine()) != null) {
			lines.add(line);
		}

		return lines;
	}

	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {

			@Override
			public boolean hasNext() {
				return hasNextLine();
			}

			@Override
			public String next() {
				return nextLine();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("LineReader does not support 'remove'.");

			}
		};
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			Log.warn("Could not close LineReader.", e);
		}
	}

	public Stream<String> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
