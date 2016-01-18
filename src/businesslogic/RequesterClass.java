package businesslogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.apache.http.entity.ContentType;



public class RequesterClass {
	
	public static String doGetRequest(String newRequest)
			throws ClientProtocolException, IOException {

		//System.out.println("bsiness logic - In get person ");

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(newRequest);
		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
	
	public static String doPostRequest(String urlToPost, String postedValue, String contentType) throws ClientProtocolException, IOException{
		HttpPost request = new HttpPost(urlToPost);		
		DefaultHttpClient client = new DefaultHttpClient();
		
	    request.setEntity(new StringEntity (postedValue));
	    request.setHeader("Content-type", contentType);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
	

}
