package cn.tealc995.kkmaid.api.model;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-12 19:29
 */
public class CountDetail {
    private int count;
    private int ratio;
    private int review_point;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getReview_point() {
        return review_point;
    }

    public void setReview_point(int review_point) {
        this.review_point = review_point;
    }
}