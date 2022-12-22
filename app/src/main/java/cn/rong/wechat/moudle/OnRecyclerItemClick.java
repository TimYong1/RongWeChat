package cn.rong.wechat.moudle;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnRecyclerItemClick {
    void onRecyclerViewClick(RecyclerView parent, View view,int postion);
}
