package fr.afavier.gout.entities;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONException;
import org.json.JSONObject;

import fr.afavier.gout.http.ApiReturn;

public class User extends Entities implements Serializable {
	private String pseudo, firstName, lastName, mail, tel, authToken;
	
	private ArrayList<Event> myEvents;
	private Hashtable<Integer, Event> myParticipations;
		
	/** Les différents constructeurs **/
	/** Le constructeur de création d'utilisateur **/
	public User(String pseudo, String firstName, String lastName, String mail, String tel, String password) {
		super();
		
		ArrayList<String> params = new ArrayList<String>();
			
		params.add("pseudo=" + pseudo);
		params.add("firstName=" + firstName);
		params.add("lastName=" + lastName);
		params.add("mail=" + mail);
		params.add("tel=" + tel);
		params.add("password=" + password);
				
		ApiReturn create = http.request("POST", "user/" , null, params);
		
		if(create.isSucess()) {
			System.out.println(create.getList().get(0));
		}else {
			System.out.println(create.getList().get(0));
		}
		
		ApiReturn result = http.request("GET", "user/user/" + pseudo, null, null);
		
		if(result.isSucess()) {
			ContructMe(result.getList().get(0));
		}else {
			System.out.println("user not found");
		}
	}

	/** Le constructeur pour obtenir un utilisateur en particulier **/
	public User(String pseudo) {
		super();
		
		ApiReturn result = http.request("GET", "user/user/" + pseudo, null, null);
		
		if(result.isSucess()) {
			ContructMe(result.getList().get(0));
		}else {
			System.out.println("user not found");
		}
	}
	
	/** Le constructeur d'identification **/
	public User(String pseudo, String password) {
		super();
		
		ArrayList<String> params = new ArrayList<String>();
		params.add("pseudo=" + pseudo);
		params.add("password=" + password);
		
		ApiReturn result = http.request("GET", "auth/login/", null, params);
		
		if(result.isSucess()) {
			ContructMe(result.getList().get(0));
		}else {
			System.out.println("user not found");
		}
	}
	
	/** Le constructeur pour obtenir une liste d'user (appeler seulement depuis Entities) **/
	public User(JSONObject user) {
		try {
			this.pseudo = user.getString("pseudo");
			this.firstName = user.getString("firstName");
			this.lastName = user.getString("lastName");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/** Constructeur qui permet de crée un utlisateur COMPLET a partir d'un JSON **/
	private void ContructMe(JSONObject user) {
		try {
			this.pseudo = user.getString("pseudo");
			this.firstName = user.getString("firstName");
			this.lastName = user.getString("lastName");
			this.mail = user.getString("mail");
			this.tel = user.getString("tel");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(!user.isNull("authtoken")) {
			try {
				this.authToken = (String) user.get("authtoken");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		myEvents = new ArrayList<Event>();
		
		String route = "event/user/" + this.pseudo;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			if( result.getHttpCode() != 204) {
				for(JSONObject json : result.getList()) {
					Event event = new Event(json);
					myEvents.add(event);
				}
			}
		}else {
			System.out.println("Erreur !!");
		}
	}
	
	/** Méthode permettant d'obtenir la liste des participation de l'utilisateur **/
	public void fillParticipation() {
		myParticipations = new Hashtable<Integer, Event>();
		
		String route = "participation/user/" + this.pseudo;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			for(JSONObject json : result.getList()) {
				Event event = new Event(json);
				try {
					myParticipations.put(json.getInt("id_event"), event);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("Erreur !!");
		}
	}
	
	/** Les méthode de modification d'user **/
	public void putNames(String firstName, String lastName) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("firstName=" + firstName);
		params.add("lastName=" + lastName);
		
		ApiReturn create = http.request("PUT", "user/names/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.firstName = firstName;
			this.lastName = lastName;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putMail(String mail) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("mail=" + mail);
		
		ApiReturn create = http.request("PUT", "user/mail/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.mail = mail;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
		
	}
	
	public void putPassword(String old, String password) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("old=" + old);
		params.add("password=" + password);
		
		ApiReturn create = http.request("PUT", "user/password/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
		
	}
	
	public void putTel(String tel) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("tel=" + tel);
		
		ApiReturn create = http.request("PUT", "user/tel/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.tel = tel;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putBorn(String born) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("dateBorn=" + tel);
		
		ApiReturn create = http.request("PUT", "user/born/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			//this. = born;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}

	/** Gestion des participation **/
	public void addParticipation(int idEvent) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idEvent=" + idEvent);
		
		ApiReturn create = http.request("POST", "participation/user/" + this.pseudo , this.authToken, params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void removeParticipation(int idEvent) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idEvent=" + idEvent);
		
		ApiReturn delete = http.request("DELETE", "participation/user/" + this.pseudo , this.authToken, params);
		
		if(delete.isSucess()) {
			System.out.println("ok : " + delete.getList().get(0));
		}else {
			System.out.println("ko : " + delete.getList().get(0));
		}
	}
		
	/** Les GETTER **/
	public String getPseudo() {
		return pseudo;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMail() {
		return mail;
	}

	public String getTel() {
		return tel;
	}

	public String getAuthToken() {
		return authToken;
	}

	public ArrayList<Event> getMyEvents() {
		return myEvents;
	}

	public Hashtable<Integer, Event> getMyParticipations() {
		return myParticipations;
	}

	/** TOSTRING : Seulement utile pour les tests **/
	@Override
	public String toString() {
		return "User [pseudo=" + pseudo + ", firstName=" + firstName + ", lastName=" + lastName + ", mail=" + mail + ", tel=" + tel + ", authToken=" + authToken + ", myEvents=" + myEvents + ", myParticipations=" + myParticipations + "]";
	}
}
