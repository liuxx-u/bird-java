package com.bird.core.utils;

import com.bird.core.Check;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by liuxx on 2017/6/7.
 */
public class CollectionWrapper<T> {
    private InternalCollection<T> collection;

    private CollectionWrapper(Collection<T> items) {

        this.collection = new InternalCollection<>(items);
    }

    public static <S> CollectionWrapper<S> init(Collection<S> items) {

        return new CollectionWrapper(items);
    }

    public CollectionWrapper<T> where(Predicate<? super T> filter) {
        Check.NotNull(filter, "filter");

        InternalCollection<T> result = new InternalCollection<>(new ArrayList());

        final Iterator<T> each = this.collection.iterator();
        while (each.hasNext()) {
            T item = each.next();
            if (filter.test(item)) {
                result.add(item);
            }
        }
        return CollectionWrapper.init(result);
    }

    public <R> CollectionWrapper<R> select(Function<T, R> func) {
        Check.NotNull(func, "func");

        InternalCollection<R> result = new InternalCollection<>(new ArrayList());
        final Iterator<T> each = this.collection.iterator();
        while (each.hasNext()) {
            result.add(func.apply(each.next()));
        }

        return CollectionWrapper.init(result);
    }

    public T first(Predicate<? super T> filter) {
        if (filter == null) return null;

        int size = this.collection.size();
        if (size == 0) return null;

        final Iterator<T> each = this.collection.iterator();
        while (each.hasNext()) {
            T item = each.next();
            if (filter.test(item)) {
                return item;
            }
        }
        return null;
    }

    public T last(Predicate<? super T> filter) {
        if (filter == null) return null;

        int size = this.collection.size();
        if (size == 0) return null;

        List list = this.collection.toList();
        for (int i = size - 1; i >= 0; i--) {
            T item = (T) list.get(i);
            if (filter.test(item)) {
                return item;
            }
        }
        return null;
    }

    public List<T> toList() {
        return collection.toList();
    }

    public T[] toArray(){
        return (T[])collection.toArray();
    }

    /**
     * 静态内部类
     *
     * 所有集合相关的链式操作都是对该内部集合的操作
     */
    private static class InternalCollection<E> implements Collection<E> {
        private Collection<E> items;

        public InternalCollection(Collection items) {
            this.items = items;
        }

        public List<E> toList() {
            return new ArrayList<>(items);
        }



        /**
         * Returns the number of elements in this collection.  If this collection
         * contains more than {@code Integer.MAX_VALUE} elements, returns
         * {@code Integer.MAX_VALUE}.
         *
         * @return the number of elements in this collection
         */
        @Override
        public int size() {
            return this.items.size();
        }

        /**
         * Returns {@code true} if this collection contains no elements.
         *
         * @return {@code true} if this collection contains no elements
         */
        @Override
        public boolean isEmpty() {
            return this.items.isEmpty();
        }

        /**
         * Returns {@code true} if this collection contains the specified element.
         * More formally, returns {@code true} if and only if this collection
         * contains at least one element {@code e} such that
         * {@code Objects.equals(o, e)}.
         *
         * @param o element whose presence in this collection is to be tested
         * @return {@code true} if this collection contains the specified
         * element
         * @throws ClassCastException   if the type of the specified element
         *                              is incompatible with this collection
         *                              (<a href="#optional-restrictions">optional</a>)
         * @throws NullPointerException if the specified element is null and this
         *                              collection does not permit null elements
         *                              (<a href="#optional-restrictions">optional</a>)
         */
        @Override
        public boolean contains(Object o) {
            return this.items.contains(o);
        }

        /**
         * Returns an iterator over the elements in this collection.  There are no
         * guarantees concerning the order in which the elements are returned
         * (unless this collection is an instance of some class that provides a
         * guarantee).
         *
         * @return an {@code Iterator} over the elements in this collection
         */
        @Override
        public Iterator<E> iterator() {
            return this.items.iterator();
        }

        /**
         * Returns an array containing all of the elements in this collection.
         * If this collection makes any guarantees as to what order its elements
         * are returned by its iterator, this method must return the elements in
         * the same order.
         * <p>
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this collection.  (In other words, this method must
         * allocate a new array even if this collection is backed by an array).
         * The caller is thus free to modify the returned array.
         * <p>
         * <p>This method acts as bridge between array-based and collection-based
         * APIs.
         *
         * @return an array containing all of the elements in this collection
         */
        @Override
        public Object[] toArray() {
            return this.items.toArray();
        }

