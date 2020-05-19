package com.nqr.smsapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;






import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {


    String address = null, name = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    ArrayList<String> smsMessagesList = new ArrayList<>();
    ListView messages;
    ArrayAdapter arrayAdapter;
    EditText input;
    SmsManager smsManager = SmsManager.getDefault();
    private static MainActivity inst;
    public static boolean active = false;

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    public static MainActivity instance() {

        return inst;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startService(new Intent(this, QuickResponseService.class));
        messages = (ListView) findViewById(R.id.messages);
        input = (EditText) findViewById(R.id.input);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsMessagesList);
        messages.setAdapter(arrayAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadContacts();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();

        } else {
            //jim refreshSmsInbox();
        }




        try {
            URL url_g = new URL("http://www.google.com/");
            new DownloadFilesTask().execute(url_g);
        } catch(MalformedURLException e) {
            //Do something with the exception.
        }






//        try {
  //          setw();
    //    } catch (Exception e) {
      //  }


    }


    @SuppressLint("ClickableViewAccessibility")
    private void setw() throws IOException {

        bluetooth_connect_device();


    }


    private void led_on_off(String i) {


        new DownloadFilesTask2().execute(i);


    }


    private void bluetooth_connect_device() throws IOException {
        try {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    address = bt.getAddress().toString();
                    name = bt.getName().toString();
                    //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

                }
            }

        } catch (Exception we) {
        }
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();


    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
        inst = this;

    }

    static boolean f_b_state = false;

    public void updateInbox(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();


        String string = smsMessage;
        String[] parts = string.split("\n");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556

        led_on_off(part2);

        //if(f_b_state) {
        //    led_on_off("f");
        //    f_b_state=false;
        //}else{
        //    led_on_off("b");
        //    f_b_state=true;
        //}


    }

    public void onSendClick(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        } else {
            String jimOldCricket="+19015308899";
            String myinput= input.getText().toString();
            myinput= "F";
            smsManager.sendTextMessage(jimOldCricket, null, myinput, null, null);
            Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSIONS_REQUEST);
        }
    }

    public void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);


        int indexBody = smsInboxCursor.getColumnIndex("body");

        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
        do {
            String str = "SMS From: " + getContactName(this, smsInboxCursor.getString(indexAddress)) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            // if (smsInboxCursor.getString(indexAddress).equals("PHONE NUMBER HERE")) {
            arrayAdapter.add(str);
            //  }
        } while (smsInboxCursor.moveToNext());


    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    public static String getContactName(Context context, String phoneNo) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return phoneNo;
        }
        String Name = phoneNo;
        if (cursor.moveToFirst()) {
            Name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return Name;

    }


    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        // Do the long-running work in here
        protected Long doInBackground(URL... urls) {



            try {
                if(false) {  //jim
                    bluetooth_connect_device();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (isCancelled()) Long.valueOf(0);
            return Long.valueOf(0);
        }
        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }
        // This is called when doInBackground() is finished
        protected void onPostExecute(Long result) {
            showNotification("Downloaded " + result + " bytes");
        }
    }

    private class DownloadFilesTask2 extends AsyncTask<String, Integer, Long> {
        // Do the long-running work in here
        protected Long doInBackground(String... strings) {

String i= strings[0];


            try {
                if (btSocket != null) {

                    btSocket.getOutputStream().write(i.toString().getBytes());
                }

            } catch (Exception e) {


            }






            if (isCancelled()) Long.valueOf(0);
            return Long.valueOf(0);
        }
        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent2(progress[0]);
        }
        // This is called when doInBackground() is finished
        protected void onPostExecute(Long result) {
            showNotification2("Downloaded " + result + " bytes");
        }
    }

    private void showNotification(String s) {
        int i=1;
    }
    private void setProgressPercent(Integer progress) {
        int i=1;
    }
    private void showNotification2(String s) {
        int i=1;
    }
    private void setProgressPercent2(Integer progress) {
        int i=1;
    }


    
}
