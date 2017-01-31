package info.guardianproject.louder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;

import info.guardianproject.nearby.NearbyListener;
import info.guardianproject.nearby.NearbyMedia;
import info.guardianproject.nearby.Neighbor;
import info.guardianproject.nearby.bluetooth.BluetoothReceiver;
import info.guardianproject.nearby.bluetooth.BluetoothSender;
import louder.guardianproject.info.louder.R;

public class MainActivity extends AppCompatActivity {

    private TOne mTone;
    private TOnePlayer mTonePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void doSpeak (View button)
    {
        if (requestPermissions()) {
            startAudio();
            startNearbySend();
        }
    }

    public void doListen (View button)
    {
        startPlayer();
        startNearbyListen();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopAudio();
    }

    private void startNearbySend ()
    {
        BluetoothSender bs = new BluetoothSender(this);
        bs.setNearbyListener(new NearbyListener() {
            @Override
            public void transferComplete(Neighbor neighbor, NearbyMedia media) {

            }

            @Override
            public void foundNeighbor(Neighbor neighbor) {
                Toast.makeText(MainActivity.this,"Found neighbor to send to: " + neighbor.mName,Toast.LENGTH_LONG).show();

            }

            @Override
            public void transferProgress(Neighbor neighbor, File fileMedia, String title, String mimeType, long transferred, long total) {

            }

            @Override
            public void noNeighborsFound() {

            }
        });
        bs.startSharing();
    }

    private void startNearbyListen ()
    {
        BluetoothReceiver br = new BluetoothReceiver(this);
        br.setNearbyListener(new NearbyListener() {
            @Override
            public void transferComplete(Neighbor neighbor, NearbyMedia media) {

            }

            @Override
            public void foundNeighbor(Neighbor neighbor) {
                Toast.makeText(MainActivity.this,"Found neighbor to receive from: " + neighbor.mName,Toast.LENGTH_LONG).show();

            }

            @Override
            public void transferProgress(Neighbor neighbor, File fileMedia, String title, String mimeType, long transferred, long total) {

            }

            @Override
            public void noNeighborsFound() {

            }
        });
        br.start();
    }

    private void startAudio ()
    {

        mTone = new TOne();
        mTone.start();
    }

    private void stopAudio ()
    {
        if (mTone != null)
            mTone.stopTone();
    }

    private void startPlayer ()
    {
        /**
        ByteArrayInputStream bais = new ByteArrayInputStream();
        mTonePlayer = new TOnePlayer(bais);
        mTonePlayer.start();
         **/
    }


    private final static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private final static int MY_PERMISSIONS_REQUEST_BLUETOOTH = MY_PERMISSIONS_REQUEST_RECORD_AUDIO + 1;

    private boolean requestPermissions () {
        // Here, thisActivity is the current activity
        // Should we show an explanation?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                return false;
            }

        // Here, thisActivity is the current activity
        // Should we show an explanation?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                return false;

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                return false;
            }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startAudio();

                    requestPermissions();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }
}

