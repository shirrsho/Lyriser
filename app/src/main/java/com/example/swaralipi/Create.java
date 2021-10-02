package com.example.swaralipi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.view.View.GONE;

public class Create extends AppCompatActivity {

    String getData;
    String getTitle;
    String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);


        EditText etData = findViewById(R.id.etPostViewData);
        EditText etTitle = findViewById(R.id.etPostViewTitle);
        TextView tvTitle = findViewById(R.id.tvPostViewTitle);
        TextView tvData = findViewById(R.id.tvPostViewData);

        ImageFilterButton savebtn = findViewById(R.id.savebtn);
        ImageFilterButton attachbtn = findViewById(R.id.btn_attach);
        ImageFilterButton editbtn = findViewById(R.id.editbtn);

        savebtn.setVisibility(View.VISIBLE);
        editbtn.setVisibility(GONE);
        attachbtn.setVisibility(GONE);
        etTitle.setX(tvTitle.getX()); etTitle.setY(tvTitle.getY());
        etData.setX(tvData.getX()); etData.setY(tvData.getY());
        tvTitle.setVisibility(GONE);
        tvData.setVisibility(GONE);
        etTitle.setVisibility(View.VISIBLE);
        etData.setVisibility(View.VISIBLE);

        savebtn.setOnClickListener(e -> {

            getTitle = etTitle.getText().toString();
            getData = etData.getText().toString();
            getId = newEntry();

            Intent i = new Intent(this,PostView.class);
            i.putExtra("data",getData);
            i.putExtra("id",getId);
            i.putExtra("title",getTitle);
            this.finishActivity(0);
            startActivity(i);
        });
    }

    private String newEntry(){

        DatabaseReference databasePosts;
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");
        String id = null;

        if(getTitle.length()!=0){

            id = databasePosts.push().getKey();
            Post post = new Post(id,id,getTitle,getData);

            assert id != null;
            databasePosts.child(id).setValue(post);

            Toast.makeText(getApplicationContext(),"Pushed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Title mandatory", Toast.LENGTH_SHORT).show();
        }
        return id;
    }
}