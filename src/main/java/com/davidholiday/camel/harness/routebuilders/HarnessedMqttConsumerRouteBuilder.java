package com.davidholiday.camel.harness.routebuilders;

import com.davidholiday.camel.harness.routing.RouteBuilderHarness;


public class HarnessedMqttConsumerRouteBuilder extends RouteBuilderHarness {

    public HarnessedMqttConsumerRouteBuilder() {
        super(HarnessedMqttConsumerRouteBuilder.class.getName(), false);
    }

    public void configure() {

        /*
        configuration
         */

        restConfiguration().component("servlet")
                           .dataFormatProperty("prettyPrint", "true");

        
        /*
        routes
         */

        from("direct:in").routeId(FROM_ROUTE_ID)
                             .to(BUSINESS_LOGIC_ROUTE_FROM_NAME);

        from(BUSINESS_LOGIC_ROUTE_FROM_NAME).routeId(BUSINESS_LOGIC_ROUTE_ID)
                                            .description(BUSINESS_LOGIC_ROUTE_DESCRIPTION)
                                            .to("");
    }


}
