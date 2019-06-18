package com.davidholiday.camel.harness.context;


import com.davidholiday.camel.harness.config.Properties;

import com.davidholiday.camel.harness.processors.VerneMqConsumerProcessor;
import com.davidholiday.camel.harness.routebuilders.HarnessedMqttConsumerRouteBuilder;
import com.davidholiday.camel.harness.routebuilders.HarnessedMqttPublisherRouteBuilder;
import com.davidholiday.camel.harness.routebuilders.HarnessedSampleGetServiceResponseRouteBuilder;

import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.servletlistener.ServletCamelContext;

import org.apache.camel.impl.JndiRegistry;

import com.davidholiday.camel.harness.context.AppContextLifecycleFunctionInterface;
import com.davidholiday.camel.harness.context.AppContextLifecycleHarness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Harness to allow us to toggle whether or not the Sample routebuilder is present when the app spins up. by default
 * the app is set up to use this class and thus will stop the sample route immediately after bootstrap is complete.
 */
public class HarnessedAppContextLifecycle extends AppContextLifecycleHarness {


    // AppContextLifecycle object to be 'harnessed'
    private static AppContextLifecycle contextLifecycle = new AppContextLifecycle();

    // alternate behavior we want available to us
    // TODO figure out why removing the routes before they got started wasn't working...
    private static AppContextLifecycleFunctionInterface afterStartFunction =
            (ServletCamelContext contextLifecycle, JndiRegistry registry) -> {

                Boolean mqttConsumerRouteEnabled =
                        Boolean.valueOf(Properties.MQTT_CONSUMER_ROUTE_ENABLED_PROPERTY.get());

                Boolean mqttProducerRouteEnabled =
                        Boolean.valueOf(Properties.MQTT_PRODUCER_ROUTE_ENABLED_PROPERTY.get());


                try {

                    // stop harnessed sample get service response route
                    HarnessedSampleGetServiceResponseRouteBuilder harnessedSampleGetServiceResponseRoute =
                            new HarnessedSampleGetServiceResponseRouteBuilder();

                    String fromRouteId = harnessedSampleGetServiceResponseRoute.getFromRouteId();
                    if (contextLifecycle.getRouteStatus(fromRouteId) != null) {
                        contextLifecycle.stopRoute(fromRouteId);
                    }

                    String businessLogicRouteId = harnessedSampleGetServiceResponseRoute.getBusinessLogicRouteId();
                    if (contextLifecycle.getRouteStatus(businessLogicRouteId) != null) {
                        contextLifecycle.stopRoute(businessLogicRouteId);
                    }


//                    if (mqttConsumerRouteEnabled) {
//                        // dirty ...
//                        for (int i = 0; i < 4; i ++) {
//                            new VerneMqConsumerProcessor().process(null);
//                        }
//                    }

                    // stop vernemq consumer route
                    if (mqttConsumerRouteEnabled == false) {

                        HarnessedMqttConsumerRouteBuilder harnessedMqttConsumerRouteBuilder =
                                new HarnessedMqttConsumerRouteBuilder();

                        fromRouteId = harnessedMqttConsumerRouteBuilder.getFromRouteId();
                        if (contextLifecycle.getRouteStatus(fromRouteId) != null) {
                            contextLifecycle.stopRoute(fromRouteId);
                        }

                        businessLogicRouteId = harnessedMqttConsumerRouteBuilder.getBusinessLogicRouteId();
                        if (contextLifecycle.getRouteStatus(businessLogicRouteId) != null) {
                            contextLifecycle.stopRoute(businessLogicRouteId);
                        }
                    }

                    // stop vernemq producer route
                    if (mqttProducerRouteEnabled == false) {
                        HarnessedMqttPublisherRouteBuilder harnessedMqttPublisherRouteBuilder =
                                new HarnessedMqttPublisherRouteBuilder();

                        fromRouteId = harnessedMqttPublisherRouteBuilder.getFromRouteId();
                        if (contextLifecycle.getRouteStatus(fromRouteId) != null) {
                            contextLifecycle.stopRoute(fromRouteId);
                        }

                        businessLogicRouteId = harnessedMqttPublisherRouteBuilder.getBusinessLogicRouteId();
                        if (contextLifecycle.getRouteStatus(businessLogicRouteId) != null) {
                            contextLifecycle.stopRoute(businessLogicRouteId);
                        }
                    }

                } catch (Exception e) {

                    LOGGER.error("exception while attempting to stop routes", e);
                }
            };

    // map alternate behaviors to lifecycle stages
    private static final Map<String, AppContextLifecycleFunctionInterface> alternateContextFunctionMap =
            new HashMap<String, AppContextLifecycleFunctionInterface>() {{
                put(AFTER_START_FUNCTION_ALTERNATE_CONTEXT_KEY, afterStartFunction);
            }};


    /**
     * default constructor which sets Harness' inAlternateContext flag to true
     */
    public HarnessedAppContextLifecycle() {
        super(contextLifecycle, true, alternateContextFunctionMap);
    }


    /**
     * optional constructor that allows caller to set Harness' inAlternateContext flag
     *
     * @param inAlternateContext
     */
    public HarnessedAppContextLifecycle(boolean inAlternateContext) {
        super(contextLifecycle, inAlternateContext, alternateContextFunctionMap);
    }

}
