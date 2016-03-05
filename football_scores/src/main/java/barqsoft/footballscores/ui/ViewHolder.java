package barqsoft.footballscores.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import barqsoft.footballscores.R;

/** -----------------------------------------------------------------------------------------------
 *  [ViewHolder] CLASS
 *  ORIGINAL DEVELOPER: Yehya Khaled
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */
public class ViewHolder {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    public TextView home_name;
    public TextView away_name;
    public TextView score;
    public TextView date;
    public ImageView home_crest;
    public ImageView away_crest;
    public double match_id;

    /** VIEWHOLDER METHODS _____________________________________________________________________ **/

    public ViewHolder(View view) {
        home_name = (TextView) view.findViewById(R.id.home_name);
        away_name = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
        home_crest = (ImageView) view.findViewById(R.id.home_crest);
        away_crest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
