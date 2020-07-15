package com.gms.admin.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gms.admin.fragment.DocsConstituentFragment;
import com.gms.admin.fragment.DocsGrievanceFragment;

public class DocumentFragmentAdapter extends FragmentStatePagerAdapter {

    public DocumentFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DocsConstituentFragment();
            case 1:
                return new DocsGrievanceFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}