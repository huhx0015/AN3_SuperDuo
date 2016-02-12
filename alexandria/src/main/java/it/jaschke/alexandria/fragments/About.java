package it.jaschke.alexandria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import it.jaschke.alexandria.R;

/** -----------------------------------------------------------------------------------------------
 *  [About] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class About extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Activity currentActivity;
    private View aboutFragmentView;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    public About() {}

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity;
        activity.setTitle(R.string.drawer_about);
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        aboutFragmentView = inflater.inflate(R.layout.fragment_about, container, false);
        setupImages(); // Sets up images for this fragment layout.
        return aboutFragmentView;
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setupImages(): Sets up the images for the fragment.
    private void setupImages() {
        ImageView aboutImageView = (ImageView) aboutFragmentView.findViewById(R.id.about_image);
        Picasso.with(currentActivity)
                .load(R.drawable.alexandria_logo)
                .into(aboutImageView);
    }
}