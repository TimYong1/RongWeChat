package cn.rong.wechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

import cn.rong.wechat.R;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesHolder> {

    List<Message> messages;
    public MessageAdapter(List<Message> messagesList){
      this.messages = messagesList;
    }
    @NonNull
    @Override
    public MessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,parent,false);
        MessagesHolder messagesHolder = new MessagesHolder(view);
        return messagesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesHolder holder, int position) {
        holder.textView.setText(((TextMessage) (messages.get(position).getContent())).getContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    class MessagesHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public MessagesHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.message_content);
        }
    }
}
