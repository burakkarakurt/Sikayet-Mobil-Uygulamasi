package com.burak.sikayet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnaEkran extends AppCompatActivity {

    private List<sikayet_adapter> modelList;
    private ListView listView;
    String[] yerler;
    TextView txtAnaEkranAlert;
    String userId;

    FirebaseAuth mAuth;

    FirebaseUser mUser;

    //Firabase veritabanı bağlantısı için değişkenimizi tanımlıyoruz...
    FirebaseDatabase database;

    //Veritabanı referansını tanımlıyoruz...
    DatabaseReference mySikayetRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);
        yerler = getResources().getStringArray(R.array.yerler);
        txtAnaEkranAlert = findViewById(R.id.txtAnaEkranAlert);

        listView = findViewById(R.id.lstSikayet);
        modelList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();

        userId = mUser.getUid();

        database = FirebaseDatabase.getInstance();

        // Veritabanından erişim sağlamak istediğimiz bölümü referansımıza atıyoruz...
        mySikayetRef = database.getReference("sikayetler");
        progressDialog = new ProgressDialog(AnaEkran.this);
        progressDialog.setMessage("Kontrol Ediliyor... Lütfen bekleyiniz...");
        progressDialog.show();
        mySikayetRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList = new ArrayList<>();

                if (snapshot.getChildrenCount() == 0) {
                    txtAnaEkranAlert.setVisibility(View.VISIBLE);
                }

                for(DataSnapshot data : snapshot.getChildren()){
                    String id=data.getKey();
                    String yeri ="";
                    String c_time ="";
                    if (data.child("sikayet_yeri").getValue() != null) {
                        txtAnaEkranAlert.setVisibility(View.GONE);
                        yeri =data.child("sikayet_yeri").getValue().toString();

                        yeri = yerler[Integer.parseInt(yeri)];
                    }

                    if (data.child("sikayet_c_time").getValue() != null) {
                        c_time =data.child("sikayet_c_time").getValue().toString();
                    }


                    sikayet_adapter model = new sikayet_adapter();

                    model.setmId(id);
                    model.setmYer(yeri);
                    model.setmCTime(c_time);

                    modelList.add(model);
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnaEkran.this, "Beklenmedik bir hata oluştu...", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getApplicationContext(), SikayetOlustur.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), ProfilGuncelle.class));
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAdapter() {



        sikayet_list_adapter adapter = new sikayet_list_adapter(getApplicationContext(), modelList);
        listView.setAdapter(adapter);

        progressDialog.dismiss();
    }
}