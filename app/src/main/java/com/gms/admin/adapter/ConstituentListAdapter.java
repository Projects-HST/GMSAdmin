package com.gms.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gms.admin.R;
import com.gms.admin.bean.support.Grievance;
import com.gms.admin.bean.support.User;
import com.gms.admin.utils.GMSConstants;
import com.gms.admin.utils.GMSValidator;
import com.gms.admin.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ConstituentListAdapter extends RecyclerView.Adapter<ConstituentListAdapter.MyViewHolder> {

    private ArrayList<User> users;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtUserName, txtUserSerial, txtUserDOB;
        private ImageView userImage;
        private RelativeLayout userLayout;

        public MyViewHolder(View view) {
            super(view);
            userLayout = (RelativeLayout) view.findViewById(R.id.user_layout);
            userLayout.setOnClickListener(this);
            txtUserName = (TextView) view.findViewById(R.id.user_name);
            txtUserSerial = (TextView) view.findViewById(R.id.user_serial);
            txtUserDOB = (TextView) view.findViewById(R.id.user_dob);
            userImage = (ImageView) view.findViewById(R.id.user_image);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public ConstituentListAdapter(ArrayList<User> users, OnItemClickListener onItemClickListener) {
        this.users = users;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ConstituentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);

        return new ConstituentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConstituentListAdapter.MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtUserName.setText(users.get(position).getfull_name());
        holder.txtUserSerial.setText(users.get(position).getmobile_no());
        holder.txtUserDOB.setText("Serial Number -" +users.get(position).getSerial_no());

        String urrl = PreferenceStorage.getClientUrl(holder.userImage.getContext()) + GMSConstants.KEY_PIC_URL + users.get(position).getprofile_picture();

        if (GMSValidator.checkNullString(users.get(position).getprofile_picture())) {
            Picasso.get().load(urrl).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_profile);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}