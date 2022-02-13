/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Martin
 */
@Path("podsistem2")
public class Podsistem2EP {

    @Resource(lookup = "bankaConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private Topic topic;

    @GET
    @Path("racun/{idKom}")
    public Response getRacunForKomitent(@PathParam("idKom") int idKom){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");
        
        TextMessage msg = context.createTextMessage("cs:13");
        try {
            msg.setIntProperty("idKom", idKom);
        } catch (JMSException ex) {
        }
        producer.send(topic, msg);
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
        List racuni = null;
        try {
            racuni = (List)objMsg.getObject();
        } catch (JMSException ex) {}
        if(racuni != null && !racuni.isEmpty())
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Racun>>(racuni){}).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @POST
    @Path("prenos/{IdRac}")
    public Response createPrenos(Transakcija transakcija, @PathParam("IdRac") int idRac){
        if(transakcija == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Pogresan format transakcije").build();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:7");
        try {
            msg.setIntProperty("IdTra", transakcija.getIdTra());
            msg.setStringProperty("DatumVreme", transakcija.getDatumVreme());
            msg.setFloatProperty("Iznos", transakcija.getIznos());
            msg.setIntProperty("RedBr", transakcija.getRedBr());
            msg.setStringProperty("Svrha", transakcija.getSvrha());
            msg.setIntProperty("IdRac", transakcija.getIdRac());
            msg.setIntProperty("NaRac", idRac);
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Transakcija je kreirana").build();
    }
    
    @POST
    @Path("uplata/{IdFil}")
    public Response createUplata(Transakcija transakcija, @PathParam("IdFil") int idFil){
        if(transakcija == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Pogresan format transakcije").build();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:8");
        try {
            msg.setIntProperty("IdTra", transakcija.getIdTra());
            msg.setStringProperty("DatumVreme", transakcija.getDatumVreme());
            msg.setFloatProperty("Iznos", transakcija.getIznos());
            msg.setIntProperty("RedBr", transakcija.getRedBr());
            msg.setStringProperty("Svrha", transakcija.getSvrha());
            msg.setIntProperty("IdRac", transakcija.getIdRac());
            msg.setIntProperty("IdFil", idFil);
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Transakcija je kreirana").build();
    }
    
    @POST
    @Path("isplata/{IdFil}")
    public Response createIsplata(Transakcija transakcija, @PathParam("IdFil") int idFil){
        if(transakcija == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Pogresan format transakcije").build();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:9");
        try {
            msg.setIntProperty("IdTra", transakcija.getIdTra());
            msg.setStringProperty("DatumVreme", transakcija.getDatumVreme());
            msg.setFloatProperty("Iznos", transakcija.getIznos());
            msg.setIntProperty("RedBr", transakcija.getRedBr());
            msg.setStringProperty("Svrha", transakcija.getSvrha());
            msg.setIntProperty("IdRac", transakcija.getIdRac());
            msg.setIntProperty("IdFil", idFil);
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Transakcija je kreirana").build();
    }
    
    @GET
    @Path("transakcija/{idRac}")
    public Response getTransakcijaForRacun(@PathParam("idRac") int idRac){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");
        
        TextMessage msg = context.createTextMessage("cs:14");
        try {
            msg.setIntProperty("idRac", idRac);
        } catch (JMSException ex) {
        }
        producer.send(topic, msg);
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
        List transakcije = null;
        try {
            transakcije = (List)objMsg.getObject();
        } catch (JMSException ex) {}
        if(transakcije != null && !transakcije.isEmpty())
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<Transakcija>>(transakcije){}).build();
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Path("racun{idRac}")
    public Response closeRacun (@PathParam("idRac") int idRac){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:6");
        try {
            msg.setIntProperty("idRac", idRac);
        } catch (JMSException ex) {
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.OK).entity("Racun je zatvoren").build();
    }
    
    @POST
    @Path("racun")
    public Response createRacun(Racun racun){
        if(racun == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Pogresan format racuna").build();
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        TextMessage msg = context.createTextMessage("cs:5");
        try {
            msg.setIntProperty("IdRac", racun.getIdRac());
            msg.setStringProperty("DatumVreme", racun.getDatumVreme());
            msg.setFloatProperty("Stanje", racun.getStanje());
            msg.setFloatProperty("DozvMinus", racun.getDozvMinus());
            msg.setStringProperty("Status", racun.getStatus());
            msg.setIntProperty("BrTransakcija", racun.getBrTransakcija());
            msg.setIntProperty("IdFil", racun.getIdFil());
            msg.setIntProperty("IdKom", racun.getIdKom());
        } catch (JMSException ex) {
            Logger.getLogger(Podsistem1EP.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.send(topic, msg);
        
        return Response.status(Response.Status.CREATED).entity("Racun je kreiran").build();
    }
    
}
