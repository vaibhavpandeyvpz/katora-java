package com.vaibhavpandey.katora.contracts;

public interface Factory<T> {

    T create(ImmutableContainer container) throws Exception;
}
