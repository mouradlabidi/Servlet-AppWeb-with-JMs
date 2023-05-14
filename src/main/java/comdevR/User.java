package comdevR;

import java.io.Serializable;

public class User implements Serializable{
	
	    private int id;
	    private String nom;
	    private String prenom;
	    private String name;
	    private String email;
	    private String password;
	    private String mobile;
	    private int isadmin;
	    private int isSuperUser;
	    
	    public User( String email, String password) {
	    	this.email = email;
	        this.password = password;
	    }
	    
	    public User( String nom, String prenom, String username, String email, String password, String mobile) {
	    	this.setNom(nom);
	    	this.setPrenom(prenom);
	        this.name = username;
	        this.email = email;
	        this.password = password;
	        this.mobile = mobile;
	        this.isadmin=0;
	        this.isSuperUser=0;
	    }

	    public User(String nom, String prenom,String username, String email, String mobile) {
			// TODO Auto-generated constructor stub
	    	this.setNom(nom);
	    	this.setPrenom(prenom);
	    	this.name = username;
	    	this.email = email;
	    	this.mobile = mobile;
		}

		public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public int isSuperUser() {
	        return isSuperUser;
	    }

	    public void setSuperUser(int superUser) {
	        isSuperUser = superUser;
	    }

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public int getIsadmin() {
			return isadmin;
		}

		public void setIsadmin(int isadmin) {
			this.isadmin = isadmin;
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public String getPrenom() {
			return prenom;
		}

		public void setPrenom(String prenom) {
			this.prenom = prenom;
		}
}


