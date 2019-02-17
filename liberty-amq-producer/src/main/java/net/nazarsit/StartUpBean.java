package net.nazarsit;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;

@Singleton
@Startup
public class StartUpBean {

    @Resource(lookup = "jms/QueueConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/HelloQueue")
    Queue helloQueue;


    @PostConstruct
    private void postConstruct(){
        messageProducer();
    }

    private void messageProducer() {
            try {
                Connection jmsConnection = connectionFactory.createConnection();
                jmsConnection.start();
                Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(helloQueue);

                TextMessage message = session.createTextMessage("Hello world");

                producer.send(message);

            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println("Hello World sent");
    }


}
