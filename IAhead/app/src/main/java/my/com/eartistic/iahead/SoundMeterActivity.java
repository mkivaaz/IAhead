package my.com.eartistic.iahead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SoundMeterActivity extends AppCompatActivity {
    private TextView mTVDecib;
    private Button mIBStop;
    private Button mIBStart;

    SoundMeter soundMeter;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_meter);

        soundMeter = new SoundMeter();
        mTVDecib = (TextView) findViewById(R.id.tv_db);

        mIBStop = (Button) findViewById(R.id.btn_stop);
        mIBStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer.purge();
                soundMeter.StopRecorder();
            }
        });

        mIBStart = (Button) findViewById(R.id.btn_start);
        mIBStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                getAmplitudeDB(soundMeter);
            }
        });
    }

    private void getAmplitudeDB(final SoundMeter soundMeter) {
        soundMeter.StartRecorder();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SoundMeterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int amplitude = soundMeter.getAmplitude();
                        if (amplitude > 0 ){
                            double amplitudeDb = 20 * Math.log10(amplitude / 10);
                            mTVDecib.setText(String.format("%.1f",amplitudeDb) + "  db");
                        }

                    }
                });
            }
        },1000,1000);
    }
}
