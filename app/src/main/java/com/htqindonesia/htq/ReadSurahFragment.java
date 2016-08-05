package com.htqindonesia.htq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadSurahFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadSurahFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadSurahFragment extends Fragment implements OnCompletionListener,
        OnSeekBarChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String ARG_surah = null;
    public static final String ARG_response = null;
    String arg_response = null;
    String arg_surah = null;
    String[] parts;
    private JSONArray jsonArray = null;
    private View view;
    private ProgressDialog pDialog;

//    int[] card, card_no;
    String[] file_audio, card, card_no, ket;
    String audio, file;
    boolean isPause = false;
    private Handler hendler = new Handler();
    private MediaPlayer mediaPlayer;
    private Utilities utilities = new Utilities();
    private SeekBar seekBar;
    int repeat_n = 1;
    int repeat_max;
    private ImageButton ibPlay;
    private ImageButton ibPause;
    private ImageButton ibStop;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReadSurahFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadSurahFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadSurahFragment newInstance(String param1, String param2) {
        ReadSurahFragment fragment = new ReadSurahFragment();
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
        view = inflater.inflate(R.layout.fragment_read_surah, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Buffering...");
        pDialog.setCancelable(false);

        Bundle bundle = getArguments();
        try {
            arg_response = bundle.getString(ARG_response);
            Log.d("arg_response : ", arg_response);
            parts = arg_response.split("#");
            jsonArray = new JSONArray(parts[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("JSONArray : ", jsonArray.toString());
        file_audio = new String[jsonArray.length()];
        card = new String[jsonArray.length()];
        card_no = new String[jsonArray.length()];
        ket = new String[jsonArray.length()];

        try {
            for (int i=0; i<jsonArray.length(); i++ ){

                JSONObject surah = (JSONObject) jsonArray.get(i);
                String rs_audio = surah.getString("audio");
                String rs_image = surah.getString("image");
                String rs_keterangan = surah.getString("keterangan");

                file_audio[i] = rs_audio;
                card[i] = rs_image;
                card_no[i] = String.valueOf(jsonArray.length() - i);
                ket[i] = rs_keterangan;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        TextView cardDesc1 = (TextView) view.findViewById(R.id.tvDesc1);
        cardDesc1.setText(parts[0]);

        ibPlay = (ImageButton) view.findViewById(R.id.ibPlay);
        ibPause = (ImageButton) view.findViewById(R.id.ibPause);
        ibStop = (ImageButton) view.findViewById(R.id.ibStop);
        ImageButton ibMic = (ImageButton) view.findViewById(R.id.ibMic);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.stop(); // STOP
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnInfoListener(this);
//        mediaPlayer.setOnPreparedListener(this);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        ibPause.setEnabled(false);
        ibStop.setEnabled(false);

        final SwipeNumberPicker swipeNumberPicker = (SwipeNumberPicker) view.findViewById(R.id.number_picker);
        swipeNumberPicker.setNumberPickerDialogTitle("PLAY REPEAT NUMBER");
        repeat_max = swipeNumberPicker.getValue();

//        card = new int[]{R.drawable.annaba_5_8, R.drawable.annaba_1_4};
//        card_no = new int[]{2, 1};
//        file_audio = new String[]{"https://www.dropbox.com/s/a8ne4ak4pn55gcz/2.mp3?raw=1", "https://www.dropbox.com/s/eoq5eszqtvtcn1b/1.mp3?raw=1"};

        int last = file_audio.length - 1;
        file = file_audio[last];

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.surah_vp);
        PagerAdapter pagerAdapter = new SurahVPAdapter(getContext(), card);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1, false);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
                TextView cardDesc = (TextView) view.findViewById(R.id.tvDesc);
                TextView cardDesc2 = (TextView) view.findViewById(R.id.tvDesc2);
                cardDesc.setText("Kartu " + card_no[i]);
                cardDesc2.setText("(Ayat "+ket[i]+")");
                Log.d("array = ", file_audio[i]);
                file = file_audio[i];
                repeat_n = 1;
                mediaPlayer.seekTo(0);
                seekBar.setProgress(0);
                mediaPlayer.stop();
                hendler.removeCallbacks(mUpdateTimeTask);

                ibPlay.setClickable(true);
                ibPause.setClickable(false);
                ibStop.setClickable(false);

                ibPlay.setEnabled(true);
                ibPause.setEnabled(false);
                ibStop.setEnabled(false);

                isPause = false;
            }

            @Override
            public void onPageSelected(final int i) {
            }

            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    if (isPause) {
                        mediaPlayer.start();
                        isPause = false;
                    } else {

                        repeat_n = 1;
                        Log.d("file", file);
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException i) {
                            i.printStackTrace();
                        }

                        try {
                            mediaPlayer.prepare();
                        } catch (IllegalStateException e) {
                            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                        }
                        mediaPlayer.start();
                        seekBar.setProgress(0);
                        seekBar.setMax(100);
                        updateProgressBar();
                        isPause = false;
                    }

                }
                ibPlay.setClickable(false);
                ibPause.setClickable(true);
                ibStop.setClickable(true);

                ibPlay.setEnabled(false);
                ibPause.setEnabled(true);
                ibStop.setEnabled(true);
            }
        });

        ibPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    if (!isPause) {
                        mediaPlayer.pause();

                        ibPlay.setClickable(true);
                        ibPause.setClickable(false);
                        ibStop.setClickable(true);

                        ibPlay.setEnabled(true);
                        ibPause.setEnabled(false);
                        ibStop.setEnabled(true);

                        isPause = true;
                    }
                }
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat_n = 1;
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();

                ibPlay.setClickable(true);
                ibPause.setClickable(false);
                ibStop.setClickable(false);

                ibPlay.setEnabled(true);
                ibPause.setEnabled(false);
                ibStop.setEnabled(false);


                isPause = false;
            }
        });

        ibMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ok", "ok");
                Toast.makeText(getContext(), "Sedang dalam pengerjaan !", Toast.LENGTH_SHORT).show();

            }
        });

        swipeNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
