package kr.appfactory.kpop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**dc dddd
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    final AppCompatActivity activity = this;

    private  String nextPageToken;
    private  String version;
    private static Context context;
    private static  int networkYn = 0;
    private SharedPreferences PageToken;
    private SharedPreferences.Editor pt;
    private DrawerLayout mDrawerLayout;
    private View drawerView;
    private ActionBarDrawerToggle mToggle;
    Toolbar myToolbar;
    private ListView mnuListView;
    private ListView mnuListView2;
    private ListView mnuListView3;
    public List<MenuItema> itemList;
    public List<MenuItema> itemList2;
    public List<MenuItema> itemList3;
    public MenuItemAdapter menuItemAdapter;
    public MenuItemAdapter menuItemAdapter2;
    public MenuItemAdapter menuItemAdapter3;
    private MaterialSearchView searchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = FirebaseInstanceId.getInstance().getToken();

        //   target = target + token;

        SharedPreferences  PageToken = getSharedPreferences(nextPageToken, 0);
        setContentView(R.layout.activity_main);


        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 화면을 landscape(가로) 화면으로 고정하고 싶은 경우

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerView = (View) findViewById(R.id.nav_view);



        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        //추가된 소스코드, Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.

        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_left_menu); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요


        searchView = findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
       // searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //SearchProduct(getApplicationContext(), query);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, SearchFragment.newInstance(query));
                fragmentTransaction.commit();

                //Toast.makeText(getApplicationContext(),"Query: " + query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.showSuggestions();
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.showSuggestions();
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        /** * 기본 화면 설정 */



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, ChannelFragment.newInstance("PLh-mdrcK_ya18h6BQrHFaNrqHtjnstQxH", "K-POP 2019"));
        fragmentTransaction.commit();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVerticalFadingEdgeEnabled(false);
        navigationView.setVerticalScrollBarEnabled(false);
        navigationView.setHorizontalScrollBarEnabled(false);




        AdsFull.getInstance(getApplicationContext()).setAds(this);

        //AdsFull.getInstance(activity).setAdsFull();
        Button MyfavoritesButton = (Button) findViewById(R.id.MyfavoritesButton);

        //즐겨찾기저장추가
        MyfavoritesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new FavoritesFragment());
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawers();

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       // Log.d(TAG, "페이지이동 ");

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setIcon(R.drawable.billiard_icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.exitmsg);
        builder.setPositiveButton(R.string.exitmsgY, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AdsFull.getInstance(getApplicationContext()).setAdsFull();
                Timer timer = new Timer();
                timer.schedule( new TimerTask()
                                {
                                    public void run()
                                    {
                                        finish();
                                    }
                                }
                        , 1000);

            }

        });
        builder.setNegativeButton(R.string.exitmsgN, null);
        AlertDialog dialog = builder.show();


    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        item.setChecked(true);
        switch (item.getItemId()){

            case android.R.id.home: {

                // 데이터 원본 준비
                itemList = new ArrayList<MenuItema>();
                //어댑터 생성
                menuItemAdapter = new MenuItemAdapter(activity, itemList);
                //어댑터 연결
                mnuListView = (ListView) findViewById(R.id.movi_channel);

                mnuListView.setAdapter(menuItemAdapter);

                // 데이터 원본 준비

                itemList.add(new MenuItema("KPOP 2019 MV", "PLh-mdrcK_ya18h6BQrHFaNrqHtjnstQxH"));
                itemList.add(new MenuItema("KPOP 2018 MV", "PLgA8PQBu3V3bWtTK4Qu-Ji4xhd1s7QZKE"));
                itemList.add(new MenuItema("KPOP 2017 MV", "PL0dHPmqhiLT7Z_4Zty9O_2E6sS6zcRgT2"));
                itemList.add(new MenuItema("KPOP 2016 MV", "PL0dHPmqhiLT7auoSrm9bmCOAzkMn-RZIE"));
                itemList.add(new MenuItema("KPOP 2015 MV", "PL37A65FDB2C0123A7"));
                itemList.add(new MenuItema("KPOP 2014 MV", "PL2HEDIx6Li8gT96_A5ZaoxThLHQl3B0Ck"));
                itemList.add(new MenuItema("KPOP 2013 MV", "PLh-mdrcK_ya18h6BQrHFaNrqHtjnstQxH"));
                itemList.add(new MenuItema("KPOP 2012 MV", "PL72879C58787B578A"));
                itemList.add(new MenuItema("KPOP 2011 MV", "PL240042845F1C3EF2"));
                itemList.add(new MenuItema("KPOP 2010 MV", "PLoIEYfEFzAMHZTOJtJPYSXMU66cVJVuPy"));



                mnuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      //  Toast.makeText (activity, "클릭 getMenu_title" + itemList.get(position).getMenu_title()  , Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, ChannelFragment.newInstance(itemList.get(position).getMenu_link(), itemList.get(position).getMenu_title()));
                        fragmentTransaction.commit();
                        mDrawerLayout.closeDrawers();
                    }
                });


                // 데이터 원본 준비
                itemList2 = new ArrayList<MenuItema>();
                //어댑터 생성
                menuItemAdapter2 = new MenuItemAdapter(activity, itemList2);
                //어댑터 연결
                mnuListView2 = (ListView) findViewById(R.id.enter_com);

                mnuListView2.setAdapter(menuItemAdapter2);

                // 데이터 원본 준비

                itemList2.add(new MenuItema("SMTOWN", "PLgA8PQBu3V3ajemYXWXTzEgCtr5WZblEY"));
                itemList2.add(new MenuItema("BIG HIT Entertainment", "PLgA8PQBu3V3YoD6HEhGFsbvoMfZlHzbpp"));
                itemList2.add(new MenuItema("YG Entertainment", "PLgA8PQBu3V3Z1A5VElFxIgR6-s64BtIdX"));
                itemList2.add(new MenuItema("JYP Entertainment", "PLgA8PQBu3V3YRjm2XQ40tQzxm2_oPS58m"));
                itemList2.add(new MenuItema("FNC Entertainment", "PLgA8PQBu3V3Yq7TItx0f2Rfx7onO4PfKD"));
                itemList2.add(new MenuItema("Woollim Entertainment", "PLgA8PQBu3V3YyGsGRWkg5lcGm__XU2ojq"));
                itemList2.add(new MenuItema("Jellyfish Entertainment", "PLgA8PQBu3V3Y6zlOVo-q80tSjEKarht1V"));
                itemList2.add(new MenuItema("CUBE Entertainment", "PLgA8PQBu3V3ZTHzobqJ7J4VlaQW724v7o"));
                itemList2.add(new MenuItema("PLEDIS Entertainment", "PLgA8PQBu3V3ZzEd50hGF3h3UeIQ69pUjx"));
                itemList2.add(new MenuItema("Starship Entertainment", "PLgA8PQBu3V3Z5XvUJZetfVbPRYcFhghhX"));



                mnuListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //  Toast.makeText (activity, "클릭 getMenu_title" + itemList.get(position).getMenu_title()  , Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, ChannelFragment.newInstance(itemList2.get(position).getMenu_link(), itemList2.get(position).getMenu_title()));
                        fragmentTransaction.commit();
                        mDrawerLayout.closeDrawers();
                    }
                });



                mDrawerLayout.openDrawer(drawerView);
                //mDrawerLayout.closeDrawers();
            }


                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(networkYn==2){

                NotOnline();
                return true;

            }else {

                Log.d("check URL", url);
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);


            }
        }
    }
    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void updateIconBadge(Context context, int notiCnt) {
        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", notiCnt);
        badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
        context.sendBroadcast(badgeIntent);
    }
    public void NotOnline() {
        final String networkmsg = getString(R.string.networkmsg);

        new AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
                .setIcon(R.drawable.billiard_icon)
                .setTitle(R.string.app_name)
                .setMessage(""+networkmsg+"")
                .setNegativeButton(R.string.exitmsgN, null)
                .setPositiveButton(R.string.exitmsgY,new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog,int whichButton)
                    {
                        finish();
                        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    }
                }).show();

    }

    /**
     * getURLEncode
     */
    public static String getURLEncode(String content){

        try {
//          return URLEncoder.encode(content, "utf-8");   // UTF-8
            return URLEncoder.encode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * getURLDecode
     */
    public static String getURLDecode(String content){

        try {
            return URLDecoder.decode(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public int Online() {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {

            Log.d("연결됨" , "연결이 되었습니다.");
            networkYn =1;
        } else {
            Log.d("연결 안 됨" , "연결이 다시 한번 확인해주세요");
            networkYn =2;
        }
        return networkYn;
    }

}



class LoadMovieTask extends AsyncTask<Void, Void, String> {


    private SharedPreferences PageToken;
    private SharedPreferences.Editor pt;

    private  String location;
    private  Context mContext;
    private DriverMovieListAdapter driveradapter;
    private List<DriverMovie> driverMovieList;
    private ListView driverMovieListView;
    String target;

    private MainActivity activity;



    public LoadMovieTask(Context context, List<DriverMovie> driverMovieList, ListView view, DriverMovieListAdapter driveradapter, String target, String location) {
        this.mContext = context;
        this.driverMovieList = driverMovieList;
        this.driveradapter = driveradapter;
        this.driverMovieListView = view;
        this.target = target;
        this.location = location;

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            URL url = new URL(target);
           // Log.e("주소 url", ""+url);
            //Toast.makeText (mContext, "클릭" + url , Toast.LENGTH_LONG).show();

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();



            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));

            String temp;
            StringBuilder stringBuilder = new StringBuilder();

            while ((temp = bufferedReader.readLine()) != null) {
                // Log.e("temp", ""+temp);
                stringBuilder.append(temp + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    protected void onPostExecute(String result) {
        String nextPageToken="";
        //Log.e("드라이버2", ""+result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            String  totalResults = jsonObject.getJSONObject("pageInfo").getString("totalResults");

            try {
                nextPageToken = jsonObject.getString("nextPageToken");
            }  catch (Exception e) {
                //e.printStackTrace();
                nextPageToken="";

            }


            SharedPreference.putSharedPreference(mContext, "totalResults", totalResults);
            SharedPreference.putSharedPreference(mContext, "nextPageToken", nextPageToken);


            int count = 0;
            String thum_pic, subjectText, descriptionText, viewCount, viewDate, viewCnt, videoId, channelId;

           //Toast.makeText (mContext, "클릭" + jsonArray.length() , Toast.LENGTH_SHORT).show();

           // Log.e("jsonArray.length", ""+jsonArray.length());

            while (count < jsonArray.length()) {
                JSONObject object = jsonArray.getJSONObject(count);

                if(jsonObject.getString("kind").equals("youtube#playlistItemListResponse")){

                    try {

                        //Toast.makeText (mContext, "클릭" + jsonObject.getString("kind"), Toast.LENGTH_SHORT).show();

                        subjectText = object.getJSONObject("snippet").getString("title");
                        descriptionText = object.getJSONObject("snippet").getString("description");
                        viewDate = object.getJSONObject("snippet").getString("publishedAt")
                                .substring(0, 10);

                        videoId = object.getJSONObject("snippet")
                                .getJSONObject("resourceId").getString("videoId");

                        thum_pic = object.getJSONObject("snippet")
                                .getJSONObject("thumbnails").getJSONObject("medium")
                                .getString("url"); // 썸내일 이미지 URL값


                        viewCnt = "0";


                       //Toast.makeText (mContext, "channelId" + object.getJSONObject("snippet").getString("channelId"), Toast.LENGTH_SHORT).show();

                        if(!object.getJSONObject("snippet").getString("channelId").equals("UCPteEGbatxsfN4sLcZsnJBQ") ) {

                        DriverMovie drivermovie = new DriverMovie(thum_pic, subjectText, viewDate, viewCnt, videoId , descriptionText);
                        driverMovieList.add(drivermovie);
                    }
                    }  catch (Exception e) {
                        //e.printStackTrace();
                        nextPageToken="";
                    }

                }else if(jsonObject.getString("kind").equals("youtube#searchListResponse")){

                    //Toast.makeText (mContext, "클릭" + jsonObject.getString("kind"), Toast.LENGTH_SHORT).show();

                    //Toast.makeText (mContext, "클릭" + object.getJSONObject("id").getString("videoId") , Toast.LENGTH_SHORT).show();


                    videoId = object.getJSONObject("id").getString("videoId");
                    subjectText = object.getJSONObject("snippet").getString("title");
                    descriptionText = object.getJSONObject("snippet").getString("description");
                    viewDate = object.getJSONObject("snippet").getString("publishedAt")
                            .substring(0, 10);
                    thum_pic = object.getJSONObject("snippet")
                            .getJSONObject("thumbnails").getJSONObject("medium")
                            .getString("url"); // 썸내일 이미지 URL값


                    viewCnt = "0";

                    //Toast.makeText (mContext, "channelId" + object.getJSONObject("snippet").getString("channelId"), Toast.LENGTH_SHORT).show();


                        DriverMovie drivermovie = new DriverMovie(thum_pic, subjectText, viewDate, viewCnt, videoId , descriptionText);
                        driverMovieList.add(drivermovie);

                }




                count++;
            }


            if(location =="main"){
                driverMovieListView.setAdapter(driveradapter);


                driverMovieListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(view.getContext(), MoviePlayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("videoId", ""+  driverMovieList.get(position).getMovie_videoId());
                        intent.putExtra("videodesc", ""+  driverMovieList.get(position).getMovie_desc());
                        intent.putExtra("title",""+ driverMovieList.get(position).getMovie_title());

                        view.getContext().startActivity(intent);

                    }
                });

            }


        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString());

        }

    }


}




