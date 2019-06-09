package com.davidholiday.camel.harness.processors;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VerneMqProducerProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerneMqProducerProcessor.class.getName());

    public void process(Exchange exchange) {

        // TODO move this to the config file
        String topic = "mqtt_lab_topic";
        String message_content = "mqtt_message_content";
        int quality_of_service = 2; // 0, 1, 2
        String broker = "tcp://localhost:32799";
        String clientId = this.toString();
        MemoryPersistence memoryPersistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, memoryPersistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            mqttClient.connect(connOpts);
            LOGGER.info("connected to broker: {}", broker);

            MqttMessage mqttMessage = new MqttMessage(message_content.getBytes());
            mqttMessage.setQos(quality_of_service);
            mqttClient.publish(topic, mqttMessage);
            LOGGER.info("published to topic: {}    message {}", topic, message_content);

            mqttClient.disconnect();
            LOGGER.info("disconnected from broker: {}", broker);

        } catch(MqttException me) {
            LOGGER.error("reason "+me.getReasonCode());
            LOGGER.error("msg "+me.getMessage());
            LOGGER.error("loc "+me.getLocalizedMessage());
            LOGGER.error("cause "+me.getCause());
            LOGGER.error("excep "+me);
        }

    }

}
