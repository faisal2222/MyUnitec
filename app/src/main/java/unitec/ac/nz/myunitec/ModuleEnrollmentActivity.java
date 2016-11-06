package unitec.ac.nz.myunitec;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ModuleEnrollmentActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {

    // use this for emulator
    static final String SERVER_URL_1 = "https://10.0.2.2:8443/MyUnitecServer/webresources/moduleenrollment";
    // use this for network
    //private static final String SERVER_URL_1 = "https://192.168.1.67:8443/MyUnitecServer/webresources/moduleenrollment";

    // use this for emulator
    static final String SERVER_URL_2 = "https://10.0.2.2:8443/MyUnitecServer/webresources/enrolleric";
    // use this for network
    //private static final String SERVER_URL_2 = "https://192.168.1.67:8443/MyUnitecServer/webresources/enrolleric";

    private Button btnMenu;
    private ArrayList<Module> enrollmentModules;
    private ModuleEnrollmentAdapter moduleEnrollmentAdapter;
    private ListView lstEnrollmentModules;
    private String firstName;
    private String lastName;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_enrollment);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);
        enrollmentModules = new ArrayList<>();
        moduleEnrollmentAdapter = new ModuleEnrollmentAdapter(this, enrollmentModules);
        lstEnrollmentModules = (ListView) findViewById(R.id.lstEnrollmentModules);
        lstEnrollmentModules.setAdapter(moduleEnrollmentAdapter);
        lstEnrollmentModules.setOnItemClickListener(this);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        username = intent.getStringExtra("username");

        new ModuleEnrollmentActivity.ModuleEnrollmentTask().execute(SERVER_URL_1, username);
    }

    @Override
    public void onClick(View v) {
        if (v == btnMenu) {
            Intent intent = new Intent(ModuleEnrollmentActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View enrollmentDialogView = layoutInflater.inflate(R.layout.enrollment_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(enrollmentDialogView);
        alertDialogBuilder.setTitle(enrollmentModules.get(position).name);
        final EditText semester = (EditText) enrollmentDialogView
                .findViewById(R.id.txtSemester);
        final EditText year = (EditText) enrollmentDialogView
                .findViewById(R.id.txtYear);
        final int index = position;
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Enroll",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                new ModuleEnrollmentActivity.ModuleEnrollTask().execute(
                                        SERVER_URL_2, username,
                                        enrollmentModules.get(index).id,
                                        semester.getText().toString(),
                                        year.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class ModuleEnrollmentTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ModuleEnrollmentActivity.this);
        private JSONObject jsonResponseObject;
        private boolean isError;

        protected void onPreExecute() {
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        protected void onPostExecute(Void arg) {
            progressDialog.dismiss();
            if (!isError) {
                try {
                    if (jsonResponseObject.getString("result").compareTo("true") == 0) {
                        JSONArray enrollments = jsonResponseObject.getJSONArray("enrollmentModules");
                        for (int index = 0; index < enrollments.length(); index++) {
                            Module module = new Module(enrollments.optJSONObject(index).getString("id"),
                                    enrollments.optJSONObject(index).getString("name"));
                            enrollmentModules.add(module);
                        }
                        moduleEnrollmentAdapter.notifyDataSetChanged();
                    } else {
                        CharSequence message = "Problem Communicating With Server";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ModuleEnrollmentActivity.this, message, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CharSequence message = "Problem Communicating With Server";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ModuleEnrollmentActivity.this, message, duration);
                toast.show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL(strings[0]);

                TrustManager[] trustManagers = new TrustManager[]{
                        new X509TrustManager() {

                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                        }
                };

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, new java.security.SecureRandom());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }

                });
                httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                httpsURLConnection.setConnectTimeout(10000);
                httpsURLConnection.setReadTimeout(10000);

                JSONObject jsonRequestObject = new JSONObject();
                jsonRequestObject.put("username", strings[1]);

                PrintWriter printWriter = new PrintWriter(httpsURLConnection.getOutputStream());
                printWriter.println(jsonRequestObject);
                printWriter.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    result = result.concat(inputLine);
                }
                bufferedReader.close();
                jsonResponseObject = new JSONObject(result);

            } catch (IOException | KeyManagementException | NoSuchAlgorithmException | JSONException e) {
                isError = true;
            }
            return null;
        }
    }

    private class ModuleEnrollTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ModuleEnrollmentActivity.this);
        private JSONObject jsonResponseObject;
        private boolean isError;

        protected void onPreExecute() {
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        protected void onPostExecute(Void arg) {
            progressDialog.dismiss();
            if (!isError) {
                try {
                    if (jsonResponseObject.getString("result").compareTo("true") == 0) {
                        CharSequence message = "Enrollment Submitted";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ModuleEnrollmentActivity.this, message, duration);
                        toast.show();
                    } else {
                        CharSequence message = "Invalid Enrollment";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ModuleEnrollmentActivity.this, message, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CharSequence message = "Problem Communicating With Server";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ModuleEnrollmentActivity.this, message, duration);
                toast.show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL(strings[0]);

                TrustManager[] trustManagers = new TrustManager[]{
                        new X509TrustManager() {

                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                        }
                };

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, new java.security.SecureRandom());
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }

                });
                httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                httpsURLConnection.setConnectTimeout(10000);
                httpsURLConnection.setReadTimeout(10000);

                JSONObject jsonRequestObject = new JSONObject();
                jsonRequestObject.put("username", strings[1]);
                jsonRequestObject.put("id", strings[2]);
                jsonRequestObject.put("semester", strings[3]);
                jsonRequestObject.put("year", strings[4]);

                PrintWriter printWriter = new PrintWriter(httpsURLConnection.getOutputStream());
                printWriter.println(jsonRequestObject);
                printWriter.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    result = result.concat(inputLine);
                }
                bufferedReader.close();
                jsonResponseObject = new JSONObject(result);

            } catch (IOException | KeyManagementException | NoSuchAlgorithmException | JSONException e) {
                isError = true;
            }
            return null;
        }
    }
}
