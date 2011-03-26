package org.jboss.arquillian.ajocado.testng.listener.dependencies;

import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.TEST1;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.TEST2;

import org.testng.annotations.Test;

public class TestAbstractConfigurationListenerPart1 extends AbstractTestingConfigurationListener {
    @Test
    public void testDependencies1() {
        DependenciesTestingConfigurationListener.assertPhase(TEST1);
    }

    @Test
    public void testDependencies2() {
        DependenciesTestingConfigurationListener.assertPhase(TEST2);
    }
}
