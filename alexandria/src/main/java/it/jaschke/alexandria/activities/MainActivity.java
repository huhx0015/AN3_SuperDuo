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

    // FRAGMENT VARIABLES
    private String currentFragment = AlexandriaConstants.LIST_OF_BOOKS_FRAGMENT;

    // SYSTEM VARIABLES
    public static boolean IS_TABLET = false;
    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

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

        messageReceiver = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,filter);

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

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        int id = R.id.container;
        if (findViewById(R.id.right_container) != null){
            id = R.id.right_container;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment)
                .addToBackStack(AlexandriaConstants.BOOK_DETAIL_FRAGMENT)
                .commit();

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
                break;

            // ADD BOOK:
            case 1:
                currentFragment = AlexandriaConstants.ADD_BOOK_FRAGMENT;
                loadFragment(new AddBook());
                break;

            // ABOUT:
            case 2:
                currentFragment = AlexandriaConstants.ABOUT_FRAGMENT;
                loadFragment(new About());
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

    private class MessageReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null){
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }
}