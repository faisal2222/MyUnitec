package unitec.ac.nz.myunitec;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
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

public class ProgrammeActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {

    // use this for emulator
    static final String SERVER_URL = "https://10.0.2.2:8443/MyUnitecServer/webresources/activeenrollment";
    // use this for network
    //private static final String SERVER_URL_1 = "https://192.168.1.67:8443/MyUnitecServer/webresources/activeenrollment";

    private Button btnMenu;
    private ListView lstProgramme;
    private ArrayList<Programme> programmes;
    private ProgrammesAdapter programmeAdapter;
    private String firstName;
    private String lastName;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programme);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);
        lstProgramme = (ListView) findViewById(R.id.lstProgramme);
        programmes = new ArrayList<>();
        programmeAdapter = new ProgrammesAdapter(this, programmes);
        lstProgramme.setAdapter(programmeAdapter);
        lstProgramme.setOnItemClickListener(this);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        username = intent.getStringExtra("username");

        new ProgrammeTask().execute(SERVER_URL, username);
    }

    @Override
    public void onClick(View v) {
        if (v == btnMenu) {
            Intent intent = new Intent(ProgrammeActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ProgrammeActivity.this, ModuleActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("programmeID", programmes.get(position).id);
        intent.putExtra("programmeName", programmes.get(position).name);
        startActivity(intent);
    }

    private class ProgrammeTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ProgrammeActivity.this);
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
                        JSONArray enrollments = jsonResponseObject.getJSONArray("enrollments");
                        for (int index = 0; index < enrollments.length(); index++) {
                            Programme programme = new Programme(enrollments.optJSONObject(index).getString("id"),
                                    enrollments.optJSONObject(index).getString("programmeName"),
                                    enrollments.optJSONObject(index).getString("startDate"));
                            programmes.add(programme);
                        }
                        programmeAdapter.notifyDataSetChanged();
                    } else {
                        CharSequence message = "Problem Communicating With Server";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ProgrammeActivity.this, message, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CharSequence message = "Problem Communicating With Server";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ProgrammeActivity.this, message, duration);
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
}
