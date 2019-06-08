package com.davidholiday.camel.harness.test.PrePostTestProcessors;


import com.google.gson.Gson;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * converts a json string to a map to make it easier to perform value content comparison during testing
 */
public class JsonStringToMapProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStringToMapProcessor.class);

    public void process (Exchange exchange) {
        String jsonPayloadAsString = (String)exchange.getIn().getBody();

        Map<String, String> jsonPayloadAsMap = new Gson().fromJson(jsonPayloadAsString, Map.class);

        exchange.getIn().setBody(jsonPayloadAsMap);
        LOGGER.info("exchange in-body is now: {}", exchange.getIn().getBody());
    }

}
