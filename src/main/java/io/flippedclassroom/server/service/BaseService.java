package io.flippedclassroom.server.service;

import java.util.List;

public interface BaseService<E> {
    E findById(Long id);

    E save(E e);

    List<E> save(Iterable<E> iterable);

    void delete(E e);

    void deleteById(Long id);

    void delete(Iterable<E> iterable);

    void deleteAll();
}
