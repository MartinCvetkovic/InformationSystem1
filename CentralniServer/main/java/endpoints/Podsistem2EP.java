/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Mesto;
import entities.Racun;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
