package com.davidholiday.camel.harness.routebuilders;


import com.davidholiday.camel.harness.context.AppContextLifecycle;
import com.davidholiday.camel.harness.routing.RouteBuilderHarness;


public class HarnessedMqttPublisherRouteBuilder extends RouteBuilderHarness {

    public HarnessedMqttPublisherRouteBuilder() {
        super(HarnessedMqttPublisherRouteBuilder.class.getSimpleName(), false);
    }

    public void configure() throws Exception {

        /*
        configuration
         */

        restConfiguration().component("servlet")
                           .dataFormatProperty("prettyPrint", "true");

        String mqttProducerProcessorName = "bean:" + AppContextLifecycle.VERNEMQ_PRODUCER_PROCESSOR;

        /*
        routes
         */

        // pipe from REST listener to camel route for processing
        rest().path("/mqtt_producer")
                .post()
                .route()
                .routeId(FROM_ROUTE_ID)
                .to(BUSINESS_LOGIC_ROUTE_FROM_NAME);

        from(BUSINESS_LOGIC_ROUTE_FROM_NAME).routeId(BUSINESS_LOGIC_ROUTE_ID)
                                            .description(BUSINESS_LOGIC_ROUTE_DESCRIPTION)
                                            .to(mqttProducerProcessorName)
                                            .log("exchange body is now: ${body}");
    }

}
