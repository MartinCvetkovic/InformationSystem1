package podsistem3;

import entities.Komitent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Martin
 */
public class Podsistem3 {

    @Resource(lookup = "bankaConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private static Topic topic;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");
        EntityManager em = emf.createEntityManager();

        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "ps3");
        consumer.setMessageListener((message) -> {
            if (message instanceof TextMessage) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    if (textMessage.getText().equals("ps1")) {
                        // TODO
                        System.out.println("Poruka primljena iz ps1 u ps3");
                    } else if (textMessage.getText().equals("ps2")) {
                        // TODO
                        System.out.println("Poruka primljena iz ps2 u ps3");
                    }
                } catch (JMSException ex) {
                }
            }
            if (message instanceof ObjectMessage) {
                try {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    if (objectMessage.getStringProperty("za") == null || !objectMessage.getStringProperty("za").equals("cs")) {
                        List result = (List) objectMessage.getObject();
                        String table = objectMessage.getStringProperty("tabela");
                        List ps3List = em.createNamedQuery(table + ".findAll").getResultList();
                        for (Object ent : result) {
                            boolean contains = false;
                            if (ps3List != null) {
                                for (Object ps3ent : ps3List) {
                                    if (ent.equals(ps3ent) && !ent.toString().equals(ps3ent.toString())) {
                                        em.getTransaction().begin();
                                        em.remove(ps3ent);
                                        em.flush();
                                        em.getTransaction().commit();
                                        break;
                                    }
                                    if (ent.equals(ps3ent) && ent.toString().equals(ps3ent.toString())) {
                                        contains = true;
                                        break;
                                    }
                                }
                            }
                            if (contains == false) {
                                em.getTransaction().begin();
                                em.persist(ent);
                                em.flush();
                                em.getTransaction().commit();
                            }
                        }
                    }
                } catch (JMSException ex) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                }
            }
        });
        try {
            while (true) {
                Thread.sleep(30 * 1000);
                TextMessage textMessage = context.createTextMessage("ps3");
                producer.send(topic, textMessage);
                System.out.println("Poslata poruka iz ps3");
            }
        } catch (InterruptedException ex) {
        }
        em.close();
        emf.close();
    }

}
