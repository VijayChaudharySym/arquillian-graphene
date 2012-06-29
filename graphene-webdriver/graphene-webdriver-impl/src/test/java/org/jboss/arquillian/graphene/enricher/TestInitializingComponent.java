/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.enricher;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.page.TestPage;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.spi.components.common.AbstractComponentStub;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Juraj Huska
 */
public class TestInitializingComponent extends Arquillian {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractComponentStub abstractComponent;

    @FindBy(xpath = "//input")
    private WebElement input;

    @Page
    private TestPage testPage;

    private final String EXPECTED_NESTED_ELEMENT_TEXT = "Some Value";

    @Drone
    WebDriver selenium;

    @ArquillianResource
    protected URL contextRoot;

    private static final String WEB_APP_SRC = "src/test/webapp";

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class, "drone-test.war").addAsWebResource(new File(WEB_APP_SRC + "/index.html"),
            ArchivePaths.create("index.html"));
    }

    @BeforeMethod
    public void loadPage() {
        selenium.get(contextRoot + "index.html");
    }

    @Test
    public void testComponentIsInitialized() {
        assertNotNull(abstractComponent, "AbstractComponent should be initialised at this point!");
    }

    @Test
    public void testComponentHasSetRootCorrectly() {
        assertEquals(abstractComponent.invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT,
            "The root was not set correctly!");
    }

    @Test
    public void testPageObjectInitialisedCorrectly() {
        assertEquals(testPage.getAbstractComponent().invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT,
            "The page component was not set correctly!");
    }

    @Test
    public void testOtherWebElementsInitialisedCorrectly() {
        String EXPECTED_VALUE = "Gooseka";
        input.sendKeys(EXPECTED_VALUE);

        assertEquals(input.getAttribute("value"), EXPECTED_VALUE,
            "The value of the input is wrong, the element which represents it was not initialised correctly!");
    }
}
