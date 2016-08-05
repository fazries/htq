package com.htqindonesia.htq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SurahFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SurahFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SurahFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressDialog pDialog;
    private String url = null;
    private String surah_name = null;
    private static String TAG = MainActivity.class.getSimpleName();
    private String jsonResponse;
    private String[] ar_audio;
    private String[] ar_image;
    private String[] ar_keterangan;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SurahFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SurahFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SurahFragment newInstance(String param1, String param2) {
        SurahFragment fragment = new SurahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_surah, container, false);

        //arabic font
        AssetManager manager = getContext().getAssets();
        try
        {
            manager.open("fonts/tahoma.ttf");
            TextView tv=(TextView) view.findViewById(R.id.surah_arabic);
            tv.setTypeface(Typeface.createFromAsset(manager, "fonts/tahoma.ttf"));
        }catch(Exception e){
            e.printStackTrace();
        }

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        final List<SurahList> rowSurahListItem = getSurahItemList();
        LinearLayoutManager lSurahLayout = new LinearLayoutManager(getContext());
        RecyclerView rSurahView = (RecyclerView) view.findViewById(R.id.surah_rv);
        SurahRVAdapter surahAdapter = new SurahRVAdapter(getActivity(), rowSurahListItem);
        rSurahView.setHasFixedSize(true);
        rSurahView.setLayoutManager(lSurahLayout);
        rSurahView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rSurahView.setItemAnimator(new DefaultItemAnimator());
        rSurahView.setAdapter(surahAdapter);

        rSurahView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rSurahView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SurahList surahList = rowSurahListItem.get(position);
//                Toast.makeText(getContext(), surahList.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                String no = surahList.getNo();
                surah_name = surahList.getName();

                url = "http://fosa-indonesia.org/htq/index.php?no="+no+"&audio_type=2";
                makeJsonArrayRequest();

//                ReadSurahFragment readSurahFragment = new ReadSurahFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.fragment, readSurahFragment).addToBackStack(null).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
//                        ar_audio = new String[response.length()];
//                        ar_image = new String[response.length()];
//                        ar_keterangan = new String[response.length()];

                        hidepDialog();
                        ReadSurahFragment readSurahFragment = new ReadSurahFragment();
                        Bundle bundle = new Bundle();

//                        bundle.putString(ReadSurahFragment.ARG_surah, surah_name);
                        bundle.putString(ReadSurahFragment.ARG_response, surah_name+"#"+response.toString());

                        readSurahFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment, readSurahFragment).addToBackStack(null).commit();

