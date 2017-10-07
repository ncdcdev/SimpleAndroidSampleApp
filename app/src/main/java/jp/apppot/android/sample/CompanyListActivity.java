package jp.apppot.android.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.ncdc.apppot.stew.APError;
import jp.co.ncdc.apppot.stew.APResponseSelectHandler;
import jp.co.ncdc.apppot.stew.dto.APObject;
import jp.co.ncdc.apppot.stew.log.Logger;

public class CompanyListActivity extends AppCompatActivity {

    private Context mContext;
    private List<Company> companyList;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshList();
    }

    private void refreshList() {

        new Company().getList(new APResponseSelectHandler() {
            @Override
            public void onSuccess(Map<String, List<APObject>> map) {
                List<APObject> objects = map.get("Company");
                companyList = new ArrayList<>();

                final List<String> names = new ArrayList<String>();

                for(APObject object: objects) {
                    Company company = (Company) object;
                    companyList.add(company);

                    String name = company.companyName;
                    names.add(name);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    ListView listView = (ListView) findViewById(R.id.company_list_view);
                    listView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, names));
                    listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            ListView listView = (ListView) parent;
                            // クリックされたアイテムを取得します
                            String item = (String) listView.getItemAtPosition(position);
                            Logger.d(item);

                            Intent intent = new Intent(mContext, CompanyDetailActivity.class);
                            intent.putExtra("objectId", companyList.get(position).objectId);
                            startActivity(intent);
                        }
                    });
                    }
                });
            }

            @Override
            public void onFailure(APError apError) {

            }
        });
    }


    private void addComnany() {
        Intent intent = new Intent(mContext, CompanyDetailActivity.class);
        intent.putExtra("objectId", "");
        startActivity(intent);
    }
}
