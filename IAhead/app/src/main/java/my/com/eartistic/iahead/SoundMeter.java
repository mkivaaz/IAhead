package my.com.eartistic.iahead;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Muguntan on 11/22/2017.
 */

public class SoundMeter {
    private MediaRecorder mRecorder = null;

    public void StartRecorder(){
        if (mRecorder == null){
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(96000);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    public void StopRecorder(){
        if(mRecorder != null){
            try{
                mRecorder.stop();
            }catch (RuntimeException e){
                e.printStackTrace();
            }

            mRecorder.release();
            mRecorder = null;
        }
    }

    public int getAmplitude(){
        if(mRecorder != null){
            int amplitude = mRecorder.getMaxAmplitude();
            Log.d("Amplitude",String.valueOf(amplitude));
            return amplitude;
        }else{
            return 0;
        }
    }
}
