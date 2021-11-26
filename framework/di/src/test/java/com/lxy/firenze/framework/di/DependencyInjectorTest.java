package com.lxy.firenze.framework.di;


import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DependencyInjectorTest {

    @Test
    public void should_filed_inject_when_class_annotated_named() {
        DependencyInjector.start(DependencyInjectorTest.class);
        TestController testController = DependencyInjector.getInstance(TestController.class);
        TestServiceA testServiceA = DependencyInjector.getInstance(TestServiceA.class);
        TestServiceB testServiceB = DependencyInjector.getInstance(TestServiceB.class);
        TestRepository testRepository = DependencyInjector.getInstance(TestRepository.class);

        assertTrue(testServiceA instanceof TestServiceAImpl);
        assertTrue(testServiceB instanceof TestServiceBImpl);
        assertTrue(testRepository instanceof TestRepositoryImpl);
        assertEquals(testController.getServiceA(), testServiceA);
        assertEquals(testController.getServiceB(), testServiceB);
        assertEquals(((TestServiceAImpl) testServiceA).getRepository(), testRepository);
    }
}

@Named
class TestController {

    @Inject
    private TestServiceA serviceA;

    @Inject
    private TestServiceB serviceB;

    public TestServiceA getServiceA() {
        return serviceA;
    }

    public TestServiceB getServiceB() {
        return serviceB;
    }
}

interface TestServiceA {
}

@Named
class TestServiceAImpl implements TestServiceA {

    @Inject
    private TestRepository repository;

    public TestRepository getRepository() {
        return repository;
    }
}

interface TestServiceB {
}

@Named
class TestServiceBImpl implements TestServiceB {


}

interface TestRepository {
}

@Named
class TestRepositoryImpl implements TestRepository {
}