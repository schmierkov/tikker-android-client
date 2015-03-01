package de.schmierkov.tikker;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    static String token = "";
    static JSONObject messages;

    public String getToken() {
        return token;
    }

    public static void setToken(String value) {
        token = value;
    }

    public static void setMessages(JSONObject value){
        messages = value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonOnClick(View v) {
        EditText username, password;
        username = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        new LoginTask(this).execute(username.getText().toString(), password.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String login(String username, String password) {
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("email", username));
        postParameters.add(new BasicNameValuePair("password", password));

        return HttpClient.getLoginToken("http://192.168.1.36:3000/api/sign_in", postParameters);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private Context context;

        public LoginTask(Context context) {
            this.context = context.getApplicationContext();
        }

        protected String doInBackground(String... arg0) {
            return login(arg0[0], arg0[1]);
        }

        protected void onPostExecute(String result) {
            setToken(result);

            Intent intent = new Intent(context, MessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
