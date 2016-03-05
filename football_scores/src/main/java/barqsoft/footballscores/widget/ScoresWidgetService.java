package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/** -----------------------------------------------------------------------------------------------
 *  [ScoresWidgetService] CLASS
 *  DEVELOPER: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetService extends RemoteViewsService {

    /** SERVICE METHODS ________________________________________________________________________ **/

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScoresWidgetRemoteViewsFactory(getApplicationContext());
    }
}