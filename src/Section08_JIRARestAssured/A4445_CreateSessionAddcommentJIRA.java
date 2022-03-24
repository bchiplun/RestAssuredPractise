package Section08_JIRARestAssured;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import com.Utilities.GetDataJSON;



public class A4445_CreateSessionAddcommentJIRA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SessionFilter session = new SessionFilter();
		
		RestAssured.baseURI="http://localhost:8080/";
		
		//Reqest to create session
		System.out.println("**************Creating session***************\n");
		given().log().all().header("Content-Type","application/json").relaxedHTTPSValidation()  // to skip https certification
			.body("{ \"username\": \"chiplunkarbhavesh\", \"password\": \"Hithere3422\" }")	
		
		.filter(session)   // to get the session so that we don't have to pass session id in header in all below requests
		
		.when().post("rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200);
		
		
		//Request to add comment in existing issue bug
		System.out.println("***********Adding comment to issue************\n");
		String addCommentresp = given().log().all().pathParam("key", "10003").header("Content-Type","application/json")
			.body("{\r\n"
					+ "    \"body\": \"Adding commect from restAPI using RestAssured tool\",\r\n"
					+ "    \"visibility\": {\r\n"
					+ "        \"type\": \"role\",\r\n"
					+ "        \"value\": \"Administrators\"\r\n"
					+ "    }\r\n"
					+ "}")
			
			.filter(session)
			                             //path parameter key
			.when().post("rest/api/2/issue/{key}/comment")
			.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		String id = GetDataJSON.getDataFromJSON(addCommentresp, "id");
		
		
		//Adding attachemnt to issue
		System.out.println("***********Adding attachemnt to issue*************");
		
		given().log().all().pathParam("key", "10003").header("X-Atlassian-Token","no-check").header("Content-Type","multipart/form-data")  // content type for multipart
			.multiPart("file", new File("Attachement"))  //not writing complete path since file is in projct folder only
			
		.filter(session)
		
		.when().post("rest/api/2/issue/{key}/attachments")
		
		.then().log().all().assertThat().statusCode(200);
		
		
		//Get details of issue
		System.out.println("\n***********GET details of issue******************\n");
		
		String getResp = given().log().all().pathParam("key", "10003").queryParam("fields", "comment")  //query parm to filer and only get comments 
		
		.filter(session)
		
		.when().get("rest/api/2/issue/{key}")
		
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(getResp);
		int count = js.getInt("fields.comment.comments.size()");  // to get count to iterate
		
		for(int i=0; i<count; i++)
		{
			String commentId = js.get("fields.comment.comments["+i+"].id").toString(); // to get id of comments
			if(commentId.equals(id))
			{
				System.out.println(js.get("fields.comment.comments["+i+"].body").toString()); //once ud matches fetching and printing comment
			}
		}
		
	}

}
