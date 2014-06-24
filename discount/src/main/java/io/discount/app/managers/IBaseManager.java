package io.discount.app.managers;

import java.util.ArrayList;

/**
 * Created by Jan on 24.3.14.
 */
public interface IBaseManager<T> {
    public ArrayList<T> getCollection();
    public T get();
    public void update(T t);
    public void delete(T t);
    public void create(T t);
}
