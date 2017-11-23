package my.com.eartistic.iahead;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    private final static int AUDIO_DURATION = 250;
    private final static int SAMPLE_RATE = 44100;

    AudioTrack mAudioTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        mETFrequency = (EditText) findViewById(R.id.et_frequency);


        mIBLeft = (ImageButton) findViewById(R.id.btn_left);
        mIBLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playSound(SoundDirection.SD_LEFT, getFrequency(), AUDIO_DURATION, mAudioTrack);
            }
        });

        mIBRight = (ImageButton) findViewById(R.id.btn_right);
        mIBRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playSound(SoundDirection.SD_RIGHT, getFrequency(), AUDIO_DURATION, mAudioTrack);
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

    private void playSound(SoundDirection sd, double frequency, int duration, AudioTrack mAudioTrack) {

        short[] buffer;

        switch (sd) {
            case SD_LEFT:
                mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), 0.0f);
                buffer = generateLeftSineWave(frequency, duration);
                break;
            case SD_RIGHT:
                mAudioTrack.setStereoVolume(0.0f, AudioTrack.getMaxVolume());
                buffer = generateRightSineWave(frequency, duration);
                break;
            default:
                buffer = new short[duration];
        }

        mAudioTrack.write(buffer, 0, buffer.length);
        mAudioTrack.play();
    }

    /***
     *
     * @param frequency
     * @param duration
     * @return
     */
    private short[] generateLeftSineWave( double frequency, int duration) {

        short[] buffer = new short[duration];

        for (int i = 0; i < buffer.length; i += 2) {
            double sample = Math.sin((2.0*Math.PI * i/(SAMPLE_RATE/frequency)));
            buffer[i] = (short) (sample*Short.MAX_VALUE);
        }
        return buffer;
    }

    /***
     *
     * @param frequency
     * @param duration
     * @return
     */
    private short[] generateRightSineWave( double frequency, int duration) {

        short[] buffer = new short[duration];

        for (int i = 0; i < buffer.length; i += 2) {
            double sample = Math.sin((2.0*Math.PI * i/(SAMPLE_RATE/frequency)));
            buffer[i] = (short) (sample*Short.MAX_VALUE);
        }

        return buffer;
    }

}
