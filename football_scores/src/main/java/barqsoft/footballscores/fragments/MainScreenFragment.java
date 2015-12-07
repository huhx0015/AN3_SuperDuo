package barqsoft.footballscores.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import barqsoft.footballscores.R;
import barqsoft.footballscores.database.DatabaseContract;
import barqsoft.footballscores.ui.ViewHolder;
import barqsoft.footballscores.activities.MainActivity;
import barqsoft.footballscores.database.ScoresAdapter;
import barqsoft.footballscores.service.ScoresFetchService;
import butterknife.Bind;
import butterknife.ButterKnife;

/** -----------------------------------------------------------------------------------------------
 *  [MainScreenFragment] CLASS
 *  ORIGINAL DEVELOPER: Yehya Khaled
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // FRAGMENT VARIABLES
    private String[] fragmentDate = new String[1];

    // LIST VARIABLES
    public static final int SCORES_LOADER = 0;
    private int last_selected_item = -1;
    public ScoresAdapter scoresAdapter;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.scores_list) ListView scoresListView;
    @Bind(R.id.fragment_main_progress_bar) ProgressBar mainProgressBar;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // MainScreenFragment(): Constructor method.
    public MainScreenFragment() {}

    // setFragmentDate(): Sets the fragmentDate value for the fragment.
    public void setFragmentDate(String date) {
        fragmentDate[0] = date;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        setupLayout();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** LOADER MANAGER METHODS _________________________________________________________________ **/

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(), null,null, fragmentDate,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(ScoresFetchService.LOG_TAG, "onLoadFinished(): Loader has finished loading.");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.v(ScoresFetchService.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }

        Log.v(ScoresFetchService.LOG_TAG, "onLoadFinished(): Loader query: " + String.valueOf(i));
        scoresAdapter.swapCursor(cursor);
        scoresAdapter.notifyDataSetChanged(); // Refreshes the ListView contents.

        mainProgressBar.setVisibility(View.GONE); // Hides the progress bar.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        scoresAdapter.swapCursor(null);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setupLayout(): Sets up the layout for this fragment.
    private void setupLayout() {
        fetchScores(); // Starts the service to retrieve the latest football scores.
        setupScoreList(); // Sets up the score list.
    }

    // setupScoreList(): Sets up the score list ListView.
    private void setupScoreList() {

        // Creates a new ScoresAdapter object and sets the adapter to the score list.
        scoresAdapter = new ScoresAdapter(getActivity(), null, 0);
        scoresListView.setAdapter(scoresAdapter);

        // Initializes the LoaderManager.
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        scoresAdapter.detailMatchId = MainActivity.selected_match_id;

        // Sets a click listener to each item in the ListView.
        scoresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                scoresAdapter.detailMatchId = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                scoresAdapter.notifyDataSetChanged(); // Refreshes the ListView contents.
            }
        });
    }

    /** SCORE SERVICE METHODS __________________________________________________________________ **/

    // fetchScores(): Starts the ScoresFetchService to retrieve the scores.
    private void fetchScores() {

        mainProgressBar.setVisibility(View.VISIBLE); // Displays the progress bar.

        // Sets up an intent to start the ScoresFetchService to retrieve the scores.
        Intent startService = new Intent(getActivity(), ScoresFetchService.class);
        getActivity().startService(startService);
    }
}