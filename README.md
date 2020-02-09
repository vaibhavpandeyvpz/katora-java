# katora-java
Simple service container for Java apps.

[![Build](https://img.shields.io/github/workflow/status/vaibhavpandeyvpz/katora-java/Java%20CI?style=flat-square)](https://github.com/vaibhavpandeyvpz/katora-java/actions)
[![Release](https://jitpack.io/v/vaibhavpandeyvpz/katora-java.svg)](https://jitpack.io/#vaibhavpandeyvpz/katora-java)

This project aims to solve the code organisation problem with the infamous [Singleton](https://www.baeldung.com/java-singleton) pattern (and factories) common among Java (and maybe other languages as well) developers.
Instead of hidden singletons throughout your project's codebase, they can now be registered at a single place and be accessed throughout.

For even better project structure, I recommend implementing service `Provider` interface and registering services therein.

## Installation

The package is built and tested using [Github Actions](https://github.com/features/actions) but installation is facilitated via [JitPack](https://jitpack.io/) for ease.
The steps to include [Katora](https://github.com/vaibhavpandeyvpz/katora-java) are as follows:

In your project's `build.gradle` file, include below repository:

```groovy
repositories {

    // ... other repositories

    maven { url 'https://jitpack.io' }
}

dependencies {

    // ... other dependencies

    implementation 'com.github.vaibhavpandeyvpz:katora-java:1.0.0'
}
```

## Usage

This library can be used in several ways. I have just document the most common ones below.

### Barebones

First approach in create just one Container singleton (instead of many) and register desired services with the container.
Let's suppose you have a service class `YourService` that depends on an object of `YourServiceDependency` class.
It can be defined as follows:

```java
import com.vaibhavpandey.katora.Container;

public class YourSingleton {

    private static Container mContainer;

    public static void createContainer() {
        Container container = new Container();
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        container.singleton(YourService.class, c -> new YourService(c.get(YourServiceDependency.class)));
        container.factory("youralias", c -> c.get(YourService.class));
        mContainer = container;
    }

    public static Container getContainer() {
        return mContainer;
    }
}
```

Then later, you first create the container (just once) and then fetch the service anywhere in your app from your container using:

```java
public class YourMainClass {

    public static void main(String[] args) {
        // call this just once ...
        YourSingleton.createContainer();
        // ... and then use it here or anywhere e.g.,
        YourService service = YourSingleton.getContainer().get(YourService.class); // or "youralias" instead of Class name
    }
}
```

### Providers

Recommended approach is to organise services into `Provider`s, install them onto your container and delegate service registrations as shown below.

Here is our provider #1 that actually registers the services.

```java
import com.vaibhavpandey.katora.contracts.Provider;

public class YourServiceProvider implements Provider {

    @Override
    public void provide(MutableContainer container) {
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        container.singleton(YourService.class, c -> new YourService(c.get(YourServiceDependency.class)));
    }
}
```

Here is our provider #2 that just registers string alias to registered services.

```java
import com.vaibhavpandey.katora.contracts.Provider;

public class YourAliasProvider implements Provider {

    @Override
    public void provide(MutableContainer container) {
        container.factory("youralias", c -> c.get(YourService.class));
    }
}
```

Then composing your container using above providers like:

```java
import com.vaibhavpandey.katora.Container;

public class YourSingleton {

    private static Container mContainer;

    public static void createContainer() {
        mContainer = new Container()
            .install(new YourServiceProvider())
            .install(new YourAliasProvider());
    }

    public static Container getContainer() {
        return mContainer;
    }
}
```

So that's how all of the above organises your project's code base while not enforcing any new coding pattern than Singleton.
I created this library to stop repeating myself among projects and maybe product more maintainable code.

### License
See [LICENSE](LICENSE) file.
