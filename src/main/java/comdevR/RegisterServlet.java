package comdevR;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
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
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	RequestDispatcher dispatcher =null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nom =request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String username =request.getParameter("name");
		String useremail =request.getParameter("email");
		String userpass =request.getParameter("pass");
		String re_userpass =request.getParameter("re_pass");
		String usermobile =request.getParameter("contact");
		
		
		
		PrintWriter out = response.getWriter();
		out.print(username);
		out.print(useremail);
		out.print(userpass);
		out.print(usermobile);
		
		if (username == null || username.equals("")) {
			 request.setAttribute("status", "invalideUsername");
        	dispatcher = request.getRequestDispatcher("registration.jsp");
        	dispatcher.forward(request, response);
		 }
		 
		 if (useremail == null || useremail.equals("")) {
			 request.setAttribute("status", "invalideEmail");
         	dispatcher = request.getRequestDispatcher("registration.jsp");
         	dispatcher.forward(request, response);
		 }
		 if (userpass == null || userpass.equals("")) {
			 request.setAttribute("status", "invalidePass");
        	dispatcher = request.getRequestDispatcher("registration.jsp");
        	dispatcher.forward(request, response);
		 }else if(!userpass.equals(re_userpass)) {
			request.setAttribute("status", "invalideconfirmepassword");
	        dispatcher = request.getRequestDispatcher("registration.jsp");
	        dispatcher.forward(request, response);
		 }
		 if (usermobile == null || usermobile.equals("")) {
			 request.setAttribute("status", "invalideMobile");
         	dispatcher = request.getRequestDispatcher("registration.jsp");
         	dispatcher.forward(request, response);
		 }else if(usermobile.length()>10 || usermobile.length()<10) {
			 request.setAttribute("status", "invalideMobileLength");
	         	dispatcher = request.getRequestDispatcher("registration.jsp");
	         	dispatcher.forward(request, response);
		 }
		 if (nom == null || nom.equals("")) {
			 request.setAttribute("status", "Invalide name");
        	dispatcher = request.getRequestDispatcher("registration.jsp");
        	dispatcher.forward(request, response);
		 }
		 if (prenom == null || prenom.equals("")) {
			 request.setAttribute("status", "Invalide surname");
        	dispatcher = request.getRequestDispatcher("registration.jsp");
        	dispatcher.forward(request, response);
		 }
		 
		 User user = new User(nom, prenom, username, useremail, userpass, usermobile);
		 try {
				// Créer une factory de connexion ActiveMQ pour connecter avec le broker
		       // ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.64:61616");
			  ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");   
			 //Créeer une connexion avec ActiveMQ
		        javax.jms.Connection connection = connectionFactory.createConnection();
		        connection.start();
		        
		      //créer la session qui va permettre la communication
		        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		        
		        /* topic*/
		        //Destination destination = session.createTopic("exemple.topic");
		        
		       //queue
		       Destination destination = session.createQueue("exemple.queue");
		       Destination replyQueue = session.createQueue("queue.replies");
		       
		       MessageProducer producer = session.createProducer(destination);
		       // consumer doit etre active car il y'a une dépendance temporelle (asyncrhone)
		       producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		       
		       // Envoi de l'objet dans la file d'attente
	           /*ObjectMessage message = session.createObjectMessage(user);
		       producer.send(message);*/
		       
		       String msg ="register" +";"+user.getName() +";"+ user.getEmail() + ";"+ user.getPassword() +";" + user.getMobile() +";"+ user.getNom()+";" +user.getPrenom();
		       TextMessage message = session.createTextMessage(msg);
		       
		       /*message.setJMSReplyTo(replyQueue);
		       message.setJMSCorrelationID("MaCorrelationID");*/
		       
		       message.setJMSReplyTo(replyQueue);
		       message.setJMSCorrelationID(UUID.randomUUID().toString()); // génère un nouvel ID de corrélation aléatoire

		       
		       
		       
		       producer.send(message);
		      
		       
		       
		       MessageConsumer consumer = session.createConsumer(replyQueue, "JMSCorrelationID = '" + message.getJMSCorrelationID() + "'");
		      // Message responseMessage = consumer.receive();
		       consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					// TODO Auto-generated method stub
					TextMessage textMessage = (TextMessage) message;
				       	try {
							System.out.println("Réponse reçue : " + textMessage.getText());
							int rowcount = Integer.parseInt(textMessage.getText());
							if (rowcount > 0){
				            	request.setAttribute("status", "success");
				            	System.out.println("Réponse reçue : " + request.getAttribute("status").toString());
				            }
				            else {
				            	request.setAttribute("status", "failed");
				            }
							 
						} catch (JMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
		    	   
		       });
		       
		       dispatcher = request.getRequestDispatcher("registration.jsp");
		       dispatcher.forward(request, response);
		       System.out.println("Message sent successfully");
		       
		       
		       //dans le cas ou je fais des test c'est préférable de la mettre à la fin
		       connection.start();
		       
			}catch(JMSException e) {    
				e.printStackTrace();
				}
		
		/*Connection con=null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
            PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, userpassword, useremail, usermobile, est_administrateur, est_super_user) VALUES (?, ?, ?, ?, 0, 0)");
            pst.setString(1, username);
            pst.setString(2, userpass);
            pst.setString(3, useremail);
            pst.setString(4, usermobile);
          
            
            int rowcount = pst.executeUpdate();
            dispatcher = request.getRequestDispatcher("registration.jsp");
            
            if (rowcount > 0){
            	request.setAttribute("status", "success");
            }
            else {
            	request.setAttribute("status", "failed");
            }
    		
            dispatcher.forward(request, response);
            
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	
	}

}
