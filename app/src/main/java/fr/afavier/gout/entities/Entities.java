package fr.afavier.gout.entities;

import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.afavier.gout.http.ApiReturn;
import fr.afavier.gout.http.Http;

public class Entities implements Serializable {
    SimpleDateFormat apiDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	SimpleDateFormat humanDate = new SimpleDateFormat("dd/MM/yyyy");
	Http http = new Http();
	
	protected Date dateApiToJAVA(String stringDate) {
        Date date = null;
        if(!stringDate.equals("null")) {
			try {
				date = apiDate.parse(stringDate.replaceAll(".000Z$", "+0000"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;
	}
	
	protected Date dateHumanToApi(String source){
		humanDate.setLenient(false);
		Date date = null;
		try {
		    date = humanDate.parse(source);
		}
		catch (Exception e) {
		    System.err.println("Format de date invalide. Usage : dd/MM/YYYY");
		    System.err.println(e.getMessage());
		}
		return date;
	}

	protected boolean booleanApiToJava(int intBoolean) {
		if(intBoolean == 0) {
			return false;
		}else {
			return true;
		}	
	}
	
	protected int booleanApiToJava(boolean bool) {
		if(bool) {
			return 1;
		}else {
			return 0;
		}	
	}
	
	public ArrayList<User> UserList(String pseudo){
		ArrayList<User> users = new ArrayList<User>();
		
		ApiReturn result = http.request("GET", "user/like/" + pseudo, null, null);
		
		if(result.isSucess()) {
			for(JSONObject json : result.getList()) {
				User user = new User(json);
				users.add(user);
			}
		}else {
			System.out.println(result.getMessage());
		}
		
		return users;
	}
	
	public ArrayList<Event> EventList(ArrayList<String> params){
		ArrayList<Event> events = new ArrayList<Event>();
		
		String route = "event/find?";
		
		for(String param : params) {
			route += param + "&";
		}
		route = route.substring(0, route.length()-1);
		
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			for(JSONObject json :  result.getList()) {
				Event event = new Event(json);
				events.add(event);
			}
		}else {
			System.out.println(result.getMessage());
		}
		
		return events;
		
	}
	
	public ArrayList<Categorie> CatgorieList(){
		ArrayList<Categorie> categories = new ArrayList<Categorie>();
		
		ApiReturn result = http.request("GET", "categorie", null, null);
		
		if(result.isSucess()) {
			for(JSONObject json : result.getList()) {
				Categorie categorie = new Categorie(json);
				categories.add(categorie);
			}
		}else {
			System.out.println(result.getMessage());
		}
		
		return categories;
	}
	
	public ArrayList<Clientele> ClienteleList(){
		ArrayList<Clientele> clienteles = new ArrayList<Clientele>();
		
		ApiReturn result = http.request("GET", "categorie", null, null);
		
		if(result.isSucess()) {
			for(JSONObject json : result.getList()) {
				Clientele clientele = new Clientele(json);
				clienteles.add(clientele);
			}
		}else {
			System.out.println(result.getMessage());
		}
		
		return clienteles;
	}
}
