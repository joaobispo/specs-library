/**
 * Copyright 2013 SuikaSoft.
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

/**
 * Code taken from OpenJFX 8 Master RT PlatformUtil.java
 * 
 * @author Joao Bispo
 * 
 */
public class PlatformUtils {

	private static final String os = System.getProperty("os.name");
	private static final String version = System.getProperty("os.version");

	private static final boolean ANDROID = isAndroidCheck();
	private static final boolean WINDOWS = os.startsWith("Windows");
	private static final boolean WINDOWS_VISTA_OR_LATER = WINDOWS
			&& versionNumberGreaterThanOrEqualTo(6.0f);
	private static final boolean WINDOWS_7_OR_LATER = WINDOWS
			&& versionNumberGreaterThanOrEqualTo(6.1f);
	private static final boolean MAC = os.startsWith("Mac");
	private static final boolean LINUX = os.startsWith("Linux") && !ANDROID;
	private static final boolean SOLARIS = os.startsWith("SunOS");
	private static final boolean IOS = os.startsWith("iOS");

	/**
	 * Utility method used to determine whether the version number as reported
	 * by system properties is greater than or equal to a given value.
	 * 
	 * @param value
	 *            The value to test against.
	 * @return false if the version number cannot be parsed as a float,
	 *         otherwise the comparison against value.
	 */
	private static boolean versionNumberGreaterThanOrEqualTo(float value) {
		try {
			return Float.parseFloat(version) >= value;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean isAndroidCheck() {
		try {
			Class.forName("android.app.Activity");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Returns true if the operating system is a form of Windows.
	 */
	public static boolean isWindows() {
		return WINDOWS;
	}

	/**
	 * Returns true if the operating system is at least Windows Vista(v6.0).
	 */
	public static boolean isWinVistaOrLater() {
		return WINDOWS_VISTA_OR_LATER;
	}

	/**
	 * Returns true if the operating system is at least Windows 7(v6.1).
	 */
	public static boolean isWin7OrLater() {
		return WINDOWS_7_OR_LATER;
	}

	/**
	 * Returns true if the operating system is a form of Mac OS.
	 */
	public static boolean isMac() {
		return MAC;
	}

	/**
	 * Returns true if the operating system is a form of Linux.
	 */
	public static boolean isLinux() {
		return LINUX;
	}

	/**
	 * Returns true if the operating system is a form of Unix, including Linux.
	 */
	public static boolean isSolaris() {
		return SOLARIS;
	}

	/**
	 * Returns true if the operating system is a form of Linux or Solaris.
	 */
	public static boolean isUnix() {
		return LINUX || SOLARIS;
	}

	/**
	 * Returns true if the operating system is iOS.
	 */
	public static boolean isIOS() {
		return IOS;
	}

	public static boolean isAndroid() {
		return ANDROID;
	}

	public static String getPlatformName() {
		return os;
	}

}
