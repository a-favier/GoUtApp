package fr.afavier.gout.entities;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Tarif extends Entities implements Serializable {
	private int id;
	private String name;
	private double price;
	
	
	public Tarif(JSONObject tarif) {
		super();

		try {
			this.id = tarif.getInt("id");
			this.name = tarif.getString("name");
			this.price = tarif.getDouble("price");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String toString() {
		return "Tarif [id=" + id + ", name=" + name + ", price=" + price + "]";
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public double getPrice() {
		return price;
	}
	
	
	
	
}
