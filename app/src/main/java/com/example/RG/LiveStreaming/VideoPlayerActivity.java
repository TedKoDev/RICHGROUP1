package com.example.RG.LiveStreaming;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.RG.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    StyledPlayerView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getSupportActionBar().hide();


        playerView = findViewById(R.id.player_view);

        // 값받는_액티비티에서 사용
        Intent intent = getIntent(); // 이전 액티비티에서 보낸 intent 받기
        String getString = intent.getStringExtra("url");


        ExoPlayer player = new ExoPlayer.Builder(getApplicationContext()).build();

// Build the media item.
        MediaItem mediaItem = MediaItem.fromUri(getString);
// Set the media item to be played.
        playerView.setPlayer(player);



        player.setMediaItem(mediaItem);
// Prepare the player.
        player.prepare();
// Start the playback.
        player.play();





    }
}