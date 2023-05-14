package comdevR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UsersListes {
	
	public UsersListes() {
        // initialisez votre connexion à la base de données ici
    }
	public static List<User> getUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mourad?useSSL=false", "root", "");
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

	
}
