package barqsoft.footballscores.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.Date;
import barqsoft.footballscores.R;
import barqsoft.footballscores.activities.MainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [PagerFragment] CLASS
 *  ORIGINAL DEVELOPER: Yehya Khaled
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class PagerFragment extends Fragment {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // VIEWPAGER VARIABLES
    public static final int NUM_PAGES = 5;
    private MyPageAdapter gameDayPageAdapter;
    private MainScreenFragment[] viewFragments = new MainScreenFragment[5];

    // VIEW INJECTION VARIABLES
    @Bind(R.id.pager) public ViewPager gameDayViewPager;

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false); // Inflates the fragment layout.
        ButterKnife.bind(this, rootView); // ButterKnife initialization.
        setupPager(); // Sets up the ViewPager adapter.
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this); // Unbinds the ButterKnife instance.
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setupPager(): Sets up the ViewPager adapter.
    private void setupPager() {

        gameDayPageAdapter = new MyPageAdapter(getChildFragmentManager());

        // Sets up the fragments for the ViewPager adapter.
        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentDate = new Date(System.currentTimeMillis() + ((i-2) * 86400000));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            viewFragments[i] = new MainScreenFragment();
            viewFragments[i].setFragmentDate(dateFormat.format(fragmentDate));
        }

        gameDayViewPager.setAdapter(gameDayPageAdapter);
        gameDayViewPager.setCurrentItem(MainActivity.current_fragment);
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    private class MyPageAdapter extends FragmentStatePagerAdapter {

        @Override
        public Fragment getItem(int i) {
            return viewFragments[i];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getActivity(),System.currentTimeMillis()+((position-2)*86400000));
        }

        // getDayName(): Retrieves the day name in String format.
        public String getDayName(Context context, long dateInMillis) {

            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.
            Time t = new Time();
            t.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            }

            else if ( julianDay == currentJulianDay +1 ) {
                return context.getString(R.string.tomorrow);
            }

            else if ( julianDay == currentJulianDay -1) {
                return context.getString(R.string.yesterday);
            }

            else {
                Time time = new Time();
                time.setToNow();

                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
            }
        }
    }
}