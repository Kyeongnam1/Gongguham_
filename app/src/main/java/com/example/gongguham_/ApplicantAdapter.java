package com.example.gongguham_;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ViewHolder> {

    private ArrayList<Applicant> ApplicantList;
    private Context mContext;
    String dbTitle;
    String userName;
    String tmCheck;
    String check;
    public ApplicantAdapter(Context context, ArrayList<Applicant> applicantList, String dbTitle, String check){
        this.mContext = context;
        this.ApplicantList = applicantList;
        this.dbTitle = dbTitle;
        this.check = check;
    }

    @NonNull
    @Override
    public ApplicantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ApplicantAdapter.ViewHolder holder, int position) {
        holder.onBind(ApplicantList.get(position));
    }

    public void setApplicantList(ArrayList<Applicant> list){
        this.ApplicantList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return ApplicantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView applicantName, applicantPosition, accountNumber, bankName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = (TextView) itemView.findViewById(R.id.applicant_name_text);
            applicantPosition = (TextView) itemView.findViewById(R.id.applicant_position_text);
            accountNumber = (TextView) itemView.findViewById(R.id.account_number_text);
            bankName = (TextView) itemView.findViewById(R.id.bank_name_text);
            checkBox = (CheckBox) itemView.findViewById(R.id.complited_check_box);

        }
        public TextView getApplicantName(String name){
            return (TextView) itemView.findViewById(R.id.applicant_name_text);
        }
        void onBind(Applicant applicant){
            applicantName.setText(applicant.applicantName);
            applicantPosition.setText((applicant.applicantPosition));
            accountNumber.setText((applicant.accountNumber));
            bankName.setText((applicant.bankName));
            if(check.equals("true"))
            {
                checkBox.setChecked(true);
            }


            if(applicantPosition.getText().toString().equals("글쓴이"))
            {
                checkBox.setVisibility(View.INVISIBLE);
            }
            else
                checkBox.setVisibility(View.VISIBLE);
            //체크박스 클릭시
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(applicantName.getText().toString());
                    documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    if (document.exists()) {
                                        userName = document.getData().get("name").toString();
                                        tmCheck = userName+"tmCheck";
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("posts").document(dbTitle)
                                                .update(tmCheck, "true")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Adapter check", "Success");
                                                    }

                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("Adapter check", "push menu in db fail" + e);
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });

                }
            });

       }




    }

}
