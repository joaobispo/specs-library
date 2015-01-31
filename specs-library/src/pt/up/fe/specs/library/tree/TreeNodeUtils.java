/**
 * Copyright 2014 SuikaSoft.
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

package pt.up.fe.specs.library.tree;

public class TreeNodeUtils {

	/**
	 * Ensures that the token has a null parent.
	 * 
	 * @param token
	 * @return the given token if it does not have a parent, or a copy of the
	 *         token if it has (a copy of a token does not have a parent)
	 */
	static <K extends TreeNode<K>> K sanitizeNode(K token) {
		if (!token.hasParent()) {
			return token;
		}

		// Copy token
		K tokenCopy = token.copy();
		return tokenCopy;
	}

	/*
	public static <K extends TreeNode<E, K>, E> String toString(K token, String prefix) {
		StringBuilder builder = new StringBuilder();

		builder.append(prefix).append(token.getType());
		if (token.getContent() != null) {
			builder.append(": ").append(token.getContent());
		}
		builder.append("\n");

		if (token.hasChildren()) {
			for (K child : token.getChildren()) {
				builder.append(toString(child, prefix + "  "));
			}
		}

		return builder.toString();
	}
	*/
}
