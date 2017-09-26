package jp.apppot.android.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.ncdc.apppot.stew.APError;
import jp.co.ncdc.apppot.stew.APResponseSelectHandler;
import jp.co.ncdc.apppot.stew.dto.APObject;

public class CompanyListActivity extends AppCompatActivity {

    private Context mContext;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComnany();
            }
        });

        mContext = this.getApplicationContext();
        refreshList();
    }

    private void refreshList() {

        Company company = new Company();

        company.getList(new APResponseSelectHandler() {
            @Override
            public void onSuccess(Map<String, List<APObject>> map) {
                List<APObject> objects = map.get("Company");
                final List<String> names = new ArrayList<String>();

                for(APObject object: objects) {
                    String name = ((Company) object).companyName;
                    names.add(name);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ListView listView = (ListView) findViewById(R.id.company_list_view);
                        listView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, names));
                    }
                });
            }

            @Override
            public void onFailure(APError apError) {

            }
        });
    }

    private void addComnany() {
        Company company = new Company();
        company.companyCode = 111;
        company.companyName = "aaaa";
        company.save();
    }
}
