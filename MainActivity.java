public class MainActivity extends AppCompatActivity {

    SeekBar distBar;
    TextView txtMesafe;
    LocationManager locationManager;
    LocationListener locationListener;
    static double latitude;
    static double longitude;
    Location lastKnownLocation;
    int PROXIMITY_RADIUS = 1000;
    int imgID;
    private AdView mAdView;
    boolean gpscheck = false;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distBar = findViewById(R.id.distBar);
        txtMesafe = findViewById(R.id.txtMesafe);
        distBar.setEnabled(true);
        distBar.setMax(5);
        distBar.setProgress(1);

        // Reklam uygulaması Start
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        distBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                switch (progress) {
                    case 0:
                        txtMesafe.setText("500 m");
                        PROXIMITY_RADIUS = 500;
                        break;

                    case 1:
                        txtMesafe.setText(String.valueOf(progress) + " km");
                        PROXIMITY_RADIUS = 1000;
                        break;

                    case 2:
                        txtMesafe.setText(String.valueOf(progress) + " km");
                        PROXIMITY_RADIUS = 2000;
                        break;

                    case 3:
                        txtMesafe.setText(String.valueOf(progress) + " km");
                        PROXIMITY_RADIUS = 3000;
                        break;

                    case 4:
                        txtMesafe.setText(String.valueOf(progress) + " km");
                        PROXIMITY_RADIUS = 4000;
                        break;

                    case 5:
                        txtMesafe.setText(String.valueOf(progress) + " km");
                        PROXIMITY_RADIUS = 5000;
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //  ImageButton imageButton = findViewById(R.id.imgBtn);

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
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } // else olabilir..
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

                // En son eklenen kod.. 31.03.2021 markette olmayan bölüm..
                if(gpscheck==false){
                    Toast.makeText(MainActivity.this, "GPS Kapalı!", Toast.LENGTH_SHORT).show();
                    gpscheck = true;
                }

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                } // else olabilir..

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        };
        //Runtime permission
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},100);
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

    }

    public void eczGit(View view){
        //apikey 3bqVOWSxGnx2CpR8gfdmOh:1Ly39CVzQffY477reg6RvA nöbetçi eczane apikey  https://collectapi.com/
        String aranacakYer = "pharmacy";
        String url = getUrl(latitude, longitude, aranacakYer);
        //Log.i("urlgoster",url);
       // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.ecz; //ana sayfa eczane ikonu
        Object dataTransfer[] = new Object[4]; //arama aktivitesine 4 elemanlı dizi-data gönderimi
        dataTransfer[0] = url; // yakın yer arama url'si
        dataTransfer[1] = latitude; //enlem bilgisi
        dataTransfer[2] = longitude; //boylam bilgisi
        dataTransfer[3] = imgID; //ikon resim id değeri->listelemede çıkması için

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer); // Yakın yer arama aktivitesini başlatma komutu

        Intent intent = new Intent(getApplicationContext(),Listele.class);
        startActivity(intent); // intent komutu aktiviteler arası bilgi taşır..

    }
    public void hastaneBtn(View view) {
        String aranacakYer = "hospital";
        String url = getUrl(latitude, longitude, aranacakYer);
       // Log.i("urlgoster",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.hospital;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);
        startActivity(intent);
    }

    public void okulBtn(View view) {
        String aranacakYer = "school";
        String url = getUrl(latitude, longitude, aranacakYer);

        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.ic_okul_ikon;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }


    public void restBtn(View view) {
        String aranacakYer = "restaurant";
        String url = getUrl(latitude, longitude, aranacakYer);

        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.restaurant;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void carBtn(View view) {
        String aranacakYer = "car_repair";
        String url = getUrl(latitude, longitude, aranacakYer);
     //   Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.ic_car;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void btnBankamatik(View view) {
        String aranacakYer = "atm";
        String url = getUrl(latitude, longitude, aranacakYer);
       // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.atm;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void disciClick(View view) {
        String aranacakYer = "dentist";
        String url = getUrl(latitude, longitude, aranacakYer);
       // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.disci;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void tesisatClick(View view) {
        String aranacakYer = "plumber";
        String url = getUrl(latitude, longitude, aranacakYer);
        // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.tesisat;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void btnMuze(View view) {
        String aranacakYer = "museum";
        String url = getUrl(latitude, longitude, aranacakYer);
        // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.museum;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void veterinerBtn(View view) {
        String aranacakYer = "veterinary_care";
        String url = getUrl(latitude, longitude, aranacakYer);
        // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.veteriner;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void avukatBtn(View view) {
        String aranacakYer = "lawyer";
        String url = getUrl(latitude, longitude, aranacakYer);
        // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.avukat;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    public void otelBtn(View view) {
        String aranacakYer = "lodging";
        String url = getUrl(latitude, longitude, aranacakYer);
        // Log.i("url",url);
        // Log.i("konum", String.valueOf(latitude)+""+String.valueOf(longitude));
        imgID = R.drawable.otel;
        Object dataTransfer[] = new Object[4];
        dataTransfer[0] = url;
        dataTransfer[1] = latitude;
        dataTransfer[2] = longitude;
        dataTransfer[3] = imgID;

        NearbySearch nearbySearch = new NearbySearch(this);
        nearbySearch.execute(dataTransfer);

        Intent intent = new Intent(getApplicationContext(),Listele.class);

        startActivity(intent);
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyABi5G60hwZYlXeiz9VCW6X0GKqwQHg4g8");

        return googlePlaceUrl.toString();

    }

}
