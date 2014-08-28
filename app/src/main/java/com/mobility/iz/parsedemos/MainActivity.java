package com.mobility.iz.parsedemos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    private Context mContext;

    @InjectView(R.id.etPhoneNo)
    EditText etPhoneNo;
    @InjectView(R.id.etEmailId)
    EditText etEmailId;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.etUserName)
    EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ParseAnalytics.trackAppOpened(getIntent());
        mContext = this;

        ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
            public void done(String result, ParseException e) {
                if (e == null) {

                    System.out.println("Result is : " + result);

                    Toast.makeText(getApplicationContext(), "Result from parse:" + result, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {

            saveUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to save register user on Parse.
     */
    private void saveUser() {

        ParseUser user = new ParseUser();
        user.setUsername(etUserName.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setEmail(etEmailId.getText().toString());

// other fields can be set just like with ParseObject
        user.put("phone", etPhoneNo.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(mContext, getString(R.string.sign_up_success),Toast.LENGTH_LONG).show();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong

                    Toast.makeText(mContext, getString(R.string.sign_up_failed),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method to hide Keyboard
     */
    private void hideKeyboard() {
        View focus = getCurrentFocus();
        if(null != focus) {
            IBinder binder = focus.getWindowToken();
            InputMethodManager imeManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imeManager.hideSoftInputFromWindow(binder, 0);
        }

    }

    /**
     * Animation for shaking a view.
     *
     * @param view View which is to be animated.
     */
    private void shakeAnimation(View view) {
        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        view.startAnimation(shake);
    }
}
