package com.gms.admin.customview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoryPlayer extends AppCompatActivity implements StoryPlayerProgressView.StoryPlayerListener {
    public static final String STORY_IMAGE_KEY = "storyImages";
    StoryPlayerProgressView storyPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    ArrayList<StoryModel> stories;
    StoryPreference storyPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_player);
        storyPlayerProgressView = findViewById(R.id.progressBarView);
        name = findViewById(R.id.storyUserName);
        time = findViewById(R.id.storyTime);
        storyPlayerProgressView.setSingleStoryDisplayTime(2000);
        imageView = findViewById(R.id.storyImage);
        storyPreference = new StoryPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            stories = intent.getParcelableArrayListExtra(STORY_IMAGE_KEY);
            initStoryProgressView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        storyPlayerProgressView.cancelAnimation();
    }

    private void initStoryProgressView() {
        if (stories != null && stories.size() > 0) {
            storyPlayerProgressView.setStoryPlayerListener(this);
            storyPlayerProgressView.setProgressBarsCount(stories.size());
            setTouchListener();
        }
    }

    private void setTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //pause
                    storyPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //resume
                    storyPlayerProgressView.resumeProgress();
                    return true;
                }else {
                    return false;
                }
            }
        });
    }


    @Override
    public void onStartedPlaying(int index) {
        loadImage(index);
        name.setText(stories.get(index).name);
        time.setText(stories.get(index).time);
        storyPreference.setStoryVisited(stories.get(index).imageUri);
    }

    @Override
    public void onFinishedPlaying() {
        finish();
    }

    private void loadImage(int index) {
        Picasso.get()
                .load(stories.get(index).imageUri)
                .into(imageView);
    }
}