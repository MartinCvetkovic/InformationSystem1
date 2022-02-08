package podsistem1;

import entities.Filijala;
import entities.Mesto;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Martin
 */
public class Podsistem1 {

    @Resource(lookup = "bankaConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private static Topic topic;

    private static void sendListToSubsystem(JMSContext context, JMSProducer producer, Query query, String table) throws JMSException {
        List resultList = query.getResultList();
        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
        objMsg.setStringProperty("tabela", table);
        producer.send(topic, objMsg);
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        EntityManager em = emf.createEntityManager();

        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "ps1");
        
        while (true) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    if (textMessage.getText().equals("ps3")) {
                        sendListToSubsystem(context, producer, em.createNamedQuery("Mesto.findAll"), "Mesto");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Filijala.findAll"), "Filijala");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Komitent.findAll"), "Komitent");
                    } else if (textMessage.getText().equals("cs")) {
                        // TODO
                        System.out.println("Poruka primljena iz cs u ps1");
                    }
                } catch (JMSException ex) {
                }
            }
        }
    }

}
