/**
 * Copyright 2015 SPeCS.
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

package org.specs.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public class IoUtils {

	/**
	 * Default CharSet used in file operations.
	 */
	final public static String DEFAULT_CHAR_SET = "UTF-8";

	/**
	 * Helper method for Guava Files.toString, which uses the default Charset
	 * and throws an unchecked exception.
	 * 
	 * @param file
	 * @return
	 */
	public static String read(File file) {
		try {
			return Files.toString(file, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException("Could not read file '" + file + "'", e);
		}
	}

	/**
	 * Reads a stream to a String. The stream is closed after it is read.
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String read(InputStream inputStream) {
		StringBuilder stringBuilder = new StringBuilder();

		// Try to read the contents of the input stream into the StringBuilder
		// Using 'finally' style 2 as described in
		// http://www.javapractices.com/topic/TopicAction.do?Id=25
		try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
				DEFAULT_CHAR_SET))) {

			// Read first character. It can't be cast to "char", otherwise the
			// -1 will be converted in a character.
			// First test for -1, then cast.
			int intChar = bufferedReader.read();
			while (intChar != -1) {
				char character = (char) intChar;
				stringBuilder.append(character);
				intChar = bufferedReader.read();
			}

		} catch (FileNotFoundException ex) {
			Log.warn("FileNotFoundException", ex);
			stringBuilder = new StringBuilder(0);
		} catch (IOException ex) {
			Log.warn("IOException", ex);
			stringBuilder = new StringBuilder(0);
		}

		return stringBuilder.toString();
	}

	public static InputStream resourceToStream(String resourceName) {
		// Obtain the current classloader
		ClassLoader classLoader = IoUtils.class.getClassLoader();

		// Load the file as a resource
		InputStream stream = classLoader.getResourceAsStream(resourceName);
		if (stream == null) {
			Log.warn("Could not load resource '" + resourceName + "'.");

		}

		return stream;
	}

	/**
	 * Given the name of a resource, returns a String with the contents of the
	 * resource.
	 * 
	 * @param resourceName
	 * @return
	 */
	public static String getResource(String resourceName) {
		try (InputStream inputStream = IoUtils.resourceToStream(resourceName)) {
			if (inputStream == null) {
				Log.warn("Could not get InputStream.");
				return null;
			}

			return IoUtils.read(inputStream);

		} catch (IOException e) {
			Log.warn("Could not open resource '" + resourceName + "'", e);
			return "";
		}
	}

	/**
	 * Helper method for Guava Files.write, which uses the default Charset and
	 * throws an unchecked exception.
	 * 
	 * @param contents
	 * @param file
	 */
	public static void write(CharSequence contents, File file) {
		try {
			Files.write(contents, file, Charset.defaultCharset());
		} catch (IOException e) {
			throw new RuntimeException("Could not write file '" + file + "'", e);
		}
	}
}
