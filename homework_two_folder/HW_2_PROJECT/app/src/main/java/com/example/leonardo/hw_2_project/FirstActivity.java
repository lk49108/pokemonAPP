package com.example.leonardo.hw_2_project;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class FirstActivity extends AppCompatActivity {

    private static final String EMPTY_INPUT_STRING_MESSAGE = "Empty url";
    private static final String ACTION_BAR_TITLE = "Web_search_app";

    private EditText urlLabel;
    private Button webViewButton;
    private Button browserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setSupportActionBar((Toolbar) findViewById(R.id.toolBarFirstActivity));
        getSupportActionBar().setTitle(ACTION_BAR_TITLE);
        getSupportActionBar().setIcon(R.drawable.web_view_icon);

        urlLabel = (EditText) findViewById(R.id.urlLabel);
        webViewButton = (Button) findViewById(R.id.buttonOpenWebView);
        browserButton = (Button) findViewById(R.id.buttonOpenBrowser);

        browserButton.setOnClickListener((v) -> {
            if(emptyInput()){
                Toast.makeText(FirstActivity.this, EMPTY_INPUT_STRING_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }
            String urlText = urlLabel.getText().toString();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlText));

            if (intent.resolveActivity(getPackageManager()) != null){
                startActivity(intent);
            }
        });

        webViewButton.setOnClickListener((v) -> {
            if(emptyInput()){
                Toast.makeText(FirstActivity.this, EMPTY_INPUT_STRING_MESSAGE, Toast.LENGTH_SHORT).show();
                return;
            }

            String urlText = urlLabel.getText().toString();

            Intent intent = new Intent(FirstActivity.this, WebBrowserActivity.class);
            intent.setData(Uri.parse(urlText));
            startActivity(intent);
        });
    }

    private boolean emptyInput() {
        if(urlLabel.getText().toString().trim().isEmpty()) {
            return  true;
        }
        return  false;
    }
}
