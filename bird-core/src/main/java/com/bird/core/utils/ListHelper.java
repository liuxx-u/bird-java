package com.bird.core.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by liuxx on 2017/6/7.
 */
public class ListHelper<T> {
    private List<T> items;

    private ListHelper(List<T> items) {
        this.items = items;
    }

    public static <S> ListHelper<S> init(List<S> items) {
        return new ListHelper(items);
    }

    public ListHelper<T> where(Predicate<? super T> filter) {
        Objects.requireNonNull(filter);

        final Iterator<T> each = this.items.iterator();
        while (each.hasNext()) {
            T item = each.next();
            if (!filter.test(item)) {
                each.remove();
            }
        }
        return this;
    }

    public <R> List<R> select(Function<T, R> func) {
        ArrayList<R> result = new ArrayList<>();
        final Iterator<T> each = this.items.iterator();
        while (each.hasNext()) {
            result.add(func.apply(each.next()));
        }
        return result;
    }

    public T firstOrDefault(Predicate<? super T> filter) {
        int size = this.items.size();
        if (size == 0) return null;

        List list = this.items;
        if (filter == null) return (T) list.get(0);

        for (int i = 0; i <= size - 1; i++) {
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

        List list = this.items;
        if (filter == null) return (T) list.get(size - 1);

        for (int i = size - 1; i >= 0; i--) {
            T item = (T) list.get(i);
            if (filter.test(item)) {
                return item;
            }
        }
        return null;
    }

    public List<T> toList(){
        return this.items;
    }
}
