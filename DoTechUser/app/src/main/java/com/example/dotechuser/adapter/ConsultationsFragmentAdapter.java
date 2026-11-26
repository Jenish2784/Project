package com.example.dotechuser.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dotechuser.Fragment.CompleteFragment;
import com.example.dotechuser.Fragment.InCompleteFragment;
import com.example.dotechuser.Fragment.PaymentFailedFragment;

public class ConsultationsFragmentAdapter extends FragmentPagerAdapter {
    public ConsultationsFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InCompleteFragment();
            case 1:
                return new CompleteFragment();
            case 2:
                return new PaymentFailedFragment();
            default:
                return new CompleteFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public
    CharSequence getPageTitle(int position) {

        String title = null;
        if (position == 0){
            title = "INCOMPLETE";
        }
        if (position == 1){
            title = "COMPLETE";
        }
        if (position == 2){
            title = "PAYMENT FAILED";
        }
        return title;
    }

}
