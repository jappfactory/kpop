package kr.appfactory.billiard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class View4Fragment extends Fragment implements AbsListView.OnScrollListener {

    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    public  ListView driverMovieListView;
    public List<DriverMovie> driverMovieList;
    public DriverMovieListAdapter driveradapter;
    private  ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    public int loading = 0;
    private static  int networkYn = 0;
    public int loadingresult = 0;
    public int viewcnt = 0;
    Toolbar myToolbar;


    Activity activity;
    String Keyword = ((MainActivity)getActivity()).getURLEncode("포켓볼");
    String target = "https://www.googleapis.com/youtube/v3/search?part=snippet&videoEmbeddable=true&order=date&videoSyndicated=true&maxResults=10&key=AIzaSyAxktqOQC0vC9eL622AzlJGwtSs2VHAH00&safeSearch=strict&type=video&q="+Keyword+"&pageToken=";

    private OnFragmentInteractionListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (Activity) getActivity();
    }
    public View4Fragment() {}

    public static View4Fragment newInstance() {
        View4Fragment fragment = new View4Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle b) {
        super.onActivityCreated(b);

        SharedPreference.putSharedPreference(getActivity(), "viewcnt", 0);
        driverMovieListView  = (ListView) getView().findViewById(R.id.subView4ListView);
        driverMovieList = new ArrayList<DriverMovie>();
        driveradapter = new DriverMovieListAdapter(activity, driverMovieList, this);
        driverMovieListView.setAdapter(driveradapter);


        driverMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videoId", ""+  driverMovieList.get(position).getMovie_videoId());
                intent.putExtra("title",""+ driverMovieList.get(position).getMovie_title());
                intent.putExtra("videodesc", ""+  driverMovieList.get(position).getMovie_desc());
                intent.putExtra("publishedAt",""+ driverMovieList.get(position).getMovie_date());
                intent.putExtra("thum_pic",""+ driverMovieList.get(position).getThum_img());

                view.getContext().startActivity(intent);

            }
        });


        driverMovieListView.setOnScrollListener(this);

        // 다음 데이터를 불러온다.
        getItem(target);
    }

    public void progressBarShow(){

        driverMovieListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                // 여기서 이벤트를 막습니다.
                return true;
            }
        });
        // 로딩중을 알리는 프로그레스바를 보인다.
        progressBar.setVisibility(View.VISIBLE);
    }

    public void progressBarHidden(){

        driverMovieListView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                // 여기서 이벤트를 막습니다.
                return false;
            }
        });
        // 로딩중을 알리는 프로그레스바를 숨기기.
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {


        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBarShow();
            String aa= SharedPreference.getSharedPreference(getActivity(), "nextPageToken");

            String target2 = target + aa;

            // 다음 데이터를 불러온다.
            getItem(target2);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = true;
    }

    public void getItem(String target){

        //loading ++ ;
        //loadingresult = loading % 10;
       // if (loadingresult == 0 ) AdsFull.getInstance(getActivity()).setAdsFull();

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        new LoadMovieTask(getActivity(), driverMovieList, driverMovieListView, driveradapter, target,"sub").execute();

        Log.d("driverMovieList6", ""+driverMovieList);

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                try {
                    driveradapter.notifyDataSetChanged();

                    String totalResults= SharedPreference.getSharedPreference(getActivity(), "totalResults");
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    totalResults = decimalFormat.format(Double.parseDouble(totalResults.toString().replaceAll(",","")));
                    TextView searchcnt = (TextView) getView().findViewById(R.id.searchcnt);
                    searchcnt.setText(totalResults);

                    // progressBar.setVisibility(View.GONE);
                    progressBarHidden();
                    mLockListView = false;
                }catch  (Exception e) {
                    e.printStackTrace();
                }

            }
        },1000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        networkYn = ((MainActivity)getActivity()).Online();
        if(networkYn==2) ((MainActivity)getActivity()).NotOnline();

        View view=inflater.inflate(R.layout.fragment_view4, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        myToolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        TextView title = (TextView) getActivity().findViewById(R.id.toolbar_title);
        actionBar.setTitle("포켓볼 강좌 영상");

        final Button sub1Button = (Button) view.findViewById(R.id.sub1Button);
        final Button sub2Button = (Button) view.findViewById(R.id.sub2Button);
        final Button sub3Button = (Button) view.findViewById(R.id.sub3Button);
        final Button sub4Button = (Button) view.findViewById(R.id.sub4Button);
        final Button sub5Button = (Button) view.findViewById(R.id.sub5Button);


        sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlueDark));

        sub1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sub1Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub2Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub3Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub5Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new View1Fragment());
                fragmentTransaction.commit();

            }
        });

        sub2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub1Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub2Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub3Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub5Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new View2Fragment());
                fragmentTransaction.commit();

            }
        });



        sub3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub1Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub2Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub3Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub5Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new View3Fragment());
                fragmentTransaction.commit();

            }
        });


        sub4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub1Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub2Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub3Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub5Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new View4Fragment());
                fragmentTransaction.commit();

            }
        });

        sub5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub1Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub2Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub3Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub4Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                sub5Button.setBackgroundColor(getResources().getColor(R.color.colorBlue));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new View5Fragment());
                fragmentTransaction.commit();

            }
        });
        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


