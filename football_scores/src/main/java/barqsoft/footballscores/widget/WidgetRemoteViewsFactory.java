package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import barqsoft.footballscores.R;

/**
 * Created by Michael Yoon Huh on 12/23/2015.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsFactory
{
    private Context context = null;
    private int appWidgetId;

    private List<String> widgetList = new ArrayList<String>();
//    private DBHelper dbhelper;

    public WidgetRemoteViewsFactory(Context context, Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));
//        dbhelper = new DBHelper(this.context);
    }

    private void updateWidgetListView()
    {
////        String[] widgetFruitsArray = dbhelper.retrieveFruitsList();
//        List<String> convertedToList = new ArrayList<String>(Arrays.asList(widgetFruitsArray));
//        this.widgetList = convertedToList;
    }

    @Override
    public int getCount()
    {
        return widgetList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.scores_list_item);

        Log.d("Loading", widgetList.get(position));
        remoteView.setTextViewText(R.id.home_name, widgetList.get(position));

        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
        // TODO Auto-generated method stub
        updateWidgetListView();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        widgetList.clear();
//        dbhelper.close();
    }
}