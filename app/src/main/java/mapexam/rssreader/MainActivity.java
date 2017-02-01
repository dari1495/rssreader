package mapexam.rssreader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    static String RSS_feed = "";
    static String old_feed = "";
    List<itemRSS> arrayRSS;
    SwipeRefreshLayout mSwipeLayout;
    final int STATIC_INTEGER_VALUE = 123;
    EditText t;
    FrameLayout f;
    TextView tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recovering RSS_feed form last session
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        RSS_feed = preferences.getString("RSS_feed","");

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        f = (FrameLayout)findViewById(R.id.frameLayout);
        tt = (TextView)findViewById(R.id.textView5) ;

        // Setting up the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // TODO: The goddamn button won't align to the right
        TextView tvSave = (TextView)findViewById(R.id.button_rss);
        // Action for button
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                changeRSS(v);
            }
        });

        // TEST
        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        detailedActivity(position);
                    }
                });
        // TEST
        Intent intent = getIntent();
        if(RSS_feed.isEmpty()) { //No saved from last session
            try {
                old_feed = RSS_feed;
                RSS_feed = intent.getExtras().getString("newRSS");
                new GetFeedTask().execute((Void) null);
                mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    public void onRefresh() {
                        new GetFeedTask().execute((Void) null);
                    }
                });

            } catch (Exception e) {//This means empty rss feed
                tt.setText("Select RSS feed");
                f.setVisibility(View.VISIBLE);
                //No RSS_feed = app is kill
            }
        }
        else{ //there's a RSS_feed saved from last session
            new GetFeedTask().execute((Void) null);
        }
    }

    private void detailedActivity(int position) {
        Intent intent = new Intent(this, detailActivity.class);
        itemRSS i = arrayRSS.get(position);
        intent.putExtra("title", i.getTitle());
        intent.putExtra("body", i.getDescription());
        intent.putExtra("link", i.getLink());
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //Save the RSS_feed for next startup
        editor.putString("RSS_feed", RSS_feed);
        editor.commit();
        super.onPause();
    }


    /**
     * Launching the activity to change the RSS feed link
     * @param v
     */
    private void changeRSS(View v){
        Intent intent = new Intent(this, ActivityChange.class);
        intent.putExtra("RSS_feed", RSS_feed);
        startActivityForResult(intent,STATIC_INTEGER_VALUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == STATIC_INTEGER_VALUE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("newRSS");
                old_feed = RSS_feed;
                RSS_feed = result;
                t = (EditText)findViewById(R.id.editText);
                t.setText(RSS_feed);
                // TODO: detailed view and icon
                if(result.isEmpty()){
                    tt.setText("Select RSS feed");
                    f.setVisibility(View.VISIBLE);
                }
                else {
                    f.setVisibility(View.GONE);
                    new GetFeedTask().execute((Void) null);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //nothing
            }
        }
    }


    private class GetFeedTask extends AsyncTask<Void, Void, Boolean> {

        private static final String TAG = "Error in getFeedTask";
        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            urlLink = RSS_feed;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink)){

                return false;}

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                XMLParser parserC = new XMLParser();
                arrayRSS = parserC.parse(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                // Fill RecyclerView
                mRecyclerView.setAdapter(new RssFeedListAdapter(arrayRSS));
            } else { //Failure, reverting to old feed
                RSS_feed = old_feed;
                if(!checkConnectivity()){ //there's connection
                    Toast.makeText(MainActivity.this,
                            "No news read, connectivity failure",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "No news read, invalid feed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean checkConnectivity(){
        Context context = this;
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public class RssFeedListAdapter
            extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder>{

        private List<itemRSS> mRssFeedModels;


        public class FeedModelViewHolder extends RecyclerView.ViewHolder {
            private View rssFeedView;

            public FeedModelViewHolder(View v) {
                super(v);
                rssFeedView = v;
            }
        }

        public RssFeedListAdapter(List<itemRSS> rssFeedModels) {
            mRssFeedModels = rssFeedModels;
        }

        @Override
        public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rss, parent, false);
            FeedModelViewHolder holder = new FeedModelViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(FeedModelViewHolder holder, int position) {

            final itemRSS rssFeedModel = mRssFeedModels.get(position);
            ((TextView)holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.title);

        }

        @Override
        public int getItemCount() {
            return mRssFeedModels.size();
        }

    }




    // useless menu
    /**
    @SuppressWarnings("ResourceType")
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.layout.toolbar_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.changefeed) {
            Intent intent = new Intent(this, ActivityChange.class);
            intent.putExtra("RSS_feed", RSS_feed);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */
}







