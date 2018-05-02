package com.phibasis.www.phiotclient.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phibasis.www.phiotclient.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import Helper.ApiHelper;
import Helper.ProjectConfig;
import Helper.VolleyCallback;

import static android.content.ContentValues.TAG;
import static android.content.Context.WIFI_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetupDevice.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SetupDevice extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    LinearLayout llScans;

    SwipeRefreshLayout swiperefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_setup_device, container, false);

        llScans = (LinearLayout) fragView.findViewById(R.id.llScans);

        swiperefresh = fragView.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(this);

        swiperefresh.setRefreshing(true);
        BindView();

        // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        // Include dialog.xml file
        dialog.setContentView(R.layout.wifi_credentials);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        dialog.show();

        return fragView;
    }

    @Override
    public void onRefresh() {
        BindView();
    }

    public void stopSwipAnimation(){
        if(swiperefresh.isRefreshing())
        {
            swiperefresh.setRefreshing(false);
        }
    }

    public void BindView()
    {
        llScans.removeAllViews();
        ApiHelper.CallWithCustomBaseUrl(getContext(),ProjectConfig.PhiOT_Base_Url,"http://192.168.4.22/wifiscan","",new VolleyCallback(){

            @Override
            public void onSuccessResponse(String result) {
                try
                {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        final View list_scan = getLayoutInflater().inflate(R.layout.row, null, false);
                        TextView tvSsid = list_scan.findViewById(R.id.tvSsid);
                        tvSsid.setText(jsonObject.getString("ssid"));

                        TextView tvEncryptionType = list_scan.findViewById(R.id.tvEncryptionType);
                        tvEncryptionType.setText(jsonObject.getString("encryptionType"));

                        TextView tvRssi = list_scan.findViewById(R.id.tvRssi);
                        tvRssi.setText(jsonObject.getString("rssi"));

                        llScans.addView(list_scan);
                    }
                }
                catch (Exception e)
                {
                    ProjectConfig.StaticToast(getContext(),"Something went wrong while fetching device information.");
                    ProjectConfig.StaticLog(result);
                }
                finally {
                    stopSwipAnimation();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}


