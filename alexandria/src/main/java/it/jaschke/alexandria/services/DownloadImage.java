package it.jaschke.alexandria.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

/** -----------------------------------------------------------------------------------------------
 *  [DownloadImage] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  -----------------------------------------------------------------------------------------------
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private ImageView bmImage;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public DownloadImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /** DOWNLOAD METHODS _______________________________________________________________________ **/

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap bookCover = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            bookCover = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bookCover;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

