package com.davidholiday.camel.harness.test.beans;


import com.davidholiday.camel.harness.beans.HealthCheckBean;

import org.junit.Assert;
import org.junit.Test;


/**
 * example test for a bean
 */
public class HealthCheckBeanTest {

    HealthCheckBean healthCheckBean = new HealthCheckBean();

    @Test
    public void happyPathTest() {
        String expectedOutput = "{}";
        String actualOutput = healthCheckBean.getEmptyResponse();
        Assert.assertEquals(
                "what came out of the healthcheck bean isn't what was expected!",
                expectedOutput,
                actualOutput
        );

    }

}
