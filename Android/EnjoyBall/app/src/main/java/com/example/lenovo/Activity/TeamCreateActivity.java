package com.example.lenovo.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.R;

public class TeamCreateActivity extends AppCompatActivity {

    private ImageView ivTeamCreateLogo;
    private ImageView ivTeamCreateMember;
    private ImageView ivTeamCreateAddress;

    private EditText etTeamCreateName;
    private EditText etTeamCreateSlogan;

    private Spinner spTeamCreateFixture;

    private Button btnTeamCreateSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_create);

        findView();

        setListeners();

    }

    private void setListeners() {

        TeamCreateListener listener = new TeamCreateListener();

        ivTeamCreateLogo.setOnClickListener(listener);

        spTeamCreateFixture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("test",spTeamCreateFixture.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class TeamCreateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_team_create_logo:
                    ActivityCompat.requestPermissions(TeamCreateActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                    break;
                case R.id.et_team_create_name:
                    break;
                case R.id.iv_team_create_member:
                    break;
                case R.id.iv_team_create_address:
                    break;
                case R.id.et_team_create_slogan:
                    break;
                case R.id.btn_team_create_save:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri,null,null,
                    null,null);
            if (cursor.moveToFirst()){
                String imgPath = cursor.getString(cursor.getColumnIndex("_data"));
                RequestOptions options = new RequestOptions()
                        .circleCrop();
                Glide.with(TeamCreateActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(ivTeamCreateLogo);
                //上传头像到服务器端
//                File file = new File(imgPath);
//                RequestBody body = RequestBody.create(MediaType.parse("image/*"),
//                        file);
//                Request request = new Request.Builder()
//                        .url(Constant.BASE_URL+"UploadServlet")
//                        .post(body)
//                        .build();
//                Call call = okHttpClient.newCall(request);
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("上传头像",response.body().string());
//                    }
//                });
            }
        }
    }

    private void findView() {

        ivTeamCreateLogo = findViewById(R.id.iv_team_create_logo);
        ivTeamCreateMember = findViewById(R.id.iv_team_create_member);
        ivTeamCreateAddress = findViewById(R.id.iv_team_create_address);

        etTeamCreateName = findViewById(R.id.et_team_create_name);
        etTeamCreateSlogan = findViewById(R.id.et_team_create_slogan);

        spTeamCreateFixture = findViewById(R.id.sp_team_create_fixture);

        btnTeamCreateSave = findViewById(R.id.btn_team_create_save);

    }

}
