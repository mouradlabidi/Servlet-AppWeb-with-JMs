package comdevR;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/login")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String result;
	RequestDispatcher dispatcher =null;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String useremail =request.getParameter("username");
		String userpass =request.getParameter("password");
		
		HttpSession session=request.getSession();
		 
		 if (useremail == null || useremail.equals("")) {
			 request.setAttribute("status", "invalideEmail");
         	dispatcher = request.getRequestDispatcher("login.jsp");
         	dispatcher.forward(request, response);
		 }
		 if (userpass == null || userpass.equals("")) {
			 request.setAttribute("status", "invalidePass");
         	dispatcher = request.getRequestDispatcher("login.jsp");
         	dispatcher.forward(request, response);
		 }
		 
		 try {
				// Créer une factory de connexion ActiveMQ pour connecter avec le broker
		        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		      //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.158.64:61616");
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
		 
		       
		       String msg ="login" +";"+useremail +";"+ userpass ;
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
					        if (!textMessage.getText().equals("") || !textMessage.getText().equals("Inexiste")) {
					            String[] Chaines = textMessage.getText().split(";");
					            System.out.println("Réponse reçue ---------: " + Chaines);
					            ArrayList<String> li = new ArrayList<String>();
					            for (String Chaine : Chaines) {
					                li.add(Chaine);
					                System.out.println(Chaine);           
					            }
					            System.out.println(session==null);  
					            request.setAttribute("users", li.get(1).toString());
					            System.out.println(li.get(1));    
					            result = li.get(0).toString();
					        } else {
					            result = "";
					        }
					    } else {
					        System.out.println("Réponse non valide.");
					    }
					} catch (JMSException e) {
					    e.printStackTrace();
					}
							       
		       /*
		       // Message responseMessage = consumer.receive();
		       consumer.setMessageListener(new MessageListener() {

		    	   
				@Override
				public void onMessage(Message message) {
					// TODO Auto-generated method stub
					TextMessage textMessage = (TextMessage) message;
					//RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
						try {
							System.out.println("Réponse reçue : " + textMessage.getText());
							//result = textMessage.getText();
								if (!textMessage.getText().equals("")|| !textMessage.getText().equals("Inexiste")) {
									String[] 	Chaines = textMessage.getText().split(";");
									System.out.println("Réponse reçue ---------: " + Chaines);
									ArrayList<String> li = new ArrayList<String>();
				        			for (String Chaine : Chaines) {
				        				li.add(Chaine);
				        			    System.out.println(Chaine);		   
				        			}
				        			request.setAttribute("users", li.get(1).toString());
										
				        			System.out.println(li.get(1));	
				        			result = li.get(0).toString();
						      }else {
						    	  result="";
						      }
						} catch (JMSException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
						}

		       }
	    	   
	       }); */ 
		       
			
		       if (!result.equals("") || !result.equals("Inexiste") ){
		    	   System.out.println(session==null) ;
					session.setAttribute("name", result);
	            	dispatcher = request.getRequestDispatcher("index.jsp");
	            	dispatcher.forward(request, response);
	            	System.out.println("Réponse reçue -----------: ") ;
	            	}
	            else {
	            	request.setAttribute("status", "failed");
	            	dispatcher = request.getRequestDispatcher("login.jsp");
	            	dispatcher.forward(request, response);
	            }
      
			       System.out.println("Message sent successfully");
			     //dans le cas ou je fais des test c'est préférable de la mettre à la fin
			       connection.start();
		 }catch(Exception e) {
					e.printStackTrace();
				}
		/*Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
            PreparedStatement pst = con.prepareStatement("select * from users Where useremail= ? and userpassword= ?");
            pst.setString(1, useremail);
            pst.setString(2, userpass);
            
            PrintWriter out = response.getWriter();
    		out.print(useremail);
    		out.print(userpass);
    		System.out.println(useremail);
            
            ResultSet rs= pst.executeQuery();
            if(rs.next()) {
            	session.setAttribute("name", rs.getString("username"));
            	dispatcher = request.getRequestDispatcher("index.jsp");
            }else{
            	request.setAttribute("status", "failed");
            	dispatcher = request.getRequestDispatcher("login.jsp");
            }
            dispatcher.forward(request, response);
            
        	System.out.println(useremail);
            
		}catch(Exception e) {
			e.printStackTrace();
		}*/
	}	 
}