package com.example.skyalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class settingactivity extends AppCompatActivity {

    CardView helpCard, aboutCard, privacyCard, unitCard,
            instaCard, shareCard, rateCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingactivity);

        helpCard = findViewById(R.id.helpCard);
        aboutCard = findViewById(R.id.aboutCard);
        privacyCard = findViewById(R.id.privacyCard);
        unitCard = findViewById(R.id.unitCard);
        instaCard = findViewById(R.id.instaCard);
        shareCard = findViewById(R.id.shareCard);
        rateCard = findViewById(R.id.rateCard);

        // HELP
        helpCard.setOnClickListener(v -> {
            showBox(
                    "Help & Support",
                    "Sky Alert helps you track weather updates, rain alerts, sunrise/sunset timings and live weather information."
            );
        });

        // ABOUT
        aboutCard.setOnClickListener(v -> {
            showBox(
                    "About App",
                    "Sky Alert is a smart weather forecasting Android app with live weather, alerts, temperature, humidity and more."
            );
        });

        // PRIVACY
        privacyCard.setOnClickListener(v -> {
            showBox(
                    "Privacy Policy",
                    "Your location is used only for weather updates. Sky Alert does not share your personal information."
            );
        });

        // WEATHER UNIT
        unitCard.setOnClickListener(v -> {
            showBox(
                    "Weather Units",
                    "You can view temperature in Celsius (°C) or Fahrenheit (°F)."
            );
        });

        // INSTAGRAM OPEN
        instaCard.setOnClickListener(v -> {

            String url = "https://www.instagram.com/";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            startActivity(intent);
        });

        // SHARE APP
        shareCard.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            String shareMessage =
                    "Download Sky Alert App 🌦️\n\n" +
                            "Best weather forecasting app!\n" +
                            "https://play.google.com/store/apps/details?id=com.example.skyalert";

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // RATE APP
        rateCard.setOnClickListener(v -> {

            try {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName()));

                startActivity(intent);

            } catch (Exception e) {

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));

                startActivity(intent);
            }
        });

    }

    // SAME PAGE INFO BOX
    private void showBox(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}