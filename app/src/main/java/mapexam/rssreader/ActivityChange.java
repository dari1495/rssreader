package mapexam.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityChange extends AppCompatActivity {

    static String RSS_feed = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        final Button button = (Button) findViewById(R.id.accept_url);
        final EditText RSS_given = (EditText) findViewById(R.id.RSS_URL);
        Intent intent = getIntent();
        RSS_feed = intent.getExtras().getString("RSS_feed");
        RSS_given.setText(RSS_feed);
    }
    public void changeRSS(View v) {
        // save the url given
        final EditText RSS_given = (EditText) findViewById(R.id.RSS_URL);
        RSS_feed = RSS_given.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("newRSS",RSS_feed);
        //startActivity(intent);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newRSS",RSS_feed);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
    public void clearText(View v) {
        //clear the field
        EditText t = (EditText) findViewById(R.id.RSS_URL);
        t.setText("");
    }
}
