package com.gms.admin.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gms.admin.R;
import com.gms.admin.customview.StoryModel;
import com.gms.admin.customview.StoryView;

import java.util.ArrayList;

public class SampleClassasa extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<StoryModel> uris = new ArrayList<>();
        uris.add(new StoryModel("https://happysanz.in/gms/assets/constituent/1587964241.jpg", "Raj Kumar", "12:00 PM"));
        uris.add(new StoryModel("https://happysanz.in/gms/assets/constituent/1587966264.png", "Super Man", "01:00 AM"));
        uris.add(new StoryModel("https://happysanz.in/gms/assets/constituent/1588048261.jpg", "Kamal", "Yesterday"));
        uris.add(new StoryModel("https://happysanz.in/gms/assets/constituent/1593515596.jpg", "Bala", "10:15 PM"));
        uris.add(new StoryModel("https://happysanz.in/gms/assets/constituent/1596015718.jpg", "Ganesh", "10:15 PM"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spl_class);
        StoryView storyView = findViewById(R.id.storyView);
        storyView.resetStoryVisits();
        storyView.setImageUris(uris);

    }
}