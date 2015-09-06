package it.jaschke.alexandria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;

/**
 * Created by Michael Yoon Huh on 9/7/2015.
 */
public class ResultsFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Activity currentActivity;

    private String UPC_CODE = "";

    // LOGGING VARIABLES
    private static final String LOG_TAG = ResultsFragment.class.getSimpleName();

    // NARRATION VARIABLES
    private TextToSpeech speechInstance;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.ap_results_upc_text) TextView results_upc_text;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // ResultsFragment(): Default constructor for the ResultsFragment fragment class.
    private final static ResultsFragment results_fragment = new ResultsFragment();

    // ResultsFragment(): Deconstructor method for the ResultsFragment fragment class.
    public ResultsFragment() {}

    // getInstance(): Returns the results_fragment instance.
    public static ResultsFragment getInstance() { return results_fragment; }

    // initializeFragment(): Sets the initial values for the fragment.
    public void initializeFragment(String upc) {
        this.UPC_CODE = upc;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity; // Sets the currentActivity to attached activity object.
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ap_results_view = (ViewGroup) inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, ap_results_view); // ButterKnife view injection initialization.

        setUpLayout(); // Sets up the layout for the fragment.

        return ap_results_view;
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Sets all injected views to null.
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void setUpLayout() {
        results_upc_text.setText(UPC_CODE); // Sets the UPC Code.
    }
}