package com.example.leonardo.hw_1_project;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * One and only activity of this android application. It contains two {@link Button}s
 * and one {@link TextView}, known as counter :D.
 * First button increments counter value by one. The other one resets that same counter.
 * Counter changes its text color according to the value it renders. If it contains even number, the color
 * of that number is green. If it contains odd number, the color is blue. Exception of the latter principle is
 * number 0 (zero) which is rendered black.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Counter initial text.
     */
    private static final String COUNTER_INITIAL_TEXT = "0";

    /**
     * Odd number color.
     */
    private static final int COUNTER_ODD_NUMBER_COLOR = Color.BLUE;

    /**
     * Even number color.
     */
    private static final int COUNTER_EVEN_NUMBER_COLOR = Color.GREEN;

    /**
     * Increment {@link Button} reference.
     */
    private Button incrementButton;

    /**
     * Reset {@link Button} reference.
     */
    private Button resetButton;

    /**
     * Counter reference.
     */
    private TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        incrementButton = (Button) findViewById(R.id.increment_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        counter = (TextView) findViewById(R.id.counter);

        resetButton.setOnClickListener((v) -> {
            resetCounter();
        });

        incrementButton.setOnClickListener((v) -> {
            int currentCounterStatus = Integer.parseInt(counter.getText().toString());
            counter.setText(Integer.toString(++currentCounterStatus));
            counter.setTextColor( (currentCounterStatus % 2 == 0) ? COUNTER_EVEN_NUMBER_COLOR : COUNTER_ODD_NUMBER_COLOR );
        });
    }

    /**
     * Resets counter.
     */
    private void resetCounter() {
        counter.setText(COUNTER_INITIAL_TEXT);
        counter.setTextColor(Color.BLACK);
    }
}
