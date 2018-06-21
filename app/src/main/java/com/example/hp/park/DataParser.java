package com.example.hp.park;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String,String> googleDirectionsMap= new HashMap<>();
        String duration ="";
        String distance ="";

        Log.d("json response",googleDirectionsJson.toString());
        try {
            duration= googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance= googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration",duration);
            googleDirectionsMap.put("distance",distance);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleDirectionsMap;
    }

    private HashMap<String,String> getPlace(JSONObject googlePlaceJSON) {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try {
            if (!googlePlaceJSON.isNull("name")) {

                placeName = googlePlaceJSON.getString("name");
            }

            if (!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity= googlePlaceJSON.getString("vicinity");
            }

            latitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference =googlePlaceJSON.getString("reference");
            googlePlacesMap.put("place_name",placeName);
            googlePlacesMap.put("vicinity",vicinity);
            googlePlacesMap.put("lat",latitude);
            googlePlacesMap.put("lng",longitude);
            googlePlacesMap.put("reference",reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        Log.d("getPlaces", "getPlaces() called ");
        Log.d("json array length", String.valueOf(jsonArray.length()));
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList= new ArrayList<>();
        HashMap<String,String> placeMap= null;

        for (int i=0;i<count;i++)
        {
            try {
                placeMap= getPlace((JSONObject) jsonArray.get(i)) ;
                placesList.add(placeMap);
            } catch (JSONException e) {
                Log.d("getPlaces", "json exceptn ");
                e.printStackTrace();
            }
        }
        Log.d("number of placesList", String.valueOf(placesList.size()));
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        Log.d("DataParse", "parse() Called");
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject= new JSONObject(jsonData);
            jsonArray= jsonObject.getJSONArray("results");
            if (jsonArray==null)
            {
                Log.d("jsonArray", "array null");
            }
        } catch (JSONException e) {
            Log.d("jsonexception", "parse() ");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJson )
    {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }


}

