package com.davidholiday.camel.harness.config;


import com.netflix.config.DynamicStringProperty;

import static com.davidholiday.camel.harness.helpers.DynamicPropertyHelpers.getOrGetDynamicStringProperty;


/**
 * will try to read from 'prod' properties location first, then failing that will read from local config.properties file
 */
public class Properties {

    // list of prod properties files to be ingested as additional app config variables that will override the list
    // set in the local property
    public static final DynamicStringProperty ADDITIONAL_PROPERTY_SOURCE_URLS_PROPERTY =
            getOrGetDynamicStringProperty(
                    "additional.properties.source.url",
                    "additional.properties.source.url.local"
            );


    // these two are an example of how to mock (or otherwise define different a different connection string) for calls
    // to external services
    public static final DynamicStringProperty SAMPLE_GET_SERVICE_SOURCE_PROPERTY =
            getOrGetDynamicStringProperty(
                    "sample.get.service.source",
                    "sample.get.service.source.local"
            );

    public static final DynamicStringProperty SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE_PROPERTY =
            getOrGetDynamicStringProperty(
                    "sample.get.service.response.mockfile",
                    "sample.get.service.response.mockfile.local"
            );


    // ...

}