package info.guardianproject.louder;

/**
 * Created by n8fr8 on 1/30/17.
 * From here: http://stackoverflow.com/questions/17503409/stream-audio-from-internal-microphone-to-3-5mm-headphone-jack
 */
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class TOne extends Thread {
    private Thread T1;
    private byte audiobuffer[] = new byte[100];

    public boolean Okay = true;
    public AudioRecord a;
    public AudioTrack aud;
    private static int[] mSampleRates = new int[] { 22050 };
    
    private static int DEFAULT_RATE = 22050;
   // private static int MIN_BUFFER_SIZE = DEFAULT_RATE;

    private static int MAX_BUFFER = 1024 * 1024;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream(MAX_BUFFER);

    private boolean isPlayer = false;

    @Override
    public void run() {

        int i=AudioRecord.getMinBufferSize(DEFAULT_RATE , AudioFormat.CHANNEL_IN_MONO ,      AudioFormat.ENCODING_PCM_16BIT);
        a= findAudioRecord();

        if(a.getState() != AudioRecord.STATE_INITIALIZED){
            a= findAudioRecord();
        }
        a.startRecording();

        if (isPlayer) {
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
        }

        while(Okay){

            a.read(audiobuffer,0,audiobuffer.length);

            if (isPlayer)
                aud.write(audiobuffer,0,audiobuffer.length);
            else {
                baos.write(audiobuffer, 0, audiobuffer.length);
                Log.d("Tone","buffer stream is: " + baos.size());

                if (baos.size() > MAX_BUFFER)
                    baos.reset();
            }

        }

        if (isPlayer)
            aud.flush();

        a.stop();

    }

    public void stopTone ()
    {
        Okay = false;

    }

    public ByteArrayOutputStream getBuffer ()
    {
        return baos;
    }

    public AudioRecord findAudioRecord() {

        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO }) {
                    try {
                        Log.d("C.TAG", "Attempting rate " + rate + "Hz, bits: " + audioFormat +      ", channel: "
                                + channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate,      AudioFormat.CHANNEL_IN_MONO , AudioFormat.ENCODING_PCM_16BIT);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(AudioSource.MIC, DEFAULT_RATE,      channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                        }
                    } catch (Exception e) {
                        Log.e("C.TAG", rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }



}