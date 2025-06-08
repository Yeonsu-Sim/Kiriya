package com.ssafy.kiriya.model.dto;

public class BucketList {
    private int mno;  // Member 번호
    private int ano;  // Attraction 번호

    public BucketList() {}

    public BucketList(int mno, int ano) {
        this.mno = mno;
        this.ano = ano;
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return "BucketList [mno=" + mno + ", ano=" + ano + "]";
    }
}
