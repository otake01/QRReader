package com.example.qrreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadResult extends AppCompatActivity {

    private LinearLayout infoLayout;
    private TextView resultText;
    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_result);

        //初期化
        infoLayout = findViewById(R.id.info_layout);
        resultText = findViewById(R.id.result_text);
        infoText = findViewById(R.id.info_text);

        String readResult;
        String title;

        Intent intent = getIntent();
        readResult = intent.getStringExtra("READ_RESULT");
        title = intent.getStringExtra("RESULT_TITLE");

        //読み取り結果をセット
        resultText.setText(readResult);

        //読み取ったものがURLかをチェック
        Pattern urlPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+"
                , Pattern.CASE_INSENSITIVE);

        Matcher matcher = urlPattern.matcher(readResult);

        if(matcher.matches())
        {
            infoText.setText(title);
        }else{
            infoLayout.setVisibility(View.INVISIBLE);
        }
    }
}
