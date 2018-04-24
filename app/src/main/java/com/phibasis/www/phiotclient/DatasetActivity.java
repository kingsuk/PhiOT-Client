package com.phibasis.www.phiotclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import Helper.ApiHelper;
import Helper.ProjectConfig;
import Helper.VolleyCallback;

public class DatasetActivity extends AppCompatActivity {

    LinearLayout llDatasets;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataset);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        token = getIntent().getStringExtra("token");
        llDatasets = (LinearLayout) findViewById(R.id.llDatasets);

        String dataDeviceId = "deviceId="+getIntent().getStringExtra("ds_deviceId");
        ApiHelper.Call(getApplicationContext(), "device/GetDeviceInfoByDeviceId?", dataDeviceId, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //ProjectConfig.StaticToast(getApplicationContext(),result);
                try
                {
                    JSONObject jsonObject = new JSONObject(result);

                    TextView tvDeviceName = findViewById(R.id.tvDeviceName);
                    tvDeviceName.setText(jsonObject.getString("deviceName"));

                    TextView tvDeviceToken = findViewById(R.id.tvDeviceToken);
                    tvDeviceToken.setText(jsonObject.getString("device_token"));
                }
                catch (Exception e)
                {
                    ProjectConfig.StaticToast(getApplicationContext(),"Something went wrong while fetching device information.");
                    ProjectConfig.StaticLog(result);
                }
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String data = "ds_deviceId="+getIntent().getStringExtra("ds_deviceId");

        ApiHelper.Call(getApplicationContext(), "Dataset/GetAllDatasetByUserIdAndDeviceId?", data, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                //ProjectConfig.StaticToast(getApplicationContext(),result);
                try
                {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final View list_dataset = getLayoutInflater().inflate(R.layout.list_dataset, null, false);

                        TextView tvDatasetName = list_dataset.findViewById(R.id.tvDatasetName);
                        tvDatasetName.setText(jsonObject.getString("ds_name"));

                        Button btnOnButton = list_dataset.findViewById(R.id.btnOnButton);
                        btnOnButton.setTag(jsonObject.getString("jsonData"));
                        btnOnButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String data = "token="+token+"&message="+view.findViewById(R.id.btnOnButton).getTag().toString();
                                ApiHelper.Call(getApplicationContext(), "publish/sendToDevice?", data, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {
                                        //ProjectConfig.StaticToast(getApplicationContext(),result);
                                        try
                                        {

                                            JSONObject jsonObject1 = new JSONObject(result);
                                            ProjectConfig.StaticToast(getApplicationContext(),jsonObject1.getString("statusMessage"));
                                        }
                                        catch (Exception e)
                                        {
                                            ProjectConfig.StaticToast(getApplicationContext(),"Something went wrong while sending request ot device.");
                                            ProjectConfig.StaticLog(result);
                                        }
                                    }
                                });
                            }
                        });

                        Button btnOffButton = list_dataset.findViewById(R.id.btnOffButton);
                        btnOffButton.setTag(jsonObject.getString("reverseJsonData"));
                        btnOffButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String data = "token="+token+"&message="+view.findViewById(R.id.btnOffButton).getTag().toString();
                                ApiHelper.Call(getApplicationContext(), "publish/sendToDevice?", data, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {
                                        //ProjectConfig.StaticToast(getApplicationContext(),result);
                                        try
                                        {

                                            JSONObject jsonObject1 = new JSONObject(result);
                                            ProjectConfig.StaticToast(getApplicationContext(),jsonObject1.getString("statusMessage"));
                                        }
                                        catch (Exception e)
                                        {
                                            ProjectConfig.StaticToast(getApplicationContext(),"Something went wrong while sending request ot device.");
                                            ProjectConfig.StaticLog(result);
                                        }
                                    }
                                });
                            }
                        });

                        llDatasets.addView(list_dataset);
                    }
                }
                catch (Exception e)
                {
                    ProjectConfig.StaticToast(getApplicationContext(),"Something went wrong while fetching dataset information.");
                    ProjectConfig.StaticLog(result);
                }
            }
        });
    }

}
