package barqsoft.footballscores.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.ScoresFetchService;
import barqsoft.footballscores.ui.ViewHolder;
import barqsoft.footballscores.utilities.Utilities;

/** -----------------------------------------------------------------------------------------------
 *  [ScoresAdapter] CLASS
 *  ORIGINAL DEVELOPER: Yehya Khaled
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class ScoresAdapter extends CursorAdapter {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detailMatchId = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    /** CURSOR ADAPTER METHODS _________________________________________________________________ **/

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        Log.v(ScoresFetchService.LOG_TAG, "newView(): newView inflated.");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.home_name.setText(cursor.getString(COL_HOME));
        mHolder.away_name.setText(cursor.getString(COL_AWAY));
        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        mHolder.score.setText(Utilities.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        mHolder.match_id = cursor.getDouble(COL_ID);
        mHolder.home_crest.setImageResource(Utilities.getTeamCrestByTeamName(
                cursor.getString(COL_HOME)));
        mHolder.away_crest.setImageResource(Utilities.getTeamCrestByTeamName(
                cursor.getString(COL_AWAY)
        ));

        Log.v(ScoresFetchService.LOG_TAG, mHolder.home_name.getText() + " Vs. " + mHolder.away_name.getText() + " id " + String.valueOf(mHolder.match_id));
        Log.v(ScoresFetchService.LOG_TAG, String.valueOf(detailMatchId));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);

        if (mHolder.match_id == detailMatchId) {
            Log.v(ScoresFetchService.LOG_TAG, "bindView(): Insert extraView.");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilities.getMatchDay(cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE), context));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilities.getLeague(cursor.getInt(COL_LEAGUE), context));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText()+" "
                            +mHolder.score.getText()+" "+mHolder.away_name.getText() + " "));
                }
            });
        }

        else { container.removeAllViews(); }
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }
}