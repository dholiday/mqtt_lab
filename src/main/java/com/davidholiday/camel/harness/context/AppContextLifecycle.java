package com.davidholiday.camel.harness.context;


import com.davidholiday.camel.harness.processors.SampleGetServiceResponseMockProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.camel.impl.JndiRegistry;

import org.apache.camel.component.servletlistener.CamelContextLifecycle;
import org.apache.camel.component.servletlistener.ServletCamelContext;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicURLConfiguration;

import com.davidholiday.camel.harness.beans.HealthCheckBean;

import com.davidholiday.camel.harness.config.Properties;

import static com.davidholiday.camel.harness.config.Properties.ADDITIONAL_PROPERTY_SOURCE_URLS_PROPERTY;


/**
 * Allows us to manage the contents of the JNDI Registry (where we register our beans for later use) as well as add
 * listeners for events and other behaviors into the various lifecycle stages of the camel context
 *
 * names of anything registered with jndi should be created here as public static final Strings
 *
 */
public class AppContextLifecycle implements CamelContextLifecycle<JndiRegistry> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppContextLifecycle.class);

    // bean names
    public static final String HEALTH_CHECK_BEAN_NAME = "healthCheckBean";

    // processor names
    public static final String SAMPLE_GET_SERVICE_RESPONSE_MOCK_PROCESSOR_NAME = "sampleGetServiceResponseMockProcessor";

    // processor constructor parameter names
    public static final String SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE =
            Properties.SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE_PROPERTY.getValue();


    @Override
    public void beforeStart(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {

        /*
        register beans
         */
        registry.bind(
                HEALTH_CHECK_BEAN_NAME,
                new HealthCheckBean()
        );


        /*
        register processors
         */
        registry.bind(
                SAMPLE_GET_SERVICE_RESPONSE_MOCK_PROCESSOR_NAME,
                new SampleGetServiceResponseMockProcessor(SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE)
        );


        //...

    }

    @Override
    public void beforeStop(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {}

    @Override
    public void afterStop(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {
        LOGGER.info(
                          "\n*----------------------------------------------------------------------*"
                        + "\n I've seen things you people wouldn't believe."
                        + "\n I've seen DSB on fire off the shoulder of Orion."
                        + "\n I watched kafka messages glitter in the dark near the Tannhäuser Gate."
                        + "\n All those moments will be lost in time, like tears in rain."
                        + "\n Time to die."
                        + "\n*----------------------------------------------------------------------*"
        );
    }

    @Override
    public void beforeAddRoutes(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {

        /*
        before we do anything we want to make sure we've registered any shared properties files with archaius
         */

        String additionalConfigSourcesAsString = ADDITIONAL_PROPERTY_SOURCE_URLS_PROPERTY.getValue();
        String[] additionalConfigSourcesAsArray = additionalConfigSourcesAsString.split(",");
        LOGGER.info("registering external configuration sources from {}", additionalConfigSourcesAsString);

        if (additionalConfigSourcesAsArray.length > 1) {
            try {
                // delay values are defaults from com.netflix.config.FixedDelayPollingScheduler. would've just used the
                // ones defined there but whomever wrote it made those fields private and didn't provide a getter.
                // womp. womp.
                DynamicURLConfiguration dynamicConfiguration =
                        new DynamicURLConfiguration(
                                30000,
                                60000,
                                false,
                                additionalConfigSourcesAsArray
                        );

                ConfigurationManager.install(dynamicConfiguration);
            } catch (Exception e) {
                LOGGER.info("failed to load one or more config files due to exception: ", e);
            }
        }

    }

    @Override
    public void afterAddRoutes(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {}

    @Override
    public void afterStart(ServletCamelContext camelContext, JndiRegistry registry) throws Exception {

        /*
        add listener(s) for specific property changes so that we can restart the impacted route(s). otherwise they
        won't pick up the changes and we'd have to restart the entire camel harness.
         */

        // ConfigurationChangeListener myConfigurationChangeListener = new ConfigurationChangeListener(...)
        // ConfigurationManager.getConfigInstance().addConfigurationListener(myConfigurationChangeListener);

    }

}
