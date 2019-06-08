package com.davidholiday.camel.harness.util;


import com.davidholiday.camel.harness.config.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


/**
 * this factory is intended to be the one source of truth for connection strings used by camel to figure out
 * where to route traffic from/to the app (intra-app traffic is handled differently as all harnessed routebuilders
 * conform to a standard layout and naming convention).
 *
 * the connection string for mock/RESTful calls usually can be pulled directly from the Properties object directly. in
 * cases where the connection string is for a component (say to allow the route to read/write from kafka) the connection
 * string needs to be assembled from a set of properties (host, port, etc...). that dynamic assembly also happens
 * here in one of the case blocks.
 *
 */
public class ConnectionStringFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionStringFactory.class);

    public static final String SAMPLE_GET_SERVICE_RESPONSE_CONNECTION_STRING_KEY = "sampleGetServiceResponse";

    /**
     *
     * @param key
     * @return
     */
    public static Optional<String> getConnectionString(String key) {

        switch (key) {

            case SAMPLE_GET_SERVICE_RESPONSE_CONNECTION_STRING_KEY:
                String sampleGetServiceSource = Properties.SAMPLE_GET_SERVICE_SOURCE_PROPERTY.getValue();
                return Optional.of(sampleGetServiceSource);

            default:
                return Optional.empty();
        }

    }

    /**
     * convenience method to keep boiler plate code from being duplicated everywhere -- throws an illegal argument
     * exception if the getConnectionString method returns an emtpy Optional for a given property.
     *
     * @param key
     * @return
     */
    public static String getConnectionStringOrThrow(String key) {
        Optional<String> connectionStringOptional = ConnectionStringFactory.getConnectionString(key);

        String connectionString;
        if (connectionStringOptional.isPresent() == false) {
            throw new IllegalArgumentException("no connection string found for key: " + key);
        } else {
            connectionString = connectionStringOptional.get();
        }

        return connectionString;
    }


}
