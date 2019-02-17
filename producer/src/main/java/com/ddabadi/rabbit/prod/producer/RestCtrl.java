package com.ddabadi.rabbit.prod.producer;


import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "api")
public class RestCtrl {

    private final static String QUEUE_NAME = "products_queue";

    @GetMapping(value = "send/{QName}" )
    public String sendData(@PathVariable("QName")String QName) throws IOException, TimeoutException,
            KeyManagementException, NoSuchAlgorithmException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setUsername(userName);
//        factory.setPassword(password);
//        factory.setVirtualHost(virtualHost);
//        factory.setHost(hostName);
//        factory.setPort(portNumber);
        factory.setAutomaticRecoveryEnabled(true);
        // attempt recovery every 10 seconds
        factory.setNetworkRecoveryInterval(10000);
//        factory.useSslProtocol();


//        Address[] addresses = {new Address("192.168.1.4"), new Address("192.168.1.5")};
//        factory.newConnection(addresses);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(QName, false, false, false, null);
        for (int i=1; i<=5; i++){

            String message = "product details " + i*3;
//            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//            channel.basicPublish("", QName,
//                    null, message.getBytes());
            channel.basicPublish("", QName,
                    new AMQP.BasicProperties.Builder()
                            .priority(0)
                            .build(),
                    message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
        return "produce";
    }

}
