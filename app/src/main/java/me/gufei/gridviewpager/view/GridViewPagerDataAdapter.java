package me.gufei.gridviewpager.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class GridViewPagerDataAdapter<T> {
    public List listAll;

    public GridViewPagerDataAdapter(List<T> listAll) {
        this.listAll = listAll;
    }

    public abstract BaseAdapter getGridViewAdapter(List<T> currentList, int pageIndex);

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id, int pageIndex);

    public void onPageSelected(int position) {

    }
}
