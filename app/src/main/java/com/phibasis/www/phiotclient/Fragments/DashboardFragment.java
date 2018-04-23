package com.phibasis.www.phiotclient.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Api;
import com.phibasis.www.phiotclient.LoginActivity;
import com.phibasis.www.phiotclient.MainActivity;
import com.phibasis.www.phiotclient.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Helper.ApiHelper;
import Helper.ProjectConfig;
import Helper.VolleyCallback;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    LinearLayout llDevices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        llDevices = (LinearLayout) fragView.findViewById(R.id.llDevices);

        ApiHelper.Call(getContext(),"device/GetAllDevicesByUser","", new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                try
                {

                    JSONArray responseArray = new JSONArray(result);
                    for(int i=0;i<responseArray.length();i++)
                    {
                        JSONObject jsonObject= responseArray.getJSONObject(i);

                        final View list_device = getLayoutInflater().inflate(R.layout.list_device, null, false);

                        TextView tvDeviceName = list_device.findViewById(R.id.tvDeviceName);
                        tvDeviceName.setText(jsonObject.getString("deviceName"));

                        ImageView ivDelete = list_device.findViewById(R.id.ivDelete);
                        ivDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProjectConfig.StaticToast(getContext(),"Delete Click");
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Delete device.")
                                        .setMessage("Are you sure you want to delete it?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Toast.makeText(getContext(), "Yaay", Toast.LENGTH_SHORT).show();
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        });

                        ImageView ivDeviceTypeImage = list_device.findViewById(R.id.ivDeviceTypeImage);
                        if(jsonObject.getInt("device_type_id")==1)
                        {
                            ivDeviceTypeImage.setImageResource(R.drawable.devicetype1);
                        }
                        else if(jsonObject.getInt("device_type_id")==2)
                        {
                            ivDeviceTypeImage.setImageResource(R.drawable.devicetype2);
                        }

                        list_device.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OnFragmentInteractionListener listener = (OnFragmentInteractionListener) getActivity();
                                listener.onFragmentInteraction("Hello");
                            }
                        });

                        llDevices.addView(list_device);
                    }

                }
                catch (Exception e)
                {
                    ProjectConfig.StaticToast(getContext(),"Something went wrong while fetching device information.");
                    ProjectConfig.StaticLog(result);
                }
            }
        });

        //apiCall("asdf","asdf");
        return fragView;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragment);
    }



}
