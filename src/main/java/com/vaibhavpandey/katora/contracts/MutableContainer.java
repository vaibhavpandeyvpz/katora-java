package com.vaibhavpandey.katora.contracts;

public interface MutableContainer extends ImmutableContainer {

    <T> MutableContainer factory(String id, Factory<T> factory);

    <T> MutableContainer factory(Class<T> clazz, Factory<T> factory);

    MutableContainer install(Provider provider);

    <T> MutableContainer singleton(String id, Factory<T> factory);

    <T> MutableContainer singleton(Class<T> clazz, Factory<T> factory);
}
