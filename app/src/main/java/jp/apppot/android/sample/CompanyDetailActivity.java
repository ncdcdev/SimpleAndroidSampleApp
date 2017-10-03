package jp.apppot.android.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import jp.apppot.android.sample.databinding.ActivityCompanyDetailBinding;

public class CompanyDetailActivity extends AppCompatActivity {

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        String objectId = getIntent().getStringExtra("objectId");

        if (objectId == null) {
            finish();
        }

        company = new Company();
        company = company.getByObjectId(objectId);

        ActivityCompanyDetailBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_company_detail);
        binding.setCompany(company);

    }

    public void onSaveClick(View view) {

        if (company == null) {
            return;
        }
        company.companyCode = Integer.parseInt(((EditText) findViewById(R.id.companyCodeEditText)).getText().toString());
        company.companyName = ((EditText) findViewById(R.id.companyNameEditText)).getText().toString();
        company.save();
    }

    public void onDeleteClick(View view) {

        if (company == null) {
            return;
        }
        company.delete();
        finish();
    }

    public void onCancelClick(View view) {

        finish();
    }
}

