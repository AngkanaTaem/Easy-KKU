package kku.team.angkana.easykku;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class SingUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, phonEditText,
            userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;
    private String nameString, phonString, userString, passwordString,
    imagePathString, imageNameString;
    private Uri uri;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //Bond Widget
        //Key :: Cast -> Alt +enter; End Process -> Shift + Ctrl + Enter
        nameEditText = (EditText) findViewById(R.id.editText);
        phonEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);


        //SidnUp Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Get Value From Edit Text
                nameString = nameEditText.getText().toString().trim();
                phonString = phonEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check Space
                if (nameString.equals("") || phonString.equals("") ||
                        userString.equals("") || passwordString.equals("")) {
                    //Have Space
                    Log.d("12novV1", "Have Space");
                    MyAlert myAlert = new MyAlert(SingUpActivity.this, R.drawable.doremon48,
                            "มีช่องว่าง", "กรุณากรอกให้ครบทุกช่องครับ");
                    myAlert.myDialog();

                } else if (aBoolean) {
                    //Non Choose Image
                    MyAlert myAlert = new MyAlert(SingUpActivity.this, R.drawable.nobita48,
                            "ยังไม่เลื่อกรูป", "กรุณาเลือกรูปครับ");
                    myAlert.myDialog();

                } else {
                    //choose Image OK
                    upLoadImageToServer();
                }

            }   //onClick
        });


        // Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกแอปดูภาพ"), 0);
                //เลขเป็นอะไรก็ได้ ขึ้นอยู่กับรูปภาพ Request code


            }//onClick
        });

    }   // Main Method

    private void upLoadImageToServer() {

        //Change Policy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {

            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com", 21,
                    "kku@swiftcodingthai.com", "Abc12345");
            simpleFTP.bin(); //Change image to string
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

        } catch (Exception e) {
            Log.d("12novV1", "e simpleFTP ==> " + e.toString());
        }

    }   // upLode

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {

            Log.d("12novV1", "Result OK");
            aBoolean = false;

            // show image
            uri = data.getData();
            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(uri));
                imageView.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }

            //Find Path of Image
            imagePathString = myFindPath(uri);
            Log.d("12novV1", "imagePath ==> " + imagePathString);

            //Find Name of image
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("12novV1", "image ==> " + imageNameString);

        }   // if

    }   // onActivity result

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {
            result = uri.getPath();
        }

        return result;
    }
}   // Main Class