//                        try {
////                            jsonResponse = "";
//                            for (int i = 0; i < response.length(); i++) {
//
//
//                                JSONObject surah = (JSONObject) response.get(i);
//
//                                String audio = surah.getString("audio");
//                                ar_audio[i] = audio;
////                                Log.d("ar_audio", ar_audio[i]);
//
//                                String image = surah.getString("image");
//                                ar_image[i] = image;
////                                Log.d("ar_image", ar_image[i]);
//
//                                String keterangan = surah.getString("keterangan");
//                                ar_keterangan[i] = keterangan;
////                                Log.d("ar_keterangan", ar_keterangan[i]);
//
////                                jsonResponse += "audio: " + audio + "\n";
////                                jsonResponse += "image: " + image + "\n";
////                                jsonResponse += "keterangan: " + keterangan + "\n\n";
//
//                            }
////                            Log.d("JSON", jsonResponse);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "File belum tersedia !", Toast.LENGTH_SHORT).show();
                        hidepDialog();
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private List<SurahList> getSurahItemList(){

        List<SurahList> surahItems = new ArrayList<>();
        surahItems.add(new SurahList("78", "An-Naba’", "Berita Besar", "Makkiyyah | 40 Ayat", ArabicUtilities.reshape("النّبا")));
        surahItems.add(new SurahList("79", "An-Nazi’at", "Malaikat-malaikat Yang Mencabut", "Makkiyyah | 46 Ayat", ArabicUtilities.reshape("النّازعات")));
        surahItems.add(new SurahList("80", "’Abasa", "Bermuka Asam", "Makkiyyah | 42 Ayat", ArabicUtilities.reshape("عبس")));
        surahItems.add(new SurahList("81", "At-Takwir", "Menggulung", "Makkiyyah | 29 Ayat", ArabicUtilities.reshape("التّكوير")));
        surahItems.add(new SurahList("82", "Al-Infitar", "Terbelah", "Makkiyyah | 19 Ayat", ArabicUtilities.reshape("الانفطار")));
        surahItems.add(new SurahList("83", "Al-Mutaffifín", "Orang-orang Yang Curang", "Makkiyyah | 36 Ayat", ArabicUtilities.reshape("المطفّفين")));
        surahItems.add(new SurahList("84", "Al-Insyiqáq", "Terbelah", "Makkiyyah | 25 Ayat", ArabicUtilities.reshape("الانشقاق")));
        surahItems.add(new SurahList("85", "Al-Burúj", "Gugusan Bintang", "Makkiyyah | 22 Ayat", ArabicUtilities.reshape("البروج")));
        surahItems.add(new SurahList("86", "At-Táriq", "Yang Datang di Malam Hari", "Makkiyyah | 17 Ayat", ArabicUtilities.reshape("الطّارق")));
        surahItems.add(new SurahList("87", "Al-A’lá", "Yang Paling Tinggi", "Makkiyyah | 19 Ayat", ArabicUtilities.reshape("الْأعلى")));
        surahItems.add(new SurahList("88", "Al-Gásyiyah", "Hari Pembalasan", "Makkiyyah | 26 Ayat", ArabicUtilities.reshape("الغاشية")));
        surahItems.add(new SurahList("89", "Al-Fajr", "Fajar", "Makkiyyah | 10 Ayat", ArabicUtilities.reshape("الفجر")));
        surahItems.add(new SurahList("90", "Al-Balad", "Negri", "Makkiyyah | 35 Ayat", ArabicUtilities.reshape("البلد")));
        surahItems.add(new SurahList("91", "Asy-Syams", "Matahari", "Makkiyyah | 15 Ayat", ArabicUtilities.reshape("الشّمس")));
        surahItems.add(new SurahList("92", "Al-Lail", "Malam", "Makkiyyah | 21 Ayat", ArabicUtilities.reshape("الّيل")));
        surahItems.add(new SurahList("93", "Ad-Duhá", "Waktu Matahari Sepenggalan Naik", "Makkiyyah | 11 Ayat", ArabicUtilities.reshape("الضحى")));
        surahItems.add(new SurahList("94", "Al-Insyiráh", "Melapangkan", "Makkiyyah | 8 Ayat", ArabicUtilities.reshape("الانشراح")));
        surahItems.add(new SurahList("95", "At-Tín", "Buah Tin", "Makkiyyah | 8 Ayat", ArabicUtilities.reshape("التِّينِ")));
        surahItems.add(new SurahList("96", "Al-‘Alaq", "Segumpal Darah", "Makkiyyah | 19 Ayat", ArabicUtilities.reshape("العَلَق")));
        surahItems.add(new SurahList("97", "Al-Qadr", "Kemuliaan", "Makkiyyah | 5 Ayat", ArabicUtilities.reshape("الْقَدْرِ")));
        surahItems.add(new SurahList("98", "Al-Bayyinah", "Bukti", "Madaniyyah | 8 Ayat", ArabicUtilities.reshape("الْبَيِّنَةُ")));
        surahItems.add(new SurahList("99", "Az-Zalzalah", "Kegoncangan", "Madaniyyah | 8 Ayat", ArabicUtilities.reshape("الزلزلة")));
        surahItems.add(new SurahList("100", "Al-‘Ádiyát", "Kuda Perang Berlari Kencang", "Makkiyyah | 11 Ayat", ArabicUtilities.reshape("العاديات")));
        surahItems.add(new SurahList("101", "Al-Qari'ah", "Hari Kiamat", "Makkiyyah | 11 Ayat", ArabicUtilities.reshape("القارعة")));
        surahItems.add(new SurahList("102", "At-Takasur", "Bermegah-megah", "Makkiyyah | 8 Ayat", ArabicUtilities.reshape("التكاثر")));
        surahItems.add(new SurahList("103", "Al-'Asr", "Masa", "Makkiyyah | 3 Ayat", ArabicUtilities.reshape("العصر")));
        surahItems.add(new SurahList("104", "Al-Humazah", "Pengumpat", "Makkiyyah | 9 Ayat", ArabicUtilities.reshape("الهُمَزة")));
        surahItems.add(new SurahList("105", "Al-Fil", "Gajah", "Makkiyyah | 5 Ayat", ArabicUtilities.reshape("الْفِيلِ")));
        surahItems.add(new SurahList("106", "Quraisy", "SukuQuraisy", "Makkiyyah | 4 Ayat", ArabicUtilities.reshape("قُرَيْشٍ")));
        surahItems.add(new SurahList("107", "Al-Ma’un", "Barang-barang yang Berguna", "Makkiyyah | 7 Ayat", ArabicUtilities.reshape("الْمَاعُونَ")));
        surahItems.add(new SurahList("108", "Al-Kausar", "Sungai di Surga", "Makkiyyah | 3 Ayat", ArabicUtilities.reshape("الكوثر")));
        surahItems.add(new SurahList("109", "Al-Kafirun", "Orang-orang Kafir", "Makkiyyah | 6 Ayat", ArabicUtilities.reshape("الْكَافِرُونَ")));
        surahItems.add(new SurahList("110", "An-Nasr", "Pertolongan", "Madaniyyah | 3 Ayat", ArabicUtilities.reshape("النصر")));
        surahItems.add(new SurahList("111", "Al-Lahab", "Gejolak Api", "Makkiyyah | 5 Ayat", ArabicUtilities.reshape("المسد")));
        surahItems.add(new SurahList("112", "Al-Ikhlas", "Memurnikan Keesaan Allah", "Makkiyyah | 4 Ayat", ArabicUtilities.reshape("الإخلاص")));
        surahItems.add(new SurahList("113", "Al-Falaq", "Waktu Subuh", "Makkiyyah | 5 Ayat", ArabicUtilities.reshape("الْفَلَقِ")));
        surahItems.add(new SurahList("114", "An-Nas", "Manusia", "Makkiyyah | 6 Ayat", ArabicUtilities.reshape("النَّاسِ")));

        return surahItems;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SurahFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SurahFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
