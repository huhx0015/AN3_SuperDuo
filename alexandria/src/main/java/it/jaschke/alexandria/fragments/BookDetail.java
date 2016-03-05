package it.jaschke.alexandria.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.activities.MainActivity;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;

/** -----------------------------------------------------------------------------------------------
 *  [BookDetail] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class BookDetail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // CLASS VARIABLES
    public static final String EAN_KEY = "EAN";
    private final int LOADER_ID = 10;
    private String ean;
    private String bookTitle;
    private ShareActionProvider shareActionProvider;
    private View rootView;

    // FRAGMENT VARIABLES
    private ListOfBooks listOfBooksFragment;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.fullBookCover) ImageView fullBookCover;
    @Bind(R.id.authors) TextView authorText;
    @Bind(R.id.categories) TextView categoriesText;
    @Bind(R.id.fullBookTitle) TextView fullBookTitle;
    @Bind(R.id.fullBookSubTitle) TextView fullBookSubTitle;
    @Bind(R.id.fullBookDesc) TextView fullBookDesc;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // BookDetail(): Default constructor method for this fragment.
    public BookDetail() {}

    // initializeFragment(): Sets a reference to the ListOfBooks fragment for this fragment.
    public void initializeFragment(ListOfBooks fragment) {
        listOfBooksFragment = fragment;
    }

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    // onCreate(): Runs when the fragment is first started.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // onCreateView(): Creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            ean = arguments.getString(BookDetail.EAN_KEY);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        rootView = inflater.inflate(R.layout.fragment_full_book, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    // onPause(): This function is called whenever the fragment is suspended.
    @Override
    public void onPause() {
        super.onDestroyView();
        if (MainActivity.IS_TABLET && rootView.findViewById(R.id.right_container) == null){
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    // onDestroyView(): This function runs when the screen is no longer visible and the view is
    // destroyed.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** FRAGMENT EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.book_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // deleteButtonClick(): Sets the listener for the delete button.
    @OnClick(R.id.delete_button)
    public void deleteButtonClick() {

        // Launches intent to service to signal the deletion of this book from the database.
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.setAction(BookService.DELETE_BOOK);
        getActivity().startService(bookIntent);

        // Loads the ListOfBooks fragment.
        ((MainActivity) getActivity()).loadFragment(new ListOfBooks());
    }

    /** CURSOR METHODS _________________________________________________________________________ **/

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(ean)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        if (!data.moveToFirst()) {
            return;
        }

        bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        fullBookTitle.setText(bookTitle);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text)+bookTitle);
        shareActionProvider.setShareIntent(shareIntent);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        fullBookSubTitle.setText(bookSubTitle);

        String desc = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.DESC));
        fullBookDesc.setText(desc);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        String[] authorsArr = authors.split(",");
        authorText.setLines(authorsArr.length);
        authorText.setText(authors.replace(",","\n"));
        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));

        if (Patterns.WEB_URL.matcher(imgUrl).matches()){
            new DownloadImage(fullBookCover).execute(imgUrl);
            fullBookCover.setVisibility(View.VISIBLE);
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        categoriesText.setText(categories);

        if (rootView.findViewById(R.id.right_container) != null){
            rootView.findViewById(R.id.backButton).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {}
}