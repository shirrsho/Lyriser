package com.example.swaralipi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    Context context;
    List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_card,parent,false);
        PostViewHolder pv = new PostViewHolder(view);

        pv.itemView.setOnClickListener(e -> {
            Toast.makeText(context,pv.datasub.getText(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(view.getContext(),PostView.class);

            for ( Post post : postList) {

                if(post.getmatchId().contentEquals(pv.idMatch.getText())){
                    i.putExtra("data",post.getData());
                    i.putExtra("id",post.getmatchId());
                    i.putExtra("title",post.getTitle());
                    break;
                }

            }
            context.startActivity(i);
        });
        return pv;
    }

    private static void showPopUpMenu(View view, String id) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_main);

        popupMenu.setOnMenuItemClickListener(e -> {

            switch (e.getItemId()){
                case R.id.deletebtn :
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(id);
                    databaseReference.removeValue();
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.datasub.setText(post.getData());
        holder.idMatch.setText(post.getmatchId());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView datasub;
        TextView idMatch;
        ImageFilterButton popmenu;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvFileTitle);
            datasub = itemView.findViewById(R.id.tvDataSub);
            idMatch = itemView.findViewById(R.id.tvMatchfile);
            popmenu = itemView.findViewById(R.id.filepopmenubtn);

            popmenu.setOnClickListener(e -> {
                showPopUpMenu(itemView,idMatch.getText().toString());
            });
        }
    }
}
