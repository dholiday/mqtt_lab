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
        String topic = "mqtt_topic";
        String message_content = "mqtt_message_content";
        int quality_of_service = 2; // 0, 1, 2
        String broker = "http://localhost:32799";
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
            LOGGER.info("publishing message: {} to topic: {}", message_content, topic);

            mqttClient.disconnect();
            LOGGER.info("disconnected from broker: {}", broker);

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }

    }

}
