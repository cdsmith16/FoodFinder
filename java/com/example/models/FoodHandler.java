package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

@XmlRootElement
public class FoodHandler {

	private double lat;
	private double lon;
	private double radius;
	//search within fixed distance (1km for now)
	private final static int START_RADIUS = 1000;
	//my google maps apiKey
	private final static String apiKey = "AIzaSyD3GGMyxXRpEk2VZKPwaWa65kecYVa25fQ";

	/**
	 * Constructor for FoodHandler. Takes in latitude & longitude and sets bounding distance
	 * within which to search.
	 * @param lat
	 * @param lon
	 */
    public FoodHandler(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.radius = START_RADIUS;
    }

    /**
     * Unique constructor for if/when users input
     * a street address instead of a latitude and longitude.
     * Takes longer than just entering lat/lon because of the extra call
     * to Google, but more convenient when clients can't easily get their
     * lat/lng position.
     * @param address
     */
    public FoodHandler(String address){
    	double[] coords = new double[2];
    	try {
			coords = getCoords(address);
		} catch (Exception e) {
			// fail gracefully, return middle of SF
			e.printStackTrace();
			lat = 37.75;
			lon = -122.38;
			radius = START_RADIUS;
		}
    	lat = coords[0];
    	lon = coords[1];
    	radius = START_RADIUS;
    }
    
    /**
     * Queries SoDa API for food trucks within radius of the given
     * current latitude and longitude. Responds with raw JSON rather
     * than cleaning it up for now, in interest of efficiency,
     *  since client would have to parse JSON anyhow.
     *  
     *  IMPORTANT NOTE: within_box doesn't work with this DataSet! Try:
     *  "http://data.sfgov.org/resource/mved-hg8j.json?$where=within_box(location,37.7878,-122.3946,37.7880,-122.3944)"
     * 
     *  Luckily, this was overcome by discovering 'within_circle' :D
     * 
     * @return
     * @throws JSONException 
     * @throws IOException 
     */
	public JSONArray getFoods() throws JSONException{
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://data.sfgov.org/resource/mved-hg8j.json?");
		
		String box = ("within_circle(location," 
		+ lat + ","
		+ lon + ","
		+ START_RADIUS +")");
		
		request.addQuerystringParameter("$where", box);
		System.out.println(box);
		
		Response response = request.send();
	    
		JSONArray foods = new JSONArray(response.getBody());
		
		return foods;
	}
	
	/**
	 * Helper method which converts an address in standard format to the
	 * format necessary for Google Maps queries.
	 * 
	 * Incoming format: "81 Santa Rosa Ave, San Francisco, CA"
	 * 
	 * Output: "81+Santa+Rosa+Ave+San+Francisco+CA"
	 * 
	 * @param commonAddress
	 * @return
	 */
	private static String addressConverter(String commonAddress){
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i<commonAddress.length(); i++){
			if(commonAddress.charAt(i)==','){
				continue;
			} else if(commonAddress.charAt(i)==' '){
				buffer.append("+");
			} else {
				buffer.append(commonAddress.charAt(i));
			}
		}
		//'commonAddress' has now been converted!
		String converted = buffer.toString();
		//System.out.println(converted);
		return converted;
	}

	public static double[] getCoords(String address) throws Exception{
		//Google-ize address
		address = addressConverter(address);
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://maps.googleapis.com/maps/api/geocode/json?");
		
		request.addQuerystringParameter("address", address);
		request.addQuerystringParameter("sensor", "true");
	    request.addQuerystringParameter("key", apiKey);
		
	    Response response = request.send();
	    double[] coords = parseCoords(response);
	    return coords;
	}
	
	/**
	 *  Converts Google's address/geometry response into the latitude
	 *  and longitude for a given address.
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private static double[] parseCoords(Response response) throws Exception {
		double[] coords = new double[2];
		
		JSONObject json = new JSONObject(response.getBody());
		JSONArray results = json.getJSONArray("results");
		JSONObject result = results.getJSONObject(0);
		JSONObject geometries = result.getJSONObject("geometry");
		JSONObject geometry = geometries.getJSONObject("location");
		
		coords[0] = geometry.getDouble("lat");
		coords[1] = geometry.getDouble("lng");
		//System.out.println(coords[0] + "," + coords[1]);
		
		return coords;
	}
	
	 
	// * tests each of the methods/get requests in the foodHandler class
	public static void main(String[] arg0) throws Exception{
		FoodHandler handler = new FoodHandler("555 California St, San Francisco, CA");
		System.out.println(handler.getFoods());
	}
	
	
}
