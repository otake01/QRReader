package com.example.qrreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScanIntegrator;
    private Button qrReaderButton;
    private Button qrCreateButton;
    private Button readHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初期化
        qrReaderButton = findViewById(R.id.qrreader);
        qrReaderButton.setOnClickListener(readerClick);
        qrCreateButton = findViewById(R.id.qrCreate);
        qrCreateButton.setOnClickListener(createClick);
        readHistoryButton = findViewById(R.id.readhistory);
        readHistoryButton.setOnClickListener(readHistoryClick);

        qrScanIntegrator = new IntentIntegrator(this);
        // 縦画面に固定
        qrScanIntegrator.setOrientationLocked(false);
        // QRコード読み取り後のビープ音を停止
        qrScanIntegrator.setBeepEnabled(false);

    }

    //QRコードを読み取る押下時
    private View.OnClickListener readerClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 読み取るバーコードのフォーマットを指定
            qrScanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            // スキャン画面起動
            qrScanIntegrator.initiateScan();
        }
    };

    //QRコードを作る押下時
    private View.OnClickListener createClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplication(), QRCreate.class);
            startActivityForResult( intent, 2 );
        }
    };

    //読み取り履歴押下時
    private View.OnClickListener readHistoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplication(), ReadHistory.class);
            startActivityForResult( intent, 3 );
        }
    };

    // 読取後に呼ばれる
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if(result.getContents() != null) {
                String title = "";
                String readResult = result.getContents();

                //読み取ったものがURLかをチェック
                Pattern urlPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+"
                        , Pattern.CASE_INSENSITIVE);

                Matcher matcher = urlPattern.matcher(readResult);

                if(matcher.matches())
                {
                    WebView webview = new WebView(this);
                    webview.loadUrl(readResult);
                    title = webview.getTitle();
                }

                DataThread dataThread= new DataThread(this,result.getContents(),title);
                dataThread.start();
            } else {
                //キャンセル
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode)
            {
                case 1:
                    break;
                default:
            }
        }
    }

    class DataThread extends Thread {
        private Context context;
        private String result;
        private String title;

        public DataThread (Context context,String result,String title) {
            this.context = context;
            this.result = result;
            this.title = title;
        }

        @Override
        public void run() {
            //読み取り履歴に保存
            //読み取り時刻(現在時刻)の取得
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            ReadHistoryDataTable historyData = new ReadHistoryDataTable(df.format(date),result,title);
            AppDatabaseSingleton.getInstance(context).readHistoryDataDao().insert(historyData);

            if(AppDatabaseSingleton.getInstance(context).readHistoryDataDao().getCount()>50)
            {
                AppDatabaseSingleton.getInstance(context).readHistoryDataDao().deleteTop();
            }

            //別画面に読み取り結果を渡す
            Intent intent = new Intent(getApplication(), ReadResult.class);
            intent.putExtra("READ_RESULT", result);
            intent.putExtra("RESULT_TITLE", title);
            startActivityForResult( intent, 1 );
        }
    }
}
