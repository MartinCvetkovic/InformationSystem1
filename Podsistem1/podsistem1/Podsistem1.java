package podsistem1;

import entities.Filijala;
import entities.Komitent;
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

    private static void sendListToSubsystemWithTag(JMSContext context, JMSProducer producer, Query query, String table) throws JMSException {
        List resultList = query.getResultList();
        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
        objMsg.setStringProperty("tabela", table);
        objMsg.setStringProperty("funkcija16", "true");
        producer.send(topic, objMsg);
    }
    
    private static void sendListToServer(JMSContext context, JMSProducer producer, Query query) throws JMSException {
        List resultList = query.getResultList();
        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
        objMsg.setStringProperty("za", "cs");
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
                    } else if (textMessage.getText().equals("cs:1")) {
                        Mesto m = new Mesto();
                        m.setNaziv(textMessage.getStringProperty("naziv"));
                        m.setPosBroj(textMessage.getStringProperty("posBroj"));
                        m.setIdMes(textMessage.getIntProperty("IdMes"));
                        em.getTransaction().begin();
                        em.persist(m);
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:2")) {
                        Filijala f = new Filijala();
                        f.setNaziv(textMessage.getStringProperty("naziv"));
                        f.setAdresa(textMessage.getStringProperty("adresa"));
                        f.setIdMes(textMessage.getIntProperty("IdMes"));
                        f.setIdFil(textMessage.getIntProperty("IdFil"));
                        em.getTransaction().begin();
                        em.persist(f);
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:3")) {
                        Komitent k = new Komitent();
                        k.setNaziv(textMessage.getStringProperty("naziv"));
                        k.setAdresa(textMessage.getStringProperty("adresa"));
                        k.setIdMes(textMessage.getIntProperty("IdMes"));
                        k.setIdKom(textMessage.getIntProperty("IdKom"));
                        em.getTransaction().begin();
                        em.persist(k);
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:4")) {
                        int idMes = textMessage.getIntProperty("idMes");
                        int idKom = textMessage.getIntProperty("idKom");
                        em.getTransaction().begin();
                        em.find(Komitent.class, idKom).setIdMes(idMes);
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:10")) {
                        sendListToServer(context ,producer, em.createNamedQuery("Mesto.findAll"));
                    } else if (textMessage.getText().equals("cs:11")) {
                        sendListToServer(context ,producer, em.createNamedQuery("Filijala.findAll"));
                    } else if (textMessage.getText().equals("cs:12")) {
                        sendListToServer(context ,producer, em.createNamedQuery("Komitent.findAll"));
                    } else if (textMessage.getText().equals("cs:16")) {
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Mesto.findAll"), "Mesto");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Filijala.findAll"), "Filijala");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Komitent.findAll"), "Komitent");
                    }
                } catch (JMSException ex) {
                    if(em.getTransaction().isActive())
                        em.getTransaction().rollback();
                }
            }
        }
    }

}
