package com.ratuc.alex.ratuc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;



public class PdfViewActivity extends AppCompatActivity {
    static final String ASSET_TO_OPEN_NAME = "ASSET_TO_OPEN_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        PDFView view = findViewById(R.id.pdfView);

        String assetToOpen = getIntent().getStringExtra(ASSET_TO_OPEN_NAME);               //luam numele pdf-ului trimis din main activity
        view.fromAsset("lines/" + assetToOpen).load();                          //deschidem pdf-ul , folosim librarie
    }
}
