package com.davidholiday.camel.harness.routebuilders;


import com.davidholiday.camel.harness.util.ConnectionStringFactory;
import com.davidholiday.camel.harness.routing.RouteBuilderHarness;


/**
 * demonstrates route that makes a RESTful call in prod and uses a mock for same when running in local mode.
 */
public class HarnessedSampleGetServiceResponseRouteBuilder extends RouteBuilderHarness {

    public HarnessedSampleGetServiceResponseRouteBuilder() {
        super(HarnessedSampleGetServiceResponseRouteBuilder.class.getSimpleName(), false);
    }

    public void configure() throws Exception {

        /*
        configuration
         */

        // this restConfiguration is what lets us make RESTful calls to other services.
        // in this case it's set up to make a call to itself.
        restConfiguration().producerComponent("http4")
                           .host("localhost")
                           .port("8889");

        // resolve the correct connection string given runtime context
        String serviceResponseConnectionString =
                ConnectionStringFactory.getConnectionStringOrThrow(ConnectionStringFactory.SAMPLE_GET_SERVICE_RESPONSE_CONNECTION_STRING_KEY);


        /*
        routes
         */

        from("direct:in").routeId(FROM_ROUTE_ID)
                             .to(BUSINESS_LOGIC_ROUTE_FROM_NAME);

        from(BUSINESS_LOGIC_ROUTE_FROM_NAME).routeId(BUSINESS_LOGIC_ROUTE_ID)
                                            .description(BUSINESS_LOGIC_ROUTE_DESCRIPTION)
                                            .to(serviceResponseConnectionString)
                                            .log("exchange body is now: ${body}");
    }

}
