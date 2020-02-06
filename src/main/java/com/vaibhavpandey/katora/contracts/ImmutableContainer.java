package com.vaibhavpandey.katora.contracts;

import com.vaibhavpandey.katora.exceptions.FactoryException;
import com.vaibhavpandey.katora.exceptions.NotFoundException;

public interface ImmutableContainer {

    <T> T get(Class<T> clazz) throws FactoryException, NotFoundException;

    <T> T get(String id) throws FactoryException, NotFoundException;

    boolean has(String id);

    <T> boolean has(Class<T> clazz);
}
