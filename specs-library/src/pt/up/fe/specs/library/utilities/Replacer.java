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

package pt.up.fe.specs.library.utilities;

import pt.up.fe.specs.library.IoUtils;
import pt.up.fe.specs.library.interfaces.ResourceProvider;

public class Replacer {

	private String currentString;

	public Replacer(String string) {
		this.currentString = string;
	}

	public Replacer(ResourceProvider resource) {
		this(IoUtils.getResource(resource));
	}

	public void replaceGeneric(CharSequence target, Object replacement) {
		replace(target, replacement.toString());
	}

	public void replace(String target, int replacement) {
		replace(target, Integer.toString(replacement));
	}

	public Replacer replace(CharSequence target, CharSequence replacement) {
		currentString = currentString.replace(target, replacement);

		return this;
	}

	@Override
	public String toString() {
		return currentString;
	}

}
