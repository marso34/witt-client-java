package com.gwnu.witt.Sign;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.card.MaterialCardView;
import com.gwnu.witt.Data.User.LocData;
import com.gwnu.witt.R;
import com.gwnu.witt.databinding.FragmentSuSelectGymBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SUSelectGymFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    FragmentSuSelectGymBinding binding;
    private static final String Body = "#4A5567";
    private static final String Signature = "#05C78C";
    private static final String White = "#ffffff";
    private static final String Background_2 = "#D1D8E2";

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_LAT = "lat";
    private static final String ARG_LON = "lon";
    private static final String ARG_GYM = "gym";
    private static final String ARG_IS_EDIT = "isEdit";

    private LocationAdapter locationAdapter;
    private List<LocData> searchResults;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private double lat, lon, gymLat, gymLon;
    private String gymName, gymAdress;

    private boolean isSelected, isEdit;

    private PlacesClient placesClient;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Location location;

    private OnFragmentInteractionListener mListner;

    public interface OnFragmentInteractionListener {
        void onSaveLocation(double userLat, double userLon, double gymLat, double gymLon, String gymName, String gymAdress);

        void onCancel();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListner = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static SUSelectGymFragment newInstance(double lat, double lon, String gymName, boolean isEdit) {
        SUSelectGymFragment fragment = new SUSelectGymFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, lat);
        args.putDouble(ARG_LON, lon);
        args.putString(ARG_GYM, gymName);
        args.putBoolean(ARG_IS_EDIT, isEdit);

        fragment.setArguments(args);
        return fragment;
    }
    private final ActivityResultLauncher<Intent> locationSettingsResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // 사용자가 설정 화면에서 돌아왔을 때 실행할 동작
                    promptEnableGPS();
                }
            });

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_LAT);
            lon = getArguments().getDouble(ARG_LON);
            gymName = getArguments().getString(ARG_GYM);
            isEdit = getArguments().getBoolean(ARG_IS_EDIT);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuSelectGymBinding.inflate(inflater);

        searchResults = new ArrayList<>();
        promptEnableGPS();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( getActivity(), new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        } else {
        }


        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        // Initialize Places API
        Places.initialize(requireContext(), getString(R.string.google_places_api_key));
        placesClient = Places.createClient(requireContext());
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
                for (Location location1 : locationResult.getLocations()) {
                    // Update current location
                    updateCurrentLocation(location1);
                    location = locationResult.getLocations().get(0);
                }
            }
        };

        // Listen to text changes in the search EditText

        return binding.getRoot();
    }
    private void promptEnableGPS() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("앱에서 위치 정보를 사용하려면, 위치 서비스를 켜야 합니다.")
                    .setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            locationSettingsResultLauncher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 '취소'를 누르면 다시 확인
                            promptEnableGPS();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // 다이얼로그가 취소되면 다시 확인
                            promptEnableGPS();
                        }
                    });
            builder.show();

        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (gymName != null) {
            if (!(gymName.equals(""))) {
                binding.selectGym.setText(gymName);
                binding.selectGym.setTextColor(Color.parseColor(Body));
                binding.mapIcon.setColorFilter(Color.parseColor(Signature));
                binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
                binding.nextBtn.setTextColor(Color.parseColor(White));
                isSelected = true;
            }
        }

        if (isEdit) {
            binding.example.setText("헬스장을 변경해요");
            binding.skip.setVisibility(View.GONE);
        }

        setRecyclerView();

        binding.backBtn.setOnClickListener(v -> mListner.onCancel());



        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            if (isSelected) {
                gymName = binding.selectGym.getText().toString();
                Log.d("위치 테스트", gymAdress);
                mListner.onSaveLocation(lat, lon, gymLat, gymLon, gymName, gymAdress);
//                ((SignUpActivity) requireActivity()).goToInputPerf(lat, lon, gymName, gymLat, gymLon);
            }
        });
        binding.skip.setOnClickListener( v -> {
            String city = "";
            String state = "" ;

            if (location != null) {
                Log.d(TAG, "로널 ");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(getContext(), Locale.KOREA);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null && !addresses.isEmpty()) {

                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    // 이제 'city'와 'state' 변수에 도시와 주/도 이름이 저장되어 있습니다.
                    Log.d(TAG, "onViewCreated: "+city+state);
                    mListner.onSaveLocation(lat, lon, 0, 0, state +" "+city , "");
                }
                else {

                    Log.d(TAG, "어널 ");
                    mListner.onSaveLocation(lat, lon, 0, 0, "", "");
                }
            }
            else{
                Toast.makeText(getContext(), "GPS 위치 권한을 켜주세요", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        locationAdapter = new LocationAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(locationAdapter);
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
        Log.d("위치 테스트", lat + " " + lon);
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

        stopLocationUpdates();
    }

    private void performSearch(final String query) {
        searchResults.clear();

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> searchTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder().build();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/autocomplete/json").newBuilder();
                    urlBuilder.addQueryParameter("input", query);
                    urlBuilder.addQueryParameter("types", "");
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
                        return response.body().string();
                    } else {
                        Log.e("Error", "Request failed with code: " + response.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String responseBody) {
                if (responseBody != null) {
                    parseResponseAndAddLocations(responseBody);
                }
            }
        };

        searchTask.execute();
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
                String buildingName = terms.getJSONObject(0).getString("value");

                String address = "";
                for (int j = terms.length()-2; j > 0 ; j--)
                    address += " " + terms.getJSONObject(j).getString("value");
                Log.d("주소", address);

                String placeId = prediction.getString("place_id");
                associatedLocations.add(new LocData(buildingName, placeId, address));
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
        LocData L;

        class LocationViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView locationView;
            TextView locationTextView;
            TextView locationDetailView;
            ImageView checkedView;

            LocationViewHolder(@NonNull View itemView) {
                super(itemView);

                locationView = itemView.findViewById(R.id.locationView);
                locationTextView = itemView.findViewById(R.id.locationTextView);
                locationDetailView = itemView.findViewById(R.id.locationDetailView);
                checkedView = itemView.findViewById(R.id.checked);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle location item click
                        L = searchResults.get(getAbsoluteAdapterPosition());
                        gymAdress = searchResults.get(getAbsoluteAdapterPosition()).getAdress();
                        Log.d("위치 테스트2", gymAdress);

                        Log.d(TAG, "onClicklll: "+ L.getName() + "aaaa" + L.getId());
                        getPlaceDetails(L.getId());
//  ------------------------------------------------------------------------------------------------
                        binding.selectGym.setText(L.getName());
                        binding.selectGym.setTextColor(Color.parseColor(Body));
                        binding.mapIcon.setColorFilter(Color.parseColor(Signature));
                        binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
                        binding.nextBtn.setTextColor(Color.parseColor(White));
                        isSelected = true;
                        locationAdapter.notifyDataSetChanged();
//  ------------------------------------------------------------------------------------------------
//                        binding.mapIcon.tint
                        // Perform any action on location item click
                    }
                });
            }
        }

        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_healthroom, parent, false);
            return new LocationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            if (L != null && L.getId() == searchResults.get(position).getId()) {
                Log.d(TAG, "test: "+L.getName()+"aaaa"+L.getId());

                holder.locationView.setStrokeWidth(2);
                holder.checkedView.setVisibility(View.VISIBLE);
            } else {
                holder.locationView.setStrokeWidth(0);
                holder.checkedView.setVisibility(View.GONE);
            }

            holder.locationTextView.setText(searchResults.get(position).getName());
            holder.locationDetailView.setText(searchResults.get(position).getAdress());
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }

        private void getPlaceDetails(String placeId) {

            List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG);

            FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);

            placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                @Override
                public void onSuccess(FetchPlaceResponse response) {
                    Place place = response.getPlace();
                    LatLng latLng = place.getLatLng();
                    gymLat = latLng.latitude;
                    gymLon = latLng.longitude;
                    Log.d(TAG, "wwwwwww: " + gymLat + " aaaa " + gymLon);
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
