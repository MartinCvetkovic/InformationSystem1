package endpoints;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Martin
 */
@Path("podsistem1")
public class Podsistem1EP {

    @Resource(lookup = "bankaConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private Topic topic;

    @POST
    @Path("mesto")
    public Response createMesto(Mesto mesto){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:1");
        try {
            msg.setIntProperty("IdMes", mesto.getIdMes());
            msg.setStringProperty("naziv", mesto.getNaziv());
            msg.setStringProperty("posBroj", mesto.getPosBroj());
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Mesto je kreirano").build();
    }
    
    @GET
    @Path("mesto")
    public Response getMesta() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");

        producer.send(topic, context.createTextMessage("cs:10"));
        Message message = consumer.receive();
        ObjectMessage objMsg;
        while (true) {
            try {
                if(message instanceof ObjectMessage){
                    objMsg = (ObjectMessage)message;
                    if (objMsg.getStringProperty("za") != null && objMsg.getStringProperty("za").equals("cs")) {
                        break;
                    }
                }
            } catch (JMSException ex) {}
            message = consumer.receive();
        }
        List mesta = null;
        try {
            mesta = (List)objMsg.getObject();
        } catch (JMSException ex) {}
        if(mesta != null && !mesta.isEmpty())
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Mesto>>(mesta){}).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @POST
    @Path("filijala")
    public Response createFilijala(Filijala filijala){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:2");
        try {
            msg.setStringProperty("naziv", filijala.getNaziv());
            msg.setStringProperty("adresa", filijala.getAdresa());
            msg.setIntProperty("IdMes", filijala.getIdMes());
            msg.setIntProperty("IdFil", filijala.getIdFil());
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Filijala je kreirana").build();
    }
    
    @GET
    @Path("filijala")
    public Response getFilijale() {

        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");

        producer.send(topic, context.createTextMessage("cs:11"));
        Message message = consumer.receive();
        ObjectMessage objMsg;
        while (true) {
            try {
                if(message instanceof ObjectMessage){
                    objMsg = (ObjectMessage)message;
                    if (objMsg.getStringProperty("za") != null && objMsg.getStringProperty("za").equals("cs")) {
                        break;
                    }
                }
            } catch (JMSException ex) {}
            message = consumer.receive();
        }
        List mesta = null;
        try {
            mesta = (List)objMsg.getObject();
        } catch (JMSException ex) {}
        if(mesta != null && !mesta.isEmpty())
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Filijala>>(mesta){}).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @POST
    @Path("komitent")
    public Response createKomitent(Komitent komitent){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:3");
        try {
            msg.setStringProperty("naziv", komitent.getNaziv());
            msg.setStringProperty("adresa", komitent.getAdresa());
            msg.setIntProperty("IdMes", komitent.getIdMes());
            msg.setIntProperty("IdKom", komitent.getIdKom());
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Komitent je kreiran").build();
    }
    
    @PUT
    @Path("komitent{idKom}/{idMes}")
    public Response changeMestoForKomitent(@PathParam("idKom") int idKom, @PathParam("idMes") int idMes){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:4");
        try {
            msg.setIntProperty("idKom", idKom);
            msg.setIntProperty("idMes", idMes);
        } catch (JMSException ex) {
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.OK).entity("Promenjeno sediste komitenta").build();
    }
    
    @GET
    @Path("komitent")
    public Response getKomitenti() {

        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");

        producer.send(topic, context.createTextMessage("cs:12"));
        Message message = consumer.receive();
        ObjectMessage objMsg;
        while (true) {
            try {
                if(message instanceof ObjectMessage){
                    objMsg = (ObjectMessage)message;
                    if (objMsg.getStringProperty("za") != null && objMsg.getStringProperty("za").equals("cs")) {
                        break;
                    }
                }
            } catch (JMSException ex) {}
            message = consumer.receive();
        }
        List mesta = null;
        try {
            mesta = (List)objMsg.getObject();
        } catch (JMSException ex) {}
        if(mesta != null && !mesta.isEmpty())
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Komitent>>(mesta){}).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
