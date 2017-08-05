package com.example.leonardo.pokemonapp.UI.customViews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leonardo.pokemonapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by leonardo on 01/08/17.
 */

public class PasswordMatcherView extends ConstraintLayout {


    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.password_confirmation_edit_text)
    EditText passwordConfirmationEditText;

    public PasswordMatcherView(Context context) {
        super(context);
        init();
    }

    public PasswordMatcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordMatcherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.password_matcher, this, true);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        invalidate();
        ButterKnife.bind(this);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String password = passwordEditText.getText().toString();
                final String passwordConfirmation = passwordConfirmationEditText.getText().toString();
                if(!TextUtils.equals(password, passwordConfirmation)) {
                    passwordEditText.setError("Passwords do not match");
                    passwordConfirmationEditText.setError("Passwords do not match");
                } else if(password.isEmpty() && passwordConfirmation.isEmpty()) {
                    passwordEditText.setError(null);
                    passwordConfirmationEditText.setError(null);
                } else if(length() < 8) {
                    passwordEditText.setError("Password must be at least 8 characters long");
                    passwordConfirmationEditText.setError("Password must be at least 8 characters long");
                } else {
                    passwordEditText.setError(null);
                    passwordConfirmationEditText.setError(null);
                }
            }
        };

        passwordEditText.addTextChangedListener(watcher);

        passwordConfirmationEditText.addTextChangedListener(watcher);
    }

    public boolean passwordsValid() {
        final String password = passwordEditText.getText().toString();
        final String passwordConfirmation = passwordConfirmationEditText.getText().toString();
        return TextUtils.equals(password, passwordConfirmation);
    }

   public String getPassword() {
       if(passwordsValid()) {
           return passwordEditText.getText().toString();
       }

       return null;
   }

   public boolean isEmpty() {
       final String password = passwordEditText.getText().toString();
       final String passwordConfirmation = passwordConfirmationEditText.getText().toString();

       return password.isEmpty() && passwordConfirmation.isEmpty() ? true : false;
   }

   public int length() {
       final String password = passwordEditText.getText().toString();
       final String passwordConfirmation = passwordConfirmationEditText.getText().toString();
       return Math.min(password.length(), passwordConfirmation.length());
   }

   public void setPasswordText(String password) {
       passwordEditText.setText(password);
   }

   public void setPasswordConfirmationText(String passwordConfirmation) {
       passwordConfirmationEditText.setText(passwordConfirmation);
   }

   public String getPasswordText() {
       return passwordEditText.getText().toString();
   }

   public String getPasswordConfirmationText() {
       return passwordConfirmationEditText.getText().toString();
   }
}