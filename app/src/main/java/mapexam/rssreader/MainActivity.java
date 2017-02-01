package mapexam.rssreader;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static String RSS_feed = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Intent intent = getIntent();
        try {
            RSS_feed = intent.getExtras().getString("newRSS");
            EditText t = (EditText)findViewById(R.id.editText);
            t.setText(RSS_feed);
        }
        catch (Exception e){
            //No RSS_feed yet = app is kill
        }
    }


    /**
     * Launching the activity to change the RSS feed link
     * @param v
     */
    public void changeRSS(View v){
        Intent intent = new Intent(this, ActivityChange.class);
        intent.putExtra("RSS_feed", RSS_feed);
        startActivity(intent);
    }

    /** useless menu
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
