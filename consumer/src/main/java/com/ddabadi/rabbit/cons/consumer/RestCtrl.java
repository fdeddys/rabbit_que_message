package com.ddabadi.rabbit.cons.consumer;

import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "api")
public class RestCtrl {

    private final static String QUEUE_NAME = "products_queue";

    @GetMapping(value = "consume")
    public String getAllMsg() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        return "consume";
    }

    @GetMapping(value = "consume2/{QName}")
    public String get2(@PathVariable("QName")String QName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        GetResponse response = channel.basicGet(QUEUE_NAME, true);
        channel.queueDeclare(QName, false, false, false, null);
        GetResponse response = channel.basicGet(QName, true);
        if (response != null) {
            String message = new String(response.getBody(), "UTF-8");
//            System.out.println(message);
            return message;
        }
        System.out.println("[*] waiting for messages. To exit press CTRL+C");
        return "tidak ada";
    }

    @GetMapping(value = "total/{QName}")
    public String getTotalPerQ(@PathVariable("QName")String QName) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        AMQP.Queue.DeclareOk response = channel.queueDeclarePassive(QName);
// returns the number of messages in Ready state in the queue
//        response.getMessageCount();
        return String.valueOf(response.getMessageCount());
    }
//        QueueingConsumer consumer = new QueueingConsumer(channel);
//        channel.basicConsume(QUEUE_NAME, consumer);
//        while(true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            int n = channel.queueDeclarePassive(QUEUE_NAME).getMessageCount();
//            System.out.println(n);
//            if(delivery != null) {
//                byte[] bs = delivery.getBody();
//                System.out.println(new String(bs));
//                //String message= new String(delivery.getBody());
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                //System.out.println("[x] Received '"+message);
//            }
//        }
//        channel.basicGet(QUEUE_NAME, true);


//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.basicQos(1);
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
//        return "duah";


    @GetMapping(value = "consume3")
    public String get3() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
//        factory.setPort(Integer.parseInt(port));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages.");

//        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, null);

//        QueueingConsumer.Delivery delivery = consumer.nextDelivery(1000);
//        while (delivery != null) {
//            String message = new String(delivery.getBody());
//            System.out.println(" [x] Received '" + message + "'");
//            delivery = consumer.nextDelivery(1000);
//        }
//        channel.close();
//        connection.close();
        return ("tiga");
    }

}
