package my.com.eartistic.iahead;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MAINACTIVITY";

    enum SoundDirection {
        SD_LEFT,
        SD_RIGHT
    };

    private ImageButton mIBLeft;
    private ImageButton mIBRight;
    private Button mIBCheck;

    private EditText mETFrequency;

    private final static int AUDIO_DURATION = 10;
    private final static int SAMPLE_RATE = 44100;
    private final static int NUM_SAMPLE = SAMPLE_RATE * AUDIO_DURATION;
    private final static double SAMPLE[] = new double[NUM_SAMPLE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mETFrequency = (EditText) findViewById(R.id.et_frequency);


        mIBLeft = (ImageButton) findViewById(R.id.btn_left);
        mIBLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(SoundDirection.SD_LEFT, getFrequency(), AUDIO_DURATION);
            }
        });

        mIBRight = (ImageButton) findViewById(R.id.btn_right);
        mIBRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(SoundDirection.SD_RIGHT, getFrequency(), AUDIO_DURATION);
            }
        });

        mIBCheck = (Button) findViewById(R.id.btn_decib);
        mIBCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),SoundMeterActivity.class));
                finish();
            }
        });

    }

    private double getFrequency() {
        String freq = mETFrequency.getText().toString();
        return Double.parseDouble(freq);
    }

    private void playSound(SoundDirection sd, double frequency, int duration) {
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);


        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();


        double[] sound = new double[44100];
        short[] buffer;

        switch (sd) {
            case SD_LEFT:
                buffer = generateLeftSineWave(sound, frequency, duration);
                break;
            case SD_RIGHT:
                buffer = generateRightSineWave(sound, frequency, duration);
                break;
            default:
                buffer = new short[duration];
        }

        mAudioTrack.write(buffer, 0, sound.length);
        mAudioTrack.stop();
        mAudioTrack.release();
    }

    /***
     *
     * @param sound
     * @param frequency
     * @param duration
     * @return
     */
    private short[] generateLeftSineWave(double[] sound, double frequency, int duration) {


        short[] buffer = new short[duration];
        Log.d(TAG, "length: " + sound.length);

        for (int i = 0; i < sound.length; ++i) {
            sound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            buffer[i + 1] = (short) (sound[i]*Short.MAX_VALUE);
            buffer[i] = 0;
        }


        return buffer;
    }

    /***
     *
     * @param sound
     * @param frequency
     * @param duration
     * @return
     */
    private short[] generateRightSineWave(double[] sound, double frequency, int duration) {

        short[] buffer = new short[duration];
        Log.d(TAG, "length: " + sound.length);

        for (int i = 0; i < sound.length; i += 2) {
            sound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            buffer[i + 1] = 0;
            buffer[i] = (short) (sound[i]*Short.MAX_VALUE);
        }

        return buffer;
    }
}
