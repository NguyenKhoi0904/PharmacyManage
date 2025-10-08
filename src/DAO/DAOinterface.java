package com.mycompany.pharmacystore.DAO;

import java.util.ArrayList;

public interface DAOinterface<T> {
    // select all
    public ArrayList<T> selectAll();

    // select by id
    public T selectById(String id);

    // insert
    public int insert(T t);

    // update 
    public void update(T t);

    // delete
    public void deleteById(String id);
}