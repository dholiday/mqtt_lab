package com.davidholiday.camel.harness.test.PrePostTestProcessors;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.junit.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * this processor inspects the contents of the exchange header to ensure the provided expected http code and
 * content type is properly set.
 */
public class HttpHeaderValidationProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStringToMapProcessor.class);

    private String expectedHttpResponseCode;
    private String expectedContentType;

    public HttpHeaderValidationProcessor(String expectedHttpResponseCode, String expectedContentType) {
        this.expectedHttpResponseCode = expectedHttpResponseCode;
        this.expectedContentType = expectedContentType;
    }

    public void process (Exchange exchange) {

        //
        String actualHttpResponseCode = (String)exchange.getIn().getHeader(exchange.HTTP_RESPONSE_CODE);
        Assert.assertEquals(
                "http response code not what was expected",
                this.expectedHttpResponseCode,
                actualHttpResponseCode
        );

        //
        String actualContentType = (String)exchange.getIn().getHeader(exchange.CONTENT_TYPE);
        Assert.assertEquals(
                "http response code not what was expected",
                this.expectedContentType,
                actualContentType
        );

    }


}
