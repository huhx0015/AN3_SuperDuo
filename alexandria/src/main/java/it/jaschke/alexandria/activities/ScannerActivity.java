package it.jaschke.alexandria.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKCode;
import com.mirasense.scanditsdk.interfaces.ScanditSDKOnScanListener;
import com.mirasense.scanditsdk.interfaces.ScanditSDKScanSession;
import it.jaschke.alexandria.R;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing premissions and
 * limitations under the License.
 */

/** -----------------------------------------------------------------------------------------------
 *  [ScannerActivity] CLASS
 *  DESCRIPTION: ScannerActivity is an Activity class that initializes the use of the Scandit
 *  barcode scanner.
 *  -----------------------------------------------------------------------------------------------
 */

public class ScannerActivity extends AppCompatActivity implements ScanditSDKOnScanListener {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SCANDIT VARIBLES
    private ScanditSDK barcodePicker; // The main object for recognizing a displaying barcodes.
    public String SCANDIT_API_KEY = "ITS-A-SECRET!"; // Enter your Scandit SDK App key here.

    // SYSTEM VARIABLES
    private static final String SCAN_RESULTS = "SCAN_RESULTS";
    private Toast toastMessage = null;

    /** ACTIVITY METHODS _______________________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Switches the activity to full screen mode.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Retrieves Scandit SDK key from the secret XML.
        SCANDIT_API_KEY = getResources().getString(R.string.scandit_sdk_key);

        // Initialize and starts the bar code recognition.
        initializeAndStartBarcodeScanning();
    }

    @Override
    protected void onResume() {

        // Once the activity is in the foreground again, restart scanning.
        barcodePicker.startScanning();
        super.onResume();
    }

    @Override
    protected void onPause() {

        // When the activity is in the background immediately stop the scanning to save resources
        // and free the camera.
        barcodePicker.stopScanning();

        super.onPause();
    }

    /** ACTIVITY EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onBackPressed() {
        barcodePicker.stopScanning();
        finish();
    }

    /** SCANDIT METHODS ________________________________________________________________________ **/

    // initializeAndStartBarcodeScanning(): Initializes and starts the bar code scanning.
    public void initializeAndStartBarcodeScanning() {

        // We instantiate the automatically adjusting barcode picker that will
        // choose the correct picker to instantiate. Be aware that this picker
        // should only be instantiated if the picker is shown full screen as the
        // legacy picker will rotate the orientation and not properly work in
        // non-fullscreen.
        ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
                this, SCANDIT_API_KEY, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

        // Add both views to activity, with the scan GUI on top.
        setContentView(picker);
        barcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        barcodePicker.addOnScanListener(this);
    }

    // didScan(): Called when a barcode has been decoded successfully.
    public void didScan(ScanditSDKScanSession session) {

        String message = "";

        for (ScanditSDKCode code : session.getNewlyDecodedCodes()) {
            String data = code.getData();

            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) { cleanData = data.substring(0, 25) + "[...]"; }
            if (message.length() > 0) { message += "\n\n\n"; }
            message += cleanData;
            message += "\n\n(" + code.getSymbologyString() + ")";
        }

        if (toastMessage != null) { toastMessage.cancel(); }

        toastMessage = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toastMessage.show();

        // Finishes activity and returns to the previous activity.
        Intent scanResults = new Intent();
        scanResults.putExtra(SCAN_RESULTS, message);
        setResult(RESULT_OK, scanResults);
        finish();
    }
}
