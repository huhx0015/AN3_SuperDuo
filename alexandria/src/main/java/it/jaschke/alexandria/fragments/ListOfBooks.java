package it.jaschke.alexandria.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.activities.MainActivity;
import it.jaschke.alexandria.api.BookListAdapter;
import it.jaschke.alexandria.interfaces.Callback;
import it.jaschke.alexandria.data.AlexandriaContract;

/** -----------------------------------------------------------------------------------------------
 *  [BookDetail] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class ListOfBooks extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private Activity currentActivity;
    private BookListAdapter bookListAdapter;
    private final int LOADER_ID = 10;
    private int position = ListView.INVALID_POSITION;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.list_of_books_fab_button) FloatingActionButton addBookButton;
    @Bind(R.id.listOfBooks) ListView bookList;
    @Bind(R.id.searchText) EditText searchText;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // ListOfBooks(): Default constructor for this fragment class.
    public ListOfBooks() {}

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    // onAttach(): The initial function that is called when the Fragment is run. The activity is
    // attached to the fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        currentActivity = activity;
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Cursor cursor = getActivity().getContentResolver().query(
                AlexandriaContract.BookEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        bookListAdapter = new BookListAdapter(getActivity(), cursor, 0);

        View rootView = inflater.inflate(R.layout.fragment_list_of_books, container, false);
        ButterKnife.bind(this, rootView);
        initListView(); // Initializes the ListView.

        return rootView;
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // addFloatingActionButtonClick(): Sets the listener for the floating action button.
    @OnClick(R.id.list_of_books_fab_button)
    public void addFloatingActionButtonClick() {
        ((MainActivity) currentActivity).loadFragment(new AddBook());
    }

    // searchButtonClick(): Sets the listener for the search button.
    @OnClick(R.id.searchButton)
    public void searchButtonClick() {
        ListOfBooks.this.restartLoader();
    }

    // initListView(): Initializes the ListView for this fragment.
    private void initListView() {
        bookList.setAdapter(bookListAdapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = bookListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback) getActivity())
                            .onItemSelected(cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID)));
                }
            }
        });
    }

    /** CURSOR METHODS _________________________________________________________________________ **/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final String selection = AlexandriaContract.BookEntry.TITLE +" LIKE ? OR " + AlexandriaContract.BookEntry.SUBTITLE + " LIKE ? ";
        String searchString =searchText.getText().toString();

        if(searchString.length()>0){
            searchString = "%"+searchString+"%";
            return new CursorLoader(
                    getActivity(),
                    AlexandriaContract.BookEntry.CONTENT_URI,
                    null,
                    selection,
                    new String[]{searchString,searchString},
                    null
            );
        }

        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookListAdapter.swapCursor(data);
        if (position != ListView.INVALID_POSITION) {
            bookList.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookListAdapter.swapCursor(null);
    }

    private void restartLoader(){
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
