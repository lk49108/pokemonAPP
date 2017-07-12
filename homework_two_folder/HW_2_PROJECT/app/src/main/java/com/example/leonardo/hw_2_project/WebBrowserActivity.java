package com.example.leonardo.hw_2_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebBrowserActivity extends AppCompatActivity {

    private static final String URL_REGEX_PATTERN = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

    private String homePageUrl = "https://www.google.com";
    private String urlString;

    private WebView webView;
    private WebSettings webSettings;

    private MenuItem backButton;
    private MenuItem forwardButton;

    private Button goButton;
    private EditText urlLabel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        getSupportActionBar().setIcon(R.drawable.web_view_icon);

        hideKeyboardIfActive();

        obtainViews();

        configureWebView();

        goButton.setOnClickListener((v) -> {
            loadWebPage();
            hideKeyboardIfActive();
        });

        Intent intent = getIntent();
        urlString = intent.getData().toString();

        checkFirstTimeUrlLoading();
    }

    private void checkFirstTimeUrlLoading() {
        if(isValidUrl()){
            loadWebPage(urlString);
        } else {
            Toast.makeText(this, "Invalid url, redirected to homepage...", Toast.LENGTH_LONG).show();
            loadHomePage();
        }
    }

    private void configureWebView() {

        //Setting webViewClient...
        setWebViewClient();

        //Setting webChromeClient...
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        //Setting webSettings...
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(webView.getUrl().toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void obtainViews() {
        goButton = (Button) findViewById(R.id.goButton);
        urlLabel = (EditText) findViewById(R.id.urlInput);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (WebView) findViewById(R.id.webView);
    }

    private void hideKeyboardIfActive() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    private void loadHomePage() {
        loadWebPage(homePageUrl);
    }

    private boolean isValidUrl(){
        Pattern p = Pattern.compile(URL_REGEX_PATTERN);
        Matcher m = p.matcher(urlString);
        return m.matches();
    }

    private void loadWebPage(String url) {
        webView.loadUrl(url);
    }

    private void loadWebPage() {
        urlString = urlLabel.getText().toString();
        webView.loadUrl(urlString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_navigation, menu);
        inflater.inflate(R.menu.menu_other, menu);

        backButton = menu.findItem(R.id.action_back);
        forwardButton = menu.findItem(R.id.action_forward);

        if(webView == null || !webView.canGoBack()) {
            disable(backButton);
        } else {
            enable(backButton);
        }
        if(webView == null || !webView.canGoForward()) {
            disable(forwardButton);
        } else {
            enable(forwardButton);
        }

        return true;
    }

    private void enable(MenuItem button) {
        button.setEnabled(true);
        button.setVisible(true);
    }

    private void disable(MenuItem button) {
        button.setEnabled(false);
        button.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                if(webView.canGoBack()){
                    webView.goBack();
                }
                return true;
            case R.id.action_forward:
                if(webView.canGoForward()){
                    webView.goForward();
                }
                return true;
            case R.id.action_homePage :
                loadHomePage();
                return true;
            case  R.id.action_changeHomepage :
                makeAndShowDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeAndShowDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptView = li.inflate(R.layout.home_page_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText homepageInput = (EditText) promptView.findViewById(R.id.homepageInsert);
        homepageInput.setText(homePageUrl);

        AlertDialog dialog = null;

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", (dialInt, i) -> {
                    homePageUrl = homepageInput.getText().toString();
                    dialInt.cancel();
                })
                .setNegativeButton("Cancel", (dialInt, i) -> {
                    dialInt.cancel();
                });

        dialog = alertDialogBuilder.create();
        dialog.show();
    }
}