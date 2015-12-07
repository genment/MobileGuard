package gmt.mobileguard.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import gmt.mobileguard.R;
import gmt.mobileguard.fragment.AppsListFragment;

public class AppManagerActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * FragmentPagerAdapter
     */
    public static class PagerAdapter extends FragmentPagerAdapter {
        Context mContext;
        Fragment[] fragments = new Fragment[2];

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null)
                fragments[position] = AppsListFragment.newInstance(position);
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case AppsListFragment.TYPE_USER_APP:
                    return mContext.getText(R.string.user_app);
                case AppsListFragment.TYPE_SYS_APP:
                    return mContext.getText(R.string.system_app);
            }
            return null;
        }
    }
}
