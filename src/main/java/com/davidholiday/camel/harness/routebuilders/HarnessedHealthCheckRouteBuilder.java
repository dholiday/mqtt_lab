package com.davidholiday.camel.harness.routebuilders;


import org.apache.camel.Exchange;

import com.davidholiday.camel.harness.routing.RouteBuilderHarness;

import static com.davidholiday.camel.harness.context.AppContextLifecycle.HEALTH_CHECK_BEAN_NAME;


/**
 * opens a REST endpoint at /healthcheck and responds with HTTP200 and an empty json body. used by health check
 * monitors to ensure the app is still alive
 */
public class HarnessedHealthCheckRouteBuilder extends RouteBuilderHarness {

    public HarnessedHealthCheckRouteBuilder() {
        super(HarnessedHealthCheckRouteBuilder.class.getSimpleName(), false);
    }

    @Override
    public void configure() throws Exception {

        /*
        configuration
         */

        restConfiguration().component("servlet")
                           .dataFormatProperty("prettyPrint", "true");


        /*
        routes
         */

        // pipe from REST listener to camel route for processing
        rest().path("/healthcheck")
              .get()
                .route()
                .routeId(FROM_ROUTE_ID)
                .to(BUSINESS_LOGIC_ROUTE_FROM_NAME);

        // camel route to process request
        from(BUSINESS_LOGIC_ROUTE_FROM_NAME).routeId(BUSINESS_LOGIC_ROUTE_ID)
                                            .routeDescription(BUSINESS_LOGIC_ROUTE_DESCRIPTION)
                                            .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("200"))
                                            .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                                            .to("bean:" + HEALTH_CHECK_BEAN_NAME);
    }

}
