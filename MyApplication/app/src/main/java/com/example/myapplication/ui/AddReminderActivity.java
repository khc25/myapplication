package com.example.myapplication.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.AlarmReceiver;
import com.example.myapplication.R;
import com.example.myapplication.utils.DBSOpenHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private String seldate,alerttime;
    private TextView tv_date,et_time;
    private EditText et_content;
    private EditText et_category;
    private ImageView iv_imgs;
    private Button bt_submit;
    private DBSOpenHelper dbOpenHelper;

    public static final  int GALLERY_REQUEST_CODE = 0x01;

    private static final int CAMERA_REQUEST_CODE = 0x02;

    private File imgDir;
    private String mSavedImagePath;
    private View _view;
    String timeString,dateString;
    long mstime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        seldate = getIntent().getStringExtra("seldate");
        tv_date = findViewById(R.id.tv_date);
        et_time =findViewById(R.id.et_time);
        tv_date.setText(seldate);
        et_content = findViewById(R.id.et_content);
        et_category = findViewById(R.id.et_category);
        iv_imgs = findViewById(R.id.iv_imgs);
        iv_imgs.setOnClickListener(this);
        bt_submit = findViewById(R.id.bt_save);
        bt_submit.setOnClickListener(this);
        timeString="";
        dateString="";

        et_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment timerPicker =new TimePickerFragment();
                timerPicker.show(getSupportFragmentManager(),"timer picker");
            }
        });

    }
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
    }
    private void updateTimeText(Calendar c) {
        et_time =findViewById(R.id.et_time);
        et_time .setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        timeString = formatter.format(new Date(c.getTimeInMillis()));
        mstime=c.getTimeInMillis();
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_imgs:
                _view = view;
                requestCameraPermission();
                break;
            case R.id.bt_save:
                String content = et_content.getText().toString().trim();
                String caretory = et_category.getText().toString().trim();
                if(content.equals("")||caretory.equals("")||timeString.equals("")){
                    Toast.makeText(this,"請輸入所有資料",Toast.LENGTH_SHORT).show();
                }else{
                    boolean flag = dbOpenHelper.addReminder(seldate,content,caretory,mSavedImagePath,timeString);
                    int requestcode;
                    if(flag){
                        Toast.makeText(this,"成功!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,"出現錯誤",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(this, AlarmReceiver.class);
                    intent.putExtra("content",content);
                    dbOpenHelper=new DBSOpenHelper(this);
                    intent.putExtra("id",dbOpenHelper.findId(seldate,timeString));

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                    AlarmManager alarmMgr = (AlarmManager)this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, mstime, pendingIntent);
                    if(flag){
                        finish();
                    }
                }
                break;
        }
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }else{
            dissel();
        }
    }

    private void dissel(){
        InputMethodManager imm1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm1.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_window_photo, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);
        TextView tvSelectPhoto = contentView.findViewById(R.id.tv_select_photo);
        //当用户点击从图库选择照片时
        tvSelectPhoto.setOnClickListener(view1 -> {

            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
            popupWindow.dismiss();
        });
        TextView tvTakePhoto = contentView.findViewById(R.id.tv_take_photo);
        tvTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            imgDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "photo");
            if(!imgDir.exists()) {
                imgDir.mkdir();
            }

            long currentTimeMillis = System.currentTimeMillis();
            Date today = new Date(currentTimeMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String title = dateFormat.format(today);
            mSavedImagePath = imgDir + File.separator + title+".jpg";

            File cameraSavePath = new File(mSavedImagePath);


            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(this, "com.sys.fileprovider", cameraSavePath);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                imageUri = Uri.fromFile(cameraSavePath);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
            popupWindow.dismiss();
        });

        popupWindow.showAtLocation(_view, Gravity.CENTER, 0, 0);
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dissel();
            } else {
                Toast.makeText(AddReminderActivity.this, R.string.camera_tip, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //如果从图库界面返回
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //如果用户选择了相片
            if(data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                //查询我们需要的数据
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                mSavedImagePath = picturePath;
                postMessageToServer(picturePath);
            }
        }

        //拍後返回
        if(requestCode == CAMERA_REQUEST_CODE) {
            try {
                postMessageToServer(mSavedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void postMessageToServer(String imgpath) {
        // TODO 调用接口
        Glide.with(this).load(imgpath).into(iv_imgs);
    }

}
