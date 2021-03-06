package net.nazarsit;


import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@WebServlet("/send")
public class SimpleJMSServlet extends HttpServlet {

    @Resource(lookup = "jms/QueueConnectionFactory")
    ConnectionFactory jmsConnectionFactory;

    @Resource(lookup = "jms/HelloQueue")
    Queue helloQueue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MediaType.TEXT_PLAIN);
        String msg = "hello";
        try {
            pushMessageToQueue(msg);
        } catch (JMSException e) {
            resp.setStatus(502);
            resp.getWriter().println("Failed to Push " + msg + " to queue");
            resp.getWriter().println(e);
            e.printStackTrace();
        }
        resp.setStatus(200);
        resp.getWriter().println("Pushed " + msg + " to queue");
    }

    private void pushMessageToQueue(String msg) throws JMSException{
        Connection con = jmsConnectionFactory.createConnection();
        Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        TextMessage message = session.createTextMessage(msg);
        session.createProducer(helloQueue).send(message);
    }

}
