public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Places> yerler = new ArrayList<>();
    double lat,lon, user_lat, user_lon;
    int position;
    String name,placeId, googleDetailedPlacesData, detayUrl, formatted_phone_number, yerAdi;
    private AdView mAdView;
    JSONObject jsonObject;
    TextView textView, yerisimTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        yerler = Listele.adapter.getEczaneler();
        textView = findViewById(R.id.phoneNo);
        yerisimTxt = findViewById(R.id.yerisim);

        position = getIntent().getIntExtra("secili_eleman",0);
        detayUrl = getIntent().getStringExtra("detay_url");

        DetayGetir detayGetir = new DetayGetir();
        detayGetir.execute(detayUrl);

        // Reklam uygulaması Start

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

     //  Log.i("urldetay",googleDetailedPlacesData);
      //  Log.i("enlem-boylam:", String.valueOf(yerler.get(posizyon).getLat())+" "+String.valueOf(yerler.get(posizyon).getLon()));
    }

    private class DetayGetir extends AsyncTask<String, String, String>{
        private String url;
        @Override
        protected String doInBackground(String... strings) {
            url = strings[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googleDetailedPlacesData = downloadUrl.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googleDetailedPlacesData;
        }

        @Override
        protected void onPostExecute(String s) {
          //  Log.i("getir",s);
            try {
                jsonObject = new JSONObject(s);
                formatted_phone_number = jsonObject.getJSONObject("result").getString("formatted_phone_number");
                yerAdi = jsonObject.getJSONObject("result").getString("name");
                if(yerAdi.isEmpty()) {
                    yerisimTxt.setText("Bilgi Yok");
                } else {
                    yerisimTxt.setText(yerAdi);
                }

                if(formatted_phone_number.isEmpty()) {
                    textView.setText("Bilgi Yok");
                } else {
                    textView.setText(formatted_phone_number);
                }


                //Log.i("telno",yerAdi+" "+formatted_phone_number);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = yerler.get(position).getLat();
        lon = yerler.get(position).getLon();
        name = yerler.get(position).getName();
        placeId = yerler.get(position).getPlaceId();
        user_lat = MainActivity.latitude;
        user_lon = MainActivity.longitude;
/*

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDetailedPlacesData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    //    DataParser parser = new DataParser();
    //    detailedPlaceList = parser.parse(googleDetailedPlacesData);

      //  Log.i("mylocation:",user_lat + " " + user_lon);
        //Konumunuz
        LatLng benimkonum = new LatLng(user_lat,user_lon);
        mMap.addMarker(new MarkerOptions().position(benimkonum).title("Konumunuz").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        // Add a marker in Sydney and move the camera
        LatLng konum = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(konum).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(konum));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
/*
    private String getUrl(String placeId){

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

        googlePlaceUrl.append("place_id="+placeId);
        googlePlaceUrl.append("&fields=name,rating,formatted_phone_number");
        googlePlaceUrl.append("&key="+"AIzaSyABi5G60hwZYlXeiz9VCW6X0GKqwQHg4g8");

        return googlePlaceUrl.toString();

    }
    */

}
