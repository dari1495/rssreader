package mapexam.rssreader;

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
        System.out.println("el usuario escribe: " + RSS_given);
        RSS_feed = RSS_given.getText().toString();
        System.out.println(RSS_feed);
        Intent i2 = getIntent();
        i2.putExtra("nRSS_feed",RSS_feed);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("newRSS",RSS_feed);
        startActivity(intent);
    }
}
