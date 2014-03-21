package com.example.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.example.models.FoodHandler;

@Path("/findfood")
@Produces(MediaType.APPLICATION_JSON)
public class FoodFinder {

	FoodHandler handler;
	
	@Path("/{lat}&{lon}")
    @GET
    public JSONArray getFromLatLong(@PathParam("lat") double lat, @PathParam("lon") double lon) throws JSONException {
        handler = new FoodHandler(lat,lon);
        return handler.getFoods();
    }
	
	@Path("/{address}")
    @GET
    public JSONArray getFromAddress(@PathParam("address") String address) throws JSONException {
        handler = new FoodHandler(address);
		return handler.getFoods();
    }

}

