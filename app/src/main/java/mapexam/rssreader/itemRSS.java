package mapexam.rssreader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dari1 on 01/02/2017.
 */

public class itemRSS {
    String title;
    private String description;
    private URL link;
    private String linkString;

    public itemRSS(){

    }
    public itemRSS(String title, String description, URL link){
        this.title = title;
        this.description = description;
        this.link = link;
    }
    public itemRSS(String title, String description, String link){
        this.title = title;
        this.description = description;
        /*try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            this.linkString = link;
            e.printStackTrace();
        }*/
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

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getLinkString() {
        return linkString;
    }

    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }
}
