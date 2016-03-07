package it.jaschke.alexandria.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import it.jaschke.alexandria.constants.AlexandriaConstants;
import it.jaschke.alexandria.fragments.About;
import it.jaschke.alexandria.fragments.AddBook;
import it.jaschke.alexandria.fragments.BookDetail;
import it.jaschke.alexandria.fragments.ListOfBooks;
import it.jaschke.alexandria.ui.NavigationDrawerFragment;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.interfaces.Callback;

/** -----------------------------------------------------------------------------------------------
 *  [MainActivity] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  DESCRIPTION: MainActivity is the main default Activity class for this application.
 *  -----------------------------------------------------------------------------------------------
 */

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // BROADCAST RECEIVER VARIABLES
    private BroadcastReceiver messageReceiver;
    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";
    public static final String SHOW_BOOK_LIST = "SHOW_BOOK_LIST";

    // FRAGMENT VARIABLES
    private String currentFragment = AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT;

    // LOGGING VARIABLES
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // SYSTEM VARIABLES
    public static boolean IS_TABLET = false;

    // UI VARIABLES
    private CharSequence title; // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private NavigationDrawerFragment navigationDrawerFragment; // Fragment managing the behaviors, interactions and presentation of the navigation drawer.

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If this device is a tablet, a layout specific to tablets is set instead.
        IS_TABLET = isTablet();
        if (IS_TABLET){
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        // Registers the BroadcastReceiver for this activity.
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, filter);

        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    /** ACTIVITY EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onBackPressed() {

        if (currentFragment.equals(AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT)) {
            finish();
        } else {
            currentFragment = AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT;
            loadFragment(new ListOfBooks());
            setTitle(getResources().getString(R.string.app_name));
            setActionbarName(getResources().getString(R.string.app_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail bookFragment = new BookDetail();
        bookFragment.setArguments(args);

        // If device is a tablet and in landscape mode, the right container displays the book
        // details.
        FragmentManager fragManager = getSupportFragmentManager();
        if (findViewById(R.id.right_container) != null){

            // Adds the new BookDetail fragment.
            fragManager.beginTransaction()
                    .replace(R.id.right_container, bookFragment)
                    .commit();

            // FIX: Fixes an issue where the ListOfBooks fragment disappears when the new BookDetail
            // fragment is replaced with an existing fragment.
            fragManager.beginTransaction()
                    .replace(R.id.container, new ListOfBooks())
                    .commit();
        }

        // Replaces the existing fragment with the BookDetail fragment.
        else {
            fragManager.beginTransaction()
                    .replace(R.id.container, bookFragment)
                    .addToBackStack(AlexandriaConstants.BOOK_DETAIL_FRAGMENT)
                    .commit();
        }

        currentFragment = AlexandriaConstants.BOOK_DETAIL_FRAGMENT;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position){
            default:

            // LIST OF BOOKS:
            case 0:
                currentFragment = AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT;
                loadFragment(new ListOfBooks());
                setActionbarName(getString(R.string.app_name));
                break;

            // ADD BOOK:
            case 1:
                currentFragment = AlexandriaConstants.ADD_BOOK_FRAGMENT;
                loadFragment(new AddBook());
                setActionbarName(getString(R.string.drawer_add_book));
                break;

            // ABOUT:
            case 2:
                currentFragment = AlexandriaConstants.ABOUT_FRAGMENT;
                loadFragment(new About());
                setActionbarName(getString(R.string.drawer_about));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // SETTINGS:
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();

        // Checks to see if the ActionBar is null or not before applying custom ActionBar attributes.
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
        }
    }

    // setActionbarName(): Sets the text in the action bar.
    public void setActionbarName(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            setTitle(title);
        }
    }

    public void setTitle(int titleId) {
        title = getString(titleId);
    }

    /** FRAGMENT METHODS _______________________________________________________________________ **/

    // loadFragment(): Loads a fragment into the fragment container view.
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getFragments() != null) {
            fragmentManager.popBackStack(); // Pops the fragment from the stack.
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack((String) title)
                .commit();
    }

    /** MISCELLANEOUS METHODS __________________________________________________________________ **/

    public void goBack(View view) {
        currentFragment = AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT;
        loadFragment(new ListOfBooks());
        setTitle(getResources().getString(R.string.app_name));
        setActionbarName(getResources().getString(R.string.app_name));
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(LOG_TAG, "onReceive(): Message received.");

            // Displays a Toast of the message that was sent via Broadcast.
            if (intent.getStringExtra(MESSAGE_KEY) != null){
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, "onReceive(): MESSAGE_KEY event.");
            }

            // Displays the ListOfBooks fragment.
            else if (intent.getStringExtra(SHOW_BOOK_LIST) != null) {
                loadFragment(new ListOfBooks());
                Log.d(LOG_TAG, "onReceive(): SHOW_BOOK_LIST event.");
            }
        }
    }
}