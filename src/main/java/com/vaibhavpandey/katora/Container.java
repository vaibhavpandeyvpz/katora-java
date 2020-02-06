package com.vaibhavpandey.katora;

import com.vaibhavpandey.katora.contracts.MutableContainer;
import com.vaibhavpandey.katora.contracts.Factory;
import com.vaibhavpandey.katora.contracts.Provider;
import com.vaibhavpandey.katora.exceptions.FactoryException;
import com.vaibhavpandey.katora.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Container implements MutableContainer {

    private final Map<String, Object> mCreated = new HashMap<>();

    private final Map<String, Factory> mFactories = new HashMap<>();

    private final List<String> mSingletons = new ArrayList<>();

    @Override
    public <T> MutableContainer factory(Class<T> clazz, Factory<T> factory) {
        return factory(clazz.getName(), factory);
    }

    @Override
    public <T> MutableContainer factory(String id, Factory<T> factory) {
        mFactories.put(id, factory);
        return this;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return get(clazz.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String id) {
        if (has(id)) {
            Object value;
            boolean singleton = mSingletons.contains(id);
            if (singleton && mCreated.containsKey(id)) {
                value = mCreated.get(id);
            } else {
                Factory factory = mFactories.get(id);
                assert factory != null;
                try {
                    value = factory.create(this);
                } catch (Exception e) {
                    throw new FactoryException(
                            String.format("Error occurred in creating service '%s'.", id), e);
                }
                if (singleton) {
                    mCreated.put(id, value);
                }
            }
            return (T) value;
        }
        throw new NotFoundException(String.format("Service '%s' could not be found.", id));
    }

    @Override
    public <T> boolean has(Class<T> clazz) {
        return has(clazz.getName());
    }

    @Override
    public boolean has(String id) {
        return mFactories.containsKey(id);
    }

    @Override
    public MutableContainer install(Provider provider) {
        provider.provide(this);
        return this;
    }

    @Override
    public <T> MutableContainer singleton(Class<T> clazz, Factory<T> factory) {
        return singleton(clazz.getName(), factory);
    }

    @Override
    public <T> MutableContainer singleton(String id, Factory<T> factory) {
        factory(id, factory);
        mSingletons.add(id);
        return this;
    }
}
