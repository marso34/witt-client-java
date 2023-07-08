package com.example.healthappttt.Sign;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User.LocData;
import com.example.healthappttt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class subFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private EditText searchEditText;
    private RecyclerView searchRecyclerView;
    private LocationAdapter locationAdapter;
    private List<LocData> searchResults;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_EMAIL = "email";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private String name;
    private String email;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button searchButton;
    private double lat;
    private double lon;
    private LocationManager locationManager;
    PlacesClient placesClient;
    private Location location;

    public static subFragment newInstance(String email) {
        subFragment fragment = new subFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub, container, false);

        // Initialize Places API
        Places.initialize(requireContext(), "AIzaSyBH3t-Fw2rKII3omCBlrbDWHWvKGUAvvLg");
        placesClient = Places.createClient(requireContext());

        // Initialize views
        searchEditText = view.findViewById(R.id.searchEditText);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);

        // Set up RecyclerView
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        locationAdapter = new LocationAdapter();
        searchRecyclerView.setAdapter(locationAdapter);

        // Initialize search results
        searchResults = new ArrayList<>();

        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Create location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // Update location every 10 seconds

        // Create location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update current location
                    updateCurrentLocation(location);
                }
            }
        };
        // Listen to text changes in the search EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Perform search when text changes
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        return view;
    }

    private void startLocationUpdates() {
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        // Stop location updates
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void updateCurrentLocation(Location location) {
        // Update latitude and longitude text views
        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
        // Handle location change
        updateCurrentLocation(location);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Start location updates
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop locationupdates
        stopLocationUpdates();
    }
    private void performSearch(final String query) {
        // Clear previous search results
        searchResults.clear();
        // Split the query into individual words
        String[] words = query.split(" ");

        // Combine words into a single query string
        StringBuilder queryBuilder = new StringBuilder();
        for (String word : words) {
            queryBuilder.append(word).append(" ");
        }
        String fullQuery = queryBuilder.toString().trim();

        // Perform search in background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder().build();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/autocomplete/json").newBuilder();
                    urlBuilder.addQueryParameter("input", fullQuery);
                    urlBuilder.addQueryParameter("types", "establishment");
                    urlBuilder.addQueryParameter("location", String.valueOf(lat) + ", " + String.valueOf(lon)); // 현재 위치
                    urlBuilder.addQueryParameter("radius", "10000");
                    urlBuilder.addQueryParameter("strictbounds", "true");
                    urlBuilder.addQueryParameter("components", "country:KR"); // 주소에 대한 국가 필터링
                    urlBuilder.addQueryParameter("key", getString(R.string.google_places_api_key)); // 실제 Places API 키로 대체
                    urlBuilder.addQueryParameter("language", "ko"); // 한국어로 결과 요청

                    String url = urlBuilder.build().toString();
                    Request request = new Request.Builder()
                            .url(url)
                            .method("GET", null)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        // TODO: Parse the response body and extract the required information
                        // You can use JSON parsing libraries like Gson or JSONObject to parse the response
                        // Handle the extracted information as per your requirement
                        // Example: Log the response body
                        Log.d("도냐?","ㅁㅁㅁ");

                        // Update the search results on the main thread
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    parseResponseAndAddLocations(responseBody);
                                    Log.d("도냐?","ㅁㅁㅁ");
                                }
                            });
                        }
                    } else {
                        // Handle the error response
                        Log.e("Error", "Request failed with code: " + response.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseResponseAndAddLocations(String responseBody) {
        Log.d(TAG, "parseResponseAndAddLocations: " + responseBody);
        // TODO: Parse the response body and extract the required information
        // You can use JSON parsing libraries like Gson or JSONObject to parse the response

        try {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray predictions = responseJson.getJSONArray("predictions");

            List<LocData> associatedLocations = new ArrayList<>();

            for (int i = 0; i < predictions.length(); i++) {
                JSONObject prediction = predictions.getJSONObject(i);
                JSONArray terms = prediction.getJSONArray("terms");
                String buildingName = terms.getJSONObject(i).getString("value");
                String placeId = prediction.getString("place_id");
                associatedLocations.add(new LocData(buildingName,placeId));
            }

            // Clear previous search results
            searchResults.clear();

            // Add the associated locations to the search results
            searchResults.addAll(associatedLocations);

            // Notify the adapter that the data set has changed
            locationAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private List<String> getAssociatedLocations(String query) {
        // Initialize Places API
        Places.initialize(requireContext(), "YOUR_API_KEY");

        // Create PlacesClient
        PlacesClient placesClient = Places.createClient(requireContext());

        List<String> associatedLocations = new ArrayList<>();

        // Create Autocomplete request
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        // Perform Autocomplete request
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener((response) -> {
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        String location = prediction.getFullText(null).toString();
                        associatedLocations.add(location);
                    }
                    locationAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener((exception) -> {
                    // Handle failure
                });

        return associatedLocations;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
        LocData L;
        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_healthroom, parent, false);
            return new LocationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            L = searchResults.get(position);
            holder.locationTextView.setText(L.getName());
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }

        class LocationViewHolder extends RecyclerView.ViewHolder {

            TextView locationTextView;

            LocationViewHolder(@NonNull View itemView) {
                super(itemView);
                locationTextView = itemView.findViewById(R.id.locationTextView);

                // Set click listener for location item
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle location item click
                        L = searchResults.get(getAbsoluteAdapterPosition());
                        Log.d(TAG, "onClicklll: "+L.getName()+"aaaa"+L.getId());
                        getPlaceDetails(L.getId());
                        // Perform any action on location item click
                    }
                });
            }
        }

        private void getPlaceDetails(String placeId) {

            List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);

            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);

            placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                @Override
                public void onSuccess(FetchPlaceResponse response) {
                    Place place = response.getPlace();
                    LatLng latLng = place.getLatLng();
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    Log.d(TAG, "wwwwwww: "+latitude+"aaaa"+longitude);
                    // Perform any action with latitude and longitude values
                    ((SubActivity) getActivity()).replaceFragment(sub1Fragment.newInstance(email, lat, lon, latitude, longitude, L.getName()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle the failure
                }
            });
        }

    }
}
