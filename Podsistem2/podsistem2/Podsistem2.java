package podsistem2;

import java.io.Serializable;
import java.util.List;
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
public class Podsistem2 {

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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");
        EntityManager em = emf.createEntityManager();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "ps2");
        
        while (true) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    if (textMessage.getText().equals("ps3")) {
                        sendListToSubsystem(context, producer, em.createNamedQuery("Racun.findAll"), "Racun");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Transakcija.findAll"), "Transakcija");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Isplata.findAll"), "Isplata");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Prenos.findAll"), "Prenos");
                        sendListToSubsystem(context, producer, em.createNamedQuery("Uplata.findAll"), "Uplata");
                    } else if (textMessage.getText().equals("cs")) {
                        // TODO
                        System.out.println("Poruka primljena iz cs u ps2");
                    }
                } catch (JMSException ex) {
                }
            }
        }
    }

}
