package fr.afavier.gout.entities;

import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Categorie implements Serializable {
	private int id;
	private String name;
	
	public Categorie(JSONObject categorie) {
		super();
		try {
			this.id = categorie.getInt("id");
			this.name = categorie.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Categorie [id=" + id + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
