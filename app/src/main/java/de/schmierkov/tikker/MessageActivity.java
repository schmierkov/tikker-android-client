package de.schmierkov.tikker;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        new GetMessagesTask().execute("http://192.168.1.36:3000/api/v1/messages", MainActivity.token);
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

    public void reloadOnClick(View v) {
        new GetMessagesTask().execute("http://192.168.1.36:3000/api/v1/messages", MainActivity.token);
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
            return HttpClient.get(arg0[0], arg0[1]);
        }

        protected void onPostExecute(JSONArray result) {
            TextView text = (TextView)findViewById(R.id.textView);
            text.setText(result.toString());

            printMessages(result);
        }
    }

    public void onBackPressed() {}
}
