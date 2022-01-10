public class NearbySearch extends AsyncTask<Object, String, String>{
    private Context context;
    String googlePlacesData;

   // GoogleMap mMap;
    String url;
    List<HashMap<String, String>> nearbyPlaceList;
    double latitude, lat, lon;
    double longitude;
    String placeName;
    String placeAddress, placeId;
    int placeDistance, imgID;

    ArrayList<Places> eczaneler = new ArrayList<>();

    public NearbySearch(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... objects) {
        //mMap = (GoogleMap)objects[0];

            url = (String)objects[0];
            latitude = (Double) objects[1];
            longitude = (Double) objects[2];
            imgID = (int) objects[3];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        boolean lst = nearbyPlaceList.isEmpty();
        if(lst == true){
            Toast.makeText(this.context,"Gösterilecek Yer Bulunamadı!",Toast.LENGTH_SHORT).show();
            Listele.hideProgressBar();
        } else {
            //Toast.makeText(this.context,"Yerler Bulundu",Toast.LENGTH_SHORT).show();
            showNearbyPlaces(nearbyPlaceList);
            Listele.hideProgressBar();

        }

    }

    public void showNearbyPlaces(List<HashMap<String,String>> nearbyPlaceList){

        Location loc1 = new Location("");
        Location loc2 = new Location("");
        loc2.setLatitude(latitude);
        loc2.setLongitude(longitude);
            // 40.75240604046968 29.816730347090754 konumum
        for(int i = 0;i<nearbyPlaceList.size();i++){
            HashMap<String,String> googlePlace = nearbyPlaceList.get(i);
            loc1.setLatitude(Double.parseDouble(googlePlace.get("lat")));
            loc1.setLongitude(Double.parseDouble(googlePlace.get("lng")));
            placeName = googlePlace.get("placename");
            placeAddress = googlePlace.get("vicinity");
            placeDistance = Math.round(loc1.distanceTo(loc2));
            placeId = googlePlace.get("reference");
            lat = loc1.getLatitude();
            lon = loc1.getLongitude();
            eczaneler.add(new Places(placeName,placeAddress,placeDistance,lat,lon,placeId));
       // Log.i("mesafe",String.valueOf(placeDistance));

        }
       // Log.i("PlaceId:",placeIdList.toString());
            Collections.sort(eczaneler);

            Listele.adapter.setEczaneler(eczaneler,imgID);

    }

}
