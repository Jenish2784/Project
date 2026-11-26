package com.example.dotechuser.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dotechuser.Fragment.HospitalAboutFragment;
import com.example.dotechuser.Fragment.HospitalDepartmentsFragment;

public class HospitalFragmentAdapter extends FragmentPagerAdapter {
    public HospitalFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HospitalAboutFragment();
            case 1:
                return new HospitalDepartmentsFragment();
            default:
                return new HospitalAboutFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if (position == 0) {
            title = "About";
        }
        if (position == 1) {
            title = "Departments";
        }
        return title;
    }
}
