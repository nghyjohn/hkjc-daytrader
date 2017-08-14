package com.ibm.websphere.samples.daytrader.rest;

import com.ibm.websphere.samples.daytrader.EndpointTest;

import org.junit.Test;

public class DeploymentTest extends EndpointTest {

    @Test
    public void testDeployment() {
        testEndpoint("/rest/index.jsp", "<h1>Hello World!</h1>");
    }
}
