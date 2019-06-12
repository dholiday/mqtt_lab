package com.davidholiday.camel.harness.routebuilders;

import com.davidholiday.camel.harness.context.AppContextLifecycle;
import com.davidholiday.camel.harness.routing.RouteBuilderHarness;
import com.davidholiday.camel.harness.util.ConnectionStringFactory;


/**
 * @implNote this is purposefully done a bit ass backwards to ensure the consumer version is exactly what we want
 * it to be. otherwise we'd just use the camel connector or write our own ...
 */
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

        String mqttConsumerConnectionString =
                ConnectionStringFactory.getConnectionStringOrThrow(AppContextLifecycle.VERNEMQ_CONSUMER_PROCESSOR);

        /*
        routes
         */

        from("direct:in").routeId(FROM_ROUTE_ID)
                             .to(BUSINESS_LOGIC_ROUTE_FROM_NAME);

        from(BUSINESS_LOGIC_ROUTE_FROM_NAME).routeId(BUSINESS_LOGIC_ROUTE_ID)
                                            .description(BUSINESS_LOGIC_ROUTE_DESCRIPTION)
                                            .to(mqttConsumerConnectionString);
    }


}
