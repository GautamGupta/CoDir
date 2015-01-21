package am.gaut.codir;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SingleContactActivity extends ActionBarActivity {

    public static final String TAG = "CoDirSingleContact";

    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);

        Intent intent = getIntent();
        String seed = intent.getStringExtra(MainActivity.EXTRA_CONTACT_SEED);

        // Need to reload as we're not storing the JSON
        // Should be fine if we store the data and/or use Google GSON
        Log.i(TAG, "Fetching contact " + seed);
        client = new AsyncHttpClient();
        client.get("http://api.randomuser.me/0.4.1/?seed=" + seed, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONArray results = null;
                try {
                    JSONObject contact = response.getJSONArray("results").getJSONObject(0).getJSONObject("user");

                    String fname = contact.getJSONObject("name").getString("first");
                    String lname = contact.getJSONObject("name").getString("last");
                    String cell  = contact.getString("cell");
                    String thumb = contact.getJSONObject("picture").getString("large");

                    setTitle(ContactList.getFullName(fname, lname));


                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON. " + e.toString());
                    failedLoadingContact();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "Failed to load user. " + throwable.toString());
                failedLoadingContact();
            }

        });
    }

    private void failedLoadingContact() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SingleContactActivity.this, "Failed to load contact.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}