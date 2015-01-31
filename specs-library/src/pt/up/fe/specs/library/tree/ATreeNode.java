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

package pt.up.fe.specs.library.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Joao Bispo
 * 
 */
public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

	private final List<K> children;
	protected K parent;

	public ATreeNode(Collection<? extends K> children) {
		this.children = new LinkedList<>();

		// Safety if given list is null

		/*if (children == null) {
			children = Collections.emptyList();
		}
		*/

		// Add children
		for (K child : children) {
			addChild(child);
		}

		parent = null;

	}

	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#getChildren()
	 */
	@Override
	public List<K> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * This method is to be used for the ListIterator.
	 * 
	 * @return a mutable view of the children
	 */
	List<K> getChildrenMutable() {
		return children;
	}

	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#removeChild(int)
	 */
	@Override
	public K removeChild(int index) {
		if (!hasChildren()) {
			throw new RuntimeException("Token does not have children, cannot remove a child.");
		}

		K child = children.remove(index);

		// Unlink child from this node
		child.removeParent();

		return child;

	}

	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#setChild(int, K)
	 */
	@Override
	public K setChild(int index, K token) {
		K sanitizedToken = TreeNodeUtils.sanitizeNode(token);
		setAsParentOf(sanitizedToken);

		if (!hasChildren()) {
			throw new RuntimeException("Token does not have children, cannot set a child.");
		}

		// Insert child
		K previousChild = children.set(index, sanitizedToken);

		// Remove the previous child from the tree
		if (previousChild != null) {
			previousChild.removeParent();
		}

		return previousChild;
	}

	/**
	 * Sets the parent of the given node to the current node. If the given node
	 * already has a parent, throws an exception.
	 * 
	 * @param childToken
	 */
	protected void setAsParentOf(K childToken) {

		if (childToken.getParent() != null) {
			throw new RuntimeException("Parent should be null.");
		}

		childToken.parent = getThis();
	}

	protected void removeParent() {
		if (this.parent == null) {
			throw new RuntimeException("Should have a parent.");
		}

		this.parent = null;
	}

	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#addChild(K)
	 */
	@Override
	public boolean addChild(K child) {
		K sanitizedChild = TreeNodeUtils.sanitizeNode(child);
		setAsParentOf(sanitizedChild);

		boolean changed = children.add(sanitizedChild);

		return changed;
	}

	/*
	@Override
	public boolean addChildren(List<K> children) {
		// If the same list (reference) create a copy, to avoid problems when
		// adding the list to itself
		if (children == this.children) {
			Log.warn("Adding the list to itself");
			children = new ArrayList<>(children);
		}

		boolean changed = false;

		for (K child : children) {
			changed = addChild(child);
		}

		return changed;
	}
	*/

	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#addChild(int, K)
	 */
	@Override
	public void addChild(int index, K child) {
		// K sanitizedToken = sanitizeToken(child);
		K sanitizedToken = TreeNodeUtils.sanitizeNode(child);
		setAsParentOf(sanitizedToken);

		/*
		if (children == null) {
		    children = FactoryUtils.newLinkedList();
		}
		*/

		// Insert child
		children.add(index, sanitizedToken);
	}

	/**
	 * Returns a new copy of the node with the same content and type, but not
	 * children.
	 * 
	 * @return
	 */
	protected abstract K copyPrivate();

	/**
	 * Creates a deep copy of the node, including children. No guarantees are
	 * made regarding the contents, they can be the same object as in the
	 * original node, and if mutable, changing the content in one node might be
	 * reflected in the copy.
	 */
	/* (non-Javadoc)
	 * @see org.suikasoft.SharedLibrary.TreeToken.TreeToken#copy()
	 */
	@Override
	public K copy() {
		K newToken = copyPrivate();

		// Check new token does not have children
		if (newToken.numChildren() != 0) {
			throw new RuntimeException("Node '" + newToken.getClass().getSimpleName()
					+ "' still has children after copyPrivate(), check implementation");
		}

		for (K child : getChildren()) {
			// Copy children of token
			K newChildToken = child.copy();

			newToken.addChild(newChildToken);
		}

		return newToken;
	}

	/**
	 * Returns a reference to the object that implements this class.
	 * 
	 * <p>
	 * This method is needed because of generics not having information about K.
	 * 
	 * @return
	 */
	protected abstract K getThis();

	/**
	 * @return the parent of this node
	 */
	@Override
	public K getParent() {
		return parent;
	}

	/**
	 * 
	 * @return the uppermost parent of this node
	 */
	@Override
	public K getRoot() {
		// Get parent
		K parent = getParent();

		// If it has no parents, return self
		if (parent == null) {
			// return (K) this;
			return getThis();
		}

		// Recursively call the funcion on the parent
		return parent.getRoot();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ATreeNode))
			return false;
		ATreeNode<?> other = (ATreeNode<?>) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getThis(), "");
	}

	/**
	 * A string representation of the node.
	 * 
	 * <p>
	 * As default, returns the name of the class.
	 * 
	 * @return
	 */
	@Override
	public String toNodeString() {
		return getThis().getClass().getSimpleName();
	}

	/**
	 * 
	 * <p>
	 * TODO: Can be moved to TreeNode?
	 * 
	 * @return a ListIterator over the children of the node. The iterator
	 *         supports methods that modify the node (set, remove, insert...)
	 */
	@Override
	public ListIterator<K> getChildrenIterator() {
		return new ChildrenIterator<>(this);
	}

}
