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

package pt.up.fe.specs.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import pt.up.fe.specs.library.interfaces.ResourceProvider;
import pt.up.fe.specs.library.utilities.ExtensionFilter;

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
	 * @return a string with the contents of the resource
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
	 * Helper method which accepts a ResourceProvider.
	 * 
	 * @param resource
	 * @return
	 */
	public static String getResource(ResourceProvider resource) {
		return getResource(resource.getResource());
	}

	/**
	 * Helper method for Guava Files.write, which uses the default Charset and
	 * throws an unchecked exception.
	 * 
	 * @param contents
	 * @param file
	 */
	public static boolean write(CharSequence contents, File file) {
		try {
			Files.write(contents, file, Charset.defaultCharset());
			return true;
		} catch (IOException e) {
			Log.warn("Could not write file '" + file + "'", e);
		}

		return false;
	}

	public static File existingFile(File parent, String filePath) {
		File completeFilepath = new File(parent, filePath);

		return existingFile(completeFilepath.getPath());
	}

	/**
	 * Method to create a File object for a file which should exist.
	 * 
	 * <p>
	 * The method does some common checks (ex.: if the file given by filepath
	 * exists, if it is a file). If any of the checks fail, returns null and
	 * logs the cause.
	 * 
	 * @param filepath
	 *            String representing an existing file.
	 * @return a File object representing a file, or null if unsuccessful.
	 */
	public static File existingFile(String filepath) {
		// Check null argument. If null, it would raise and exception and stop
		// the program when used to create the File object.
		if (filepath == null) {
			throw new RuntimeException("Input 'filepath' is null.");
		}

		// Create File object
		final File file = new File(filepath);

		// Check if File is a file
		final boolean isFile = file.isFile();
		if (isFile) {
			return file;
		}

		// Check if File exists. If true, is not a file.
		final boolean fileExists = file.exists();
		if (fileExists) {
			throw new RuntimeException("Path '" + filepath + "' exists, but doesn't " + "represent a file.");
		}

		// File doesn't exist, throw exception
		throw new RuntimeException("Path '" + filepath + "' does not exist.");

	}

	/**
	 * Guarantees that a folder exists.
	 * 
	 * @param parentFolder
	 * @param foldername
	 * @return a folder, if it is exists, or throws an exception otherwise
	 */
	public static File existingFolder(File parentFolder, String foldername) {
		File folder = new File(parentFolder, foldername);

		if (!folder.isDirectory()) {
			throw new RuntimeException("Could not open folder '" + folder.getPath() + "'");
		}

		return folder;
	}

	/**
	 * Helper method which accepts a parent File and a child String as input.
	 * 
	 * @param parentFolder
	 * @param child
	 * @return
	 */
	public static File getFolder(File parentFolder, String child) {
		return getFolder(new File(parentFolder, child));
	}

	/**
	 * Helper method which accepts a File as input.
	 * 
	 * @param folder
	 * @return
	 */
	public static File getFolder(File folder) {
		return getFolder(folder.getPath());
	}

	/**
	 * Given a string representing a filepath to a folder, returns a File object
	 * representing the folder.
	 * 
	 * <p>
	 * If the folder doesn't exist, the method will try to create the folder and
	 * necessary sub-folders. If an error occurs (ex.: the folder could not be
	 * created, the given path does not represent a folder), throws an
	 * exception.
	 * 
	 * *
	 * <p>
	 * If the given folderpath is an empty string, returns the current working
	 * folder.
	 * 
	 * <p>
	 * If an object different than null is returned it is guaranteed that the
	 * folder exists.
	 * 
	 * @param folderpath
	 *            String representing a folder.
	 * @return a File object representing a folder, or an expection if
	 *         unsuccessful.
	 */
	public static File getFolder(String folderpath) {
		// Check null argument. If null, it would raise and exception and stop
		// the program when used to create the File object.
		if (folderpath == null) {
			throw new RuntimeException("Input 'folderpath' is null.");
		}

		// Check if folderpath is empty
		// if (folderpath.isEmpty()) {
		if (ParseUtils.isEmpty(folderpath)) {
			return IoUtils.getWorkingDir();
		}

		// Create File object
		final File folder = new File(folderpath);

		// The following checks where done in that sequence to avoid having
		// more than one level of if-nesting.

		// Check if File is a folder
		final boolean isFolder = folder.isDirectory();
		if (isFolder) {
			return folder;
		}

		// Check if File exists. If true, is not a folder.
		final boolean folderExists = folder.exists();
		if (folderExists) {
			throw new RuntimeException("Path '" + folderpath + "' exists, but " + "doesn't represent a folder.");
		}

		// Try to create folder.
		final boolean folderCreated = folder.mkdirs();
		if (folderCreated) {
			try {
				Log.lib("Folder created (" + folder.getCanonicalPath() + ").");
			} catch (IOException ex) {
				Log.lib("Folder created (" + folder.getAbsolutePath() + ").");
			}
			return folder;

		}

		// Check if folder exists
		if (folder.exists()) {
			Log.warn("Folder created (" + folder.getAbsolutePath() + ") but 'mkdirs' returned false.");
			return folder;
		}

		// Couldn't create folder
		throw new RuntimeException("Path '" + folderpath + "' does not exist and " + "could not be created.");

	}

	/**
	 * @param folder
	 *            a File representing a folder.
	 * @return all the folders inside the given folder, excluding other files.
	 */
	public static List<File> getFolders(File folder) {
		List<File> fileList = new ArrayList<>();
		File[] files = folder.listFiles();

		if (files == null) {
			return fileList;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				fileList.add(file);
			}
		}

		return fileList;
	}

	/**
	 * @param folder
	 *            a File representing a folder.
	 * @param extension
	 *            a string
	 * @return all the files inside the given folder, excluding other folders,
	 *         that have a certain extension.
	 */
	public static List<File> getFilesRecursive(File folder, String extension) {
		if (!folder.isDirectory()) {
			Log.warn("Folder '" + folder + "' does not exist.");
			return Collections.emptyList();
		}

		List<File> fileList = new ArrayList<>();
		File[] files = folder.listFiles(new ExtensionFilter(extension));

		fileList.addAll(Arrays.asList(files));

		files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				fileList.addAll(getFilesRecursive(file, extension));
			}
		}

		return fileList;
	}

	/**
	 * 
	 * @return a File representing the working directory
	 */
	public static File getWorkingDir() {
		return new File(".");
	}

	/**
	 * Get the relative path from one file to another, specifying the directory
	 * separator. If one of the provided resources does not exist, it is assumed
	 * to be a file unless it ends with '/' or '\'.
	 * 
	 * <p>
	 * Taken from:
	 * http://stackoverflow.com/questions/204784/how-to-construct-a-relative
	 * -path-in-java-from-two-absolute-paths-or-urls?rq=1
	 * 
	 * @param targetPath
	 *            targetPath is calculated to this file
	 * @param basePath
	 *            basePath is calculated from this file
	 * @param pathSeparator
	 *            directory separator. The platform default is not assumed so
	 *            that we can test Unix behaviour when running on Windows (for
	 *            example)
	 * @return
	 */

	public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {

		if (basePath.isEmpty()) {
			Log.warn("IoUtils.getRelativePath: CHECK IF CORRECT");
			return targetPath;
		}

		String[] base = basePath.split(Pattern.quote(pathSeparator));
		String[] target = targetPath.split(Pattern.quote(pathSeparator));

		// First get all the common elements. Store them as a string,
		// and also count how many of them there are.
		StringBuffer common = new StringBuffer();

		int commonIndex = 0;
		while (commonIndex < target.length && commonIndex < base.length
				&& target[commonIndex].equals(base[commonIndex])) {
			common.append(target[commonIndex] + pathSeparator);
			commonIndex++;
		}

		if (commonIndex == 0) {
			// No single common path element. Try again, but assuming both paths
			// come from the same base
			return getRelativePath("/" + targetPath, "/" + basePath, pathSeparator);

			// return targetPath;
			// No single common path element. This most
			// likely indicates differing drive letters, like C: and D:.
			// These paths cannot be relativized.
			// throw new
			// PathResolutionException("No common path element found for '" +
			// normalizedTargetPath + "' and '" + normalizedBasePath
			// + "'");

		}

		// The number of directories we have to backtrack depends on whether the
		// base is a file or a dir
		// For example, the relative path from
		//
		// /foo/bar/baz/gg/ff to /foo/bar/baz
		//
		// ".." if ff is a file
		// "../.." if ff is a directory
		//
		// The following is a heuristic to figure out if the base refers to a
		// file or dir. It's not perfect, because
		// the resource referred to by this path may not actually exist, but
		// it's the best I can do
		boolean baseIsFile = true;

		File baseResource = new File(basePath);

		if (baseResource.exists()) {
			baseIsFile = baseResource.isFile();

		} else if (basePath.endsWith(pathSeparator)) {
			baseIsFile = false;
		}

		StringBuffer relative = new StringBuffer();

		if (base.length != commonIndex) {
			int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

			for (int i = 0; i < numDirsUp; i++) {
				relative.append(".." + pathSeparator);
			}
		}

		relative.append(targetPath.substring(common.length()));
		return relative.toString();
	}

	/**
	 * Returns the path of 'file', relative to 'baseFile'.
	 * 
	 * <p>
	 * The output path is normalized to use the '/' as path separator.
	 * 
	 * @param file
	 *            The file the user needs the relative path of.
	 * 
	 * @return the relative path of the file given in parameter.
	 */
	public static String getRelativePath(File file, File baseFile) {

		if (!baseFile.isDirectory()) {
			baseFile = baseFile.getParentFile();
			if (baseFile == null) {
				baseFile = new File("");
			}
			// throw new IllegalArgumentException("'" + startFolder
			// + "' is not a folder (argument 'startFolder')");
		}

		final String currentSeparator = File.separator;

		// final String CURRENT_FOLDER = ".";
		final String PREVIOUS_FOLDER = "..";
		/*
		final String WINDOWS = "\\";
		final String LINUX = "/";
		String currentSeparator = WINDOWS;
		*/
		// Finds the current folder path
		// String mainFolder = IoUtils.getWorkingDir().getAbsolutePath();
		String mainFolder = null;
		try {
			mainFolder = baseFile.getCanonicalPath();
			file = file.getCanonicalFile();
		} catch (IOException e) {
			Log.warn("Could not convert given files to canonical paths.");
			return null;
		}

		// System.out.println("Input File:" + file);
		// System.out.println("Base folder:" + mainFolder);
		// File fileInMainFolder = new File("temporary");
		// String mainFolder =
		// IoUtils.getParentFolder(fileInMainFolder).getAbsolutePath();
		/*
			if (mainFolder.contains(LINUX)) {
			    currentSeparator = LINUX;
			}
		*/
		// finds the parents of both files
		List<String> currentFileParents = getParentNames(file);
		List<String> mainFolderParents = getParentNames(new File(mainFolder));

		// find the first different parent
		int nbSimilarParents = 0;

		while (currentFileParents.size() > nbSimilarParents && mainFolderParents.size() > nbSimilarParents
				&& currentFileParents.get(nbSimilarParents).equals(mainFolderParents.get(nbSimilarParents))) {
			nbSimilarParents++;
		}

		// int nbParentToGoBack = mainFolderParents.length - nbSimilarParents;
		int nbParentToGoBack = mainFolderParents.size() - nbSimilarParents;

		// Writes the relative path
		// String relativePath = CURRENT_FOLDER;
		StringBuilder relativePath = new StringBuilder();

		for (int i = 0; i < nbParentToGoBack; i++) {
			relativePath.append(PREVIOUS_FOLDER);
			relativePath.append(currentSeparator);
			// relativePath += currentSeparator + PREVIOUS_FOLDER;
		}
		/*
			for (int i = nbSimilarParents; i < currentFileParents.length; i++) {
			    relativePath += currentSeparator + currentFileParents[i];
			}
			*/
		for (int i = nbSimilarParents; i < currentFileParents.size() - 1; i++) {
			// relativePath += currentSeparator + currentFileParents.get(i);
			relativePath.append(currentFileParents.get(i));
			relativePath.append(currentSeparator);
		}

		// Append last element
		relativePath.append(currentFileParents.get(currentFileParents.size() - 1));

		// return relativePath;
		String relativePathResult = relativePath.toString();

		// Normalize path separator
		relativePathResult = relativePathResult.replace('\\', '/');
		return relativePathResult;
	}

	/**
	 * The names of the parents of the given file.
	 * 
	 * @param file
	 * @return
	 */
	public static List<String> getParentNames(File file) {
		List<String> names = new ArrayList<>();

		getParentNamesReverse(file, names);
		Collections.reverse(names);
		return names;
	}

	/**
	 * 
	 * @param file
	 * @param names
	 * @return
	 */
	private static void getParentNamesReverse(File file, List<String> names) {
		// add current file name
		names.add(file.getName());

		File parent = file.getParentFile();

		// If null stop
		if (parent == null) {
			return;
		}
		// If empty string stop
		if (parent.getName().isEmpty()) {
			return;
		}

		// Call function recursively
		getParentNamesReverse(parent, names);
	}

	public static File getCanonicalFile(File file) {

		try {
			file = file.getCanonicalFile();
			// return new File(file.getAbsolutePath().replace('\\', '/'));
			return new File(sanitizePath(file.getAbsolutePath()));
		} catch (IOException e) {
			throw new RuntimeException("Could not get canonical file for " + file.getPath());
		}
	}

	/**
	 * Converts all '\' to '/'
	 * 
	 * @param path
	 * @return
	 */
	public static String sanitizePath(String path) {
		return path.replace('\\', '/');
	}

	/**
	 * 
	 * @param folder
	 * @return true in case the operation was successful (could delete all
	 *         files, or the folder does not exit)
	 */
	public static boolean deleteFolderContents(File folder) {
		if (!folder.exists()) {
			return true;
		}

		if (!folder.isDirectory()) {
			Log.warn("Not a folder");
			return false;
		}

		Log.lib("Deleting contents of folder '" + folder + "'");
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				deleteFolderContents(file);
			}
			boolean deleted = file.delete();
			if (deleted) {
				Log.lib("Deleted '" + file + "'");
			} else {
				Log.lib("Could not delete '" + file + "'");
			}
		}

		return true;
	}
}
