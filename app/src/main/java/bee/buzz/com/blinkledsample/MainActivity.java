package bee.buzz.com.blinkledsample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

public class MainActivity extends Activity {

    private static final int FLASH_LOOP_MAX = 10;

    // These GPIO names are based on the rainbow hat breakout kit (See https://pinout.xyz/pinout/rainbow_hat)
    private static final String LED_1_NAME = "GPIO_32"; //Green LED Name
    private static final String LED_2_NAME = "GPIO_34"; //Red LED Name
    private static final String LED_3_NAME = "GPIO_37"; //Blue LED Name

    private static final String TAG = MainActivity.class.getSimpleName();


    private class FlashTask extends AsyncTask<String,String,String> {

        private Gpio[] mLeds = new Gpio[3];

        public FlashTask() {
            try  {
                PeripheralManagerService mgr = new PeripheralManagerService();

                mLeds[0] = mgr.openGpio(LED_1_NAME);
                mLeds[0].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                mLeds[1] = mgr.openGpio(LED_2_NAME);
                mLeds[1].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                mLeds[2] = mgr.openGpio(LED_3_NAME);
                mLeds[2].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            } catch (Exception e) {
                Log.e(TAG, "Could not setup LED GPIO");
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"Starting flash routine");

            for (int i = 1; i <= 10; i++) {

                Log.i(TAG,String.format("Flashing loop %d of %d",i,FLASH_LOOP_MAX));

                try {
                    Thread.sleep(1000);
                    flipAllLED();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private void flipAllLED(){
            for (Gpio led: mLeds) {
                try {
                    if (led.getValue()) {
                        led.setValue(false);
                    } else {
                        led.setValue(true);
                    }
                } catch (Exception e) {
                    Log.e(TAG,  String.format("Could not set GPIO : %s", led.getName()));
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlashTask task = new FlashTask();
        task.execute();
    }
}
