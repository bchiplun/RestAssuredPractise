import org.testng.Assert;
import org.testng.annotations.Test;

import files.PayLoad;
import io.restassured.path.json.JsonPath;


public class Section06_ComplexJsonParse {
	
	//public static void main(String[] args) {
	
	@Test
	public void sumOfCourses()
    {
    	//System.out.println(PayLoad.practiseBooks());
		JsonPath js = new JsonPath(PayLoad.practiseBooks());
		
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		
		int sum =0;
		for(int i=0; i<js.getInt("courses.size()"); i++)
		{
			sum += js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies");
		}
		
		System.out.println(sum);
		
		Assert.assertEquals(totalAmount, sum);
	}

}
