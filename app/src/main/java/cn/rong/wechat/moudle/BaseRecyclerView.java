package cn.rong.wechat.moudle;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseRecyclerView extends RecyclerView.Adapter<BaseRecyclerView.BaseViewHodler> {
    @NonNull
    @Override
    public BaseViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHodler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class BaseViewHodler extends RecyclerView.ViewHolder {
        public BaseViewHodler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
