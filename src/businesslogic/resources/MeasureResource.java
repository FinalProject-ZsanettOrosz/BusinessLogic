package businesslogic.resources;

import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.client.ClientProtocolException;

import businesslogic.RequesterClass;


@Stateless
@LocalBean
@Path("/measureTypes")
public class MeasureResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMeasureTypees()
			throws ClientProtocolException, IOException {
		String externalService = "http://10.218.223.84:5700/sdelab/measureTypes"; 
		String result = RequesterClass.doGetRequest(externalService);
		System.out.println(result);

		return Response.ok(result).build();
	}
}
