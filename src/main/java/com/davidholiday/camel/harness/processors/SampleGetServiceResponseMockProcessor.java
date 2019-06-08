package com.davidholiday.camel.harness.processors;


import com.davidholiday.camel.harness.helpers.MockFileHelpers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * processor that demonstrates how to inject a harness response into the exchange
 */
public class SampleGetServiceResponseMockProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleGetServiceResponseMockProcessor.class);

    private final String mockResourceName;


    /**
     *
     * @param mockResourceName
     */
    public SampleGetServiceResponseMockProcessor(String mockResourceName) {
        LOGGER.info("setting mockResourceName to: {}", mockResourceName);
        this.mockResourceName = mockResourceName;
    }

    /**
     *
     * @implNote it appears as though we need to use the ClassResolver provided by the camel context rather than going
     * the usual route of something like "String.class.GetResource("foo").getPath();" as camel seems to be doing
     * something that necessitates this
     *
     * @return
     */
    public void process(Exchange exchange) throws IOException {

        String mockResponseAsString = MockFileHelpers.getFileContentsAsString(mockResourceName, exchange.getContext());

        // take care NOT to set the out message of the exchange unless you really really mean it. I know it seems like
        // you would want to but most of the time we're only going to use the 'in' message as getting the 'getOut()'
        // method will actually create a new message which will overwrite anything previously in he in-message
        // section of the exchange.
        //
        // http://camel.apache.org/using-getin-or-getout-methods-on-exchange.html
        exchange.getIn()
                .setBody(mockResponseAsString);

        LOGGER.debug("in-message is now: {}", exchange.getIn().getBody(String.class));
    }

}
