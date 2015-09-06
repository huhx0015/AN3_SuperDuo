package it.jaschke.alexandria.interfaces;

/**
 * Created by Michael Yoon Huh on 9/7/2015.
 */
public interface OnScanResultsListener {

    // Runs when the scan barcode results are completed
    void displayResultsFragment(String upc, Boolean isDisplay);
}