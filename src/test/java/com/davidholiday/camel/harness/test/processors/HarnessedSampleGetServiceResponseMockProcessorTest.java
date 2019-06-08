package com.davidholiday.camel.harness.test.processors;


import com.davidholiday.camel.harness.context.AppContextLifecycle;
import com.davidholiday.camel.harness.context.HarnessedAppContextLifecycleWithStopSampleRoute;

import com.davidholiday.camel.harness.processors.SampleGetServiceResponseMockProcessor;

import com.davidholiday.camel.harness.helpers.MockFileHelpers;
import com.davidholiday.camel.harness.testing.ProcessorTestHarness;

import org.apache.camel.Processor;

import org.junit.Test;


/**
 * example of what a Processor level test looks like using the ProcessorTestHarness. note that in test context the
 * contextlifecycle object is used only to populate the JNDI registry - the lifecycle management aspects don't apply.
 * also - if you need capability to pre/post processes the contents of the exchange you can use the TestHarness methods
 * setProcessorFor{Pre/Post}processorMockEndpoint() to inject that functionality into the test route provided by the
 * harness.
 */
public class HarnessedSampleGetServiceResponseMockProcessorTest extends ProcessorTestHarness {

    // this is what we want to perform tests on
    private static final Processor HARNESSED_PROCESSOR_TO_TEST =
            new SampleGetServiceResponseMockProcessor(AppContextLifecycle.SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE);

    // this is what populates the JNDI registry the test(s) will use
    private static final HarnessedAppContextLifecycleWithStopSampleRoute HARNESSED_CAMEL_CONTEXT_LIFECYCLE =
            new HarnessedAppContextLifecycleWithStopSampleRoute();

    // this constructor is how we inject the thing we want to test and the jndi registry we want to use when those tests
    // are running into the ProcessorTestHarness
    public HarnessedSampleGetServiceResponseMockProcessorTest() {
        super(HARNESSED_PROCESSOR_TO_TEST, HARNESSED_CAMEL_CONTEXT_LIFECYCLE);
    }


    @Test
    public void processorTestHappyPath() throws InterruptedException {
        String expectedMessageBodyOut =
            MockFileHelpers.getFileContentsAsString(AppContextLifecycle.SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE , template);

        setExpectedOutputBody(expectedMessageBodyOut);

        runTest();
    }

}
