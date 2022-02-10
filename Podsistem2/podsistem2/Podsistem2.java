package podsistem2;

import entities.Komitent;
import entities.Racun;
import java.io.Serializable;
import java.util.Date;
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
                    } else if (textMessage.getText().equals("cs:5")) {
                        Racun r = new Racun();
                        r.setIdRac(textMessage.getIntProperty("IdRac"));
                        r.setDatumVreme(textMessage.getStringProperty("DatumVreme"));
                        r.setStanje(textMessage.getFloatProperty("Stanje"));
                        r.setDozvMinus(textMessage.getFloatProperty("DozvMinus"));
                        r.setStatus(textMessage.getStringProperty("Status"));
                        r.setBrTransakcija(textMessage.getIntProperty("BrTransakcija"));
                        r.setIdFil(textMessage.getIntProperty("IdFil"));
                        r.setIdKom(textMessage.getIntProperty("IdKom"));
                        em.getTransaction().begin();
                        em.persist(r);
                        em.flush();
                        em.getTransaction().commit();
                    }
                } catch (JMSException ex) {
                }
            }
        }
    }

}
