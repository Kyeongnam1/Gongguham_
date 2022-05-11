package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class AddPostItem extends AppCompatActivity {

    private FirebaseUser user;
    private Button btnClose;
    private EditText postTitle,postContent, postMeetingArea, postCloseTime, postMaxPerson;
    private static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_item);

        Spinner hourSpinner = (Spinner)findViewById(R.id.spinner_add_post_close_time_hour);
        ArrayAdapter hourAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_hour, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        Spinner minuteSpinner = (Spinner)findViewById(R.id.spinner_add_post_close_time_minute);
        ArrayAdapter minuteAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_minute, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        Spinner personSpinner = (Spinner)findViewById(R.id.spinner_add_post_max_person);
        ArrayAdapter personAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_person, android.R.layout.simple_spinner_item);
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSpinner.setAdapter(personAdapter);

        btnClose = (Button) findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
                startMainActivity();
            }
        });
    }


    private void addPost(){
        postTitle = (EditText) findViewById(R.id.add_post_title);
        String title = postTitle.getText().toString();
        postContent = (EditText) findViewById(R.id.add_post_content);
        String content = postTitle.getText().toString();
        postMeetingArea = (EditText) findViewById(R.id.add_post_meeting_area);
        String meetingArea = postMeetingArea.getText().toString();
       // postCloseTime = (EditText) findViewById(R.id.add_post_close_time);
       // String closeTime = postCloseTime.getText().toString();
      //  postMaxPerson = (EditText) findViewById(R.id.add_post_max_person);
    //    int maxPerson = Integer.parseInt(postMaxPerson.getText().toString());

        Spinner hourS = (Spinner) findViewById(R.id.spinner_add_post_close_time_hour);
        String closeTime_hour = hourS.getSelectedItem().toString();

        Spinner minuteS = (Spinner) findViewById(R.id.spinner_add_post_close_time_minute);
        String closeTime_minute = minuteS.getSelectedItem().toString();

        Spinner personS = (Spinner) findViewById(R.id.spinner_add_post_max_person);
        String maxPerson = personS.getSelectedItem().toString();

        if(title.length()>0 && content.length()>0 && meetingArea.length()>0 && closeTime_hour.length()>0 && closeTime_minute.length()>0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            PostInfo postInfo = new PostInfo(title, content, meetingArea, closeTime_hour, closeTime_minute, maxPerson, user.getProviderId());
            uploader(postInfo);
        }
    }

    private void uploader(PostInfo postInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts").add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddPost Activity", "DocumentSnapShot" + documentReference);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AddPost Activity", "Error adding post" + e);

                    }
                });
    }


    private  void startMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}