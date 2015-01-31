/*
 * Copyright 2010 Ancora Research Group.
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
package org.specs.library.PatternDetector;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;

import org.specs.library.PushingQueue.MixedPushingQueue;
import org.specs.library.PushingQueue.PushingQueue;

/**
 * Looks for patterns in integer values.
 * 
 * 
 * @author Joao Bispo
 */
public class PatternDetector {

	/**
	 * INSTANCE VARIABLES
	 */
	private int maxPatternSize;
	private BitSet[] matchQueues;
	// private PushingQueue<Integer> queue;
	private PushingQueue<Integer> queue2;
	private int currentPatternSize;
	private PatternState state;
	private boolean priorityToBiggerPatterns;

	/**
	 * Creates a new PatternFinder which will try to find patterns of maximum
	 * size 'maxPatternSize', in the given integer values.
	 * 
	 * @param maxPatternSize
	 */
	public PatternDetector(int maxPatternSize, boolean priorityToBiggerPatterns) {
		currentPatternSize = 0;
		state = PatternState.NO_PATTERN;

		this.maxPatternSize = maxPatternSize;
		this.priorityToBiggerPatterns = priorityToBiggerPatterns;
		matchQueues = new BitSet[maxPatternSize];

		// Initialize matching queues
		for (int i = 0; i < maxPatternSize; i++) {
			matchQueues[i] = new BitSet();
		}
		// queue = new PushingQueue<Integer>(maxPatternSize + 1);
		// queue2 = new PushingQueueOld<>(maxPatternSize + 1);
		queue2 = new MixedPushingQueue<>(maxPatternSize + 1);

		// Initialize Queue
		for (int i = 0; i < queue2.size(); i++) {
			queue2.insertElement(null);
		}
		// for (int i = 0; i < queue.size(); i++) {
		// queue.insertElement(null);
		// }

	}

	public int getMaxPatternSize() {
		return maxPatternSize;
	}

	/**
	 * Gives another value to check for pattern.
	 * 
	 * @param value
	 */
	public PatternState step(Integer hashValue) {
		// Insert new element
		// queue.insertElement(hashValue);
		queue2.insertElement(hashValue);

		// Compare first element with all other elements and store result on
		// match queues
		// List<Integer> elements = queue.getElements(1, maxPatternSize + 1);
		Iterator<Integer> iterator = queue2.iterator();

		// Ignore first element of the queue
		iterator.next();

		for (int i = 0; i < maxPatternSize; i++) {

			// Check if there is a match
			// if (hashValue.equals(queue.getElement(i + 1))) {
			if (hashValue.equals(iterator.next())) {
				// if (hashValue.equals(elements.get(i))) {
				// We have a match.
				// Shift match queue to the left
				matchQueues[i] = matchQueues[i].get(1, i + 1);
				// Set the bit.
				matchQueues[i].set(i);
			} else {
				// Reset queue
				matchQueues[i].clear();
			}
		}

		// Put all the results in a single bit array
		BitSet bitArray = new BitSet();
		for (int i = 0; i < matchQueues.length; i++) {
			if (matchQueues[i].get(0)) {
				bitArray.set(i);
			} else {
				bitArray.clear(i);
			}
		}

		int newPatternSize = calculatePatternSize(bitArray, currentPatternSize, priorityToBiggerPatterns);
		state = calculateState(currentPatternSize, newPatternSize);
		currentPatternSize = newPatternSize;
		return state;
	}

	public int getPatternSize() {
		return currentPatternSize;
	}

	public static int calculatePatternSize(BitSet bitArray, int previousPatternSize, boolean priorityToBiggerPatterns) {

		int firstSetSize = bitArray.nextSetBit(0) + 1;

		// Give priority to bigger patters which were previously activated
		if (!priorityToBiggerPatterns) {
			return firstSetSize;
		}

		if (previousPatternSize > firstSetSize) {
			// Check if previous pattern size is still active
			boolean previousPatternStillActive = bitArray.get(previousPatternSize - 1);
			if (previousPatternStillActive) {
				return previousPatternSize;
			}
		}

		return firstSetSize;
	}

	public PatternState getState() {
		return state;
	}

	public static PatternState calculateState(int previousPatternSize, int patternSize) {
		PatternState newState = null;
		// Check if pattern state has changed
		if (previousPatternSize != patternSize) {
			// If previous pattern size was 0, a new pattern started
			if (previousPatternSize == 0) {
				newState = PatternState.PATTERN_STARTED;
			} // If current pattern size is 0, the previous pattern has stopped.
			else if (patternSize == 0) {
				newState = PatternState.PATTERN_STOPED;
			} // The case that is left is that the previous pattern stopped, but
				// there is a new pattern with a different size.
			else {
				newState = PatternState.PATTERN_CHANGED_SIZES;
			}
		} // The size of the pattern hasn't changed
		else {
			if (patternSize > 0) {
				newState = PatternState.PATTERN_UNCHANGED;
			} else {
				newState = PatternState.NO_PATTERN;
			}
		}

		return newState;
	}

	/* (non-Javadoc)
	* @see java.lang.Object#toString()
	*/
	@Override
	public String toString() {
		return "PatternDetector [matchQueues=" + Arrays.toString(matchQueues) + ", queue=" + queue2 + ", patternSize="
				+ currentPatternSize + "]";
	}

	public enum PatternState {
		PATTERN_STOPED,
		PATTERN_STARTED,
		PATTERN_CHANGED_SIZES,
		PATTERN_UNCHANGED,
		NO_PATTERN
	}

}
