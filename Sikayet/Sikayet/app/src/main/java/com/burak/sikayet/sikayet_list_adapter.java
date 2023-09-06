package com.burak.sikayet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class sikayet_list_adapter extends BaseAdapter {

    Context mContext;
    List<sikayet_adapter> mAdapter;

    String userId;

    FirebaseAuth mAuth;

    FirebaseUser mUser;

    //Firabase veritabanı bağlantısı için değişkenimizi tanımlıyoruz...
    FirebaseDatabase database;

    //Veritabanı referansını tanımlıyoruz...
    DatabaseReference mySikayetRef;

    public sikayet_list_adapter(Context mContext, List<sikayet_adapter> mAdapter) {
        this.mContext = mContext;
        this.mAdapter = mAdapter;
    }

    @Override
    public int getCount() {
        return mAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.sikayet_list_item,null);

        TextView txtBaslik = v.findViewById(R.id.txtSikayetBaslik);
        TextView txtCTime = v.findViewById(R.id.txtSikayetCTime);

        Button btnDuzenle = v.findViewById(R.id.btnSikayetDuzenle);
        Button btnSil = v.findViewById(R.id.btnSikayetSil);

      v.setTag(mAdapter.get(position).getmYer());

        txtBaslik.setText(mAdapter.get(position).getmYer());
        txtCTime.setText(mAdapter.get(position).getmCTime().split(" ")[0] +"\n"+ mAdapter.get(position).getmCTime().split(" ")[1]);

        btnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(v.getRootView().getContext(),SikayetOlustur.class);
                i.putExtra("id",mAdapter.get(position).mId);
                mContext.startActivity(i);
            }
        });

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                mAuth = FirebaseAuth.getInstance();

                                mUser = mAuth.getCurrentUser();

                                userId = mUser.getUid();

                                database = FirebaseDatabase.getInstance();

                                // Veritabanından erişim sağlamak istediğimiz bölümü referansımıza atıyoruz...
                                mySikayetRef = database.getReference("sikayetler");

                                mySikayetRef.child(userId).child(mAdapter.get(position).mId).removeValue();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setMessage("Silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                        .setNegativeButton("Hayır", dialogClickListener).show();
            }
        });



        return v;
    }
}
