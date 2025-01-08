package cn.tealc995.asmronline.api.model;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:31
 */
public class Rank {
    private String term;
    private String rank_date;
    private int rank;
    private String category;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRank_date() {
        return rank_date;
    }

    public void setRank_date(String rank_date) {
        this.rank_date = rank_date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}