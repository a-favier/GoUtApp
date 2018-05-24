package fr.afavier.gout.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Handler;

import org.json.JSONException;
import org.json.JSONObject;


public class Http {	
	private HttpURLConnection connexion;
	private String url;
	
	/** Constructeur **/
	public Http() {
		/** Le constructeur initialise la connexion
		/** Récupération des conf de l'api + céation du lien de connexion **/
		this.url = "http://" + "192.168.1.11" + ":" + "3000" + "/" + "api" + "/";
	}
		
	/** Méthode qui crée la connexion avec le bon verbe et la bonne route **/
	private HttpURLConnection connexion(String method, String route) {
		URL completUrl;
		try {
			completUrl = new URL(this.url + route);

            connexion = (HttpURLConnection) completUrl.openConnection();
			connexion.setRequestMethod(method);
			connexion.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return connexion;
	}
	
	/** Méthode qui va definir le Token dans le header **/
	private HttpURLConnection defineToken(String token) {
		connexion.setRequestProperty("auth-token", token);
		return connexion;
	}
	
	/** Méthode qui va définir le body de la requête **/
	private HttpURLConnection writer(ArrayList<String> params) {
		try {		
			OutputStreamWriter out = new OutputStreamWriter(connexion.getOutputStream());
			// On insert les paramètres
			for(String line : params) {
				out.write(line);
				out.write('&');
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connexion;
	}
		
	/** Méthode qui récupère le résultat de la requête **/
	private ApiReturn reader(ApiReturn retour) {
		String result = null;
		BufferedReader in = null;

        try {
            retour.setHttpCode(connexion.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** On récupère le corp de la requête **/
		try {
			in = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			retour.setSucess(true);

		} catch (IOException e) {
			in = new BufferedReader(new InputStreamReader(connexion.getErrorStream()));
			retour.setSucess(false);
		}
		
		try {
			/** On crée un string en précisant l'encodage pour avoir les bon accents**/
			result = new String(in.readLine().getBytes(),Charset.forName("UTF-8"));
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		retour.setList(jsonToArray(result, retour));

		return retour;
	}

	/** Conversion d'un résult de l'API en arraylist de JSON **/
	private ArrayList<JSONObject> jsonToArray(String json, ApiReturn retour) {
		ArrayList<JSONObject> jsonArray = new ArrayList<JSONObject>();
				
		/** Si le result est vide on retourne un seul objet vide **/
		if(json.equals("[]")) {
			retour.setHttpCode(204);
			JSONObject emptyJson = null;
			try {
				emptyJson = new JSONObject("{}");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArray.add(emptyJson);
			return jsonArray;
		/** Sinon on retour l'array avec tous les objets **/
		}else {
			json = json.substring(1, json.length()-1);
			String values[]  = json.split("\\},");
			for(int i = 0; i < values.length; i++) {
				JSONObject generateJson = null;
				try {
					generateJson = new JSONObject(values[i] + "}");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jsonArray.add(generateJson);
			}
			return jsonArray;
		}
	}
	
	/** Méthode principal qui permet de faire toutes les requêtes **/
	public  ApiReturn request(String method, String route, String token, ArrayList<String> body){
		method = method.toUpperCase();

		/** On crée un array avec les valeur encoder en UTF-8 **/
		ArrayList<String> body_utf8 = new ArrayList<String>();
		if(body != null) {
			for(String str : body) {
				try {
					str = new String(str.getBytes("UTF-8"));
					body_utf8.add(str);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if(method == "GET" || method == "POST" || method == "DELETE" || method == "PUT") {
			connexion = connexion(method, route);
			if(token != null) {
				connexion = defineToken(token);
			}
			if(body != null) {
				connexion = writer(body_utf8);
			}

			ApiReturn ApiObject = new ApiReturn();

			ApiObject = reader(ApiObject);

			connexion.disconnect();

			return ApiObject;
		}
		return new ApiReturn(404, false, "WRONG HTTP METHOD", null);
	}
}
