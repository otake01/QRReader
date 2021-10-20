package com.example.qrreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AndroidRuntimeException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class QRCreate extends AppCompatActivity {

    private LinearLayout editLayout;
    private EditText textEdit;
    private Button createButton;

    private LinearLayout imageLayout;
    private ImageView qrImage;
    private Button shareButton;

    private static final int EDIT_MODE = 0;
    private static final int IMAGE_MODE = 1;

    private int mode;

    //QRコード画像の大きさを指定(pixel)
    private static final int QR_SIZE = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_create);

        //初期化
        editLayout = findViewById(R.id.edit_layout);
        textEdit = findViewById(R.id.text_edit);
        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(createClick);

        imageLayout = findViewById(R.id.image_layout);
        qrImage = findViewById(R.id.qr_image);
        shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(shareClick);

        mode = EDIT_MODE;

        imageLayout.setVisibility(View.GONE);

    }

    //QRの作成
    private View.OnClickListener createClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //QRコード化する文字列
            String input = textEdit.getText().toString();

            if(input.equals(""))
            {
                Toast.makeText(QRCreate.this, "1文字以上入力してください", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    HashMap hints = new HashMap();

                    //文字コードの指定
                    hints.put(EncodeHintType.CHARACTER_SET, "shiftjis");

                    //誤り訂正レベルを指定
                    //L 7%が復元可能
                    //M 15%が復元可能
                    //Q 25%が復元可能
                    //H 30%が復元可能
                    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

                    //QRコードのバージョンを指定
                    hints.put(EncodeHintType.QR_VERSION, 20);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    //QRコードをBitmapで作成
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(input, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE,hints);

                    //作成したQRコードを画面上に配置
                    qrImage.setImageBitmap(bitmap);

                    //QR表示画面に切り替え
                    mode = IMAGE_MODE;
                    editLayout.setVisibility(View.GONE);
                    imageLayout.setVisibility(View.VISIBLE);

                } catch (WriterException e) {
                    throw new AndroidRuntimeException("Barcode Error.", e);
                }
            }
        }
    };

    //QRを共有
    private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //QRコードをスクリーンショット
            qrImage.setDrawingCacheEnabled(true);
            Bitmap cache = qrImage.getDrawingCache();
            Bitmap screenShot = Bitmap.createBitmap(cache);
            qrImage.setDrawingCacheEnabled(false);

            Context context = getApplicationContext();
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            File filePath = new File(cachePath, "qr.png");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath.getPath());
                // 画像のフォーマットと画質と出力先を指定して保存
                screenShot.compress(Bitmap.CompressFormat.PNG, 95, fos);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ie) {
                        fos = null;
                    }
                }
            }

            Uri contentUri = FileProvider.getUriForFile(QRCreate.this, context.getPackageName() + ".fileprovider", filePath);

            //シェア先のアプリを起動
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            //intent.putExtra(Intent.EXTRA_TEXT, "シェアするテキスト");
            startActivity(Intent.createChooser(intent, "アプリを選ぶ"));
        }
    };

    //バックキーが押されたときの制御
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
            if(mode==IMAGE_MODE)
            {
                //QR表示画面の時
                mode = EDIT_MODE;
                editLayout.setVisibility(View.VISIBLE);
                imageLayout.setVisibility(View.GONE);
                return false;
            }else{
                return super.onKeyDown(keyCode, event);
            }
        }
    }
}
