package com.davidholiday.camel.harness.processors;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VerneMqProducerProcessor implements Processor {


    public void process(Exchange exchange) {

        String topic = "mqtt_topic";
        String message_content = "mqtt_message_content";



    }

}
