package com.ibm.websphere.samples.daytrader.web;

import com.ibm.websphere.samples.daytrader.EndpointTest;

import org.junit.Test;

public class DeploymentTest extends EndpointTest {

    @Test
    public void testDeployment() {
        testEndpoint("/daytrader/index.jsp", "<h1>Hello World!</h1>");
    }
}
