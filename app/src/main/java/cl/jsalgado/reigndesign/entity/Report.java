package cl.jsalgado.reigndesign.entity;

import java.util.List;

/**
 * Created by joels on 04-09-2017.
 *
 */

public class Report {

    private List<Hit> hits;
    private int hitsPerPage;

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public void setHitsPerPage(int hitsPerPage) {
        this.hitsPerPage = hitsPerPage;
    }
}
