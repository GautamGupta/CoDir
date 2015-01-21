package am.gaut.codir;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "CoDirMain";

    private ListView mContactList;
    private String[] seeds;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContactList = (ListView) findViewById(R.id.contact_list);

        client = new AsyncHttpClient();
        client.get("http://api.randomuser.me/?results=100", new JsonHttpResponseHandler() {
            ContactList adapter;

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                JSONArray results = null;
                try {
                    results = response.getJSONArray("results");

                    Log.i(TAG, "Fetching contacts");

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

                            // Load Single Contact Activity
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse JSON. " + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "Failed to get contacts.");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e(TAG, "Retrying #" + retryNo);
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