        /**
         * Returns an array containing all of the elements in this collection;
         * the runtime type of the returned array is that of the specified array.
         * If the collection fits in the specified array, it is returned therein.
         * Otherwise, a new array is allocated with the runtime type of the
         * specified array and the size of this collection.
         * <p>
         * <p>If this collection fits in the specified array with room to spare
         * (i.e., the array has more elements than this collection), the element
         * in the array immediately following the end of the collection is set to
         * {@code null}.  (This is useful in determining the length of this
         * collection <i>only</i> if the caller knows that this collection does
         * not contain any {@code null} elements.)
         * <p>
         * <p>If this collection makes any guarantees as to what order its elements
         * are returned by its iterator, this method must return the elements in
         * the same order.
         * <p>
         * <p>Like the {@link #toArray()} method, this method acts as bridge between
         * array-based and collection-based APIs.  Further, this method allows
         * precise control over the runtime type of the output array, and may,
         * under certain circumstances, be used to save allocation costs.
         * <p>
         * <p>Suppose {@code x} is a collection known to contain only strings.
         * The following code can be used to dump the collection into a newly
         * allocated array of {@code String}:
         * <p>
         * <pre>
         *     String[] y = x.toArray(new String[0]);</pre>
         * <p>
         * Note that {@code toArray(new Object[0])} is identical in function to
         * {@code toArray()}.
         *
         * @param a the array into which the elements of this collection are to be
         *          stored, if it is big enough; otherwise, a new array of the same
         *          runtime type is allocated for this purpose.
         * @return an array containing all of the elements in this collection
         * @throws ArrayStoreException  if the runtime type of the specified array
         *                              is not a supertype of the runtime type of every element in
         *                              this collection
         * @throws NullPointerException if the specified array is null
         */
        @Override
        public <T> T[] toArray(T[] a) {
            return this.items.toArray(a);
        }

        /**
         * Ensures that this collection contains the specified element (optional
         * operation).  Returns {@code true} if this collection changed as a
         * result of the call.  (Returns {@code false} if this collection does
         * not permit duplicates and already contains the specified element.)<p>
         * <p>
         * Collections that support this operation may place limitations on what
         * elements may be added to this collection.  In particular, some
         * collections will refuse to add {@code null} elements, and others will
         * impose restrictions on the type of elements that may be added.
         * Collection classes should clearly specify in their documentation any
         * restrictions on what elements may be added.<p>
         * <p>
         * If a collection refuses to add a particular element for any reason
         * other than that it already contains the element, it <i>must</i> throw
         * an exception (rather than returning {@code false}).  This preserves
         * the invariant that a collection always contains the specified element
         * after this call returns.
         *
         * @param e element whose presence in this collection is to be ensured
         * @return {@code true} if this collection changed as a result of the
         * call
         * @throws UnsupportedOperationException if the {@code add} operation
         *                                       is not supported by this collection
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this collection
         * @throws NullPointerException          if the specified element is null and this
         *                                       collection does not permit null elements
         * @throws IllegalArgumentException      if some property of the element
         *                                       prevents it from being added to this collection
         * @throws IllegalStateException         if the element cannot be added at this
         *                                       time due to insertion restrictions
         */
        @Override
        public boolean add(E e) {
            return this.items.add(e);
        }

        /**
         * Removes a single instance of the specified element from this
         * collection, if it is present (optional operation).  More formally,
         * removes an element {@code e} such that
         * {@code Objects.equals(o, e)}, if
         * this collection contains one or more such elements.  Returns
         * {@code true} if this collection contained the specified element (or
         * equivalently, if this collection changed as a result of the call).
         *
         * @param o element to be removed from this collection, if present
         * @return {@code true} if an element was removed as a result of this call
         * @throws ClassCastException            if the type of the specified element
         *                                       is incompatible with this collection
         *                                       (<a href="#optional-restrictions">optional</a>)
         * @throws NullPointerException          if the specified element is null and this
         *                                       collection does not permit null elements
         *                                       (<a href="#optional-restrictions">optional</a>)
         * @throws UnsupportedOperationException if the {@code remove} operation
         *                                       is not supported by this collection
         */
        @Override
        public boolean remove(Object o) {
            return this.items.remove(o);
        }

