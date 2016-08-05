package com.htqindonesia.htq;

/**
 * Created by wahyudhzt on 27/05/2016.
 */
public class SurahList {
    private String no, name, translate, desc, arabic;

    public SurahList(String no, String name, String translate, String desc, String arabic){
        this.no = no;
        this.name = name;
        this.translate = translate;
        this.desc = desc;
        this.arabic = arabic;
    }

    public String getNo(){
        return no;
    }

    public void setNo(String no){
        this.no = no;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getArabic(){
        return arabic;
    }

    public void setArabic(String arabic){
        this.arabic = arabic;
    }
}
