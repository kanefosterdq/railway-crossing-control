package dao;

import java.util.List;

// Database Access Object
public interface DAO<T> {

int insert(T object); 
int update(T object);
int delete(T object);
List<T> query();	  
}