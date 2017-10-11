package jp.apppot.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.co.ncdc.apppot.stew.APConfig;
import jp.co.ncdc.apppot.stew.APEntityManager;
import jp.co.ncdc.apppot.stew.APResponse;
import jp.co.ncdc.apppot.stew.APResponseHandler;
import jp.co.ncdc.apppot.stew.APService;
import jp.co.ncdc.apppot.stew.APUserInfo;
import jp.co.ncdc.apppot.stew.dto.APObject;
import jp.co.ncdc.apppot.stew.json.AJSONObject;
import jp.co.ncdc.apppot.stew.log.LogLevel;

import static jp.apppot.android.sample.AppPotConfig.companyId;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    protected Handler mHandler = null;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mHandler = new Handler();
        initAppInfo();
        createDatabase();
    }

    private void initAppInfo() {
        final APService service = APService.getInstance();
        service.setLogLevel(LogLevel.verbose);
        if (service.isInit()) {
            return;
        }

        Log.v(this.getLocalClassName(), "initAppInfo start");
        try {
            service.setServiceInfo(
                    getApplicationContext(),
                    companyId,
                    AppPotConfig.appID,
                    AppPotConfig.appKey,
                    AppPotConfig.appVersion,
                    AppPotConfig.hostName,
                    AppPotConfig.contextRoot,
                    AppPotConfig.portNumber,
                    AppPotConfig.isUsePushNotification,
                    AppPotConfig.isUseSSL);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Log.v(this.getLocalClassName(), "initAppInfo end");
    }

    private void login(String userId, String password) {
        final APService service = APService.getInstance();
        service.authentication(companyId, userId, password, new APResponseHandler() {
            @Override
            public void onSuccess(APResponse result) {

                Log.i("login", "login:onSuccess");
                APUserInfo info = APService.getInstance().getUserInfo();
                Log.d("login", "info.lastName=" + info.lastName);

                handleToastMessage("Login successed.");

                // メニュー画面へ遷移
                goMenu();
            }

            @Override
            public void onFailure(APResponse result) {
                AJSONObject object = (AJSONObject) result.getResponseData();
                String message = object.getString(APConfig.Key.DESCRIPTION);
                Log.w("login", "login:onFailure:" + message);
                handleToastMessage(message);
            }
        });
    }

    /**
     * Database生成
     */
    private void createDatabase() {
        Log.i(this.getLocalClassName(), "createDB:start");

        List<APObject> tables = new ArrayList<>();
        tables.add(new Company());
        tables.add(new Employee());
        tables.add(new Task());

        // isReset=falseにすることで、既にあるときにはデータベースは作られません
        boolean isResetDatabase = false;
        int DATABASE_VERSION = 1;

        // ここでDBを作っています
        APEntityManager.getInstance().createAppDB(this, tables, AppPotConfig.companyId, isResetDatabase, DATABASE_VERSION, new APResponseHandler() {
            @Override
            public void onSuccess(APResponse result) {
                Log.i("createDatabase", "createDB:onSuccess");
            }

            @Override
            public void onFailure(APResponse result) {
                handleToastMessage("ローカルデータベースの作成に失敗しました。アプリを終了します。");
                Log.d("createDatabase", "createDB:onFailure=" + result.getResponseData());
                finish();
            }

            @Override
            public void onError(String messageHTML, Throwable e) {
                handleToastMessage("ローカルデータベースの作成に失敗しました。アプリを終了します。");
                Log.e("createDatabase", messageHTML, e);
                super.onError(messageHTML, e);
                finish();
            }
        });
    }

    protected void handleToastMessage(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            login(email, password);
        }
    }

    private void goMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}