        /**
         * Returns {@code true} if this collection contains all of the elements
         * in the specified collection.
         *
         * @param c collection to be checked for containment in this collection
         * @return {@code true} if this collection contains all of the elements
         * in the specified collection
         * @throws ClassCastException   if the types of one or more elements
         *                              in the specified collection are incompatible with this
         *                              collection
         *                              (<a href="#optional-restrictions">optional</a>)
         * @throws NullPointerException if the specified collection contains one
         *                              or more null elements and this collection does not permit null
         *                              elements
         *                              (<a href="#optional-restrictions">optional</a>),
         *                              or if the specified collection is null.
         * @see #contains(Object)
         */
        @Override
        public boolean containsAll(Collection<?> c) {
            return this.items.containsAll(c);
        }

        /**
         * Adds all of the elements in the specified collection to this collection
         * (optional operation).  The behavior of this operation is undefined if
         * the specified collection is modified while the operation is in progress.
         * (This implies that the behavior of this call is undefined if the
         * specified collection is this collection, and this collection is
         * nonempty.)
         *
         * @param c collection containing elements to be added to this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code addAll} operation
         *                                       is not supported by this collection
         * @throws ClassCastException            if the class of an element of the specified
         *                                       collection prevents it from being added to this collection
         * @throws NullPointerException          if the specified collection contains a
         *                                       null element and this collection does not permit null elements,
         *                                       or if the specified collection is null
         * @throws IllegalArgumentException      if some property of an element of the
         *                                       specified collection prevents it from being added to this
         *                                       collection
         * @throws IllegalStateException         if not all the elements can be added at
         *                                       this time due to insertion restrictions
         * @see #add(Object)
         */
        @Override
        public boolean addAll(Collection<? extends E> c) {
            return this.items.addAll(c);
        }

        /**
         * Removes all of this collection's elements that are also contained in the
         * specified collection (optional operation).  After this call returns,
         * this collection will contain no elements in common with the specified
         * collection.
         *
         * @param c collection containing elements to be removed from this collection
         * @return {@code true} if this collection changed as a result of the
         * call
         * @throws UnsupportedOperationException if the {@code removeAll} method
         *                                       is not supported by this collection
         * @throws ClassCastException            if the types of one or more elements
         *                                       in this collection are incompatible with the specified
         *                                       collection
         *                                       (<a href="#optional-restrictions">optional</a>)
         * @throws NullPointerException          if this collection contains one or more
         *                                       null elements and the specified collection does not support
         *                                       null elements
         *                                       (<a href="#optional-restrictions">optional</a>),
         *                                       or if the specified collection is null
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            return this.retainAll(c);
        }

        /**
         * Retains only the elements in this collection that are contained in the
         * specified collection (optional operation).  In other words, removes from
         * this collection all of its elements that are not contained in the
         * specified collection.
         *
         * @param c collection containing elements to be retained in this collection
         * @return {@code true} if this collection changed as a result of the call
         * @throws UnsupportedOperationException if the {@code retainAll} operation
         *                                       is not supported by this collection
         * @throws ClassCastException            if the types of one or more elements
         *                                       in this collection are incompatible with the specified
         *                                       collection
         *                                       (<a href="#optional-restrictions">optional</a>)
         * @throws NullPointerException          if this collection contains one or more
         *                                       null elements and the specified collection does not permit null
         *                                       elements
         *                                       (<a href="#optional-restrictions">optional</a>),
         *                                       or if the specified collection is null
         * @see #remove(Object)
         * @see #contains(Object)
         */
        @Override
        public boolean retainAll(Collection<?> c) {
            return this.items.retainAll(c);
        }

        /**
         * Removes all of the elements from this collection (optional operation).
         * The collection will be empty after this method returns.
         *
         * @throws UnsupportedOperationException if the {@code clear} operation
         *                                       is not supported by this collection
         */
        @Override
        public void clear() {
            this.items.clear();
        }
    }
}
