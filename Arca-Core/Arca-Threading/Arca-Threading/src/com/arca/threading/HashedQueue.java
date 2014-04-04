package com.arca.threading;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

public class HashedQueue<T> implements Queue<T> {
	private Node<T> mHead = null;
	private Node<T> mTail = null;
	private Map<T, Node<T>> mNodeMap = new HashMap<T, Node<T>>();

	private static class Node<T> {
		T data;
		private Node<T> next = null;
		private Node<T> previous = null;

		Node(final T data) {
			this.data = data;
		}

		public Node<T> getNext() {
			return next;
		}

		public void setNext(final Node<T> next) {
			this.next = next;
		}

		public Node<T> getPrevious() {
			return previous;
		}

		public void setPrevious(final Node<T> previous) {
			this.previous = previous;
		}
	}

	@Override
	public synchronized boolean addAll(final Collection<? extends T> arg0) {
		for (final T data : arg0) {
			add(data);
		}
		return true;
	}

	@Override
	public synchronized void clear() {
		mHead = null;
		mTail = null;
		mNodeMap = new HashMap<T, Node<T>>();
	}

	@Override
	public synchronized boolean contains(final Object object) {
		return mNodeMap.containsKey(object);
	}

	@Override
	public synchronized boolean containsAll(final Collection<?> arg0) {
		for (final Object object : arg0) {
			if (!mNodeMap.containsKey(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized boolean isEmpty() {
		return mHead == null;
	}

	@Override
	public synchronized Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> current = mHead;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				if (current == null) {
					throw new NoSuchElementException("No more elements inside of the iterator.");
				}

				final T data = current.data;
				current = current.getNext();
				return data;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public synchronized boolean remove(final Object object) {
		final Node<T> node = mNodeMap.remove(object);
		if (node == null) {
			return false;
		}

		Node<T> previous, next;
		previous = node.getPrevious();
		next = node.getNext();

		if (previous != null) {
			previous.setNext(next);
		}
		if (next != null) {
			next.setPrevious(previous);
		}

		if (previous == null) {
			mHead = next;
		}

		if (next == null) {
			mTail = previous;
		}

		return true;
	}

	@Override
	public synchronized boolean removeAll(final Collection<?> arg0) {
		return false;
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		return false;
	}

	@Override
	public synchronized int size() {
		return mNodeMap.size();
	}

	@Override
	public synchronized Object[] toArray() {
		final Object[] dataArray = new Object[size()];
		Node<T> temp = mHead;
		final int i = 0;
		while (temp != null) {
			dataArray[i] = temp;
			temp = temp.getNext();
		}
		return dataArray;
	}

	@Override
	@SuppressWarnings("hiding")
	public <T> T[] toArray(final T[] array) {
		return null;
	}

	@Override
	public synchronized boolean add(final T e) {
		Node<T> node;
		if (mNodeMap.containsKey(e)) {
			node = mNodeMap.get(e);

			/*
			 * TODO: This "remove" call is not high performance in this case. We
			 * want the node removed from the queue but not the map.
			 */
			remove(e);

			node.next = null;
			node.previous = null;
		} else {
			node = new Node<T>(e);
		}

		if (mTail == null) {
			mHead = node;
			mTail = node;
		} else {
			mTail.setNext(node);
			node.setPrevious(mTail);
			mTail = node;
		}
		mNodeMap.put(e, node);
		return true;
	}

	@Override
	public T element() {
		return null;
	}

	@Override
	public synchronized boolean offer(final T e) {
		add(e);
		return true;
	}

	@Override
	public synchronized T peek() {
		return mHead == null ? null : mHead.data;
	}

	@Override
	public synchronized T poll() {
		T data = null;
		if (mHead != null) {
			mNodeMap.remove(mHead.data);

			data = mHead.data;
			mHead = mHead.next;
			if (mHead != null) {
				mHead.setPrevious(null);
			} else {
				mTail = null;
			}
		}
		return data;
	}

	@Override
	public synchronized T remove() {
		if (mHead == null) {
			throw new NoSuchElementException();
		}

		return poll();
	}

	public synchronized void bump(final T e) {
		remove(e);
		add(e);
	}
}
