package com.example.qrreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReadHistory extends AppCompatActivity {

    private ListView readList;
    private ReadHistoryAdapter adapter;
    private ArrayList<HistoryData> list = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_history);

        //初期化
        readList = findViewById(R.id.read_list);
        readList.setOnItemClickListener(historyClick);

        adapter = new ReadHistoryAdapter(ReadHistory.this);
        adapter.setHistoryDataList(list);
        readList.setAdapter(adapter);

        DataThread dataThread= new DataThread(this);
        dataThread.start();

    }

    //読み取り履歴押下時
    private AdapterView.OnItemClickListener historyClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            ListView listView = (ListView) parent;
            // クリックされたアイテムを取得し、読み取り結果画面に表示
            HistoryData item = (HistoryData) listView.getItemAtPosition(position);

            Intent intent = new Intent(getApplication(), ReadResult.class);
            intent.putExtra("READ_RESULT", item.getText());
            intent.putExtra("RESULT_TITLE", item.getTitle());
            startActivityForResult( intent, 1 );
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 1:
                break;
            default:
        }
    }

    class DataThread extends Thread {
        private Context context;

        public DataThread (Context context) {
            this.context = context;
        }

        @Override
        public void run() {

            //リストに追加
            List<ReadHistoryDataTable> readHistoryData = AppDatabaseSingleton.getInstance(context).readHistoryDataDao().getAll();
            for(int i=0;i<readHistoryData.size();i++)
            {
                HistoryData historyData = new HistoryData();
                historyData.setDate(readHistoryData.get(i).getReadTime());
                historyData.setText(readHistoryData.get(i).getReadResult());
                historyData.setTitle(readHistoryData.get(i).getResultTitle());
                list.add(historyData);
            }

            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    //リストの更新
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
