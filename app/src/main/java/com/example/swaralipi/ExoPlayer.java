package com.example.swaralipi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class ExoPlayer extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        url = getIntent().getStringExtra("url");

        SimpleExoPlayer exoPlayer = new SimpleExoPlayer.Builder(this).build();

        PlayerView playerView = findViewById(R.id.exoPlayer);

        Uri uri = Uri.parse(url);

        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(uri);

        exoPlayer.addMediaItem(mediaItem);

        exoPlayer.prepare();

        exoPlayer.play();
    }
}