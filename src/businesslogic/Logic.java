package businesslogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;

public class Logic {

	public String allGoals() throws ClientProtocolException, IOException {
		String reqUrl = "https://intense-mesa-6521.herokuapp.com/sdelab/goals/";// from SS
		String result = RequesterClass.doGetRequest(reqUrl);

		// System.out.println(result);

		return result;
	}

	public double calculateDailyGoal(int person, String measureType,
			Date dateToCheck) throws ClientProtocolException, IOException,
			ParseException {
		// get current sataus for that measure tpye - SS
		// get all history of that type for a date - SS
		// add then
		// check if it meets the goal of that type
		// return with bool
		String reqUrl = "https://intense-mesa-6521.herokuapp.com/sdelab/person/"
				+ person; // from SS

		String result = RequesterClass.doGetRequest(reqUrl);

		// CURRENT LIFE STATUS - CONSIDERED TODAY
		JSONObject p = new JSONObject(result);
		JSONObject healthP = new JSONObject(p.get("healthProfile").toString());
		if (!healthP.get("lifeStatus").toString().startsWith("[")) {
			System.out.println("No registered life status!");
			return 0;
		}
		JSONArray lifeArray = new JSONArray(healthP.get("lifeStatus")
				.toString());
		double sum = 0;
		for (int i = 0; i < lifeArray.length(); i++) {
			JSONObject lifeS = new JSONObject(lifeArray.get(i).toString());
			JSONObject measure = new JSONObject(lifeS.get("measureType")
					.toString());
			if (measure.get("name").equals(measureType)) {
				sum += lifeS.getDouble("value");
			}
			System.out.println("  " + measure.get("name") + ": "
					+ lifeS.getDouble("value"));
		}
		System.out.println("From current: " + sum);

		// FROM HISTORY
		String reqUrl2 = "https://intense-mesa-6521.herokuapp.com/sdelab/person/"
				+ person + "/" + measureType; //from SS

		String result2 = RequesterClass.doGetRequest(reqUrl2);
		JSONArray hisArray = new JSONArray(result2);
		//System.out.println(dateToCheck.toString());
		for (int i = 0; i < hisArray.length(); i++) {
			JSONObject history = new JSONObject(hisArray.get(i).toString());
			String strDate = history.getString("created");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date dateFormat = format.parse(strDate);
			// System.out.println(dateFormat);

			if (dateToCheck.getYear() == dateFormat.getYear()
					&& dateToCheck.getMonth() == dateFormat.getMonth()
					&& dateToCheck.getDay() == dateFormat.getDay()) {
				double vale = history.getDouble("value");
				sum += vale;
			}

		}
		System.out.println("Current plus history: " + sum);
		return sum;
	}

	public JSONObject goalIsMet(int person, String measureType, double sum) {
		JSONArray goalArray;
		try {
			String a = allGoals();
			System.out.println(a);
			goalArray = new JSONArray(allGoals());
			System.out.println(goalArray.length());
			for (int i = 0; i < goalArray.length(); i++) {
				JSONObject goal = new JSONObject(goalArray.get(i).toString());
				JSONObject measureDef = new JSONObject(goal.get("measureDef")
						.toString());
				//System.out.println(measureDef.toString(4));

				if (measureDef.get("name").equals(measureType)
						&& sum >= goal.getDouble("goalValue")) {

					return goal;

				}

			}

		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
