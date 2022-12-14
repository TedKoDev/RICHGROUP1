package com.example.RG.Groupintro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.RG.CustomDialog;
import com.example.RG.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GroupNameImgSetActivity extends AppCompatActivity implements View.OnClickListener {

    TextView logname, grname,  grdisc, gropendate,grnum,onoroff,cattt;
    ImageView grpback, grpimg;
    Uri selecteduri;
    String imgString;
    Bitmap bitmap;
    String grsetnum,encodeImagae,grouponoff,groupname,groupdisc,groupdate,groucate;
    String loginname,category2,grou1;
    String urlload = "http://3.38.117.213/gr_updetgetinfo.php";
    String urlupdate = "http://3.38.117.213/RG3groupupdate.php";
    String url3;
    EditText category;
    Button grpohoto, grpostaddbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name_img_set);
        getSupportActionBar().hide();


        grpback = findViewById(R.id.ivback);
        grpohoto = findViewById(R.id.introupdateimgadd);
        grpimg = findViewById(R.id.introupdateimg);

//              Textview


        grname = findViewById(R.id.introupdatename);
        groupname = grname.getText().toString();

        grdisc = findViewById(R.id.introupdatedisc);
        groupdisc = grdisc.getText().toString();

        gropendate = findViewById(R.id.introupdateopendate);

        onoroff = findViewById(R.id.introupdateonoff);
        grouponoff = onoroff.getText().toString();


        category = findViewById(R.id.introcate);
        cattt = findViewById(R.id.cattt);
        groucate = category.getText().toString();

        grpostaddbtn = findViewById(R.id.introupdatesave);


        grnum = findViewById(R.id.grnum);
        logname = findViewById(R.id.logname);

        SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
         loginname = sp.getString("userName", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        logname.setText(loginname); // name ?????? xml???????????? ???????????? ????????????id???


// =================================== GroupActivity ?????? Bottomsheet Fragment ???????????? ?????? ?????????  ???????????? onpause ??? ?????? ???
        sp = getSharedPreferences("gridset", MODE_PRIVATE);
        grsetnum = sp.getString("????????????", " "); //key?????? ????????? ?????? ???????????????, ???????????????""(??????)??? ??????
        grnum.setText(grsetnum); // name ?????? xml???????????? ???????????? ????????????id???
        //======================================================



        loaddata();

        onoroff.setOnClickListener(this); // ?????? ????????? customdialog ?????? ?????????


        spinner();


        grpohoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickimgpermission();
            }
        });

        grpback.setOnClickListener(new View.OnClickListener() { // ????????????
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
                startActivity(intent);


            }
        });


        grpostaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                update();
                Intent intent = new Intent(getApplicationContext(), GroupSettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//???????????? ????????????
                startActivity(intent);


            }
        });






    }

    private void update() {



        groupname = grname.getText().toString();

        Log.e(  "iwjfoiejf", groupname);


        groupname = grname.getText().toString();

        groupdisc = grdisc.getText().toString();

        grouponoff = onoroff.getText().toString();

        groucate = category.getText().toString();

        BitmapDrawable drawable1 = (BitmapDrawable) grpimg.getDrawable(); // imageview??? Bitmap?????? bitmap??? String??????
        Bitmap bitmap1 = drawable1.getBitmap();
        grpimg.setImageBitmap(bitmap1);

        //Bitmap??? String????????? ??????
        ByteArrayOutputStream baos1= new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 70, baos1);
        byte[] bytes1 = baos1.toByteArray();
      encodeImagae = Base64.encodeToString(bytes1, Base64.DEFAULT);



        StringRequest request = new StringRequest(Request.Method.POST, urlupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
Log.e(  "ggggg", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> param = new HashMap<String, String>();
                //????????? ????????? ?????????, ??????, ??????, ?????????(shared??? ?????????)??? ?????????


                param.put("grimage", encodeImagae);  //encodeImagae???  String ????????? ????????????  ???????????? ?????? ??????
                param.put("grid", grsetnum);  //encodeImagae???  String ????????? ????????????  ???????????? ?????? ??????
                param.put("grname", groupname);          //gr??????
                param.put("grdisc", groupdisc);           // ?????? ??????
                param.put("gruserName", loginname); // ????????? ??????
                param.put("category", groucate);    // ????????????
                param.put("onoff", grouponoff);  //?????? ?????????


                return param;
            }
        };
        // requestQueue??? ????????? ?????? ??????  php?????? ?????? DB table??? ????????????.
        RequestQueue requestQueue = Volley.newRequestQueue(GroupNameImgSetActivity.this);
        requestQueue.add(request);

    }

    private void spinner() {
        Spinner spinnerMenu = (Spinner)findViewById(R.id.spinner);

        final String[] ???????????? = getResources().getStringArray(R.array.my_array);




        ArrayAdapter menuAdapter = ArrayAdapter.createFromResource(this, R.array.my_array, android.R.layout.simple_spinner_item);

        // Spinner ????????? DropDown ????????? ??????
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // ???????????? ???????????? ??????
        spinnerMenu.setAdapter(menuAdapter);


        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category.setText(????????????[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void pickimgpermission() { // 1  ????????? ?????? ????????????.
        // ???????????? ????????? ??????????????? manifest ?????? ???????????? ?????? permission??? ?????? ???????????? ????????? ????????????


        //Dexter?????? ?????????????????? ???????????????
        Dexter.withContext(GroupNameImgSetActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE) // ???????????? ????????????
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) { // ????????? ???????????? ??????
                        chooseImage(); // ????????? ??????????????? ???????????? ????????? ???????????? class (????????????)

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) { // ????????? ??????????????????
                        Toast.makeText(getApplicationContext(), "permission required", Toast.LENGTH_SHORT).show();
                    }

                    @Override // ????????? ?????????????????????????????? (?????? ??????)
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


    }


    private void chooseImage() { // ??????????????? ???????????? ???????????? ?????????

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

//
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 1 || requestCode == RESULT_OK || data.getData() != null) {


                selecteduri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selecteduri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    grpimg.setImageBitmap(bitmap);
                    imageStore(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imagebyte = stream.toByteArray();

        encodeImagae = android.util.Base64.encodeToString(imagebyte, Base64.DEFAULT);


    }


    private void loaddata() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success"); //????????? ?????? ??? ????????? ???????????? ?????????
                    if (success) {

                        String gid = jsonObject.getString("grid");
                        String gname = jsonObject.getString("grname");
                         url3 = jsonObject.getString("grimage");
                        String gdisc = jsonObject.getString("grdisc");
                        String guname = jsonObject.getString("gruserName");
                        String onoff = jsonObject.getString("onoff");
                        String cate = jsonObject.getString("category");
                        String creat = jsonObject.getString("created_at");

//                        String urlimage = "http://3.38.117.213/image/"+url2; Log.e(  "?????????", "5");

                        Glide.with(getApplicationContext()).load("http://3.38.117.213/image/" + url3).into(grpimg);
                        grnum.setText(gid);
                        grname.setText(gname);
                        grdisc.setText(gdisc);
                        onoroff.setText(onoff);

                        category.setText(cate);
                        gropendate.setText(creat);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();


//                    Toast.makeText(getApplicationContext(), "?????? : " + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


//                Toast.makeText(getApplicationContext(), "?????? : " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grid", grsetnum);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.introupdateonoff:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String name) {
                        onoroff.setText("??????");
                        grouponoff= onoroff.getText().toString();// ?????? or ????????? ???????????? String onoff??? ??????
//                        onoroff.setText("??????");

                    }

                    @Override
                    public void onNegativeClicked() {
                        onoroff.setText("?????????");
                        grouponoff= onoroff.getText().toString();// ?????? or ????????? ???????????? String onoff??? ??????
//                        onoroff.setText("?????????");
                    }
                });
                dialog.show();
                break;
        }
    }


}