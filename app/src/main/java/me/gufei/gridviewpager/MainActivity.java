package me.gufei.gridviewpager;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import me.gufei.gridviewpager.view.GridViewPager;
import me.gufei.gridviewpager.view.GridViewPagerDataAdapter;
import me.gufei.gridviewpager.view.PageIndicatorView;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private String[] mStrs = new String[]{"01", "02", "03", "04", "05", "06",
            "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};

    private GridViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG, "onCreate");
        final List<String> list = Arrays.asList(mStrs);
        mViewPager = (GridViewPager) findViewById(R.id.myviewpager);
        mViewPager.setIndicator((PageIndicatorView) findViewById(R.id.indicator));
        mViewPager.setColumns(2, 4);
        mViewPager.setGridViewPagerDataAdapter(new GridViewPagerDataAdapter<String>(list) {
            @Override
            public BaseAdapter getGridViewAdapter(List<String> currentList, int pageIndex) {
                return new MyGridViewAdapter(getApplicationContext(), currentList);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, int pageIndex) {

            }

        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "竖屏");
            if (mViewPager != null)
                mViewPager.setColumns(2, 4);
        } else {
            if (mViewPager != null)
                mViewPager.setColumns(1, 8);
            Log.d(TAG, "横屏");
        }
    }

    class MyGridViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mLists;

        public MyGridViewAdapter(Context pContext, List<String> mLists) {
            this.mContext = pContext;
            this.mLists = mLists;

        }

        @Override
        public int getCount() {

            return mLists.size();
        }

        @Override
        public Object getItem(int position) {

            return mLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Holder holder = null;
            if (null == convertView) {
                holder = new Holder();
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.gridview_item, null);
                holder.btn_gv_item = (Button) convertView.findViewById(R.id.btn_gv_item);
                holder.btn_gv_item.setFocusable(false);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.btn_gv_item.setText(mLists.get(position));

            return convertView;
        }

        private class Holder {
            Button btn_gv_item;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}