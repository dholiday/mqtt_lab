package com.davidholiday.camel.harness.context;


import com.davidholiday.camel.harness.routebuilders.HarnessedSampleGetServiceResponseRouteBuilder;

import org.apache.camel.component.servletlistener.ServletCamelContext;

import org.apache.camel.impl.JndiRegistry;

import com.davidholiday.camel.harness.context.AppContextLifecycleFunctionInterface;
import com.davidholiday.camel.harness.context.AppContextLifecycleHarness;

import java.util.HashMap;
import java.util.Map;


/**
 * Harness to allow us to toggle whether or not the Sample routebuilder is present when the app spins up. by default
 * the app is set up to use this class and thus will stop the sample route immediately after bootstrap is complete.
 */
public class HarnessedAppContextLifecycleWithStopSampleRoute extends AppContextLifecycleHarness {

    // AppContextLifecycle object to be 'harnessed'
    private static AppContextLifecycle contextLifecycle = new AppContextLifecycle();

    // alternate behavior we want available to us
    // TODO figure out why removing the routes before they got started wasn't working...
    private static AppContextLifecycleFunctionInterface afterStartFunction =
            (ServletCamelContext contextLifecycle, JndiRegistry registry) -> {
                try {
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
    public HarnessedAppContextLifecycleWithStopSampleRoute() {
        super(contextLifecycle, true, alternateContextFunctionMap);
    }


    /**
     * optional constructor that allows caller to set Harness' inAlternateContext flag
     *
     * @param inAlternateContext
     */
    public HarnessedAppContextLifecycleWithStopSampleRoute(boolean inAlternateContext) {
        super(contextLifecycle, inAlternateContext, alternateContextFunctionMap);
    }

}
