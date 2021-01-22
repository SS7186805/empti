package com.empti.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.empti.app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ExpandableListView mExpandableListView;
    private int[] groupStatus;
    Boolean isActive = false;
    public String question[] = {"A", "B", "C", "D"};
    public String andswer[] = {"Answer a", "Answer b", "Answer c", "Answer d"};
    ArrayList<HashMap<String, String>> dataArr = new ArrayList<>();

    public ExpandableListViewAdapter(Context pContext, ExpandableListView pExpandableListView, ArrayList<HashMap<String, String>> dataArr) {
        mContext = pContext;
        this.dataArr = dataArr;
        mExpandableListView = pExpandableListView;
        groupStatus = new int[dataArr.size()];
        setListEvent();
    }

    private void setListEvent() {
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int arg0) {
                groupStatus[arg0] = 1;
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int arg0) {
                groupStatus[arg0] = 0;
            }
        });
    }

    @Override
    public String getChild(int arg0, int arg1) {
        return dataArr.get(arg1).get("answer");
    }

    @Override
    public long getChildId(int arg0, int arg1) {
        return arg1;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean arg2, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expend_category_item, null);
            childHolder = new ChildHolder();
            childHolder.category_name = (TextView) convertView.findViewById(R.id.category_name);
            childHolder.main_layout = convertView.findViewById(R.id.main_layout);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.category_name.setText(dataArr.get(groupPosition).get("answer"));
        return convertView;
    }

    @Override
    public int getChildrenCount(int arg0) {
        return 1;
    }

    @Override
    public Object getGroup(int arg0) {
        return dataArr.get(arg0).get("question");
    }

    @Override
    public int getGroupCount() {
        return dataArr.size();
    }

    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean arg1, View view, ViewGroup parent) {
        GroupHolder groupHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.parent_view_of_layout, null);
            groupHolder = new GroupHolder();
            groupHolder.checkbox_image = (ImageView) view.findViewById(R.id.checkbox_image);
            groupHolder.service_name = (TextView) view.findViewById(R.id.category_name);
            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) view.getTag();
        }
        groupHolder.service_name.setText(dataArr.get(groupPosition).get("question"));
        if (arg1) {
            groupHolder.checkbox_image.setImageResource(R.drawable.minus);
        } else {
            groupHolder.checkbox_image.setImageResource(R.drawable.plus);
        }
        return view;
    }

    class GroupHolder {
        public TextView service_name;
        public ImageView checkbox_image;
        public RelativeLayout main_layout;
    }

    class ChildHolder {
        public TextView category_name;
        public RelativeLayout main_layout;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
}
