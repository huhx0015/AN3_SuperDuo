package it.jaschke.alexandria.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.activities.ScannerActivity;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.network.NetworkConnectivity;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;

/** -----------------------------------------------------------------------------------------------
 *  [AddBook] CLASS
 *  ORIGINAL DEVELOPER: Sascha Jaschke
 *  MODIFIED BY: Michael Yoon Huh (HUHX0015)
 *  -----------------------------------------------------------------------------------------------
 */

public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final int SCAN_BARCODE_RESULTS = 1337;
    private final int LOADER_ID = 1;
    private static final String SCAN_RESULTS = "SCAN_RESULTS";
    private final String EAN_CONTENT="eanContent";
    private View rootView;

    // VIEW INJECTION VARIABLES
    @Bind(R.id.delete_button) Button deleteButton;
    @Bind(R.id.scan_button) Button scanButton;
    @Bind(R.id.save_button) Button saveButton;
    @Bind(R.id.ean) EditText ean;
    @Bind(R.id.bookCover) ImageView bookCoverImage;
    @Bind(R.id.bookTitle) TextView bookTitle;
    @Bind(R.id.bookSubTitle) TextView bookSubtitle;
    @Bind(R.id.authors) TextView authorsText;
    @Bind(R.id.categories) TextView categoriesText;

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    public AddBook() {}

    /** FRAGMENT LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        ButterKnife.bind(this, rootView);

        setupLayout();

        if (savedInstanceState != null){
            ean.setText(savedInstanceState.getString(EAN_CONTENT));
            ean.setHint("");
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /** FRAGMENT EXTENSION METHODS _____________________________________________________________ **/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_BARCODE_RESULTS && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Bundle bundle = data.getExtras();
                String barcodeMessage = bundle.getString(SCAN_RESULTS);
                ean.setText(barcodeMessage);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(ean!=null) {
            outState.putString(EAN_CONTENT, ean.getText().toString());
        }
    }

    /** CURSOR METHODS _________________________________________________________________________ **/

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(ean.getText().length()==0){
            return null;
        }

        String eanStr= ean.getText().toString();

        if(eanStr.length()==10 && !eanStr.startsWith("978")){
            eanStr="978"+eanStr;
        }

        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
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

        String bookTitleString = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        bookTitle.setText(bookTitleString);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        bookSubtitle.setText(bookSubTitle);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));

        // If authors string data is not null, the authors strings is split up.
        if (authors != null) {
            String[] authorsArr = authors.split(",");
            authorsText.setLines(authorsArr.length);
            authorsText.setText(authors.replace(",", "\n"));
        }

        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));

        if (Patterns.WEB_URL.matcher(imgUrl).matches()){
            new DownloadImage((ImageView) rootView.findViewById(R.id.bookCover)).execute(imgUrl);
            rootView.findViewById(R.id.bookCover).setVisibility(View.VISIBLE);
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        ((TextView) rootView.findViewById(R.id.categories)).setText(categories);

        rootView.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {}

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    // setupLayout(): Sets up the layout for the fragment.
    private void setupLayout() {
        setupButtons();
    }

    // setupButtons(): Sets up the buttons for the fragment.
    private void setupButtons() {

        ean.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                String ean = s.toString();

                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() < 13) {
                    clearFields();
                    return;
                }

                // Checks to see if the device is currently connected to the internet.
                QueryTask task = new QueryTask();
                task.execute(ean);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScannerActivity.class);
                startActivityForResult(i, SCAN_BARCODE_RESULTS);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {}
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent bookIntent = new Intent(getActivity(), BookService.class);
                bookIntent.putExtra(BookService.EAN, ean.getText().toString());
                bookIntent.setAction(BookService.DELETE_BOOK);
                getActivity().startService(bookIntent);
                ean.setText("");
            }
        });
    }

    private void clearFields(){
        bookTitle.setText("");
        bookSubtitle.setText("");
        authorsText.setText("");
        categoriesText.setText("");
        bookCoverImage.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
    }

    private void retrieveBookData(String isbnCode) {
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, isbnCode);
        bookIntent.setAction(BookService.FETCH_BOOK);
        getActivity().startService(bookIntent);
        AddBook.this.restartLoader();
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    // QueryTask(): An AsyncTask that runs in the background to process the network calls for
    // accessing the book database from the Internet.
    public class QueryTask extends AsyncTask<String, Void, Void> {

        /** SUBCLASS VARIABLES _________________________________________________________________ **/

        Boolean isConnected = false; // Used to determine if the device has Internet connectivity.
        String isbnCode; // References the ISBN string code.

        /** ASYNCTASK METHODS __________________________________________________________________ **/

        // doInBackground(): This method constantly runs in the background while AsyncTask is
        // running.
        @Override
        protected Void doInBackground(String... params) {

            // Checks the device's current network and Internet connectivity state.
            isConnected = NetworkConnectivity.checkConnectivity(getActivity());

            if (params != null) { isbnCode = params[0]; } // Sets the ISBN string code.

            return null;
        }

        // onPostExecute(): This method runs on the UI thread after the doInBackground operation has
        // completed.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Retrieves the book data based on the ISBN code.
            if (isConnected) { retrieveBookData(isbnCode); }
        }
    }
}
