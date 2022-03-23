package com.Utilities;

import io.restassured.path.json.JsonPath;

public class JsonSerializer {
	
	public static JsonPath rawToJson(String resp)
	{
		JsonPath js = new JsonPath(resp);
		return js;
				
	}

}
