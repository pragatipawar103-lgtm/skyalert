package com.example.skyalert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private TextView cityText,
            tempText,
            weatherText,
            humidityValue,
            windValue,
            pressureValue,
            feelsValue,
            visibilityValue,
            cloudValue,
            minTempValue,
            maxTempValue,
            sunriseTime,
            sunsetTime,
            noonTime,
            nightTime;


    ImageView settings;

    DatabaseReference weatherRef;

    private ImageView weatherIcon;
    private ConstraintLayout mainLayout;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private static final String API_KEY =
            "8ac6a38693cb1e56a3da60d5665fa913";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // FIREBASE
        weatherRef =
                FirebaseDatabase.getInstance()
                        .getReference("weather");

        // IMAGEVIEW

        settings = findViewById(R.id.settingid);

        // TEXTVIEWS
        cityText = findViewById(R.id.cityText);
        tempText = findViewById(R.id.tempText);
        weatherText = findViewById(R.id.weatherText);
        humidityValue = findViewById(R.id.humidityValue);
        windValue = findViewById(R.id.windValue);
        pressureValue = findViewById(R.id.pressureValue);
        feelsValue = findViewById(R.id.feelsValue);
        visibilityValue = findViewById(R.id.visibilityValue);
        cloudValue = findViewById(R.id.cloudValue);
        minTempValue = findViewById(R.id.minTempValue);
        maxTempValue = findViewById(R.id.maxTempValue);

        // WEATHER ICON
        weatherIcon = findViewById(R.id.weatherIcon);

        // TIME TEXT
        sunriseTime = findViewById(R.id.sunriseTime);
        sunsetTime = findViewById(R.id.sunsetTime);
        noonTime = findViewById(R.id.noonTime);
        nightTime = findViewById(R.id.nightTime);

        // MAIN LAYOUT
        mainLayout = findViewById(R.id.main);

        // LOCATION CLIENT
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        // DEFAULT TIMES
        noonTime.setText("12:00 PM");
        nightTime.setText("09:00 PM");


        // SETTINGS CLICK
        settings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(MainActivity.this,
                            settingactivity.class);

            startActivity(intent);
        });

        // CHECK PERMISSION
        checkLocationPermission();
    }

    // LOCATION PERMISSION
    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&

                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );

        } else {

            fetchLocation();
        }
    }

    // PERMISSION RESULT
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                fetchLocation();

            } else {

                Toast.makeText(
                        this,
                        "Location Permission Denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    // FETCH LOCATION
    @SuppressLint("MissingPermission")
    private void fetchLocation() {

        fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
        ).addOnSuccessListener(location -> {

            if (location != null) {

                double latitude = location.getLatitude();

                double longitude = location.getLongitude();

                String url =
                        "https://api.openweathermap.org/data/2.5/weather?lat="
                                + latitude
                                + "&lon="
                                + longitude
                                + "&units=metric&appid="
                                + API_KEY;

                fetchWeatherData(url);

            } else {

                Toast.makeText(
                        this,
                        "Location Not Found",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // FETCH WEATHER DATA
    private void fetchWeatherData(String url) {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,

                response -> {

                    try {

                        JSONObject jsonObject =
                                new JSONObject(response);

                        JSONObject main =
                                jsonObject.getJSONObject("main");

                        JSONArray weatherArray =
                                jsonObject.getJSONArray("weather");

                        JSONObject weatherObject =
                                weatherArray.getJSONObject(0);

                        JSONObject windObject =
                                jsonObject.getJSONObject("wind");

                        JSONObject cloudObject =
                                jsonObject.getJSONObject("clouds");

                        JSONObject sysObject =
                                jsonObject.getJSONObject("sys");

                        // GET DATA
                        String city =
                                jsonObject.getString("name");

                        String temp =
                                main.getString("temp");

                        String humidity =
                                main.getString("humidity");

                        String pressure =
                                main.getString("pressure");

                        String feelsLike =
                                main.getString("feels_like");

                        String weather =
                                weatherObject.getString("main");

                        String wind =
                                windObject.getString("speed");

                        String visibility =
                                jsonObject.getString("visibility");

                        String clouds =
                                cloudObject.getString("all");

                        String minTemp =
                                main.getString("temp_min");

                        String maxTemp =
                                main.getString("temp_max");

                        long sunrise =
                                sysObject.getLong("sunrise");

                        long sunset =
                                sysObject.getLong("sunset");

                        // SET DATA
                        cityText.setText(city);

                        tempText.setText(temp + "°C");

                        weatherText.setText(weather);

                        humidityValue.setText(humidity + "%");

                        windValue.setText(wind + " m/s");

                        pressureValue.setText(pressure + " hPa");

                        feelsValue.setText(feelsLike + "°C");

                        visibilityValue.setText(visibility + " m");

                        cloudValue.setText(clouds + "%");

                        minTempValue.setText(minTemp + "°C");

                        maxTempValue.setText(maxTemp + "°C");

                        // SAVE TO FIREBASE
                        weatherRef.child("city").setValue(city);
                        weatherRef.child("temp").setValue(temp);
                        weatherRef.child("weather").setValue(weather);
                        weatherRef.child("humidity").setValue(humidity);
                        weatherRef.child("wind").setValue(wind);
                        weatherRef.child("pressure").setValue(pressure);
                        weatherRef.child("feelsLike").setValue(feelsLike);
                        weatherRef.child("visibility").setValue(visibility);
                        weatherRef.child("clouds").setValue(clouds);
                        weatherRef.child("minTemp").setValue(minTemp);
                        weatherRef.child("maxTemp").setValue(maxTemp);

                        // DANGER LOGIC
                        double tempValue =
                                Double.parseDouble(temp);

                        boolean isDanger =
                                tempValue > 40 ||
                                        weather.equalsIgnoreCase("Thunderstorm");

                        weatherRef.child("danger").setValue(isDanger);

                        weatherRef.child("time")
                                .setValue(System.currentTimeMillis());

                        // SUNRISE SUNSET TIME
                        long timezoneOffset =
                                jsonObject.getLong("timezone");

                        long sunriseLocal =
                                (sunrise + timezoneOffset) * 1000L;

                        long sunsetLocal =
                                (sunset + timezoneOffset) * 1000L;

                        SimpleDateFormat sdf =
                                new SimpleDateFormat(
                                        "hh:mm a",
                                        Locale.getDefault());

                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                        String sunriseFormatted =
                                sdf.format(new Date(sunriseLocal));

                        String sunsetFormatted =
                                sdf.format(new Date(sunsetLocal));

                        sunriseTime.setText(sunriseFormatted);

                        sunsetTime.setText(sunsetFormatted);

                        // WEATHER BACKGROUND + ICON
                        if (weather.equalsIgnoreCase("Clouds")) {

                            mainLayout.setBackgroundResource(
                                    R.drawable.bg_gradient
                            );

                            weatherIcon.setImageResource(
                                    R.drawable.weather
                            );

                        } else if (weather.equalsIgnoreCase("Rain")) {

                            mainLayout.setBackgroundResource(
                                    R.drawable.bg_gradient
                            );

                            weatherIcon.setImageResource(
                                    R.drawable.weather
                            );

                        } else if (weather.equalsIgnoreCase("Clear")) {

                            mainLayout.setBackgroundResource(
                                    R.drawable.bg_gradient
                            );

                            weatherIcon.setImageResource(
                                    R.drawable.weather
                            );

                        } else if (weather.equalsIgnoreCase("Thunderstorm")) {

                            mainLayout.setBackgroundResource(
                                    R.drawable.bg_gradient
                            );

                            weatherIcon.setImageResource(
                                    R.drawable.weather
                            );

                        } else {

                            mainLayout.setBackgroundResource(
                                    R.drawable.bg_gradient
                            );

                            weatherIcon.setImageResource(
                                    R.drawable.weather
                            );
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(
                                this,
                                "Error Parsing Weather",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                },

                error -> {

                    error.printStackTrace();

                    Toast.makeText(
                            this,
                            "Weather Fetch Failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }

        );

        Volley.newRequestQueue(this).add(request);
    }
}