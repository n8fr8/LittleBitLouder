
LibNearbyShare is an Android library that is meant to simplify the discovery of other LibNearbyShare enabled Android devices nearby, and sharing data with them. It attemps to unify all possible nearby network technologies under a simple, clean interface that any developer can implement without too much trouble or overhead.

PLEASE NOTE: This library does not require any Internet connection, cloud service, or other centralized method for discovering peers. We don't use GPS, cell towers, or other infrastructure. All detection is done purely using the signals being broadcast by devices themselves.

This project is still under heavy development, and does not yet have a finalized API or implementation. However, as we are doing work to unify and organize code for effectively using Bluetooth, WiFi LAN/NSD sharing, and Wifi P2P Sharing, we felt it is still useful even in its current form. We are also investigating adding additional methods for discovery and sharing, including audio-based discovery using ultrasonic and audible tones, QR code bootstrapping, WiFi Hotspot auto-setup, NFC and more.

License
-------

This library is licensed under the LGPLv2.1.  We believe this is compatible
with all reasonable uses, including proprietary software, but let us know if
it provides difficulties for you.  For more info on how that works with Java,
see:

https://www.gnu.org/licenses/lgpl-java.en.html

Using the Library
-------

There are many methods and tricks to discovering nearby devices via Bluetooth or the various forms of WiFi, and this library aims to make that as opaque and automatic as possible for developers. Here is an example of how you share a file via WiFi Network Service Discovery (NSD).

```java
        NearbyMedia nearbyMedia = new NearbyMedia();
	nearbyMedia.setMimeType("video/mp4");
	nearbyMedia.setTitle("my movie");

	nearbyMedia.setFileMedia(new File("/sdcard/somevideo.mp4"));

	//get a JSON representation of the metadata we want to share
	Gson gson = new GsonBuilder();
        nearbyMedia.mMetadataJson = gson.toJson(myMetadataClass);

        NSDSender nsdSender = NSDSender(this);
        nsdSender.setShareFile(nearbyMedia);
        nsdSender.startSharing();
```

To receive a file over NSD, you start a receiver instance

```java
	NSDReceiver nsdClient = new NSDReceiver(this);
        nsdClient.startDiscovery();
```

That's it! Similary, you can create a Bluetooth sender or receiver:
```java

	BluetoothReceiver btReceiver = new BluetoothReceiver(this);

	//check if the network type is available
        if (btReceiver.isNetworkEnabled()) {
            btReceiver.setNearbyListener(mNearbyListener);
            btReceiver.start();
        }

	BluetoothSender btSender = new BluetoothSender(this);
	btSender.setShareFile(nearbyMedia);
	btSender.startSharing();
```

Once you decide which NearbySender or NearbyReceiver implementation you want to use, you must implement a NearbyListener to receive callbacks from those processes as they discover Neighbors and send or receive NearbyMedia files.

```java
 mNearbyListener = new NearbyListener() {

            @Override
            public void transferComplete (Neighbor neighbor, NearbyMedia media) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_nearby), "Received media file " + media.mTitle, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

            @Override
            public void foundNeighbor (Neighbor neighbor)
            {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_nearby), "Found " + neighbor.mName, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

            @Override
            public void transferProgress (Neighbor neighbor, File fileMedia, String title, String mimeType, long transferred, long total)
            {
                int perComplete = (int) ((((float) (total-transferred)) / ((float) total)) * 100f);

            }

            @Override
            public void noNeighborsFound()
            {

            }
        };

        nsdClient.setListener(mNearbyListener);
```

Get help
------------------

Do not hesitate to contact us with any questions. The best place to start is our community forums and https://devsq.net. To send a direct message, email support@guardianproject.info

We want your feedback! Please report any problems, bugs or feature requests to our issue tracker on this repo.
