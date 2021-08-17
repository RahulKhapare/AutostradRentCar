package com.autostrad.rentcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.FragmentBookingBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private Context context;
    private FragmentBookingBinding binding;
    private UpcomingReservationFragment upcomingReservationFragment = UpcomingReservationFragment.newInstance();
    private PastRentalFragment pastRentalFragment = PastRentalFragment.newInstance();
    private CancelBookingFragment cancelBookingFragment = CancelBookingFragment.newInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        if (binding.getRoot() != null)
        {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null)
            {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    private void initView(){
        setUpViewPager();
        setupTabIcons();
    }

    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.activity_customt_child_tab, null);
        ((TextView) view1.findViewById(R.id.text)).setText(getResources().getString(R.string.upcomingReservation));
        ((TextView) view1.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.lightBlue));

        View view2 = LayoutInflater.from(context).inflate(R.layout.activity_customt_child_tab, null);
        ((TextView) view2.findViewById(R.id.text)).setText(getResources().getString(R.string.pastRental));

        View view3 = LayoutInflater.from(context).inflate(R.layout.activity_customt_child_tab, null);
        ((TextView) view3.findViewById(R.id.text)).setText(getResources().getString(R.string.cancelled));

        binding.tabLayout.getTabAt(0).setCustomView(view1);
        binding.tabLayout.getTabAt(1).setCustomView(view2);
        binding.tabLayout.getTabAt(2).setCustomView(view3);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.lightBlue));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((TextView) view.findViewById(R.id.text)).setTextColor(getResources().getColor(R.color.textDark));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager() {
        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(upcomingReservationFragment,"");
        adapter.addFragment(pastRentalFragment, "");
        adapter.addFragment(cancelBookingFragment, "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static BookingFragment newInstance() {
        BookingFragment fragment = new BookingFragment();
        return fragment;
    }

}
