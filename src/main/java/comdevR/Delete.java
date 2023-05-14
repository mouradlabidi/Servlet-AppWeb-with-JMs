package comdevR;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String scroll = request.getParameter("scroll");
		System.out.println(scroll);
		 try {
				// Créer une factory de connexion ActiveMQ pour connecter avec le broker
		        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		        //Créeer une connexion avec ActiveMQ
		        javax.jms.Connection connection = connectionFactory.createConnection();
		        connection.start();
		        
		      //créer la session qui va permettre la communication
		        Session sess= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		        
		        /* topic*/
		        //Destination destination = session.createTopic("exemple.topic");
		        
		       //queue
		       Destination destination = sess.createQueue("exemple.queue");
		       Destination replyQueue = sess.createQueue("queue.replies");
		       
		       MessageProducer producer = sess.createProducer(destination);
		       // consumer doit etre active car il y'a une dépendance temporelle (asyncrhone)
		       producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		       
		       // Envoi de l'objet dans la file d'attente
	           /*ObjectMessage message = session.createObjectMessage(user);
		       producer.send(message);*/
		 
		       
		       String msg ="delete" +";"+scroll ;
		       TextMessage message = sess.createTextMessage(msg);
		       
		       /*message.setJMSReplyTo(replyQueue);
		       message.setJMSCorrelationID("MaCorrelationID");*/
		       message.setJMSReplyTo(replyQueue);
		       message.setJMSCorrelationID(UUID.randomUUID().toString()); // génère un nouvel ID de corrélation aléatoire

		       
		       producer.send(message);
		       MessageConsumer consumer = sess.createConsumer(replyQueue, "JMSCorrelationID = '" + message.getJMSCorrelationID() + "'");
			     
				
				try {
				    Message responseMessage = consumer.receive();
				    if (responseMessage instanceof TextMessage) {
				        TextMessage textMessage = (TextMessage) responseMessage;
				        System.out.println("Réponse reçue : " + textMessage.getText());
				        if(textMessage.getText().equals("true") ) {
				        	System.out.println("fait");
				        }else if (textMessage.getText().equals("") || textMessage.getText().equals("false")) {
				        	  //
				        	System.out.println("true");
				        	System.out.println("true");
				        }

						 /*RequestDispatcher dispatcher = request.getRequestDispatcher("propos.jsp");
				         dispatcher.forward(request, response);*/
				        response.sendRedirect(request.getRequestURI());

				         System.out.println("false");
				         System.out.println("false");
				    }
				} catch (JMSException e) {
					System.out.println("false");
				    e.printStackTrace();
				}
				
				
		/*java.sql.Connection con=null;
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
            PreparedStatement pst = con.prepareStatement("delete from users Where username = ?");
            pst.setString(1, scroll);
            int rs =  pst.executeUpdate();
            if(rs>0) {
            	
            	System.out.println("true");
            }
            else {
            	System.out.println("false");
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("propos.jsp");
            dispatcher.forward(request, response);
           // request.getRequestDispatcher("propos.jsp").forward(request, response);
           // pst.setString(2, liste.get(2));*/
		}	catch(Exception e) {
		
		}
	}

}
