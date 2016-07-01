package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by richa on 6/30/16.
 */
public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    String mActivityName;
    ArrayList<ContactAndGroup> contactAndGroupArrayList;


    public ContactsRecyclerViewAdapter(Context context, String activityName, ArrayList<ContactAndGroup> cgList) {
        mContext = context;
        mActivityName = activityName;
        contactAndGroupArrayList = cgList;

    }

    @Override
    public int getItemCount() {
        return contactAndGroupArrayList.size();
    }

    public ContactAndGroup getItem(int position) {
        return contactAndGroupArrayList.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType ==0){
            view = layoutInflater.inflate(R.layout.list_label_item, parent, false);
            LabelViewHolder vh = new LabelViewHolder(view);
            return vh;
        }else {
            view = layoutInflater.inflate(R.layout.contact_list_item, parent, false);
            ContactsRecyclerViewAdapter.ViewHolder vh = new ContactsRecyclerViewAdapter.ViewHolder(view);

            return vh;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType()==0){
            ContactsRecyclerViewAdapter.LabelViewHolder lvh = (ContactsRecyclerViewAdapter.LabelViewHolder)holder;
            lvh.label.setText(getItem(position).getName());
        }else {
            ContactsRecyclerViewAdapter.ViewHolder vh = (ContactsRecyclerViewAdapter.ViewHolder)holder;
            if (mActivityName.equals("ShareEventActivity"))
                vh.isSelected.setVisibility(View.VISIBLE);
            if (mActivityName.equals("MainActivity"))
                vh.isSelected.setVisibility(View.GONE);
            vh.contactName.setText(getItem(position).getName());
            if (getItem(position).getPhotoUrl() == null) {
                vh.contactImageView
                        .setImageDrawable(ContextCompat
                                .getDrawable(mContext,
                                        R.drawable.ic_account_circle_black_48dp));
            } else {
                Glide.with(mContext)
                        .load(getItem(position).getPhotoUrl())
                        .into(vh.contactImageView);
            }

            vh.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        contactAndGroupArrayList.get(position).setSelected(isChecked);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

   @Override
    public int getItemViewType(int position) {
        return contactAndGroupArrayList.get(position).getType();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView contactImageView;
        public TextView contactName;
        public CheckBox isSelected;
        public ViewHolder(View view) {
            super(view);
            contactImageView = (CircleImageView) view.findViewById(R.id.contact_image_view);
            contactName = (TextView) view.findViewById(R.id.contact_name);
            isSelected = (CheckBox) view.findViewById(R.id.select_box);
        }
    }

    public static class LabelViewHolder extends RecyclerView.ViewHolder{
        public TextView label;
        public LabelViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.name);
        }
    }
}

