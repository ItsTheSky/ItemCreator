package info.itsthesky.itemcreator.utils;

import java.util.*;

public class Pagination<T> extends ArrayList<T> {

    private final int pageSize;

    public Pagination(int pageSize) {
        this(pageSize, new ArrayList<T>());
    }

    @SafeVarargs
    public Pagination(int pageSize, T... objects) {
        this(pageSize, Arrays.asList(objects));
    }

    public Pagination(int pageSize, Collection<T> objects) {
        this.pageSize = pageSize;
        addAll(objects);
    }

    public int pageSize() {
        return pageSize;
    }

    public int totalPages() {
        return (int) Math.ceil((double) size() / pageSize);
    }

    public boolean exists(int page) {
        return !(page < 0) && page < totalPages();
    }

    public List<T> getPage(int page) {
        if(page < 0 || page >= totalPages()) throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + totalPages());

        List<T> objects = new ArrayList<>();

        int min = page * pageSize;
        int max = ((page * pageSize) + pageSize);

        if(max > size()) max = size();

        for(int i = min; max > i; i++)
            objects.add(get(i));

        return objects;
    }
}