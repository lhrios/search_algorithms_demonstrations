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

import java.util.LinkedList;
import java.util.Random;

class MyInt extends BinaryHeapElement {
	/* Public: */
	public int value;

	public MyInt(int value) {
		this.value = value;
	}

	@Override
	public boolean lessThanForHeap(BinaryHeapElement e) {
		return this.value < ((MyInt) e).value;
	}
}

public class BinaryHeapTest {
	/* Public: */
	public BinaryHeapTest() {
		long seed = System.nanoTime();
		Random random = new Random(seed);
		final int SIZE = 50;
		final int MAX_PLUS_ONE = 1000;
		BinaryHeap heap = new BinaryHeap(SIZE);
		LinkedList<MyInt> v = new LinkedList<MyInt>();

		System.out.println(seed);

		for (int i = 0; i < SIZE; i++) {
			MyInt myint = new MyInt(random.nextInt(MAX_PLUS_ONE));
			heap.insert(myint);
			v.add(myint);
		}

		for (int i = 0; i < SIZE * 0.8f; i++) {
			MyInt myint = v.get(random.nextInt(v.size()));
			myint.value = random.nextInt(MAX_PLUS_ONE);
			heap.insert(myint);
		}

		for (int i = 0; i < SIZE * 0.8f; i++) {
			int a = random.nextInt(v.size());
			MyInt myint = v.get(a);
			v.remove(a);
			heap.delete(myint);
		}

		{
			int smallest = 0;
			while (heap.size() > 0) {
				MyInt myint = (MyInt) heap.pop();
				if (myint.value < smallest) {
					System.out.println("Falha!");
					return;
				}
				smallest = myint.value;
				System.out.println(myint.value);
			}
		}
	}
}