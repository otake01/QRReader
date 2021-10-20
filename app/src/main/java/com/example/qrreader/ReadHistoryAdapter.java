package com.example.qrreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReadHistoryAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<HistoryData> historyDataList;

    public ReadHistoryAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setHistoryDataList(ArrayList<HistoryData> historyDataList) {
        this.historyDataList = historyDataList;
    }

    @Override
    public int getCount() {
        return historyDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return historyDataList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.read_history_list,parent,false);

        ((TextView)convertView.findViewById(R.id.list_date_text)).setText(historyDataList.get(position).getDate());
        ((TextView)convertView.findViewById(R.id.list_title_text)).setText(historyDataList.get(position).getTitle());
        ((TextView)convertView.findViewById(R.id.list_text)).setText(historyDataList.get(position).getText());

        return convertView;
    }
}
