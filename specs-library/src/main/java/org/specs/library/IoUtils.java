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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public class IoUtils {

    /**
     * Helper method for Guava Files.toString, which uses the default Charset and throws an unchecked exception.
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
     * Helper method for Guava Files.write, which uses the default Charset and throws an unchecked exception.
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
