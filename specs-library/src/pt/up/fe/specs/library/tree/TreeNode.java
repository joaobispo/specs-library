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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public interface TreeNode<K extends TreeNode<K>> {

	default Iterator<K> iterator() {
		return getChildren().iterator();
	}

	default boolean hasChildren() {
		if (getChildren().isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the child token at the specified position.
	 * 
	 * @param index
	 * @return
	 */
	default K getChild(int index) {
		if (!hasChildren()) {
			return null;
		}

		return getChildren().get(index);
	}

	default Stream<K> getChildrenStream() {
		return getChildren().stream();
	}

	/**
	 * 
	 * @param index1
	 * @param index2
	 * @param indexes
	 * @return the child after travelling the given indexes
	 */

	default K getChild(int index1, int index2, int... indexes) {
		K currentChild = getChild(index1);
		currentChild = currentChild.getChild(index2);
		for (int i = 0; i < indexes.length; i++) {
			currentChild = currentChild.getChild(indexes[i]);
		}

		return currentChild;
	}

	/**
	 * Returns an unmodifiable view of the children of the token.
	 * 
	 * <p>
	 * To modify the children of the token use methods such as addChild() or
	 * removeChild().
	 * 
	 * @return the children
	 */
	List<K> getChildren();

	/**
	 * @param children
	 *            the children to set
	 */
	void setChildren(Collection<K> children);

	/**
	 * @return
	 */
	default int numChildren() {
		if (!hasChildren()) {
			return 0;
		}

		return getChildren().size();
	}

	/**
	 * Removes the child at the specified position.
	 * 
	 * <p>
	 * Puts the parent of the child as null.
	 * 
	 * TODO: should remove all it's children recursively?
	 * 
	 * @param index
	 * @return
	 */
	K removeChild(int index);

	/**
	 * Replaces the token at the specified position in this list with the
	 * specified token.
	 * 
	 * @param index
	 * @param token
	 */
	K setChild(int index, K token);

	/**
	 * 
	 * @param child
	 * @return
	 */
	boolean addChild(K child);

	/**
	 * 
	 * 
	 * @param index
	 * @param child
	 * @return
	 */
	void addChild(int index, K child);

	default boolean addChildren(List<K> children) {
		boolean changed = false;
		for (K child : children) {
			addChild(child);
			changed = true;
		}

		return changed;
	}

	/**
	 * Returns a deep copy of the current token.
	 * 
	 * TODO: This should be abstract; Remove return empty instance
	 * 
	 * @return
	 */
	K copy();

	/**
	 * @return the parent of this node
	 */
	public K getParent();

	/**
	 * 
	 * @return the uppermost parent of this node
	 */
	public K getRoot();

	default boolean hasParent() {
		return getParent() != null;
	}

	/**
	 * @return the index of this token in its parent token, or -1 if it does not
	 *         have a parent
	 */
	default int indexOfSelf() {
		if (!hasParent()) {
			return -1;
		}

		return getParent().getChildren().indexOf(this);
	}

	/**
	 * 
	 * @param nodeClass
	 * @return the index of the first child that is an instance of the given
	 *         class, or -1 if none is found
	 */
	default int getChildIndex(Class<? extends K> nodeClass) {
		for (int i = 0; i < numChildren(); i++) {
			if (nodeClass.isInstance(getChild(i))) {
				return i;
			}
		}

		return -1;
	}

	default <T extends K> T getChild(Class<T> nodeClass, int index) {
		K childNode = getChild(index);
		if (!nodeClass.isInstance(childNode)) {
			throw new RuntimeException("Wanted a '" + nodeClass.getSimpleName() + "' at index '" + index
					+ "', but is was a '" + childNode.getClass().getSimpleName() + "':\n" + this);
		}

		return nodeClass.cast(childNode);
	}

	/**
	 * 
	 * @param index1
	 * @param index2
	 * @param indexes
	 * @return the child after travelling the given indexes
	 */
	/*
	default <T extends K> K getChild(List<Class<T>> nodeClasses, int index1, int index2, int... indexes) {
	K currentChild = getChild(nodeClasses.get(0), index1);
	currentChild = currentChild.getChild(nodeClasses.get(1), index2);
	for (int i = 0; i < indexes.length; i++) {
	    currentChild = currentChild.getChild(nodeClasses.get(i + 2), indexes[i]);
	}

	return currentChild;
	}
	*/
	/**
	 * TODO: Move to AbstractClass, include parent
	 * 
	 * @param node
	 * @return
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	/*
	static <E extends Enum<E>, K extends TreeNode<E, K>> int hashCodeHelper(TreeNode<E, K> node) {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((node.getChildren() == null) ? 0 : node.getChildren().hashCode());
	result = prime * result + ((node.getContent() == null) ? 0 : node.getContent().hashCode());
	result = prime * result + ((node.getType() == null) ? 0 : node.getType().hashCode());
	return result;
	}
	*/

	/**
	 * TODO: Move to AbstractClass, include parent
	 * 
	 * @param node
	 * @param obj
	 * @return
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/*
	static <E extends Enum<E>, K extends TreeNode<E, K>> boolean equalsHelper(TreeNode<E, K> node, Object obj) {
	if (node == obj)
	    return true;
	if (obj == null)
	    return false;
	if (node.getClass() != obj.getClass())
	    return false;
	Token<?, ?> other = (Token<?, ?>) obj;
	if (node.getChildren() == null) {
	    if (other.children != null)
		return false;
	} else if (!node.getChildren().equals(other.children))
	    return false;
	if (node.getContent() == null) {
	    if (other.getContent() != null)
		return false;
	} else if (!node.getContent().equals(other.getContent()))
	    return false;
	if (node.getType() == null) {
	    if (other.getType() != null)
		return false;
	} else if (!node.getType().equals(other.getType()))
	    return false;
	return true;
	}
	*/
}