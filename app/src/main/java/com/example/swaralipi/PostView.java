package com.example.swaralipi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.view.View.GONE;

public class PostView extends AppCompatActivity implements DialogFileName.DialogListener {

    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    Uri fileUri;
    ImageFilterButton uploadbtn;
    TextView fileTitle;

    ArrayList<File> fileArrayList;
    FileAdapter fileAdapter;
    RecyclerView recyclerView;

    String putId;

    private FileAdapter.fileRVClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        String getData = getIntent().getStringExtra("data");
        String getTitle = getIntent().getStringExtra("title");
        String getId = getIntent().getStringExtra("id");

        TextView data = findViewById(R.id.tvPostViewData);
        TextView title = findViewById(R.id.tvPostViewTitle);

        title.setText(getTitle);
        data.setText(getData);

        EditText etData = (EditText) findViewById(R.id.etPostViewData);
        EditText etTitle = (EditText) findViewById(R.id.etPostViewTitle);
        
        ImageFilterButton editbtn = findViewById(R.id.editbtn);
        ImageFilterButton savebtn = findViewById(R.id.savebtn);
        ImageFilterButton attachbtn = findViewById(R.id.btn_attach);
        FloatingActionButton attachments_btn = findViewById(R.id.attachments_btn);
        
        uploadbtn = findViewById(R.id.uploadbtn);
        fileTitle = findViewById(R.id.fileTitle);
        
        ProgressBar progressBar = findViewById(R.id.progressBar);

        firebaseStorage = FirebaseStorage.getInstance();

        attachbtn.setVisibility(View.VISIBLE);
        editbtn.setVisibility(View.VISIBLE);


        editbtn.setOnClickListener(e -> {
            //savebtn.setX(editbtn.getX()); savebtn.setY(editbtn.getY());
            editbtn.setVisibility(View.GONE);
            savebtn.setVisibility(View.VISIBLE);

            //data.setX(etData.getX()); data.setY(etData.getY());
            //etData.setX(data.getX()); etData.setY(data.getY());
            etData.setText(data.getText().toString());
            data.setVisibility(View.GONE);
            etData.setVisibility(View.VISIBLE);

            //etTitle.setX(title.getX()); etTitle.setY(title.getY());
            etTitle.setText(title.getText().toString());
            title.setVisibility(View.GONE);
            etTitle.setVisibility(View.VISIBLE);
            etTitle.requestFocus();
            etTitle.selectAll();
            System.out.println("jkjeijeidjejdjdjdjajjsdjsdjsdsjsdjsdj");
        });

        savebtn.setOnClickListener(e -> {
            //Post post = new Post(id,id,title.getText().toString(),data.getText().toString());
            if(etTitle.getText().toString().length()==0){
                Toast.makeText(PostView.this,"Title Mandatory",Toast.LENGTH_SHORT).show();
            }
            else {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");
                databaseReference.child(getId).setValue(new Post(getId, getId, etTitle.getText().toString(), etData.getText().toString()));
                savebtn.setVisibility(View.GONE);
                editbtn.setVisibility(View.VISIBLE);
                data.setText(etData.getText());
                title.setText(etTitle.getText());
                etData.setVisibility(GONE);
                data.setVisibility(View.VISIBLE);
                Toast.makeText(PostView.this,"Edited",Toast.LENGTH_SHORT).show();
            }
        });

        attachments_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   //recyclerView = findViewById(R.id.rvB);


                   BottomSheetDialog bottomSheetDialog = new
                           BottomSheetDialog(PostView.this, R.style.BottomSheetDialogTheme);
                   View bottomSheetView = LayoutInflater.from(getApplicationContext())
                           .inflate(
                                   R.layout.attachments_bottom_sheet,
                                   findViewById(R.id.bottomSheetContainer)
                           );
                   bottomSheetDialog.setContentView(bottomSheetView);
                   bottomSheetDialog.show();

                   recyclerView = bottomSheetDialog.findViewById(R.id.rvB);

                   databaseReference = FirebaseDatabase.getInstance().getReference().child("files");
                   fileArrayList = new ArrayList<>();
                   fileRVclickListener();
                   fileAdapter = new FileAdapter(getApplicationContext(), fileArrayList,listener);
                   recyclerView.setLayoutManager(new LinearLayoutManager(PostView.this));
                   recyclerView.setAdapter(fileAdapter);

                   databaseReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           fileArrayList.clear();
                           for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                               File file = dataSnapshot.getValue(File.class);
                               if (!file.getPostId().equals(getId)) continue;
                               fileArrayList.add(file);
                               fileAdapter.notifyDataSetChanged();
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
           });

        attachbtn.setOnClickListener(e -> {
            mGetContent.launch("*/*");
        });

        uploadbtn.setOnClickListener(e -> {
            putId = databaseReference.push().getKey();
            if(fileUri!=null){
                StorageReference storageReference = firebaseStorage.getReference().child(putId);
                storageReference.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(GONE);
                            attachbtn.setVisibility(View.VISIBLE);
                            uploadbtn.setVisibility(GONE);
                            fileTitle.setVisibility(GONE);
                            fileUri = null;
                            Toast.makeText(PostView.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(PostView.this,"Not",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        uploadbtn.setVisibility(View.INVISIBLE);
                        attachbtn.setVisibility(View.INVISIBLE);
                        //progressBar.setX(attachbtn.getX()); progressBar.setY(progressBar.getY());
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("files");
                        String fileId = putId;
                        databaseReference.child(fileId).setValue(
                                new File(fileId,fileId,getId,fileTitle.getText().toString(),url.toString()));
                    }
                });
            }
        });
    }

    private void fileRVclickListener() {
        listener = new FileAdapter.fileRVClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(),ExoPlayer.class);
                i.putExtra("url",fileArrayList.get(position).getUrl());
                startActivity(i);
            }
        };
    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent()
            , new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result!=null){
                        fileUri = result;
                        uploadbtn.setVisibility(View.VISIBLE);
                        //fileTitle.setText(fileUri.getLastPathSegment());

                        DialogFileName dialogFileName = new DialogFileName(fileUri.getLastPathSegment());
                        dialogFileName.show(getSupportFragmentManager(),"chilled dialog");

                        fileTitle.setVisibility(View.VISIBLE);
                        result = null;
                    }
                }
    });

    @Override
    public void applyTexts(String fileName) {
        fileTitle.setText(fileName);
    }
}