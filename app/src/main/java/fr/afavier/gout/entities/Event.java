package fr.afavier.gout.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import fr.afavier.gout.http.ApiReturn;

public class Event extends Entities implements Serializable {
	private int id, nbParticipant;
	private String pseudo_organizer, name, country, city, postalCode, adresse, description;
	private boolean booking, active;
	private Date dateStart, dateEnd;
	private double lat, lng;
	
	private ArrayList<Tarif> listTarif;
	private ArrayList<Clientele> listClientele;
	private ArrayList<Categorie> listCategorie;
	private ArrayList<Participation> listParticipation;
	
    /** Les différents constructeurs **/
	/** Le constructeur de création d'event **/
	public Event(User me, String pseudo_organizer, String name, String country, String city, String postalCode, String adresse, String description, int booking, int active, String dateStart, String dateEnd, float lat, float lng) {
		super();
		
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("pseudo_organizer=" + pseudo_organizer);
		params.add("name=" + name);
		params.add("country=" + country);
		params.add("city=" + city);
		params.add("postalCode=" + postalCode);
		params.add("adresse=" + adresse);
		params.add("description=" + description);
		params.add("booking=" + booking);
		params.add("active=" + active);
		params.add("dateStart=" + dateStart);
		params.add("dateEnd=" + dateEnd);
		params.add("lat=" + lat);
		params.add("lng=" + lng);
		
		ApiReturn create = http.request("POST", "event/user/" + me.getPseudo() , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println(create.getList().get(0));
		}else {
			System.out.println(create.getList().get(0));
		}


		ApiReturn result = null;
		try {
			result = http.request("GET", "event/event/" + create.getList().get(0).get("id"), null, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(result.isSucess()) {
			ContructMe(result.getList().get(0));
		}else {
			System.out.println(create.getMessage());
		}
	}

	/** Le constructeur pour obtenir un event en particulier **/
	public Event(int id) {
		super();
		
		ApiReturn result = http.request("GET", "event/event/" + id, null, null);
		
		if(result.isSucess()) {
			ContructMe(result.getList().get(0));
		}else {
			System.out.println(result.getMessage());
		}
	}
	
	/** Le constructeur pour obtenir une liste d'event (appeler seulement depuis Entities) **/
	public Event(JSONObject event) {
		try {
			this.id = event.getInt("id");
			this.name = event.getString("name");
			this.pseudo_organizer = event.getString("pseudo_organizer");
			if(event.getString("description").equals("null")){
                this.description = "Sorry, no description available";
            }else{
                this.description = event.getString("description");
            }

            String bool = String.valueOf(event.get("booking"));
            if(bool.equals("1")){
                this.active = true;
            }else{
                this.active = false;
            }
            Log.i("DEBUG", String.valueOf(active));

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	/** Constructeur qui permet de crée un event COMPLET a partir d'un JSON **/
	private void ContructMe(JSONObject event) {
		try {
			this.id = event.getInt("id");
			this.pseudo_organizer = event.getString("pseudo_organizer");
			this.name = event.getString("name");
			this.country = event.getString("country");
			this.city = event.getString("city");
			this.postalCode = event.getString("postalCode");
			this.adresse = event.getString("adresse");
            if(event.getString("description").equals("null")){
                this.description = "Sorry, no description available";
            }else{
                this.description = event.getString("description");
            }

            String bool = String.valueOf(event.get("booking"));
            if(bool.equals("1")){
                this.booking = true;
            }else{
                this.booking = false;
            }

            bool = String.valueOf(event.get("booking"));
            if(bool.equals("1")){
                this.active = true;
            }else{
                this.active = false;
            }

			this.dateStart =  this.dateApiToJAVA(event.getString("dateStart"));
			this.dateEnd = this.dateApiToJAVA(event.getString("dateEnd"));
			this.lat = event.getDouble("lat");
			this.lng = event.getDouble("lng");
            this.nbParticipant = event.getInt("nbParticipation");
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		listTarif = new ArrayList<Tarif>();
		listClientele = new ArrayList<Clientele>();
		listCategorie = new ArrayList<Categorie>();
		listParticipation = new ArrayList<Participation>();
		
		fillTarif();
		fillCategorie();
		fillClientele();		
	}
		
	/** Méthode qui remplisse automatiquement les Array d'information sur l'event **/
	private void fillTarif() {
		String route = "tarif/event/" + this.id;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			/** On verifie qu'il existe des tarifs dans le résult **/
			if(result.getHttpCode() != 204) {
				for(JSONObject json : result.getList()) {
					Tarif tarif = new Tarif(json);
					this.listTarif.add(tarif);
				}
			}
		}else {
			System.out.println(result.getMessage());
		}
	}
	
	private void fillClientele() {
		String route = "clientele/event/" + this.id;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			/** On verifie qu'il existe de la clientele dans le résult **/
			if(result.getHttpCode() != 204) {
				for(JSONObject json : result.getList()) {
					Clientele clientele = new Clientele(json);
					listClientele.add(clientele);
				}
			}
		}else {
			System.out.println(result.getMessage());
		}
	}
	
	private void fillCategorie() {
		String route = "categorie/event/" + this.id;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			/** On verifie qu'il existe des categorie dans le résult **/
			if(result.getHttpCode() != 204) {
				for(JSONObject json : result.getList()) {
					Categorie categorie = new Categorie(json);
					listCategorie.add(categorie);
				}
			}
		}else {
			System.out.println(result.getMessage());
		}
	}
	
	/** Méthode pour obtenir la liste des participant a un event **/
	private void fillParticipation() {
		String route = "participation/event/" + this.id;
		ApiReturn result = http.request("GET", route, null, null);
		
		if(result.isSucess()) {
			/** On verifie qu'il existe des participant dans le résult **/
			if(result.getHttpCode() != 204) {
				for(JSONObject json : result.getList()) {
					Participation participation = new Participation(json);
					listParticipation.add(participation);
				}
			}
		}else {
			System.out.println(result.getMessage());
		}
	}

	/** Méthodes de modification d'un event **/
	public void putActive(User me, int active) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("active=" + active);
		
		ApiReturn create = http.request("PUT", "event/active/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			if(active == 0) {
				this.active = false;
			}else {
				this.active = true;
			}

		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putBooking(User me, int booking) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("booking=" + booking);
		
		ApiReturn create = http.request("PUT", "event/booking/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			if(booking == 0) {
				this.booking = false;
			}else {
				this.booking = true;
			}
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putLocal(User me, String country, String city, String postalCode, String adresse, float lat, float lng) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("country=" + country);
		params.add("city=" + city);
		params.add("postalCode=" + postalCode);
		params.add("adresse=" + adresse);
		params.add("lat=" + lat);
		params.add("lng=" + lng);
		
		ApiReturn create = http.request("PUT", "event/local/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.country= country;
			this.city= city;
			this.postalCode= postalCode;
			this.adresse= adresse;
			this.lat= lat;
			this.lng= lng;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}

	public void putDate(User me, String dateStart, String dateEnd) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("dateStart=" + dateStart);
		params.add("dateEnd=" + dateStart);
		
		ApiReturn create = http.request("PUT", "event/dates/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putDescription(User me, String description) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("description=" + description);
		
		ApiReturn create = http.request("PUT", "event/description/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.description = description;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void putName(User me, String name) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("name=" + name);
		
		ApiReturn create = http.request("PUT", "event/description/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.name = name;
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	/** Méthodes d'ajout et de supression des categorie **/
	public void addCategorie(User me, int idCategorie) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idCategorie=" + idCategorie);
		
		ApiReturn create = http.request("POST", "categorie/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listCategorie.clear();
			this.fillCategorie();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void removeCategorie(User me, int idCategorie) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idCategorie=" + idCategorie);
		
		ApiReturn create = http.request("DELETE", "categorie/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listCategorie.clear();
			this.fillCategorie();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	/** Méthodes d'ajout et de supression de la clientele **/
	public void addClientele(User me, int idClientele) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idClientele=" + idClientele);
		
		ApiReturn create = http.request("POST", "clientele/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listClientele.clear();
			this.fillClientele();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void removeClientele(User me, int idClientele) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("idClientele=" + idClientele);
		
		ApiReturn create = http.request("DELETE", "clientele/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listClientele.clear();
			this.fillClientele();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}

	/** Méthode d'ajout et de supression des tarif **/
	public void addTarif(User me, String name, float price) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("name=" + name);
		params.add("price=" + price);
		
		ApiReturn create = http.request("POST", "tarif/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listTarif.clear();
			this.fillTarif();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	public void removeTarif(User me, int id) {
		ArrayList<String> params = new ArrayList<String>();
		
		params.add("id=" + id);
		
		ApiReturn create = http.request("DELETE", "tarif/event/" + this.id , me.getAuthToken(), params);
		
		if(create.isSucess()) {
			System.out.println("ok : " + create.getList().get(0));
			this.listTarif.clear();
			this.fillTarif();
		}else {
			System.out.println("ko : " + create.getList().get(0));
		}
	}
	
	/** Les GETTER **/
	public int getId() {
		return id;
	}

	public String getPseudo_organizer() {
		return pseudo_organizer;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getDescription() {
		return description;
	}

	public boolean isBooking() {
		return booking;
	}

	public boolean isActive() {
		return active;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

    public int getNbParticipant() {
        return nbParticipant;
    }

	public ArrayList<Tarif> getListTarif() {
		return listTarif;
	}

	public ArrayList<Clientele> getListClientele() {
		return listClientele;
	}

	public ArrayList<Categorie> getListCategorie() {
		return listCategorie;
	}

	public ArrayList<Participation> getListParticipation() {
		this.fillParticipation();
		return listParticipation;
	}	

	/** TOSTRING : Seulement utile pour les tests **/
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", nbParticipant=" + nbParticipant +
                ", pseudo_organizer='" + pseudo_organizer + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", adresse='" + adresse + '\'' +
                ", description='" + description + '\'' +
                ", booking=" + booking +
                ", active=" + active +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
