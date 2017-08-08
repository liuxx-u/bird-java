package com.cczcrv.core.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by liuxx on 2017/6/7.
 */
public class CollectionHelper<T> {
    private Collection<T> items;

    private CollectionHelper(Collection<T> items) {
        this.items = items;
    }

    public static <S> CollectionHelper<S> init(Collection<S> items) {
        return new CollectionHelper(items);
    }

    public CollectionHelper<T> where(Predicate<? super T> filter) {
        Objects.requireNonNull(filter);

        List list = new ArrayList(items);
        final Iterator<T> each = list.iterator();
        while (each.hasNext()) {
            T item = each.next();
            if (!filter.test(item)) {
                each.remove();
            }
        }
        this.items = list;
        return this;
    }

    public <R> Collection<R> select(Function<T,R> func){
        ArrayList<R> result=new ArrayList<>();
        List list = new ArrayList(items);
        final Iterator<T> each = list.iterator();
        while (each.hasNext()) {
            result.add(func.apply(each.next()));
        }
        return result;
    }

    public T firstOrDefault(Predicate<? super T> filter){
        int size = this.items.size();
        if(size==0)return null;
        List list=new ArrayList(items);
        if(filter==null)return (T)list.get(0);

        for (int i = 0; i <= size-1; i++) {
            T item = (T) list.get(i);
            if (filter.test(item)) {
                return item;
            }
        }
        return null;
    }

    public T lastOrDefault(Predicate<? super T> filter) {
        int size = this.items.size();
        if (size == 0) return null;
        List list = new ArrayList(items);
        if (filter == null) return (T) list.get(size - 1);

        for (int i = size - 1; i >= 0; i--) {
            T item = (T) list.get(i);
            if (filter.test(item)) {
                return item;
            }
        }
        return null;
    }

    public List<T> toList() {
        return new ArrayList<>(this.items);
    }

    public Set<T> toSet() {
        return new HashSet<>(this.items);
    }

    public T[] toArray() {
        return (T[]) this.items.toArray();
    }
}
