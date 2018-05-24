package fr.afavier.gout.http;

import java.util.ArrayList;

import org.json.JSONObject;

public class ApiReturn {
	private int httpCode;
	private boolean sucess;
	private String message;
	
	private ArrayList<JSONObject> list;

	public ApiReturn(int httpCode, boolean sucess, String message, ArrayList<JSONObject> list) {
		super();
		this.httpCode = httpCode;
		this.sucess = sucess;
		this.message = message;
		this.list = list;
	}
	
	public ApiReturn() {
	}


	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<JSONObject> getList() {
		return list;
	}

	public void setList(ArrayList<JSONObject> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ApiReturn [httpCode=" + httpCode + ", sucess=" + sucess + ", message=" + message + ", list=" + list + "]";
	}
	
}
