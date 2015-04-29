/**
 * Copyright 2012 SuikaSoft.
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

package pt.up.fe.specs.library.interfaces;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.library.IoUtils;

import com.google.common.base.Preconditions;

/**
 * Represents a class which provides a string to a Java resource.
 * 
 * @author Joao Bispo
 * 
 */
@FunctionalInterface
public interface ResourceProvider {

	/**
	 * The string corresponding to the path to a resource.
	 * 
	 * <p>
	 * Resources are '/' separated, and must not end with a '/'.
	 * 
	 * @return
	 */
	String getResource();

	/**
	 * Utility method which returns the ResourceProviders in an enumeration that
	 * implements ResourceProvider.
	 * 
	 * @param enumClass
	 * @return
	 */
	public static <K extends Enum<K> & ResourceProvider> List<ResourceProvider> getResources(Class<K> enumClass) {
		K[] enums = enumClass.getEnumConstants();

		List<ResourceProvider> resources = new ArrayList<>(enums.length);

		for (K anEnum : enums) {
			resources.add(anEnum);
		}

		return resources;
	}

	default String getResourceName() {
		String resourcePath = getResource();
		// Remove path
		int slashIndex = resourcePath.lastIndexOf('/');
		if (slashIndex == -1) {
			return resourcePath;
		}

		return resourcePath.substring(slashIndex + 1);
	}

	default File write(File folder) {
		Preconditions.checkArgument(folder.isDirectory(), folder + " does not exist");
		File outputFile = new File(folder, getResourceName());

		boolean success = IoUtils.write(IoUtils.getResource(this), outputFile);
		if (!success) {
			throw new RuntimeException("Could not write file '" + outputFile + "'");
		}

		return outputFile;
	}

}
