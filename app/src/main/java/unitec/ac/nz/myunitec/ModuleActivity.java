package unitec.ac.nz.myunitec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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

public class ModuleActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {

    // use this for emulator
    static final String SERVER_URL = "https://10.0.2.2:8443/MyUnitecServer/webresources/activemodules";
    // use this for network
    //private static final String SERVER_URL = "https://192.168.1.67:8443/MyUnitecServer/webresources/activemodules";

    private String firstName;
    private String lastName;
    private String username;
    private String programmeID;
    private String programmeName;
    private ListView lstModule;
    private ArrayList<Module> modules;
    private ModuleAdpater moduleAdapter;
    private Button btnProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        modules = new ArrayList<>();
        moduleAdapter = new ModuleAdpater(this, modules);
        lstModule = (ListView) findViewById(R.id.lstModule);
        lstModule.setAdapter(moduleAdapter);
        lstModule.setOnItemClickListener(this);

        btnProgramme = (Button) findViewById(R.id.btnProgramme);
        btnProgramme.setOnClickListener(this);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        username = intent.getStringExtra("username");
        programmeID = intent.getStringExtra("programmeID");
        programmeName = intent.getStringExtra("programmeName");

        setTitle(programmeName);

        new ModuleTask().execute(SERVER_URL, username, programmeID);
    }

    @Override
    public void onClick(View v) {
        if (v == btnProgramme) {
            Intent intent = new Intent(ModuleActivity.this, ProgrammeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ModuleActivity.this, GradeActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("programmeID", programmeID);
        intent.putExtra("programmeName", programmeName);
        intent.putExtra("moduleID", modules.get(position).id);
        intent.putExtra("moduleName", modules.get(position).name);
        startActivity(intent);
    }

    private class ModuleTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ModuleActivity.this);
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
                        JSONArray enrollments = jsonResponseObject.getJSONArray("modules");
                        if (enrollments.length() > 0) {
                            for (int index = 0; index < enrollments.length(); index++) {
                                Module module = new Module(enrollments.optJSONObject(index).getString("id"),
                                        enrollments.optJSONObject(index).getString("moduleName"),
                                        enrollments.optJSONObject(index).getString("semester"),
                                        enrollments.optJSONObject(index).getString("year"),
                                        enrollments.optJSONObject(index).getString("status"));
                                modules.add(module);
                            }
                            moduleAdapter.notifyDataSetChanged();
                        } else {
                            CharSequence message = "No Enrollments";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(ModuleActivity.this, message, duration);
                            toast.show();
                            Intent intent = new Intent(ModuleActivity.this, ProgrammeActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("firstName", firstName);
                            intent.putExtra("lastName", lastName);
                            startActivity(intent);
                        }
                    } else {
                        CharSequence message = "Problem Communicating With Server";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ModuleActivity.this, message, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CharSequence message = "Problem Communicating With Server";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ModuleActivity.this, message, duration);
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
                jsonRequestObject.put("programmeID", strings[2]);

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
