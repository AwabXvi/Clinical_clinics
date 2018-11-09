package com.bahri.crimnal.clinical_clinics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private  final  String TAG = "RecyclerViewAdapter";
    private  ArrayList <String> mName = new ArrayList<  >();
    private  ArrayList <Boolean> mAcceptence = new ArrayList<>();
     private  ArrayList <String> mImage = new ArrayList<>();
    private  ArrayList <String> mPhone = new ArrayList<>();
    private  ArrayList <String> mDoctor = new ArrayList<>();
    private  ArrayList <String> mID = new ArrayList<>();
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CollectionReference ref;


    public RecyclerViewAdapter(ArrayList<String> Name,ArrayList<Boolean> Aceeptence,ArrayList <String> id ,ArrayList<String> Image, ArrayList<String> Phone, ArrayList<String> Doctor, Context Context) {
        mName = Name;
        mImage = Image;
        mPhone = Phone;
        mDoctor = Doctor;
        mContext = Context;
        mAcceptence = Aceeptence;
        mID = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list,viewGroup ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d(TAG , "OnBindViewHolder: called. ");
        Glide.with(mContext)
                .asBitmap()
                .load(mImage.get(i))
                .into(viewHolder.img);

        viewHolder.name_txt.setText(mName.get(i));
        viewHolder.doctor_txt.setText(mDoctor.get(i));
        viewHolder.phone_txt.setText(mPhone.get(i));
        if(mAcceptence.get(i) != null && mAcceptence.get(i)){
            viewHolder.accept.setBackgroundColor(Color.GRAY);
            viewHolder.accept.setText("ACCEPTED");
            viewHolder.accept.setClickable(false);
        }
        final String id = mID.get(i);
         final  Map<String , Object> map = new HashMap<>();
        map.put("name" , mName.get(i));
        map.put("phone", mPhone.get(i));
        map.put("doctor", mDoctor.get(i));
        map.put("clinic_name", user.getDisplayName());

        viewHolder.accept.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ref.document(id).update("acceptence", true).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        db.collection("Accepted_reservation").document().set(map).addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }
                                        );
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "update failed ", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                        viewHolder.accept.setBackgroundColor(Color.GRAY);
                        viewHolder.accept.setText("ACCEPTED");
                        viewHolder.accept.setClickable(false);

                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name_txt , phone_txt , doctor_txt;
        Button accept ;
        RelativeLayout relativeLayout ;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_view);
            name_txt = itemView.findViewById(R.id.res_name);
            phone_txt = itemView.findViewById(R.id.res_phone);
            doctor_txt = itemView.findViewById(R.id.res_doctor);
            accept = itemView.findViewById(R.id.btn_accept);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            ref = db.collection("Reservation");


        }
    }
}
