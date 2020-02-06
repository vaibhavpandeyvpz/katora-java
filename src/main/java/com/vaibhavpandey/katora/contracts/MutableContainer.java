package com.vaibhavpandey.katora.contracts;

import com.vaibhavpandey.katora.exceptions.ProviderInstalledException;

public interface MutableContainer extends ImmutableContainer {

    <T> MutableContainer factory(String id, Factory<T> factory);

    <T> MutableContainer factory(Class<T> clazz, Factory<T> factory);

    MutableContainer install(Provider provider) throws ProviderInstalledException;

    <T> MutableContainer singleton(String id, Factory<T> factory);

    <T> MutableContainer singleton(Class<T> clazz, Factory<T> factory);
}
