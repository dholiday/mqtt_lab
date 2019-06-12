package com.davidholiday.camel.harness.processors;


import com.davidholiday.camel.harness.config.Properties;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VerneMqConsumerProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerneMqProducerProcessor.class.getName());

    public void process(Exchange exchange) {

        String topic = Properties.MQTT_TOPIC_PROPERTY.get();
        String broker = Properties.MQTT_BROKER_PROPERTY.get();
        String clientId = this.toString();
        MemoryPersistence memoryPersistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, memoryPersistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            mqttClient.connect(connOpts);
            LOGGER.info("connected to broker: {}", broker);

            mqttClient.subscribe(topic);
            LOGGER.info("connecting to topic: {}", topic);
            mqttClient.setCallback(new MessageHandler());

            //mqttClient.disconnect();
            //LOGGER.info("disconnected from broker: {}", broker);

        } catch(MqttException me) {
            LOGGER.error("reason "+me.getReasonCode());
            LOGGER.error("msg "+me.getMessage());
            LOGGER.error("loc "+me.getLocalizedMessage());
            LOGGER.error("cause "+me.getCause());
            LOGGER.error("excep "+me);
        }

    }

    public class MessageHandler implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {
            LOGGER.error("connection lost error: {}", cause.toString());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            LOGGER.info("message arrived on topic: {}", topic);
            LOGGER.info("message is: {}", new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // TODO Auto-generated method stub

        }
    }

}