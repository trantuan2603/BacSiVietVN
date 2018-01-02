package vn.bacsiviet.bacsivietvn.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.bacsiviet.bacsivietvn.R;

/**
 * Created by Administrator on 12/12/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView messageTextView;
    public ImageView messageImageView;
    public TextView messengerTextView;
    public CircleImageView messengerImageView;


    public MessageViewHolder(View itemView) {
        super(itemView);
        messageTextView =  itemView.findViewById(R.id.messageTextView);
        messageImageView =  itemView.findViewById(R.id.messageImageView);
        messengerTextView =  itemView.findViewById(R.id.messengerTextView);
        messengerImageView =  itemView.findViewById(R.id.messengerImageView);
    }
}
