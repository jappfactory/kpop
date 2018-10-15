package kr.co.pointn.golfya;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WoodFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WoodFragment newInstance(String param1, String param2) {
        WoodFragment fragment = new WoodFragment();
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

    private ListView driverMovieListView;
    private DriverMovieListAdapter driveradapter;
    private List<DriverMovie> driverMovieList;

    @Override
    public void onActivityCreated(@Nullable Bundle b) {
        super.onActivityCreated(b);


        driverMovieListView  = (ListView) getView().findViewById(R.id.subWoodListView);
        driverMovieList = new ArrayList<DriverMovie>();

        driverMovieList.add(new DriverMovie("https://www.sacoop.kr/upload/project_img/33.jpg","쥬피터 우드 영상","100"));
        driverMovieList.add(new DriverMovie("https://www.sacoop.kr/upload/project_img/33.jpg","쥬피터 우드 영상","100"));
        driverMovieList.add(new DriverMovie("https://www.sacoop.kr/upload/project_img/33.jpg","쥬피터 우드 영상","100"));
        driverMovieList.add(new DriverMovie("https://www.sacoop.kr/upload/project_img/33.jpg","쥬피터 우드 영상","100"));
        driverMovieList.add(new DriverMovie("https://www.sacoop.kr/upload/project_img/33.jpg","쥬피터 우드 영상","100"));

        driveradapter = new DriverMovieListAdapter(getContext().getApplicationContext(), driverMovieList);
        driverMovieListView.setAdapter(driveradapter);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wood, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}