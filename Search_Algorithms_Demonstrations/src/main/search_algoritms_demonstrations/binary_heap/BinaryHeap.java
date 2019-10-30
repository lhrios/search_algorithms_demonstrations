/*
 * Copyright 2010, 2013, 2014 Luis Henrique Oliveira Rios
 *
 * This file is part of Search Algorithms Demonstrations.
 *
 * Search Algorithms Demonstrations is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Search Algorithms Demonstrations is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Search Algorithms Demonstrations.  If not, see <http://www.gnu.org/licenses/>.
 */

package search_algoritms_demonstrations.binary_heap;

import java.util.ArrayList;
import java.util.List;

public class BinaryHeap {
	/* Public: */

	public BinaryHeap(int max_size) {
		this.heap = new BinaryHeapElement[max_size];
		this.next_position = 0;
		this.max_size = max_size;
	}

	public void clear() {
		for (int i = 0; i < this.next_position; i++)
			this.heap[i].binary_heap_index = 0;
		this.next_position = 0;
	}

	public void delete(BinaryHeapElement e) {
		if (!this.has(e))
			throw new BinaryHeapException("The element could not be deleted because it is not in the heap.");
		int position = (0x7FFFFFFF & e.binary_heap_index);

		e.binary_heap_index = 0;
		if (--this.next_position != position) {
			this.heap[position] = this.heap[this.next_position];
			this.heap[position].binary_heap_index = 0x80000000 | position;
			if (position > 0 && this.heap[position].lessThanForHeap(this.heap[(position - 1) / 2]))
				this.heapifyUp(position);
			else
				this.heapifyDown(position);
		}
	}

	public void insert(BinaryHeapElement e) {
		if (this.has(e)) {
			int position = (0x7FFFFFFF & e.binary_heap_index);
			assert (position < this.next_position);
			if (position > 0 && e.lessThanForHeap(this.heap[(position - 1) / 2]))
				this.heapifyUp(position);
			else
				this.heapifyDown(position);
		} else {
			if (this.next_position == this.max_size)
				throw new BinaryHeapException("The Binary Heap has exceeded the available space.");
			e.binary_heap_index = 0x80000000 | this.next_position;
			this.heap[this.next_position] = e;

			this.heapifyUp(this.next_position++);
		}
	}

	public boolean isValidHeap() {
		int i = 0, j;
		j = 2 * i + 1;
		while (j < this.next_position) {
			if (this.heap[j].lessThanForHeap(this.heap[i]))
				return false;
			if (++j < this.next_position && this.heap[j].lessThanForHeap(this.heap[i]))
				return false;
			i++;
			j = 2 * i + 1;
		}
		return true;
	}

	public BinaryHeapElement getElement(int index) {
		if (index >= this.next_position)
			throw new BinaryHeapException("There is no element with this index.");
		return this.heap[index];
	}

	public boolean has(BinaryHeapElement e) {
		return (e.binary_heap_index & 0x80000000) != 0;
	}

	public BinaryHeapElement peek() {
		if (this.next_position == 0)
			throw new BinaryHeapException("The Binary Heap is empty.");
		return this.heap[0];
	}

	public BinaryHeapElement pop() {
		if (this.next_position == 0)
			throw new BinaryHeapException("The Binary Heap is empty.");
		BinaryHeapElement min = this.heap[0];
		assert ((0x7FFFFFFF & this.heap[0].binary_heap_index) == 0);
		this.heap[0] = this.heap[--this.next_position];
		this.heapifyDown(0);
		min.binary_heap_index = 0;
		return min;
	}

	public int size() {
		return this.next_position;
	}

	public List<BinaryHeapElement> asSortedList() {
		int oldSize = this.size();
		List<BinaryHeapElement> list = new ArrayList<BinaryHeapElement>();

		BinaryHeapElement[] oldHeap = new BinaryHeapElement[oldSize];
		System.arraycopy(this.heap, 0, oldHeap, 0, oldSize);

		while (this.size() > 0) {
			list.add(this.pop());
		}

		/* Now, restore the heap state. */
		System.arraycopy(oldHeap, 0, this.heap, 0, oldSize);
		for (int i = 0; i < oldSize; i++) {
			this.heap[i].binary_heap_index = 0x80000000 | i;
		}
		this.next_position = oldSize;

		return list;
	}

	/* Private: */
	private BinaryHeapElement heap[];
	private int max_size, next_position;

	private void heapifyDown(int position) {
		int i, j;
		BinaryHeapElement aux;

		i = position;
		j = i * 2 + 1;
		aux = this.heap[i];
		while (j < this.next_position) {
			if (j + 1 < this.next_position && this.heap[j + 1].lessThanForHeap(this.heap[j]))
				j++;
			if (!this.heap[j].lessThanForHeap(aux))
				break;
			this.heap[i] = this.heap[j];
			this.heap[i].binary_heap_index = 0x80000000 | i;
			i = j;
			j = i * 2 + 1;
		}
		this.heap[i] = aux;
		this.heap[i].binary_heap_index = 0x80000000 | i;
	}

	private void heapifyUp(int position) {
		int i = position, j;
		BinaryHeapElement aux;

		j = (i - 1) / 2;
		while (i > 0 && this.heap[i].lessThanForHeap(this.heap[j])) {
			aux = this.heap[i];
			this.heap[i] = this.heap[j];
			this.heap[j] = aux;

			this.heap[i].binary_heap_index = 0x80000000 | i;
			this.heap[j].binary_heap_index = 0x80000000 | j;

			i = j;
			j = (i - 1) / 2;
		}
	}

}
