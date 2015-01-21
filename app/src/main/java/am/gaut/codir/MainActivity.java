package am.gaut.codir;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "CoDirMain";
    public static final String EXTRA_CONTACT_SEED = "am.gaut.codir.CONTACT_SEED";
    public static final String API_END_POINT = "http://api.randomuser.me/0.4.1/";
    public static final Integer NUM_RESULTS = 5;

    private ListView mContactList;
    private String[] seeds;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactList = (ListView) findViewById(R.id.contact_list);

        Log.i(TAG, "Fetching contacts");
        client = new AsyncHttpClient();
        client.get(API_END_POINT + "?results=" + NUM_RESULTS, new JsonHttpResponseHandler() {
            ContactList adapter;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    final JSONArray results = response.getJSONArray("results");

                    seeds           = new String[results.length()]; // Basically IDs
                    String[] fnames = new String[results.length()];
                    String[] lnames = new String[results.length()];
                    String[] cells  = new String[results.length()];
                    String[] thumbs = new String[results.length()];

                    for(int i = 0; i < results.length(); i++) {
                        seeds[i]  = results.getJSONObject(i).getString("seed");
                        fnames[i] = results.getJSONObject(i).getJSONObject("user").getJSONObject("name").getString("first");
                        lnames[i] = results.getJSONObject(i).getJSONObject("user").getJSONObject("name").getString("last");
                        cells[i]  = results.getJSONObject(i).getJSONObject("user").getString("cell");
                        thumbs[i] = results.getJSONObject(i).getJSONObject("user").getJSONObject("picture").getString("thumbnail");
                    }

                    adapter = new ContactList(MainActivity.this, fnames, lnames, cells, thumbs);
                    mContactList.setAdapter(adapter);
                    mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                            final String seed = seeds[p];
                            Log.i(TAG, "Selected contact " + seed + " at " + p);

                            // Load Single Contact Activity
                            Intent intent = new Intent(MainActivity.this, SingleContactActivity.class);
                            intent.putExtra(EXTRA_CONTACT_SEED, seed);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON. " + e.toString());
                    failedLoadingContacts();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "Failed to load contacts. " + throwable.toString());
                failedLoadingContacts();
            }

        });
    }

    private void failedLoadingContacts() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Failed to load contact.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /* if (id == R.id.action_settings) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }
}
