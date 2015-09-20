package com.kanishka.virustotal.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by noab on 10/09/15.
 */
public class SearchResult {
    @SerializedName("next_page")
    private String nextPage;
    private List<String> hashes;
    private int result;

    public SearchResult() {
    }

    public SearchResult(String nextPage, List<String> hashes, int result) {
        this.nextPage = nextPage;
        this.hashes = hashes;
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<String> getHashes() {
        return hashes;
    }

    public void setHashes(List<String> hashes) {
        this.hashes = hashes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (result != that.result) return false;
        if (!hashes.equals(that.hashes)) return false;
        if (!nextPage.equals(that.nextPage)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = nextPage.hashCode();
        result1 = 31 * result1 + hashes.hashCode();
        result1 = 31 * result1 + result;
        return result1;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "nextPage='" + nextPage + '\'' +
                ", hashes=" + hashes +
                ", result=" + result +
                '}';
    }
}
