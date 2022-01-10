public class Places implements Comparable<Object>{
    private String name;
    private String vicinity;
    private int distance;
    private double lat;
    private double lon;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    private String placeId;

    public Places(String name, String vicinity, int distance, double lat, double lon, String placeId) {
        this.name = name;
        this.vicinity = vicinity;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int compareTo(Object o) {
        Places s = (Places) o;
        return this.distance - s.distance;
    }
}
