package com.example.echoguard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.echoguard.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private SpeechRecognizer speechRecognizer;
    private Button activateButton;
    private TextView statusText;
    private FusedLocationProviderClient fusedLocationClient;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateButton = findViewById(R.id.activate_button);
        statusText = findViewById(R.id.status_text);

        // Initialize the SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                statusText.setText("Ready for speech...");
            }

            @Override
            public void onBeginningOfSpeech() {
                statusText.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float rmdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                statusText.setText("Processing...");
            }

            @Override
            public void onError(int errorCode) {
                handleError(errorCode);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenCodeWord = matches.get(0);
                    if (spokenCodeWord.equalsIgnoreCase(Constants.CODE_WORD)) {
                        statusText.setText("Code word recognized! Sending location...");
                        getLocationAndSendAlert();
                    } else {
                        statusText.setText("Code word not recognized.");
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        // Set up the location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Button click listener
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListening();
            }
        });

        // Request permissions on app start
        requestPermissions();
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechRecognizer.startListening(intent);
    }

    private void getLocationAndSendAlert() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds interval for updates

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permissions not granted", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastKnownLocation = locationResult.getLastLocation();
                if (lastKnownLocation != null) {
                    // Send the location to the emergency contact
                    ContactManager contactManager = new ContactManager(MainActivity.this);
                    contactManager.sendLocationToEmergencyContact(lastKnownLocation);
                    Toast.makeText(MainActivity.this, "Location sent to emergency contact!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("MainActivity", "Failed to retrieve location.");
                    Toast.makeText(MainActivity.this, "Failed to retrieve location.", Toast.LENGTH_SHORT).show();
                }
            }
        }, getMainLooper());
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS
        };

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void handleError(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                restartListening();
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                statusText.setText("oops! Try again");
                Log.e("SpeechRecognizer", "Client error occurred.");
                Toast.makeText(this, "Client error. Please try again.", Toast.LENGTH_LONG).show();
                break;
            default:
                statusText.setText("oops! Try again");
                Log.e("SpeechRecognizer", "Error code: " + errorCode);
                Toast.makeText(this, "Speech recognition error: " + errorCode, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void restartListening() {
        speechRecognizer.stopListening();
        startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean permissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }

            if (!permissionsGranted) {
                Toast.makeText(this, "All permissions must be granted for the app to function properly.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
