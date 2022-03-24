package com.Utilities;

import io.restassured.path.json.JsonPath;

public class GetDataJSON {
	
	public static String getDataFromJSON(String resp, String field)
	{
		JsonPath js2 = new JsonPath(resp);
		return js2.getString(field);
	}

}
