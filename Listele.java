public class Listele extends AppCompatActivity implements RecyclerViewClickInterface{

    static RecyclerView recyclerView;
    static MyAdapter adapter;
    static ArrayList<Places> eczaneler = new ArrayList<>();
    int imgId;
    static ProgressBar progressBar;
    private AdView mAdView;
    LocationManager locationManager;
    LocationListener locationListener;
    static double latitude;
    static double longitude;
    Location lastKnownLocation;
    boolean gpscheck = false;

    //12 Nisan'da eklenen başlangıç
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } // else olabilir..
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }
    //12 Nisan'da eklenen  bitiş

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listele);


        // Reklam uygulaması Start
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Reklam Uygulaması End

        progressBar = (ProgressBar)findViewById(R.id.prgress_bar);
       // progressBar.setVisibility(View.VISIBLE);


       // Toast.makeText(this,"resimId:"+imgId,Toast.LENGTH_SHORT).show();

       // eczaneler.add(new Places("Eczane","Yakın",0));

     //   setTitle("Yakındaki Eczaneler");
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new MyAdapter(this);
        adapter.setEczaneler(eczaneler, imgId);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      //  ProgressBar progressBar = (ProgressBar)findViewById(R.id.prgress_bar);
      //  progressBar.setVisibility(View.GONE);

        //12 Nisan'da eklenen başlangıç
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Toast.makeText(MainActivity.this,""+location.getLatitude()+"-"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                //   Toast.makeText(MainActivity.this,"Konum Belirleniyor...",Toast.LENGTH_SHORT).show();

                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                if (ActivityCompat.checkSelfPermission(Listele.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Listele.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } // else olabilir..
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // En son eklenen kod.. 31.03.2021 markette olmayan bölüm..

                if(gpscheck==false){
                    Toast.makeText(Listele.this, "GPS Kapalı!", Toast.LENGTH_SHORT).show();
                    gpscheck = true;
                }
                if (ActivityCompat.checkSelfPermission(Listele.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Listele.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } // else olabilir..

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        };

        if(ContextCompat.checkSelfPermission(Listele.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Listele.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},100);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation != null){
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();

            }
        }
        //12 Nisan'da eklenen bitiş


    }

    public static void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onItemClick(int position) {
         //   Toast.makeText(this, "position:"+position,Toast.LENGTH_SHORT).show();
            String placeId = adapter.getEczaneler().get(position).getPlaceId();
            String urlDetail = getUrl(placeId);

            Intent intent = new Intent(this,MapsActivity.class);
            intent.putExtra("secili_eleman",position);
            intent.putExtra("detay_url",urlDetail);
            startActivity(intent);
    }

    private String getUrl(String placeId){

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

        googlePlaceUrl.append("place_id="+placeId);
        googlePlaceUrl.append("&fields=name,rating,formatted_phone_number");
        googlePlaceUrl.append("&key="+"AIzaSyABi5G60hwZYlXeiz9VCW6X0GKqwQHg4g8");

        return googlePlaceUrl.toString();

    }


}
