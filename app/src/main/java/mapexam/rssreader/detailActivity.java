package mapexam.rssreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

interface RecyclerViewClickListener
{

    public void recyclerViewListClicked(View v, int position);
}

public class detailActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mBody;
    TextView mLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        mTitle = (TextView)findViewById(R.id.titleView);
        mBody = (TextView)findViewById(R.id.bodyView);
        mLink = (TextView)findViewById(R.id.linkView);
        String title = intent.getExtras().getString("title");
        String body = intent.getExtras().getString("body");
        String link = intent.getExtras().getString("link");
        System.out.println("Title: "+title + "" +
                "\n Body: " + body +"" +
                "|n link: "+ link);
        mTitle.setText(title);
        mBody.setText(body);
        mLink.setText(link);
        mLink.setMovementMethod(LinkMovementMethod.getInstance()); //doing the link clickable
    }

    public void goBack(View v){
        finish();
    }
}
