/**
 *  Copyright 2015 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.library;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ParseUtils {

	/**
	 * Transforms a number of nano-seconds into a string representing from
	 * milliseconds to minutes.
	 * 
	 * @param nanos
	 * @return
	 */
	public static String getTime(long nanos) {
		NumberFormat doubleFormat = NumberFormat.getNumberInstance();
		doubleFormat.setMaximumFractionDigits(2);

		// Check millis
		double millis = (double) nanos / 1000000;

		if (millis < 1000) {
			return doubleFormat.format(millis) + "ms";
		}

		double secs = millis / 1000;
		if (secs < 60) {
			return doubleFormat.format(secs) + "s";
		}

		double mins = secs / 60.0;

		return doubleFormat.format(mins) + " minutes";
	}

	public static String getRegexGroup(String contents, Pattern pattern, int capturingGroupIndex) {

		String tester = null;

		try {

			Matcher regexMatcher = pattern.matcher(contents);
			if (regexMatcher.find()) {

				tester = regexMatcher.group(capturingGroupIndex);
			}
		} catch (PatternSyntaxException ex) {

			Log.warn(ex.getMessage());
		}

		return tester;
	}

}
