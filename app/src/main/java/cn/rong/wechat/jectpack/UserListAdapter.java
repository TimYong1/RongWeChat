package cn.rong.wechat.jectpack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.rong.wechat.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {

    List<Userinfo> userinfos;

    public UserListAdapter(List<Userinfo> userinfos) {
        this.userinfos = userinfos;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
         holder.userid.setText(userinfos.get(position).getId());
         holder.userName.setText(userinfos.get(position).getUserName());
         holder.userAge.setText(userinfos.get(position).getUserAge());
    }

    @Override
    public int getItemCount() {
        return userinfos.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView userid;
        private TextView userName;
        private TextView userAge;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            userid = itemView.findViewById(R.id.user_id);
            userName = itemView.findViewById(R.id.user_name);
            userAge = itemView.findViewById(R.id.user_age);
        }
    }
}
