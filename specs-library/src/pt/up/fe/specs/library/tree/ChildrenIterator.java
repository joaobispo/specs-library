/**
 * Copyright 2015 SuikaSoft.
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

import java.util.ListIterator;

public class ChildrenIterator<N extends ATreeNode<N>> implements ListIterator<N> {

	private final ATreeNode<N> parent;
	private final ListIterator<N> iterator;
	private N lastReturned;

	public ChildrenIterator(ATreeNode<N> parent) {

		this.parent = parent;
		this.iterator = parent.getChildrenMutable().listIterator();

		lastReturned = null;
	}

	protected ListIterator<N> getIterator() {
		return iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public N next() {
		lastReturned = iterator.next();
		return lastReturned;
	}

	@Override
	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}

	@Override
	public N previous() {
		lastReturned = iterator.previous();
		return lastReturned;
	}

	@Override
	public int nextIndex() {
		return iterator.nextIndex();
	}

	@Override
	public int previousIndex() {
		return iterator.previousIndex();
	}

	@Override
	public void remove() {
		// Remove from list
		iterator.remove();

		// Unlink child from this node
		lastReturned.removeParent();

		// Reset last returned
		lastReturned = null;
	}

	@Override
	public void set(N e) {
		// Sanitize input node
		N sanitizedToken = TreeNodeUtils.sanitizeNode(e);

		// Set parent
		parent.setAsParentOf(sanitizedToken);

		// Insert child
		iterator.set(sanitizedToken);

		// Remove the previous child from the tree
		lastReturned.removeParent();

		// Update last returned node
		lastReturned = sanitizedToken;
	}

	@Override
	public void add(N e) {
		// Sanitize input node
		N sanitizedToken = TreeNodeUtils.sanitizeNode(e);

		// Set parent
		parent.setAsParentOf(sanitizedToken);

		// Add node
		iterator.add(sanitizedToken);

		// Reset last returned
		lastReturned = null;
	}

	/**
	 * Moves the cursor back the given amount of places.
	 * 
	 * <p>
	 * If the given amount is bigger than the number of positions, stops when
	 * the cursor is at the beginning.
	 * 
	 * @param amount
	 */
	public void back(int amount) {
		for (int i = 0; i < amount; i++) {
			if (!iterator.hasPrevious()) {
				return;
			}
			iterator.previous();
		}

	}
}
