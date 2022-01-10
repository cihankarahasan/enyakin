public class DataParser {

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName = "NA";
        String vicinity = "NA";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!googlePlaceJson.isNull("name")){
                placeName = googlePlaceJson.getString("name");
            }

            if(!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");


            googlePlacesMap.put("placename",placeName);
            googlePlacesMap.put("vicinity",vicinity);
            googlePlacesMap.put("lat",latitude);
            googlePlacesMap.put("lng",longitude);
            googlePlacesMap.put("reference",reference);
           // googlePlacesMap.put("formatted_phone_number",formatted_phone_number);


        }catch (JSONException e){
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        HashMap<String,String> placeMap = null;

        for(int i=0; i<count ; i++){
            try{

                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");


        } catch (JSONException e) {

            e.printStackTrace();
        }
            return getPlaces(jsonArray);
    }
}
