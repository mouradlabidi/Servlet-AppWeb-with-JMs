package comdevR;

import java.util.ArrayList;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
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
 * Servlet implementation class consulter
 */
@WebServlet("/consulter")
public class consulter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public consulter() {
        super();
        // TODO Auto-generated constructor stub
    }
    List<User> users = new ArrayList<User>(); ;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
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
	       String msg ="consulter";
	       TextMessage message = sess.createTextMessage(msg);
	       
	       /*message.setJMSReplyTo(replyQueue);
	       message.setJMSCorrelationID("MaCorrelationID");*/
	       message.setJMSReplyTo(replyQueue);
	       message.setJMSCorrelationID(UUID.randomUUID().toString()); // génère un nouvel ID de corrélation aléatoire

	       
	       producer.send(message);
	       
	      MessageConsumer consumer = sess.createConsumer(replyQueue);
	       // Message responseMessage = consumer.receive();
		   consumer.setMessageListener(new MessageListener() {
			@Override
				public void onMessage(Message message) {
				// TODO Auto-generated method stub
				
				TextMessage textMessage = (TextMessage) message;
				//RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			       	try {
			       		String[] 	sousChaines = textMessage.getText().split("!");
						System.out.println("Réponse reçue : " + textMessage.getText());
						ArrayList<String> liste = new ArrayList<String>();
	        			for (String sousChaine : sousChaines) {
	        				liste.add(sousChaine);
	        			    System.out.println(sousChaine);		   
	        			}
	        			
	        			System.out.println("--------"+liste.get(0));
	        			
	        			for(int i=0; i<liste.size(); i++) {
	        				String[] 	sous = liste.get(i).split(";");
	        				ArrayList<String> usss = new ArrayList<String>();
	        				for (String sous1 : sous) {
		        				usss.add(sous1);
		        			    //System.out.println(sous1);		   
		        			}
	        				User us= new User(usss.get(0),usss.get(1),usss.get(2),usss.get(3),usss.get(4));
	        				users.add(us);
	        			    usss.clear();	
	        			}
	        			for(int i=0; i<users.size(); i++) {
	        				System.out.println(users.get(i).getName());		
	        			}
	        			request.setAttribute("users", users);
	        	        System.out.println("++++"+users.get(0).getEmail());
	        	       /* RequestDispatcher dispatcher = request.getRequestDispatcher("propos.jsp");
	        	        System.out.println("++++");*/
	        	        //dispatcher.forward(request, response);
	        	        //response.sendRedirect("propos.jsp");
	        			//List<User> users = (User) liste;	
	        	        System.out.println("++++"+users.get(0).getEmail());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
				  } 
		       });
		
		/*List<User> users;
		//try {
			users = UsersListes.getUsers();*/
		if(users!=null) {
			if(users != null && !users.isEmpty()) {
			    request.setAttribute("users", users);
			    System.out.println(users.get(0).getEmail());
			} 
			
		RequestDispatcher dispatcher = request.getRequestDispatcher("propos.jsp");
        dispatcher.forward(request, response);
        	request.getAttribute("users");
        	users.clear();
		}
		else {
			 System.out.println("problème");
		}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
