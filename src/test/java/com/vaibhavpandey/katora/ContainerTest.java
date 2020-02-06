package com.vaibhavpandey.katora;

import com.vaibhavpandey.katora.exceptions.FactoryException;
import com.vaibhavpandey.katora.exceptions.NotFoundException;
import com.vaibhavpandey.katora.fixtures.YourService;
import com.vaibhavpandey.katora.fixtures.YourServiceDependency;
import com.vaibhavpandey.katora.fixtures.YourServiceProvider;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void testExistence() {
        Container container = new Container();
        assertFalse(container.has(YourServiceDependency.class));
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        assertTrue(container.has(YourServiceDependency.class));
    }

    @Test
    public void testFactory() {
        Container container = new Container();
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        assertNotEquals(
                container.get(YourServiceDependency.class),
                container.get(YourServiceDependency.class));
    }

    @Test
    public void testSingleton() {
        Container container = new Container();
        container.singleton(YourServiceDependency.class, c -> new YourServiceDependency());
        assertEquals(
                container.get(YourServiceDependency.class),
                container.get(YourServiceDependency.class));
    }

    @Test
    public void testDependency() {
        Container container = new Container();
        container.factory(YourServiceDependency.class, c -> new YourServiceDependency());
        container.singleton(YourService.class, c -> new YourService(c.get(YourServiceDependency.class)));
        assertNotEquals(
                container.get(YourServiceDependency.class),
                container.get(YourServiceDependency.class));
        assertEquals(
                container.get(YourService.class),
                container.get(YourService.class));
    }

    @Test
    public void testProvider() {
        Container container = new Container();
        container.install(new YourServiceProvider());
        assertNotEquals(
                container.get(YourServiceDependency.class),
                container.get(YourServiceDependency.class));
        assertEquals(
                container.get(YourService.class),
                container.get(YourService.class));
    }

    @Test
    public void testStringAlias() {
        Container container = new Container();
        container.install(new YourServiceProvider());
        container.factory("alias1", c -> c.get(YourServiceDependency.class));
        container.singleton("alias2", c -> c.get(YourServiceDependency.class));
        assertTrue(container.has("alias1"));
        assertTrue(container.has("alias2"));
        assertFalse(container.has("alias3"));
        YourServiceDependency alias11 = container.get("alias1");
        YourServiceDependency alias12 = container.get("alias1");
        assertNotEquals(alias11, alias12);
        YourServiceDependency alias21 = container.get("alias2");
        YourServiceDependency alias22 = container.get("alias2");
        assertEquals(alias21, alias22);
    }

    @Test(expected = FactoryException.class)
    public void testFactoryException() {
        Container container = new Container();
        container.factory(YourServiceDependency.class, c -> {
            throw new Exception();
        });
        container.get(YourServiceDependency.class);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundException() {
        Container container = new Container();
        container.get(YourServiceDependency.class);
    }
}
