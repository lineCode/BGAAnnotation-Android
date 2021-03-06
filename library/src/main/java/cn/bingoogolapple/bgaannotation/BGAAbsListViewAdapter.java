package cn.bingoogolapple.bgaannotation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BGAAbsListViewAdapter<M, VH> extends BaseAdapter {
    protected Context mContext;
    protected List<M> mDatas;
    protected Class<VH> mViewHolderClazz;
    protected AbsListView mAbsListView;

    public BGAAbsListViewAdapter(AbsListView absListView, Class<VH> viewHolderClazz) {
        this(absListView, viewHolderClazz, null);
    }

    public BGAAbsListViewAdapter(AbsListView absListView, Class<VH> viewHolderClazz, List<M> datas) {
        mViewHolderClazz = viewHolderClazz;
        mAbsListView = absListView;
        mDatas = datas;
        mContext = absListView.getContext();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            VH viewHolder = null;
            try {
                viewHolder = mViewHolderClazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(mViewHolderClazz.getName() + " default constructor is not visible");
            }
            convertView = BGAA.injectView2ViewHolderOrFragment(viewHolder, mContext);
            convertView.setTag(viewHolder);
        }
        initViewHolder((VH) convertView.getTag(), mDatas.get(position), position);
        return convertView;
    }

    public void setDatas(List<M> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void reloadItem(int position) {
        if (mDatas != null) {
            getView(position, mAbsListView.getChildAt(position), mAbsListView);
        }
    }

    protected abstract void initViewHolder(VH viewHolder, M listItem, int position);

}