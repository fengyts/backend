package com.backend.designmodel.iterator;

public class TestIterator {

    public static void main(String[] args) {
        CollectionCustom coll = new CollectionCustom();
        coll.add("a");
        coll.add("b");
        coll.add("c");
        coll.add("d");
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
