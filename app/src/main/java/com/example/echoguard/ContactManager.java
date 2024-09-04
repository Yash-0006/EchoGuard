package com.example.echoguard;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.echoguard.utils.Constants;

public class ContactManager {
    private final Context context;

    public ContactManager(Context context) {
        this.context = context;
    }

    public void sendLocationToEmergencyContact(Location location) {
        SmsManager smsManager = SmsManager.getDefault();
        String message = "I'm in distress. My location is: " + location.getLatitude() + ", " + location.getLongitude();

        try {
            smsManager.sendTextMessage(Constants.EMERGENCY_CONTACT_NUMBER, null, message, null, null);
            Log.d("ContactManager", "SMS sent to: " + Constants.EMERGENCY_CONTACT_NUMBER);
        } catch (Exception e) {
            Log.e("ContactManager", "Failed to send SMS to " + Constants.EMERGENCY_CONTACT_NUMBER, e);
        }
    }
}



//package com.example.echoguard;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.location.Location;
//import android.provider.ContactsContract;
//import android.telephony.SmsManager;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ContactManager {
//    private final Context context;
//
//    public ContactManager(Context context) {
//        this.context = context;
//    }
//
//    public void sendLocationToContacts(Location location) {
//        List<String> contactNumbers = getEmergencyContactNumbers();
//        SmsManager smsManager = SmsManager.getDefault();
//        for (String contactNumber : contactNumbers) {
//            smsManager.sendTextMessage(contactNumber, null, "I'm in distress. My location is: " + location.getLatitude() + ", " + location.getLongitude(), null, null);
//        }
//    }
//
//    private List<String> getEmergencyContactNumbers() {
//        List<String> contactNumbers = new ArrayList<>();
//        Cursor cursor = null;
//        try {
//            cursor = context.getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null, null, null, null
//            );
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    contactNumbers.add(phoneNumber);
//                }
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close(); // Ensure cursor is closed to avoid memory leaks
//            }
//        }
//        return contactNumbers;
//    }
//}
