package fr.afavier.gout.entities;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Clientele implements Serializable {
	private int id;
	private String name;
	
	public Clientele(JSONObject clientele) {
		super();
		try {
			this.id = clientele.getInt("id");
			this.name = clientele.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Clientele [id=" + id + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
