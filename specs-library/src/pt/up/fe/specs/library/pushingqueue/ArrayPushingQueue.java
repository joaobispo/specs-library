/*
 * Copyright 2009 Ancora Research Group.
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

package pt.up.fe.specs.library.pushingqueue;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * "Pushing Queue" of fixed size.
 *
 * <p>
 * Elements can only be added at the head of the queue. Every time an element is
 * added, every other elements gets "pushed" (its index increments by one). If
 * an element is added when the queue is full, the last element in the queue
 * gets dropped.
 *
 * TODO: remove capacity, replace with size
 * 
 * 
 * @author Joao Bispo
 */
public class ArrayPushingQueue<T> implements PushingQueue<T> {

	/**
	 * INSTANCE VARIABLES
	 */
	private ArrayList<T> queue;

	private int maxSize;

	/**
	 * Creates a PushingQueue with the specified size.
	 *
	 * @param capacity
	 *            the size of the queue
	 */
	public ArrayPushingQueue(int capacity) {
		this.maxSize = capacity;
		queue = new ArrayList<>(capacity);

	}

	/**
	 * Inserts an element at the head of the queue, pushing all other elements
	 * one position forward. If the queue is full, the last element is dropped.
	 *
	 * @param element
	 *            an element to insert in the queue
	 */
	@Override
	public void insertElement(T element) {
		// Insert element at the head
		// queue.addFirst(element);
		queue.add(0, element);

		// If size exceed capacity, remove last element
		while (queue.size() > maxSize) {
			queue.remove(queue.size() - 1);
		}

	}

	/**
	 * Returns the element at the specified position in this queue.
	 *
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this queue
	 */
	@Override
	public T getElement(int index) {
		if (index >= queue.size()) {
			return null;
		}

		return queue.get(index);
	}

	/**
	 * Returns the capacity of the queue.
	 *
	 * @return the capacity of the queue
	 */
	@Override
	public int size() {
		return maxSize;
	}

	/**
	 * 
	 * @return the number of inserted elements
	 */
	@Override
	public int currentSize() {
		return queue.size();
	}

	@Override
	public Iterator<T> iterator() {
		return queue.iterator();
	}

	@Override
	public String toString() {
		if (maxSize == 0) {
			return "[]";
		}

		StringBuilder builder = new StringBuilder();

		builder.append("[").append(getElement(0));

		for (int i = 1; i < maxSize; i++) {
			builder.append(", ").append(getElement(i));
		}
		builder.append("]");

		return builder.toString();

	}

}
