package com.gms.admin.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gms.admin.R;
import com.gms.admin.helper.AlertDialogHelper;
import com.gms.admin.helper.ProgressDialogHelper;
import com.gms.admin.interfaces.DialogClickListener;
import com.gms.admin.servicehelpers.ServiceHelper;
import com.gms.admin.serviceinterfaces.IServiceListener;
import com.gms.admin.utils.CommonUtils;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.PreferenceStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocsConstituentFragment extends Fragment implements IServiceListener, DialogClickListener {

    private static final String TAG = DocsConstituentFragment.class.getName();
    private View view;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    public static LinearLayout loadMoreListView;
    private TextView constituentCount;
    String url, filename;
    static int cc = 0;

    public static DocsConstituentFragment newInstance(int position) {
        DocsConstituentFragment frag = new DocsConstituentFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doc_list, container, false);
        serviceHelper = new ServiceHelper(view.getContext());
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(view.getContext());

        loadMoreListView = view.findViewById(R.id.list_view_user);

        getUsersList();

        return view;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateResponse(response)) {
            try {
                cc = response.getJSONArray("constituent_documents").length();
                loadMembersList(response.getJSONArray("constituent_documents"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void getUsersList() {
        if (CommonUtils.isNetworkAvailable(getActivity())) {
            JSONObject jsonObject = new JSONObject();
            try {
//                if (val == 0) {
//                    paguthiID = "ALL";
//                }
                jsonObject.put(GMSConstants.KEY_CONSTITUENT_ID, PreferenceStorage.getConstituentID(getActivity()));
                jsonObject.put(GMSConstants.DYNAMIC_DATABASE, PreferenceStorage.getDynamicDb(getActivity()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.GET_CONSTITUENT_DOC;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.error_no_net));
        }
    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(GMSConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                        view.findViewById(R.id.scrollList).setVisibility(View.GONE);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onError(String error) {

    }

    private void loadMembersList(JSONArray memberCount) {

        try {

            for (int c1 = 0; c1 < memberCount.length(); c1++) {
                LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mainLayoutParams.setMargins(0, 0, 0, 0);

                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cellParams.setMargins(20, 10, 20, 10);

                LinearLayout.LayoutParams msgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                msgParams.setMargins(20, 20, 20, 10);

                LinearLayout.LayoutParams sentDateParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sentDateParams.setMargins(0, 0, 20, 10);

                LinearLayout.LayoutParams sentByParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sentByParams.setMargins(20, 10, 20, 0);

                TextView titleDoc = new TextView(getActivity());
                titleDoc.setId(R.id.doc_name);
                titleDoc.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                titleDoc.setLayoutParams(msgParams);
                titleDoc.setText(memberCount.getJSONObject(c1).getString("doc_name"));
                titleDoc.setTextSize(14.0f);
                titleDoc.setTypeface(null, Typeface.BOLD);

                TextView sentAt = new TextView(getActivity());
                sentAt.setId(R.id.doc_date);
                sentAt.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
                sentAt.setLayoutParams(sentByParams);
                sentAt.setTextSize(12.0f);
                sentAt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar, 0, 0, 0);
                sentAt.setCompoundDrawablePadding(20);

                String date = memberCount.getJSONObject(c1).getString("created_at");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date testDate = null;
                try {
                    testDate = formatter.parse(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMM yyyy");

                sentAt.setText(sdf.format(testDate));

                TextView download = new TextView(getActivity());
                download.setTextColor(ContextCompat.getColor(getActivity(), R.color.msg_by_grey));
                download.setLayoutParams(sentDateParams);
                download.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_download, 0, 0, 0);
                download.setCompoundDrawablePadding(20);
                download.setPadding(20, 20, 20, 20);
                download.setText(getString(R.string.doc_download));
                download.setTextSize(14.0f);
                download.setGravity(Gravity.END);
                url = PreferenceStorage.getClientUrl(getActivity()) + GMSConstants.KEY_DOC_URL + memberCount.getJSONObject(c1).getString("doc_file_name");
                filename = memberCount.getJSONObject(c1).getString("doc_name");
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    111);
                        } else {
                            showDownloadAlert();
                        }
                    }
                });

                LinearLayout cell = new LinearLayout(getActivity());
                cell.setOrientation(LinearLayout.VERTICAL);
                cell.setLayoutParams(cellParams);
                cell.setElevation(10.0f);
                cell.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shadow_round));
                cell.setGravity(Gravity.END);
                cell.addView(titleDoc);
                cell.addView(sentAt);
                cell.addView(download);

                LinearLayout mainLayout = new LinearLayout(getActivity());
                mainLayout.setOrientation(LinearLayout.VERTICAL);
                mainLayout.setLayoutParams(mainLayoutParams);
                mainLayout.addView(cell);

                loadMoreListView.addView(mainLayout);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void seaarchtext(String text) {
        for (int a = 0; a < cc; a++) {
            if (text.isEmpty()) {
                loadMoreListView.getChildAt(a).setVisibility(View.VISIBLE);
            } else {
                TextView date = loadMoreListView.getChildAt(a).findViewById(R.id.doc_date);
                TextView titi = loadMoreListView.getChildAt(a).findViewById(R.id.doc_name);
                if (!(date.getText().toString().toLowerCase().contains(text) || titi.getText().toString().toLowerCase().contains(text))) {
                    loadMoreListView.getChildAt(a).setVisibility(View.GONE);
                }
            }
        }
    }

    private void showDownloadAlert() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Download");
        alertDialogBuilder.setMessage("Do you want to download " + filename + "?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new DownloadFile().execute(url);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getActivity());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Download/GMSDocuments/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getActivity(),
                    message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 111) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {

                    Log.d("permission", "permission denied to SEND_SMS - requesting it");
                    String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE};

                    ActivityCompat.requestPermissions(getActivity(), perm, 111);

                } else {
                    new DownloadFile().execute(url);
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }


}