package com.gms.admin.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gms.admin.bean.support.Paguthi;
import com.gms.admin.fragment.DynamicConstituentFragment;

import java.util.ArrayList;

public class ConstituentPaguthiTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<Paguthi> paguthiArrayList;
    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    public ConstituentPaguthiTabAdapter(FragmentManager fm, int NumOfTabs, ArrayList<Paguthi> paguthis) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.paguthiArrayList = paguthis;
    }

    @Override
    public Fragment getItem(int position) {
        return DynamicConstituentFragment.newInstance(position, paguthiArrayList);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}