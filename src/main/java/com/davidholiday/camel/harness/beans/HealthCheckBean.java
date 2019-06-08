package com.davidholiday.camel.harness.beans;


import java.io.Serializable;


/**
 * behavior target for the /healthcheck endpoint
 */
public class HealthCheckBean implements Serializable {

    /**
     * responds with an empty json body as the only thing the consumer of http://.../healthcheck endpoint cares about
     * is the HTTP200 response code
     *
     * @return
     */
    public String getEmptyResponse() {
        return "{}";
    }

}