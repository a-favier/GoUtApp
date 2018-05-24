package fr.afavier.gout.entities;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Participation implements Serializable {
	private int id;
	private String pseudo;
	
	public Participation(JSONObject participation) {
		super();
		try {
			this.id = participation.getInt("id");
			this.pseudo = participation.getString("pseudo");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Participant [id=" + id + ", pseudo=" + pseudo + "]";
	}

	public int getId() {
		return id;
	}

	public String getPseudo() {
		return pseudo;
	}
}
