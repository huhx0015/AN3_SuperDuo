package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import barqsoft.footballscores.R;
import barqsoft.footballscores.activities.MainActivity;

/** -----------------------------------------------------------------------------------------------
 *  [ScoresWidgetProvider] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class ScoresWidgetProvider extends AppWidgetProvider {

    /** WIDGET METHODS _________________________________________________________________________ **/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Loops through the array of widget IDs for the matching widget ID.
        for (int widgetId : appWidgetIds) {

            // Sets the layout for the widget.
            RemoteViews scoresRemoteView = new RemoteViews(context.getPackageName(), R.layout.widget_match_list);

            // Sets the service intent.
            Intent scoresWidgetServiceIntent = new Intent(context, ScoresWidgetService.class);
            scoresWidgetServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            scoresWidgetServiceIntent.setData(Uri.parse(scoresWidgetServiceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            scoresRemoteView.setRemoteAdapter(R.id.match_list_widget_collection_listview, scoresWidgetServiceIntent);

            // Sets the widget intent.
            Intent scoresWidgetIntent = new Intent(context, MainActivity.class);
            scoresWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, scoresWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            scoresRemoteView.setPendingIntentTemplate(R.id.match_list_widget_collection_listview, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, scoresRemoteView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}