package info.guardianproject.louder;

/**
 * Created by n8fr8 on 1/30/17.
 * From here: http://stackoverflow.com/questions/17503409/stream-audio-from-internal-microphone-to-3-5mm-headphone-jack
 */
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TOnePlayer extends Thread {
    private Thread T1;

    public boolean Okay = true;
    public AudioTrack aud;
    private static int[] mSampleRates = new int[] { 22050 };
    
    private final static int DEFAULT_RATE = 22050;
    private final static int BUFFER = 100;
    private byte[] buffer = new byte[100];
   // private static int MIN_BUFFER_SIZE = DEFAULT_RATE;

    private boolean isPlayer = false;
    private ByteArrayInputStream bais;

    public TOnePlayer (ByteArrayInputStream bais)
    {
        this.bais = bais;
    }

    @Override
    public void run() {

        Log.e("Play Audio", "Start");

        aud = new AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(DEFAULT_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);

        if (aud.getState() != AudioTrack.STATE_INITIALIZED) {
            aud = new AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_RATE,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(DEFAULT_RATE, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);

        }

        aud.play();

        while(Okay){

            try {
                int len = bais.read(buffer);

                if (len != -1)
                    aud.write(buffer, 0, len);

            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        aud.flush();

    }

    public void stopTone ()
    {
        Okay = false;

    }




}