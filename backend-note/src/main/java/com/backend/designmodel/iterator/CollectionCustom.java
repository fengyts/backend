package com.backend.designmodel.iterator;

import java.util.ArrayList;
import java.util.List;

public class CollectionCustom<E> implements Container<E> {

    private List<E> datas = new ArrayList<>();

    @Override
    public Iterator<E> iterator() {
        return new CollectionIterator<E>();
    }

    @Override
    public void add(E ele) {
        datas.add(ele);
    }


    private class CollectionIterator<E> implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            return index < datas.size();
        }

        @Override
        public Object next() {
            return hasNext() ? datas.get(index++) : null;
        }

    }


}
