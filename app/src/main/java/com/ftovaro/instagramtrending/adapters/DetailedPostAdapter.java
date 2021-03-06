package com.ftovaro.instagramtrending.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ftovaro.instagramtrending.fragments.DetailedPostFragment;
import com.ftovaro.instagramtrending.model.InstagramPost;
import com.ftovaro.instagramtrending.model.InstagramUser;

import java.util.ArrayList;

/**
 * Adapter for the fragment of detailed post.
 * Created by FelipeTovar on 14-Mar-16.
 */
public class DetailedPostAdapter extends FragmentStatePagerAdapter {

    /** list of InstagramPosts **/
    ArrayList<InstagramPost> instagramPosts;

    public DetailedPostAdapter(FragmentManager fm, ArrayList<InstagramPost> instagramPosts){
        super(fm);
        this.instagramPosts = instagramPosts;
    }

    @Override
    public Fragment getItem(int position) {
        DetailedPostFragment fragment = new DetailedPostFragment();
        InstagramPost post = instagramPosts.get(position);
        InstagramUser user = post.getInstagramUser();
        fragment.setImageURL(post.getImageURL());
        fragment.setPublishDate(post.getTimeStamp());
        fragment.setTag(post.getCompleteTags());
        fragment.setAuthor(user.getUserName());
        fragment.setProfileURL(user.getUrlProfile());
        return fragment;
    }

    @Override
    public int getCount() {
        return instagramPosts.size();
    }
}
