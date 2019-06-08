package com.davidholiday.camel.harness.test.routebuilders;


import com.davidholiday.camel.harness.context.HarnessedAppContextLifecycleWithStopSampleRoute;
import com.davidholiday.camel.harness.routebuilders.HarnessedSampleGetServiceResponseRouteBuilder;
import com.google.gson.Gson;

import com.davidholiday.camel.harness.test.PrePostTestProcessors.JsonStringToMapProcessor;


import com.davidholiday.camel.harness.context.AppContextLifecycleHarness;

import com.davidholiday.camel.harness.routing.RouteBuilderHarness;

import com.davidholiday.camel.harness.testing.HarnessedRouteBuilderTestHarness;


import org.apache.camel.Processor;

import org.junit.Test;


import java.util.Map;


/**
 * example Harnessed route test. we use the constructor to inject the route we want to test and the contextlifecycle
 * object we want used for this set of tests. note that the context lifecycle object in the context of a test is used
 * only to populate the registry as the camel context doesn't exist in the same way in test as it does in prod. if any
 * pre/post processing behavior necessary it can be injected by using the Harness'
 * setProcessorFor{Pre/Post}MockEndpoint() methods.
 */
public class HarnessedSampleGetServiceResponseRouteBuilderTest extends HarnessedRouteBuilderTestHarness {

    // this is what we want to perform tests on
    private static final RouteBuilderHarness HARNESSED_ROUTE_TO_TEST = new HarnessedSampleGetServiceResponseRouteBuilder();

    // this is what populates the JNDI registry the test(s) will use
    private static final AppContextLifecycleHarness HARNESSED_CAMEL_CONTEXT_LIFECYCLE =
            new HarnessedAppContextLifecycleWithStopSampleRoute();

    // inject camel objects into harness via common constructor
    public HarnessedSampleGetServiceResponseRouteBuilderTest() {
        super(HARNESSED_ROUTE_TO_TEST, HARNESSED_CAMEL_CONTEXT_LIFECYCLE);
    }


    @Test
    public void toHeaderRouteTestHappyPath() throws Exception {

        // before we inspect the output which normally is a json string we convert it into a Gson map so we can
        // do value checking on the contents and not worry about failures due to String inequality issues (ie - issues
        // where the json content is the same but one instance has spaces or carriage returns where the other does not)
        Processor jsonStringToMapProcessor = new JsonStringToMapProcessor();
        setProcessorForPostprocessorMockEndpoint(jsonStringToMapProcessor);

        // set what we're expecting to match the output of the post processor
        String expectedOutputAsString = "{\"foo\":\"bar\"}";
        Map<String, String> expectedOutputAsMap = new Gson().fromJson(expectedOutputAsString, Map.class);
        setExpectedOutputBody(expectedOutputAsMap);

        //
        runTest();
    }

}
