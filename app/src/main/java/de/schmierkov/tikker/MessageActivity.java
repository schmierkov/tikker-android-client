package de.schmierkov.tikker;

import de.schmierkov.tikker.model.Message;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new MessageAdapter());

        new GetMessagesTask().execute(MainActivity.token, "http://192.168.1.36:3000/api/v1/messages");
    }

    private void makeList(JSONArray messages) {
        DatabaseHelper db = new DatabaseHelper(this);

        try {
            for(int i=0;i<messages.length();i++) {
                JSONObject json_data = messages.getJSONObject(i);
                Message message = new Message();

                message.setId(Integer.parseInt(json_data.getString("id")));
                message.setText(json_data.getString("text"));

                db.addMessage(message);
                db.getAllMessages();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printMessages(JSONArray messages) {
        makeList(messages);
        System.out.printf(messages.toString());
    }

    class MessageAdapter extends ArrayAdapter<Message> {
        MessageAdapter() {
            super(MessageActivity.this, R.layout.row, messageItems);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView==null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, null);

                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.populateFrom(messageItems.get(position));

            return convertView;
        }
    }

    class ViewHolder {
        public TextView id;
        public TextView text;
        public TextView date;
        public TextView user;

        ViewHolder(View row) {
            text=(TextView)row.findViewById(R.id.text);
            // date=(TextView)row.findViewById(R.id.date);
            // user=(TextView)row.findViewById(R.id.user);
        }

        void populateFrom(Message m) {
            text.setText(m.text);
            // date.setText(m.date);
            // user.setText(m.user);
        }
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
        String message_text = message.getText().toString();

        if (!message_text.equals("")) {
            new SendMessageTask().execute(MainActivity.token, message_text);
            message.getText().clear();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }

        return true;
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
