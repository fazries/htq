package com.htqindonesia.htq;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wahyudhzt on 27/05/2016.
 */
public class SurahRVAdapter extends RecyclerView.Adapter<SurahRVAdapter.SurahViewHolder> {

    private Context context;
    private List<SurahList> surahList;

    public SurahRVAdapter(Context context, List<SurahList> surahList) {
        this.context = context;
        this.surahList = surahList;
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    @Override
    public void onBindViewHolder(SurahViewHolder holder, int position) {
        holder.Surah_No.setText(surahList.get(position).getNo());
        holder.Surah_Name.setText(surahList.get(position).getName());
        holder.Surah_Translate.setText(surahList.get(position).getTranslate());
        holder.Surah_Desc.setText(surahList.get(position).getDesc());
        holder.Surah_Arabic.setText(surahList.get(position).getArabic());
    }

    @Override
    public SurahViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.surah_list, viewGroup, false);

        return new SurahViewHolder(itemView);
    }

    public static class SurahViewHolder extends RecyclerView.ViewHolder {

        protected TextView Surah_No;
        protected TextView Surah_Name;
        protected TextView Surah_Translate;
        protected TextView Surah_Desc;
        protected TextView Surah_Arabic;

        public SurahViewHolder(View v) {
            super(v);
            Surah_No = (TextView) v.findViewById(R.id.surah_no);
            Surah_Name = (TextView) v.findViewById(R.id.surah_name);
            Surah_Translate = (TextView) v.findViewById(R.id.surah_translate);
            Surah_Desc = (TextView) v.findViewById(R.id.surah_desc);
            Surah_Arabic = (TextView) v.findViewById(R.id.surah_arabic);
        }
    }
}
