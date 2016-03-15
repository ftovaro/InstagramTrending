package com.ftovaro.instagramtrending.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ftovaro.instagramtrending.R;
import com.ftovaro.instagramtrending.adapters.GridImagesAdapter;
import com.ftovaro.instagramtrending.interfaces.CommunicatorListener;
import com.ftovaro.instagramtrending.interfaces.OnImageSliderListener;
import com.ftovaro.instagramtrending.model.InstagramPost;
import com.ftovaro.instagramtrending.interfaces.OnDownloadTaskCompleted;
import com.ftovaro.instagramtrending.interfaces.OnPostPressListener;
import com.ftovaro.instagramtrending.utils.VolleyUtils;

import java.util.ArrayList;

/**
 * Created by FelipeTovarMac on 12/5/15.
 */
public class GridImagesFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static ArrayList<InstagramPost> posts = new ArrayList<>();
    /** ProgressDialog that shows a loading text **/
    private static ProgressDialog pDialog;
    /** An adapter that has the settings of the list **/
    private static GridImagesAdapter gridImagesAdapter;
    /** The message shown if there is a problem downloading the data **/
    private static final String MESSAGE_ERROR_DOWNLOADING_DATA = "Error updating view";
    /** The message shown in the progress dialog **/
    private static final String MESSAGE_INFO_LOADING = "Loading...";

    private CommunicatorListener communicatorListener;

    //private OnImageSliderListener sendInstrgramPosts;

    GridView gridview;

    //OnPostPressListener postPressListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView =  inflater.inflate(R.layout.grid_images_fragment, container, false);

        gridview = (GridView) rootView.findViewById(R.id.grid);
        gridImagesAdapter = new GridImagesAdapter(this.getActivity().getApplicationContext(),
                posts);
        downloadInstagramData();
        gridview.setAdapter(gridImagesAdapter);
        gridview.setOnItemClickListener(this);
        //communicatorListener.sendInstagramPosts(posts);
        return rootView;
    }

    private void downloadInstagramData(){
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage(MESSAGE_INFO_LOADING);
        pDialog.show();
        VolleyUtils.updatePostList(getActivity().getApplicationContext(),
                new OnDownloadTaskCompleted() {
            @Override
            public void onTaskCompleted(ArrayList<InstagramPost> instagramPosts,
                                        boolean error, String message) {
                if(!error){
                    posts.addAll(instagramPosts);
                    gridImagesAdapter.swapPosts(posts);
                    gridImagesAdapter.updateDataSet();
                    hidePDialog();
                    communicatorListener.sendInstagramPosts(posts);
                } else {
                    hidePDialog();
                    Toast.makeText(getActivity().getApplicationContext(),
                            MESSAGE_ERROR_DOWNLOADING_DATA,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void setPosts(ArrayList<InstagramPost> instagramPosts){
        //posts.addAll(instagramPosts);
        //gridImagesAdapter.swapPosts(instagramPosts);
        /*
        if(isSwipeRefreshActive){
            isSwipeRefreshActive = false;
            swipeRefreshLayout.setRefreshing(false);
        }
        */
        hidePDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // retrieve the GridView item
        //GridViewItem item = mItems.get(position);

        // do something
        //Toast.makeText(getActivity(), posts.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        communicatorListener.onPostPressed(position);

    }

    /*
    @Override
    public void onResume() {
        /*
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        /** Starts the thread to update the list of posts **
        new UpdatePostsTask().execute();
        //hidePDialog();
        super.onResume();
    }
    */

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {

            communicatorListener = (CommunicatorListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement CommunicatorListener");
        }
    }
}