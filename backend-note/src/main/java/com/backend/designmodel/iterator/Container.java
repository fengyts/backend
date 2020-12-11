package com.backend.designmodel.iterator;

public interface Container<E> {

    Iterator<E> iterator();

    void add(E ele);

}
