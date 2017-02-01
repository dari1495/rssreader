package mapexam.rssreader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dari1 on 01/02/2017.
 */

public class itemRSS {
    String title;
    String description;
    String link;

    public itemRSS(){

    }
    public itemRSS(String title, String description, String link){
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
