package jp.apppot.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    final static String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClickCompanyList(View view) {

        Intent intent = new Intent(this, CompanyListActivity.class);
        startActivity(intent);
    }

    public void onClickLogout(View view) {

    }

}
