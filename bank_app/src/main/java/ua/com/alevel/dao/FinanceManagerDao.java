package ua.com.alevel.dao;

import java.util.List;
import java.util.Optional;

public interface FinanceManagerDao<T> {

    Long create(T t) throws Exception;

    Optional<T> findById(Long id) throws Exception;

    List<T> readAll() throws Exception;

    void update(T t) throws Exception;

    void delete(Long id);

}

