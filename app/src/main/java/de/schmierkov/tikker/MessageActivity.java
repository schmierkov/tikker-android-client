package de.schmierkov.tikker;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        new GetMessagesTask().execute(MainActivity.token, "http://192.168.1.36:3000/api/v1/messages");
    }

    private void printMessages(JSONArray messages) {
        System.out.printf(messages.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    public void sendOnClick(View v) {
        EditText message;
        message = (EditText)findViewById(R.id.message);

        new SendMessageTask().execute(MainActivity.token, message.getText().toString());
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

        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetMessagesTask extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... arg0) {
            return HttpClient.getMessages(arg0[0], arg0[1]);
        }

        protected void onPostExecute(JSONArray result) {
            printMessages(result);
        }
    }

    private class SendMessageTask extends AsyncTask<String, Void, JSONArray> {
        protected JSONArray doInBackground(String... arg0) {
            return sendMessage(arg0[0], arg0[1]);
        }

        protected void onPostExecute(JSONArray result) {
            printMessages(result);
        }
    }

    private JSONArray sendMessage(String token, String message) {
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("message", message));

        return HttpClient.sendMessage(token, "http://192.168.1.36:3000/api/v1/messages", postParameters);
    }

    public void onBackPressed() {}
}
