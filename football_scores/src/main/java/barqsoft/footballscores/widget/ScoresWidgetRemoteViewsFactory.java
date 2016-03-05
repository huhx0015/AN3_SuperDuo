package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import barqsoft.footballscores.R;
import barqsoft.footballscores.database.DatabaseContract;
import barqsoft.footballscores.utilities.Utilities;

/** -----------------------------------------------------------------------------------------------
 *  [ScoresWidgetRemoteViewsFactory] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    private Context context;
    private final static String LOG_TAG = ScoresWidgetRemoteViewsFactory.class.getSimpleName();

    // DATABASE VARIABLES
    private Cursor scoresCursor;
    public static final int COLUMN_MATCHTIME = 2;
    public static final int COLUMN_HOME = 3;
    public static final int COLUMN_AWAY = 4;
    public static final int COLUMN_HOME_GOALS = 6;
    public static final int COLUMN_AWAY_GOALS = 7;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public ScoresWidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }

    /** REMOTE VIEWS FACTORY METHODS ___________________________________________________________ **/

    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {
        if (scoresCursor != null) { scoresCursor.close(); }
    }

    @Override
    public void onDataSetChanged() {

        if (scoresCursor != null) {
            scoresCursor.close();
        }

        // Needed to be run in a separate thread or a permission error will occur.
        Thread thread = new Thread() {
            public void run() {
                scoresCursor = fetchGameMatches();
            }
        };

        thread.start(); // Starts the thread.

        try { thread.join(); }
        catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "onDataSetChanged(): An error occured while attempting to fetch the matches.");
        }
    }

    @Override
    public int getCount() {
        if (scoresCursor != null) { return scoresCursor.getCount(); }
        else { return 0; }
    }

    @Override
    public long getItemId(int position) {
        if (scoresCursor != null) {
            return scoresCursor.getLong(scoresCursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
        }
        else { return 0; }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        // Sets up the fill in intent.
        Intent fillInIntent = new Intent();
        RemoteViews scoresRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_match_list_item);

        // Retrieves the home/away team, date, and score data from the database and sets it to the
        // TextView objects in the widget ListView.
        if (scoresCursor.moveToPosition(position)) {
            String homeTeam = scoresCursor.getString(COLUMN_HOME);
            String awayTeam = scoresCursor.getString(COLUMN_AWAY);
            String date = scoresCursor.getString(COLUMN_MATCHTIME);
            String score = Utilities.getScores(scoresCursor.getInt(COLUMN_HOME_GOALS), scoresCursor.getInt(COLUMN_AWAY_GOALS));
            scoresRemoteView = setRemoteView(scoresRemoteView, date, homeTeam, awayTeam, score);
        }

        scoresRemoteView.setOnClickFillInIntent(R.id.match_list_widget_item_container, fillInIntent);
        return scoresRemoteView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setRemoteView(): Sets the string values for the TextViews in the remote view widget layout.
    private RemoteViews setRemoteView(RemoteViews remoteView, String date, String homeTeam, String awayTeam, String score) {
        remoteView.setTextViewText(R.id.match_list_widget_date_text, date);
        remoteView.setTextViewText(R.id.match_list_widget_home_team_text, homeTeam);
        remoteView.setTextViewText(R.id.match_list_widget_away_team_text, awayTeam);
        remoteView.setTextViewText(R.id.match_list_widget_score_text, score);
        return remoteView;
    }

    /** DATABASE METHODS _______________________________________________________________________ **/

    // fetchGameMatches(): Retrieves the game match data from the database.
    private Cursor fetchGameMatches() {
        return context.getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
    }
}