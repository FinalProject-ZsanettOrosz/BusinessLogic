package businesslogic.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import businesslogic.RequesterClass;


@Stateless
@LocalBean
@Path("/goals/person")
public class GoalsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	
	
	@GET
	@Path("/{personID}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getPersonAchivedGoals(@PathParam("personID") int id)
			throws ClientProtocolException, IOException {
		String externalService = "https://intense-mesa-6521.herokuapp.com/sdelab/goals/person/" + id; //from SS
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}
	

}
