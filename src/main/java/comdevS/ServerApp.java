package comdevS;

import java.util.ArrayList;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import comdevR.User;
import comdevR.UsersListes;

public class ServerApp {

	public static int insert(ArrayList<String> liste,TextMessage textmessage, Session session, Destination destination) {
		 int rowcount = 0;
		try {
			
			System.out.println("Received messgae: " +textmessage.getText());
			System.out.println("length: " +liste.size());
			java.sql.Connection con=null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
	            PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, userpassword, useremail, usermobile, est_administrateur, est_super_user, nom, prénom) VALUES (?, ?, ?, ?, 0, 0, ?, ?)");
	            System.out.println("length: " +liste.size());
	            pst.setString(1, liste.get(1));
	            pst.setString(2, liste.get(3));
	            pst.setString(3, liste.get(2));
	            pst.setString(4, liste.get(4));
	            pst.setString(5, liste.get(5));
	            pst.setString(6, liste.get(6));
	            
	            rowcount = pst.executeUpdate();
	            
	            con.close();
	            /*MessageProducer producer = session.createProducer(destination);
			    // consumer doit etre active car il y'a une dépendance temporelle (asyncrhone)
			    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			    String msg =String.valueOf(rowcount);
			    TextMessage messa = session.createTextMessage(msg);
			    producer.send(messa);
			    
			    ------------  dispatcher = request.getRequestDispatcher("registration.jsp");
	            
	            if (rowcount > 0){
	            	request.setAttribute("status", "success");
	            }
	            else {
	            	request.setAttribute("status", "failed");
	            }
	    		
	            dispatcher.forward(request, response);
	         }*/   
					}catch(Exception e) {
						e.printStackTrace();
					}finally{
						try {
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				catch(JMSException e){
					e.printStackTrace();
				}
		return rowcount;
	}
	//----------------------------------------
	private static String authentification(ArrayList<String> liste,TextMessage textmessage, Session session, Destination destination) {
		// TODO Auto-generated method stub
		
		 
				try {
					System.out.println("Received messgae: " +textmessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("length: " +liste.size());
				java.sql.Connection con=null;
				
				try {
			            Class.forName("com.mysql.cj.jdbc.Driver");
						con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
			            PreparedStatement pst = con.prepareStatement("select * from users Where useremail= ? and userpassword= ?");
			            System.out.println("length------: " +liste.size());
			            pst.setString(1, liste.get(1));
			            pst.setString(2, liste.get(2));
			            
			            ResultSet rs =  pst.executeQuery();
			           if (rs.next()) {
			        	   String useremaiName=rs.getString("useremail")+";"+rs.getString("nom");
			               System.out.println("problème : " +useremaiName);
			               return useremaiName;
			           }else {
			        	   return "inexiste";
			           }
			            /*String result;
			            if(rs.next()) {
			            	result="Next";
			            }
			            else {
			            	result="NoNext";
			            }*/
			           
				}catch(Exception e) {
					e.printStackTrace();
				}finally{
					try {
				           con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			return "";	
			
		}
	
		
	//-----------------------------------------
	
	public static List<User> getUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		java.sql.Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");;
            PreparedStatement pst = con.prepareStatement("select * from users");
            
            ResultSet rs= pst.executeQuery();
            while (rs.next()) {
            	if(Integer.parseInt(rs.getString("est_super_user"))==0 && Integer.parseInt(rs.getString("est_administrateur"))== 0) {
                User user = new User(rs.getString("nom"),rs.getString("prénom"),rs.getString("username"),rs.getString("useremail"),rs.getString("userpassword"),rs.getString("usermobile"));
                users.add(user);}
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	//-----------------------------------------
	public static String UserDelete(ArrayList<String> liste) {
		java.sql.Connection con=null;
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
            PreparedStatement pst = con.prepareStatement("delete from users Where username = ?");
            pst.setString(1, liste.get(1));
            int rs =  pst.executeUpdate();
            String res="false";;
            if(rs>0) {
            	res="true";
            	System.out.println("true");
            }
            else {
            	res="false";
            	System.out.println("false");
            }
            return res;
            /*RequestDispatcher dispatcher = request.getRequestDispatcher("propos.jsp");
            dispatcher.forward(request, response);*/
           // request.getRequestDispatcher("propos.jsp").forward(request, response);
           // pst.setString(2, liste.get(2));*/
		}	catch(Exception e) {
		
		}
		return"";
	}
	
	//-----------------------------------------
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {	
			// Créer une factory de connexion ActiveMQ pour connecter avec le broker
	        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
	        

	        //Créeer une connexion avec ActiveMQ
	        Connection connection = connectionFactory.createConnection();
	        
	        //créer la session qui va permettre la communication
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        /* topic*/
	         //Destination destination = session.createTopic("exemple.topic");
	         
	        //queue
	        Destination destination = session.createQueue("exemple.queue");
	        Destination replyQueue = session.createQueue("queue.replies");
	        
	        MessageConsumer consumer = session.createConsumer(destination);
	        System.out.println("Received user");
	        // mode asyncrhone (abonnée dans le broker) (getreceive dans le cas de synchrone)
	        consumer.setMessageListener(new MessageListener() {
	        	public void onMessage(Message message) {
	        		if (message instanceof TextMessage) {
	        			System.out.println("here");
	        			TextMessage textmessage = (TextMessage) message;
	        			String[] sousChaines;
					try {
									sousChaines = textmessage.getText().split(";");
								
			        			ArrayList<String> liste = new ArrayList<String>();
			        			for (String sousChaine : sousChaines) {
			        				liste.add(sousChaine);
			        			    System.out.println(sousChaine);		   
			        			}
			        	System.out.println("Demande reçue : "+liste.get(0));
			     				
					        if(liste.get(0).equals("register")) {		
			        			 int rowcount =insert(liste, textmessage, session, destination);
			        			 
			        			 	
				        			 System.out.println("Demande reçue : " + textmessage.getText());
				
						            MessageProducer producer = session.createProducer(replyQueue);
						            String msg =String.valueOf(rowcount);
						            TextMessage responseMessage;
									
										responseMessage = session.createTextMessage();
									
						            responseMessage.setText(msg);
						            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
					
						            producer.send(responseMessage);
					         }
					        else if(liste.get(0).equals("login")) {
					        	System.out.println("login----------");
					        	String rs  = authentification(liste, textmessage, session, destination); 
					        	if (!rs.equals("") || !rs.equals("Inexiste")) {
									        	String[] 	Chaines = rs.split(";");
												System.out.println("Réponse reçue : " + rs);
												ArrayList<String> li = new ArrayList<String>();
							        			for (String Chaine : Chaines) {
							        				li.add(Chaine);
							        			    System.out.println(Chaine);		   
							        			}
					        	}			
									        	String useremaiName = rs;
									        	System.out.println("Demande reçue : " + useremaiName);
									        	
									        	// créer une nouvelle session et un nouveau producteur de message pour chaque réponse
									            Session replySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
									            MessageProducer replyProducer = replySession.createProducer(replyQueue);

										        	//MessageProducer producer = session.createProducer(replyQueue);
										            //String msg =String.valueOf(result);
										            TextMessage responseMessage;
													
														responseMessage = replySession .createTextMessage();
													//System.out.println("problème-------- : " + li.get(0));
													
										            responseMessage.setText(useremaiName.toString());
										            System.out.println("Demande reçue : " + useremaiName);
										            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
									
										            replyProducer.send(responseMessage);
										            System.out.println("Demande reçue : " + useremaiName);
						        	
					        	}else if(liste.get(0).equals("consulter")) {
						        		System.out.println("consulter-------- : ");
						        		List<User> myList = getUsers();
						        		
						        		Session replySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
							            MessageProducer replyProducer = replySession.createProducer(replyQueue);

	
							            //MessageProducer producer = session.createProducer(replyQueue);
							            String listAsString ="";
							            for (User element : myList) {
							            	String userString = element.getNom()+";"+ element.getPrenom()+";"+ element.getName() + ";" + element.getEmail() + ";" + element.getMobile();
							                if (listAsString.isEmpty()) {
							                    listAsString = userString;
							                } else {
							                    listAsString += "!" + userString;
							                }
							            }
							            System.out.println(listAsString);
							            TextMessage responseMessage;
										
										responseMessage = session.createTextMessage();
										
							            responseMessage.setText(listAsString);
							            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
						
							            replyProducer.send(responseMessage);
							            System.out.println(listAsString + "---------");
							            
						        	
					        	}else if(liste.get(0).equals("delete")) {
					        		String fin= UserDelete(liste);

					        		Session replySession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
						            MessageProducer replyProducer = replySession.createProducer(replyQueue);
						            TextMessage responseMessage;
									
									responseMessage = session.createTextMessage();
									
						            responseMessage.setText(fin);
						            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
					
						            replyProducer.send(responseMessage);
						            System.out.println(fin + "---------");

					        	}
					        
		        	} catch (JMSException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	        	  }
	        	}
	        });
	        
	        connection.start();
	        
			}catch(JMSException e) {    
				e.printStackTrace();
			}
	}
}
		
