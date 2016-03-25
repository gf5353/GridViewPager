package me.gufei.gridviewpager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 10/30/15.
 */
public class GridViewPager extends ViewPager {
    private String TAG = "GridViewPager";
    private List<GridView> mLists = new ArrayList<>();
    private GridViewPagerAdapter adapter;
    private List listAll;
    private int rowInOnePage;
    private int columnInOnePage;
    private GridViewPagerDataAdapter gridViewPagerDataAdapter;
    private PageIndicatorView indicatorView;

    public GridViewPager(Context context) {
        super(context);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewPagerDataAdapter getGridViewPagerDataAdapter() {
        return gridViewPagerDataAdapter;
    }

    public void setColumns(int rowInOnePage, int columnInOnePage) {
        this.rowInOnePage = rowInOnePage;
        this.columnInOnePage = columnInOnePage;
        if (listAll != null && listAll.size() > 0) {
            init();
        }
    }

    public void setGridViewPagerDataAdapter(GridViewPagerDataAdapter gridViewPagerDataAdapter) {
        this.gridViewPagerDataAdapter = gridViewPagerDataAdapter;
        if (gridViewPagerDataAdapter.listAll == null || gridViewPagerDataAdapter.listAll.size() == 0) {
            return;
        }
        listAll = gridViewPagerDataAdapter.listAll;
        init();
        addOnPageChangeListener(onPageChangeListener);
    }

    public void setIndicator(PageIndicatorView indicatorView) {
        this.indicatorView = indicatorView;
    }

    public void init() {
        int sizeInOnePage = rowInOnePage * columnInOnePage;//一栏显示总数
        int pageCount = listAll.size() / sizeInOnePage;//划分页数
        Log.d(TAG, "setColumns:" + rowInOnePage + "||" + columnInOnePage);
        Log.d(TAG, "init:" + sizeInOnePage + "||" + pageCount + "页");
        pageCount += listAll.size() % sizeInOnePage == 0 ? 0 : 1;
        if (indicatorView != null) {
            indicatorView.initIndicator(pageCount);
        }
        int size = mLists.size();
        Log.d(TAG, "GridView size :" + size);
        if (size > pageCount) {
            for (int i = size - 1; i >= pageCount; i--) {
                mLists.remove(i);
            }
        }
        Log.d(TAG, "GridView size2 :" + size);
        WrapContentGridView gv;
        int end;
        for (int i = 0; i < pageCount; i++) {
            final int pageIndex = i;
            if (i < mLists.size()) {
                gv = (WrapContentGridView) mLists.get(i);
            } else {
                gv = new WrapContentGridView(getContext());
                gv.setGravity(Gravity.CENTER);
                gv.setClickable(true);
                gv.setFocusable(true);
                mLists.add(gv);
            }
            gv.setNumColumns(columnInOnePage);//动态改变
            end = Math.min((i + 1) * sizeInOnePage, listAll.size());
            gv.setAdapter(gridViewPagerDataAdapter.getGridViewAdapter(listAll.subList(i * sizeInOnePage, end), i));
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gridViewPagerDataAdapter.onItemClick(arg0, arg1, arg2, arg3, pageIndex);
                }
            });
        }
        if (adapter == null) {
            adapter = new GridViewPagerAdapter(getContext(), mLists);
            setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
            Log.d(TAG, "notifyDataSetChanged:");
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; mLists != null && i < mLists.size(); i++) {
            View child = mLists.get(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "position:" + position);
            if (indicatorView != null) {
                indicatorView.setSelectedPage(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
