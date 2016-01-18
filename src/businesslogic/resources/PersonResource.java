package businesslogic.resources;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import businesslogic.Logic;
import businesslogic.RequesterClass;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;

@Stateless
@LocalBean
@Path("/person")
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private Logic logic;


	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPersons() throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/person";
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}

	@GET
	@Path("/{personID}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPersonById(@PathParam("personID") int id)
			throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/person/"
				+ id;
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}

	@GET
	@Path("/{personID}/{measureType}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPersonHistory(@PathParam("personID") int id,
			@PathParam("measureType") String measureType)
			throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/person/"
				+ id + "/" + measureType;
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}
	
	@GET
	@Path("/{personID}/{measureType}/{mid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPersonHistoryById(@PathParam("personID") int id,
			@PathParam("measureType") String measureType, @PathParam("mid") int mid)
			throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/person/"
				+ id + "/" + measureType + "/" + mid;
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}
	

	
	@GET
	@Path("/goals/pic")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPicture()
			throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/goals/pic"; 
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}
	

	
	@POST
	@Path("/{personID}/{measureType}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,  MediaType.TEXT_PLAIN  })
	@Consumes({ MediaType.TEXT_PLAIN  })
	public Response newLifeStatus(@PathParam("personID") int personID,
			@PathParam("measureType") String measureType,
			String postedValue) throws ClientProtocolException, IOException, ParseException{
		
		System.out.println("BL - in POST");
		String urlToPost = "http://10.218.223.84:5700/sdelab/person/" + personID + "/" + measureType;
		
		String result = RequesterClass.doPostRequest(urlToPost, postedValue, "text/plain");
		
		
		//check logic for achivements 
		
		logic = new Logic();
		double sum = logic.calculateDailyGoal(personID, measureType, new Date());
		JSONObject met = logic.goalIsMet(personID, measureType, sum);
		//System.out.println(met);
		String picUrl = "";
		JSONObject achivement = null;
		if(met != null){
			//String picAddress = "http://10.218.223.84:5700/sdelab/goals/pic"; 
			//picUrl = RequesterClass.doGetRequest(picAddress);
			//achived goal should change
			
			achivement = new JSONObject();
			achivement.put("achivedGoal", met);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String strToday = df.format(new Date());
			achivement.put("achivementDate", strToday);
			String urlToPostAchivement = "http://10.218.223.84:5700/sdelab/goals/person/" + personID;
			System.out.println("We met the goal" + achivement.toString(4));
			String personAfterAchivement = RequesterClass.doPostRequest(urlToPostAchivement, achivement.toString(), "application/json");
			System.out.println("refressed person" + personAfterAchivement);
			//System.out.println(picUrl);
		}
		//Response res = Response.ok(result.toString()).build();
		
		String urlToGetUpdatedPerson = "http://10.218.223.84:5700/sdelab/person/" + personID;
		String updatedWithAchivements = RequesterClass.doGetRequest(urlToGetUpdatedPerson);
		System.out.println( "Re-asked person" + updatedWithAchivements);
		return Response.ok(updatedWithAchivements).build();
	}
	
	@POST
	@Path("/{personID}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML   })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML  })
	public Response loggedInPerson(@PathParam("personID") int personID){
		
		logic = new Logic();
		//set up goal for today
		
		return Response.ok().build();
	}
	
}
