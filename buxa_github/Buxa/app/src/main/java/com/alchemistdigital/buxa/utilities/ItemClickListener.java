package com.alchemistdigital.buxa.utilities;

import android.view.View;

/**
 * Created by Pimpu on 11/15/2016.
 */

public interface ItemClickListener {
    void onTransportClick(int position);
    void onCCClick(int position);
    void onFFClick(int position);
    void onPdfViewClick(int position);
    void onAcceptwClick(int position);
    void onCancelClick(int position);
}
