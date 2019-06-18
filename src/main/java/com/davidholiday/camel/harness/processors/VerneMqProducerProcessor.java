package com.davidholiday.camel.harness.processors;


import java.util.concurrent.ThreadLocalRandom;

import com.davidholiday.camel.harness.config.Properties;

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

    private  Integer ageOutCount = ThreadLocalRandom.current().nextInt(1, 999);

    public void process(Exchange exchange) {

        //ageOutCount --;

        String topic = Properties.MQTT_TOPIC_PROPERTY.get();
        String message_content = "mqtt_message_content";
        int quality_of_service = new Integer(Properties.MQTT_QOS_PROPERTY.get()); // 0, 1, 2
        String broker = Properties.MQTT_BROKER_PROPERTY.get();
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

//            if (ageOutCount < 1) {
//                mqttClient.disconnect();
//                LOGGER.info("disconnected from broker: {} due to ageout", broker);
//                ageOutCount = ThreadLocalRandom.current().nextInt(1, 999);
//
//
//                mqttClient = new MqttClient(broker, clientId, memoryPersistence);
//                connOpts = new MqttConnectOptions();
//                mqttClient.connect(connOpts);
//                LOGGER.info("reconnected to broker: {}", broker);
//            }

        } catch(MqttException me) {
            LOGGER.error("reason "+me.getReasonCode());
            LOGGER.error("msg "+me.getMessage());
            LOGGER.error("loc "+me.getLocalizedMessage());
            LOGGER.error("cause "+me.getCause());
            LOGGER.error("excep "+me);
        }

    }


}
