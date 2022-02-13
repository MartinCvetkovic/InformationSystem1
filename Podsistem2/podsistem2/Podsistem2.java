package podsistem2;

import entities.Isplata;
import entities.Komitent;
import entities.Prenos;
import entities.Racun;
import entities.Transakcija;
import entities.Uplata;
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
    
    private static void sendListToSubsystemWithTag(JMSContext context, JMSProducer producer, Query query, String table) throws JMSException {
        List resultList = query.getResultList();
        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
        objMsg.setStringProperty("tabela", table);
        objMsg.setStringProperty("funkcija16", "true");
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
                    } else if (textMessage.getText().equals("cs:6")) {
                        int idRac = textMessage.getIntProperty("idRac");
                        em.getTransaction().begin();
                        em.find(Racun.class, idRac).setStatus("Ugasen");
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:7")) {
                        em.getTransaction().begin();
                        Racun r = em.find(Racun.class, textMessage.getIntProperty("IdRac"));
                        if (r.getStatus().equals("Aktivan")) {
                            Transakcija t = new Transakcija();
                            t.setIdTra(textMessage.getIntProperty("IdTra"));
                            t.setDatumVreme(textMessage.getStringProperty("DatumVreme"));
                            t.setIznos(textMessage.getFloatProperty("Iznos"));
                            t.setSvrha(textMessage.getStringProperty("Svrha"));
                            t.setIdRac(textMessage.getIntProperty("IdRac"));
                            Prenos u = new Prenos();
                            u.setIdTra(textMessage.getIntProperty("IdTra"));
                            u.setNaRac(textMessage.getIntProperty("NaRac"));
                            r.setStanje(r.getStanje() - t.getIznos());
                            if (r.getStanje() <= -r.getDozvMinus()) {
                                r.setStatus("Blokiran");
                            }
                            int br = r.getBrTransakcija();
                            r.setBrTransakcija(br + 1);
                            t.setRedBr(br + 1);
                            Racun naRac = em.find(Racun.class, textMessage.getIntProperty("NaRac"));
                            naRac.setStanje(naRac.getStanje() + t.getIznos());
                            if (naRac.getStatus().equals("Blokiran") && naRac.getStanje() > -naRac.getDozvMinus()) {
                                naRac.setStatus("Aktivan");
                            }
                            naRac.setBrTransakcija(naRac.getBrTransakcija() + 1);
                            em.persist(t);
                            em.persist(u);
                            em.flush();
                        }
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:8")) {
                        Transakcija t = new Transakcija();
                        t.setIdTra(textMessage.getIntProperty("IdTra"));
                        t.setDatumVreme(textMessage.getStringProperty("DatumVreme"));
                        t.setIznos(textMessage.getFloatProperty("Iznos"));
                        t.setSvrha(textMessage.getStringProperty("Svrha"));
                        t.setIdRac(textMessage.getIntProperty("IdRac"));
                        Uplata u = new Uplata();
                        u.setIdTra(textMessage.getIntProperty("IdTra"));
                        u.setIdFil(textMessage.getIntProperty("IdFil"));
                        em.getTransaction().begin();
                        Racun r = em.find(Racun.class, textMessage.getIntProperty("IdRac"));
                        r.setStanje(r.getStanje() + t.getIznos());
                        if (r.getStatus().equals("Blokiran") && r.getStanje() > -r.getDozvMinus()) {
                            r.setStatus("Aktivan");
                        }
                        int br = r.getBrTransakcija();
                        r.setBrTransakcija(br + 1);
                        t.setRedBr(br + 1);
                        em.persist(t);
                        em.persist(u);
                        em.flush();
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:9")) {
                        em.getTransaction().begin();
                        Racun r = em.find(Racun.class, textMessage.getIntProperty("IdRac"));
                        if (r.getStatus().equals("Aktivan")) {
                            Transakcija t = new Transakcija();
                            t.setIdTra(textMessage.getIntProperty("IdTra"));
                            t.setDatumVreme(textMessage.getStringProperty("DatumVreme"));
                            t.setIznos(textMessage.getFloatProperty("Iznos"));
                            t.setSvrha(textMessage.getStringProperty("Svrha"));
                            t.setIdRac(textMessage.getIntProperty("IdRac"));
                            Isplata u = new Isplata();
                            u.setIdTra(textMessage.getIntProperty("IdTra"));
                            u.setIdFil(textMessage.getIntProperty("IdFil"));
                            r.setStanje(r.getStanje() - t.getIznos());
                            if (r.getStanje() <= -r.getDozvMinus()) {
                                r.setStatus("Blokiran");
                            }
                            int br = r.getBrTransakcija();
                            r.setBrTransakcija(br + 1);
                            t.setRedBr(br + 1);
                            em.persist(t);
                            em.persist(u);
                            em.flush();
                        }
                        em.getTransaction().commit();
                    } else if (textMessage.getText().equals("cs:13")) {
                        int idKom = textMessage.getIntProperty("idKom");
                        Query q = em.createNamedQuery("Racun.findByIdKom");
                        q.setParameter("idKom", idKom);
                        List resultList = q.getResultList();
                        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
                        objMsg.setStringProperty("za", "cs");
                        producer.send(topic, objMsg);
                    } else if (textMessage.getText().equals("cs:14")) {
                        int idRac = textMessage.getIntProperty("idRac");
                        Query q = em.createNamedQuery("Transakcija.findByIdRac");
                        q.setParameter("idRac", idRac);
                        List resultList = q.getResultList();
                        ObjectMessage objMsg = context.createObjectMessage((Serializable) resultList);
                        objMsg.setStringProperty("za", "cs");
                        producer.send(topic, objMsg);
                    } else if (textMessage.getText().equals("cs:16")) {
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Racun.findAll"), "Racun");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Transakcija.findAll"), "Transakcija");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Isplata.findAll"), "Isplata");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Prenos.findAll"), "Prenos");
                        sendListToSubsystemWithTag(context, producer, em.createNamedQuery("Uplata.findAll"), "Uplata");
                    }
                } catch (JMSException ex) {
                }
            }
        }
    }

}
