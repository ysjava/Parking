package com.demo.parking.ui;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.demo.parking.R;
import com.demo.parking.db.Collection;
import com.demo.parking.db.Comment;
import com.demo.parking.db.User;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by White paper on 2020/3/18
 * Describe :
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private List<Comment> dataList;
    public CommentAdapter(@NonNull Context context, List<Comment> list) {
        super(context, 0, list);
        dataList = list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Nullable
    @Override
    public Comment getItem(int position) {
        return dataList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Comment comment = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);

        TextView date = view.findViewById(R.id.tv_date);
        TextView content = view.findViewById(R.id.tv_content);
        TextView name = view.findViewById(R.id.tv_user_name);

        Comment c = LitePal.find(Comment.class,comment.getId(),true);
        date.setText(c.getDateStr());
        content.setText(comment.getContent());

        name.setText(c.getUser().getName());

        return view;

    }
}
