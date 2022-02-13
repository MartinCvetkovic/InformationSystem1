package podsistem3;

import entities.Komitent;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
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
public class Podsistem3 {

    @Resource(lookup = "bankaConnectionFactory")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private static Topic topic;

    private static void sendListToServer(JMSContext context, JMSProducer producer, Query query) throws JMSException {
        List resultList = query.getResultList();
        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
        objMsg.setStringProperty("za", "cs");
        producer.send(topic, objMsg);
    }

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
                    if (textMessage.getText().equals("cs:15")) {
                        sendListToServer(context, producer, em.createNamedQuery("Mesto.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Filijala.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Komitent.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Racun.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Transakcija.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Uplata.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Isplata.findAll"));
                        sendListToServer(context, producer, em.createNamedQuery("Prenos.findAll"));
                    }
                } catch (JMSException ex) {
                }
            }
            if (message instanceof ObjectMessage) {
                try {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    if (objectMessage.getStringProperty("funkcija16") != null && objectMessage.getStringProperty("funkcija16").equals("true")) {
                        List result = (List) objectMessage.getObject();
                        String table = objectMessage.getStringProperty("tabela");
                        List ps3List = em.createNamedQuery(table + ".findAll").getResultList();
                        List resList = new Vector();
                        for (Object ent : result) {
                            boolean contains = false;
                            if (ps3List != null) {
                                for (Object ps3ent : ps3List) {
                                    if (ent.equals(ps3ent) && ent.toString().equals(ps3ent.toString())) {
                                        contains = true;
                                        break;
                                    }
                                }
                            }
                            if (contains == false) {
                                resList.add(ent);
                            }
                        }
                        ObjectMessage returnMessage = context.createObjectMessage((Serializable) resList);
                        returnMessage.setStringProperty("za", "cs");
                        producer.send(topic, returnMessage);
                    } else if (objectMessage.getStringProperty("za") == null || !objectMessage.getStringProperty("za").equals("cs")) {
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
                Thread.sleep(120 * 1000);
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
