package cn.rong.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import cn.rong.wechat.R;
import cn.rong.wechat.moudle.MoudlesDataBean;
import cn.rong.wechat.moudle.OnRecyclerItemClick;

public class AllMoudleAdadpter extends RecyclerView.Adapter<AllMoudleAdadpter.AllMoudleHolder> implements View.OnClickListener{

    private List<MoudlesDataBean> moudlesDataBeans ;
    private RecyclerView rvParent;
    private OnRecyclerItemClick onRecyclerItemClick;
    public AllMoudleAdadpter(List<MoudlesDataBean> moudles) {
        this.moudlesDataBeans = moudles;
    }

    @NonNull
    @Override
    public AllMoudleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.rvParent = (RecyclerView) parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moudles_item,parent,false);
        AllMoudleHolder allMoudleHolder = new AllMoudleHolder(view);
        return allMoudleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllMoudleHolder holder, int position) {
        holder.mMouldeName.setText(moudlesDataBeans.get(position).getMoudleName());
        holder.mMouldeName.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return moudlesDataBeans.size();
    }

    @Override
    public void onClick(View v) {
        int postion = rvParent.getChildAdapterPosition(v);
        if (onRecyclerItemClick!=null){
            onRecyclerItemClick.onRecyclerViewClick(rvParent,v,postion);
        }
    }

    class AllMoudleHolder extends RecyclerView.ViewHolder {
        private MotionButton mMouldeName;
        public AllMoudleHolder(@NonNull View itemView) {
            super(itemView);
            mMouldeName = itemView.findViewById(R.id.item_name);
        }
    }
   public void setOnItemClick(OnRecyclerItemClick onRecyclerItemClick){
        this.onRecyclerItemClick = onRecyclerItemClick;
   }
}
