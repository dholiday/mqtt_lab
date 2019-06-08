package com.davidholiday.camel.harness.test.routebuilders;


import com.davidholiday.camel.harness.context.HarnessedAppContextLifecycleWithStopSampleRoute;
import com.davidholiday.camel.harness.routebuilders.HarnessedHealthCheckRouteBuilder;
import com.davidholiday.camel.harness.test.PrePostTestProcessors.HttpHeaderValidationProcessor;

import com.davidholiday.camel.harness.context.AppContextLifecycleHarness;
import com.davidholiday.camel.harness.routing.RouteBuilderHarness;
import com.davidholiday.camel.harness.testing.HarnessedRouteBuilderTestHarness;

import org.apache.camel.Processor;

import org.junit.Test;


/**
 * example Harnessed route test. we use the constructor to inject the route we want to test and the contextlifecycle
 * object we want used for this set of tests. note that the context lifecycle object in the context of a test is used
 * only to populate the registry as the camel context doesn't exist in the same way in test as it does in prod. if any
 * pre/post processing behavior necessary it can be injected by using the Harness'
 * setProcessorFor{Pre/Post}MockEndpoint() methods.
 */
public class HarnessedHealthCheckRouteBuilderTest extends HarnessedRouteBuilderTestHarness {

    // this is what we want to perform tests on
    private static final RouteBuilderHarness HARNESSED_ROUTE_TO_TEST = new HarnessedHealthCheckRouteBuilder();

    // this is what populates the JNDI registry the test(s) will use
    private static final AppContextLifecycleHarness HARNESSED_CAMEL_CONTEXT_LIFECYCLE =
            new HarnessedAppContextLifecycleWithStopSampleRoute();

    // inject camel objects into harness via common constructor
    public HarnessedHealthCheckRouteBuilderTest() {
        super(HARNESSED_ROUTE_TO_TEST, HARNESSED_CAMEL_CONTEXT_LIFECYCLE);
    }


    @Test
    public void toHeaderRouteTestHappyPath() throws Exception {
        Processor httpHeaderValidationProcessor =
                new HttpHeaderValidationProcessor("200", "application/json");

        setProcessorForPostprocessorMockEndpoint(httpHeaderValidationProcessor);

        String expectedOutputAsString = "{}";
        setExpectedOutputBody(expectedOutputAsString);

        runTest();
    }


}
