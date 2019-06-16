package com.davidholiday.camel.harness.config;


import com.netflix.config.DynamicStringProperty;

import static com.davidholiday.camel.harness.helpers.DynamicPropertyHelpers.getOrGetDynamicStringProperty;


/**
 * will try to read from 'prod' properties location first, then failing that will read from local config.properties file
 */
public class Properties {

    // list of prod properties files to be ingested as additional app config variables that will override the list
    // set in the local property
    public static final DynamicStringProperty ADDITIONAL_PROPERTY_SOURCE_URLS_PROPERTY =
            getOrGetDynamicStringProperty(
                    "additional.properties.source.url",
                    "additional.properties.source.url.local"
            );


    // these two are an example of how to mock (or otherwise define different a different connection string) for calls
    // to external services
    public static final DynamicStringProperty SAMPLE_GET_SERVICE_SOURCE_PROPERTY =
            getOrGetDynamicStringProperty(
                    "sample.get.service.source",
                    "sample.get.service.source.local"
            );

    public static final DynamicStringProperty SAMPLE_GET_SERVICE_RESPONSE_MOCKFILE_PROPERTY =
            getOrGetDynamicStringProperty(
                    "sample.get.service.response.mockfile",
                    "sample.get.service.response.mockfile.local"
            );


    public static final DynamicStringProperty MQTT_TOPIC_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.topic",
                    "mqtt.topic.local"
            );

    public static final DynamicStringProperty MQTT_QOS_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.qos",
                    "mqtt.qos.local"
            );

    public static final DynamicStringProperty MQTT_BROKER_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.broker",
                    "mqtt.broker.local"
            );

    public static final DynamicStringProperty MQTT_PRODUCER_ROUTE_ENABLED_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.producer.route.enabled",
                    "mqtt.producer.route.enabled.local"
            );

    public static final DynamicStringProperty MQTT_CONSUMER_ROUTE_ENABLED_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.consumer.route.enabled",
                    "mqtt.consumer.route.enabled.local"
            );

    // unused for now as we're using the camel component for this
    public static final DynamicStringProperty MQTT_PRODUCER_NAME_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.producer.processor.name",
                    "mqtt.producer.processor.name.local"
            );

    public static final DynamicStringProperty MQTT_CONSUMER_NAME_PROPERTY =
            getOrGetDynamicStringProperty(
                    "mqtt.consumer.processor.name",
                    "mqtt.consumer.processor.name.local"
            );

}