//                result2.setText(String.valueOf(newValue));
                Log.v("LOG SNP = ", String.valueOf(newValue));
                repeat_max = newValue;
                return true;
            }
        });

        return view;
    }

    public void updateProgressBar() {
        hendler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            int progress = (int)(utilities.getProgressPercentage(currentDuration, totalDuration));

            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);
            hendler.postDelayed(this, 100);

        }
    };

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

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mediaPlayer.stop();
    }

//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent)
//    {
////        double ratio = percent / 100.0;
////        int bufferingLevel = (int)(mp.getDuration() * ratio);
////        seekBar.setSecondaryProgress(bufferingLevel);
//        if (percent > 99){
//            Log.d("BUFFER", "hide");
//            hidepDialog();
//        }
//    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

//    @Override
//    public boolean onInfo(MediaPlayer mp, int what, int extra) {
//        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
////            loadingDialog.setVisibility(View.VISIBLE);
//            showpDialog();
//        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
////            loadingDialog.setVisibility(View.GONE);
//            hidepDialog();
//        }
//        return false;
//    }

//    @Override
//    public void onStop(){
//        super.onStop();
//        mediaPlayer.stop();
//    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        mediaPlayer.stop();
//    }

//    @Override
//    public void setUserVisibleHint(boolean visible)
//    {
//        super.setUserVisibleHint(visible);
//        if (visible)
//        {
//            if (view == null) {
//                Toast.makeText(getActivity(), "ERROR ", Toast.LENGTH_LONG ).show();
//                return;
//            }
//        }
//    }

//    @Override
//    public void onPrepared(MediaPlayer mp){
//        mediaPlayer.start();
//    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("repeat_n", Integer.toString(repeat_n));
        Log.d("repeat_max", Integer.toString(repeat_max));
        if (repeat_n < repeat_max){
            mediaPlayer.start();
            repeat_n++;
        }else {
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.seekTo(0);
            ibPlay.setClickable(true);
            ibPause.setClickable(false);
            ibStop.setClickable(false);

            ibPlay.setEnabled(true);
            ibPause.setEnabled(false);
            ibStop.setEnabled(false);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        hendler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        hendler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        Log.d("haha","dada");
        // update timer progress again
        updateProgressBar();
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
}
