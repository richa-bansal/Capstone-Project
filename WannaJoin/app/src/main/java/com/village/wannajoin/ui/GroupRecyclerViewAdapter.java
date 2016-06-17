package com.village.wannajoin.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Group;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by richa on 6/15/16.
 */
public class GroupRecyclerViewAdapter extends FirebaseRecyclerAdapter<Group, GroupRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    public GroupRecyclerViewAdapter(Class<Group> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass, Query ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, Group group, int position) {
        viewHolder.groupName.setText(group.getName());
        if (group.getGroupPhotoUrl() == null) {
            viewHolder.groupImageView
                    .setImageDrawable(ContextCompat
                            .getDrawable(mContext,
                                    R.drawable.ic_account_circle_black_48dp));
        } else {
            Glide.with(mContext)
                    .load(group.getGroupPhotoUrl())
                    .into(viewHolder.groupImageView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView groupImageView;
        public TextView groupName;
        public ViewHolder(View view) {
            super(view);
            groupImageView = (CircleImageView) view.findViewById(R.id.group_image_view);
            groupName = (TextView) view.findViewById(R.id.group_name);
        }
    }
}
