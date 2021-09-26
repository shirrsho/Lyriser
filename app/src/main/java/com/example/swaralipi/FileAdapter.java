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

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    Context context;
    List<File> fileList;

    private fileRVClickListener listener;

    public FileAdapter(Context context, List<File> fileList, fileRVClickListener listener) {
        this.context = context;
        this.fileList = fileList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_card,parent,false);
        FileAdapter.FileViewHolder fv = new FileViewHolder(view);
        return fv;
    }


    private static void showPopUpMenu(View view, String id) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_main);
        //popupMenu.setGravity(Gravity.CENTER);
        //DatabaseReference databaseReference;
        popupMenu.setOnMenuItemClickListener(e -> {
            switch (e.getItemId()){
                case R.id.deletebtn :
                    System.out.println("sadasda:"+id);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("files").child(id);
                    databaseReference.removeValue();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(id);
                    storageReference.delete();
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        File file = fileList.get(position);
        holder.fileTitle.setText(file.getName());
        holder.fileOrgId.setText(file.getId());
        holder.fileUri.setText(file.getUrl());
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fileTitle;
        TextView fileOrgId;
        TextView fileUri;
        ImageFilterButton popmenufile;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fileTitle = itemView.findViewById(R.id.tvFileTitle);
            fileOrgId = itemView.findViewById(R.id.tvMatchfile);
            popmenufile = itemView.findViewById(R.id.filepopmenubtn);
            fileUri = itemView.findViewById(R.id.fileUri);

            popmenufile.setOnClickListener(e->{
                showPopUpMenu(itemView,fileOrgId.getText().toString());
            });
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v,getBindingAdapterPosition());
        }
    }

    public interface fileRVClickListener{
        void onClick(View view, int position);
    }
}
