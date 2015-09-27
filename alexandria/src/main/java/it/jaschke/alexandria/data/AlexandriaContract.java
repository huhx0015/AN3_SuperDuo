package it.jaschke.alexandria.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/** -----------------------------------------------------------------------------------------------
 *  [AlexandriaContact] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  -----------------------------------------------------------------------------------------------
 */

public class AlexandriaContract {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    public static final String CONTENT_AUTHORITY = "it.jaschke.alexandria";
    public static final String PATH_BOOKS = "books";
    public static final String PATH_AUTHORS = "authors";
    public static final String PATH_CATEGORIES = "categories";
    public static final String PATH_FULLBOOK = "fullbook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static final class BookEntry implements BaseColumns {

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String TABLE_NAME = "books";
        public static final String TITLE = "title";
        public static final String IMAGE_URL = "imgurl";
        public static final String SUBTITLE = "subtitle";
        public static final String DESC = "description";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS).build();
        public static final Uri FULL_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FULLBOOK).build();

        public static Uri buildBookUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFullBookUri(long id) {
            return ContentUris.withAppendedId(FULL_CONTENT_URI, id);
        }

    }

    public static final class AuthorEntry implements BaseColumns {

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_AUTHORS;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_AUTHORS;
        public static final String TABLE_NAME = "authors";
        public static final String AUTHOR = "author";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUTHORS).build();

        public static Uri buildAuthorUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns {

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;
        public static final String TABLE_NAME = "categories";
        public static final String CATEGORY = "category";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}