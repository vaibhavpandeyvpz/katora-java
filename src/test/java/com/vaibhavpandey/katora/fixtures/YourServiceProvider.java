package com.vaibhavpandey.katora.fixtures;

import com.vaibhavpandey.katora.contracts.MutableContainer;
import com.vaibhavpandey.katora.contracts.Provider;

public class YourServiceProvider implements Provider {

    @Override
    public void provide(MutableContainer container) {
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        container.singleton(YourService.class, c -> new YourService(c.get(YourServiceDependency.class)));
    }
}
