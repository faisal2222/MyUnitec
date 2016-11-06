package unitec.ac.nz.myunitec;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class LoginActivity extends AppCompatActivity implements OnClickListener {

    // use this for emulator
    private static final String SERVER_URL = "https://10.0.2.2:8443/MyUnitecServer/webresources/login";
    // use this for network
    //private static final String SERVER_URL_1 = "https://192.168.1.67:8443/MyUnitecServer/webresources/login";

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new LoginTask().execute(SERVER_URL, txtUsername.getText().toString(), txtPassword.getText().toString());
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
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
                        String firstName = jsonResponseObject.getString("firstName");
                        String lastName = jsonResponseObject.getString("lastName");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", txtUsername.getText().toString());
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        startActivity(intent);
                    } else {
                        CharSequence message = "Invalid Username or Password";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(LoginActivity.this, message, duration);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                CharSequence message = "Problem Communicating With Server";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(LoginActivity.this, message, duration);
                toast.show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL(strings[0]);

                TrustManager[] trustManagers = new TrustManager[] {
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
                jsonRequestObject.put("password", strings[2]);

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

            } catch (IOException | KeyManagementException | NoSuchAlgorithmException |JSONException e) {
                isError = true;
            }
            return null;
        }
    }
}
