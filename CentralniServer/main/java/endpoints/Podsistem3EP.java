/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Mesto;
import java.util.List;
import java.util.Vector;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

/**
 *
 * @author Martin
 */
@Path("podsistem3")
public class Podsistem3EP {

    @Resource(lookup = "bankaConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "bankaTopic")
    private Topic topic;

    @GET
    @Path("sve")
    public Response getSve() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");

        producer.send(topic, context.createTextMessage("cs:15"));
        Message message = consumer.receive();
        ObjectMessage objMsg;
        int i = 0;
        List lista = null;
        while (true) {
            try {
                if (message instanceof ObjectMessage) {
                    objMsg = (ObjectMessage) message;
                    if (objMsg.getStringProperty("za") != null && objMsg.getStringProperty("za").equals("cs")) {
                        i++;
                        if (lista == null) {
                            lista = new Vector();
                        }
                        lista.add((List) objMsg.getObject());

                        if (i == 8) {
                            break;
                        }
                    }
                }
            } catch (JMSException ex) {
            }
            message = consumer.receive();
        }
        if (lista != null && !lista.isEmpty()) {
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<List<Object>>>(lista) {
            }).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("razlika")
    public Response getRazlika() {
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer = context.createSharedConsumer(topic, "cs");

        producer.send(topic, context.createTextMessage("cs:16"));
        Message message = consumer.receive();
        ObjectMessage objMsg;
        List lista = null;
        int i = 0;
        while (true) {
            try {
                if (message instanceof ObjectMessage) {
                    objMsg = (ObjectMessage) message;
                    if (objMsg.getStringProperty("za") != null && objMsg.getStringProperty("za").equals("cs")) {
                        i++;
                        if (lista == null) {
                            lista = new Vector();
                        }
                        Object o = objMsg.getObject();
                        if(o != null)
                            lista.add((List) o);

                        if (i == 8) {
                            break;
                        }
                    }
                }
            } catch (JMSException ex) {
            }
            message = consumer.receive();
        }
        if (lista != null && !lista.isEmpty()) {
            return Response.status(Response.Status.OK).entity(new GenericEntity<List<List<Object>>>(lista) {}).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
