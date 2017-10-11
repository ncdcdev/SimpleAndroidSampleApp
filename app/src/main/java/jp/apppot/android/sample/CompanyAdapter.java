package jp.apppot.android.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class CompanyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    List<Company> companyList;

    public CompanyAdapter(Context context) {

        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    @Override
    public int getCount() {
        return companyList.size();
}

    @Override
    public Object getItem(int position) {
        return companyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return companyList.get(position).companyCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.company_row, parent, false);

        TextView companyIdTextView = ((TextView)convertView.findViewById(R.id.companyIdTextView));
        companyIdTextView.setText(String.valueOf(companyList.get(position).companyCode));
        ((TextView)convertView.findViewById(R.id.companyNameTextView)).setText(String.valueOf(companyList.get(position).companyName));

        return convertView;
    }
}
