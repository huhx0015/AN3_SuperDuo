package barqsoft.footballscores.widget;

/**
 * Created by Michael Yoon Huh on 10/24/2015.
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;
import barqsoft.footballscores.activities.MainActivity;
import barqsoft.footballscores.database.DatabaseContract;
import barqsoft.footballscores.database.ScoresAdapter;
import barqsoft.footballscores.fragments.MainScreenFragment;
import barqsoft.footballscores.service.ScoresFetchService;

public class ScoresWidgetService extends Service implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG = "de.vogella.android.widget.example";

    public ScoresAdapter mAdapter;
    private String[] fragmentdate = new String[1];

    private RemoteViews remoteViews;

    @Override
    public void onStart(Intent intent, int startId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

//    ComponentName thisWidget = new ComponentName(getApplicationContext(),
//        MyWidgetProvider.class);
//    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {
            // create some random data
            int number = (new Random().nextInt(100));

            remoteViews = new RemoteViews(this
                    .getApplicationContext().getPackageName(),
                    R.layout.widget_scores);


            // OLD Example UPDATE
//            Log.w("WidgetExample", String.valueOf(number));
//            // Set the text
//            remoteViews.setTextViewText(R.id.update,
//                    "Random: " + String.valueOf(number));


            // TODO: Update the scores here.
            update_scores();
            setDate(); // Updates the date.

            setupScoreList(); // Sets up the score list widget.


            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(),
                    ScoresWidgetProvider.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        stopSelf();

        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void update_scores() {
        Intent service_start = new Intent(this, ScoresFetchService.class);
        startService(service_start);
    }


    private void setDate() {
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        fragmentdate[0] = mformat.format(todayDate);
    }


    private void setupScoreList() {

        //            // Set the text
//            remoteViews.setTextViewText(R.id.update,
//                    "Random: " + String.valueOf(number));

        //ListView score_list = (ListView) remoteViews.findViewById(R.id.widget_scores_list);
        mAdapter = new ScoresAdapter(this,null,0);
        //score_list.setAdapter(mAdapter);
        //getLoaderManager().initLoader(SCORES_LOADER,null,this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,fragmentdate,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");
        //cursor.moveToFirst();
        /*
        while (!cursor.isAfterLast())
        {
            Log.v(FetchScoreTask.LOG_TAG,cursor.getString(1));
            cursor.moveToNext();
        }
        */

        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            i++;
            cursor.moveToNext();
        }


        //Log.v(FetchScoreTask.LOG_TAG,"Loader query: " + String.valueOf(i));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }
}