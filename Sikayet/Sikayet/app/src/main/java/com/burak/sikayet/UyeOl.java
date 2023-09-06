package com.burak.sikayet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UyeOl extends AppCompatActivity {

    //Firebase kullanıcı işlemleri için mAuth adında bir değişken tanımlıyoruz...
    private FirebaseAuth mAuth;

    //XML dosyasında kullanılan butonlar için değişkenleri tanımlıyoruz...
    private Button btnUyeOl;

    private ImageButton btnUyeOlGiris;

    //XML dosyasında kullanılan editTextler için değişkenleri tanımlıyoruz...
    private EditText etUyeOlMail, etUyeOlPass;

    //Metinsel ifadelerde kullanacağımız String türleri için değişkenleri tanımlıyoruz...
    private String mail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);

        mAuth = FirebaseAuth.getInstance();


        btnUyeOl = findViewById(R.id.btnUyeOl);
        btnUyeOlGiris = findViewById(R.id.btnUyeOlGiris);


        etUyeOlMail = findViewById(R.id.etUyeOlMail);
        etUyeOlPass = findViewById(R.id.etUyeOlPass);

        btnUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etUyeOlMail.getText().toString() != "" && etUyeOlMail.getText().toString().contains("@") && etUyeOlPass.getText().toString() != "") {

                    mail = etUyeOlMail.getText().toString();
                    pass = etUyeOlPass.getText().toString();

                    mAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(UyeOl.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(UyeOl.this, "Kayıt Başarılı...", Toast.LENGTH_LONG).show();

                                        //Ana ekrana yönlendirilecek...
                                        startActivity(new Intent(getApplicationContext(),AnaEkran.class));
                                        finish();

                                    } else {

                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(UyeOl.this, "Beklenmedik bir hata oluştu.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                } else {
                    if (etUyeOlMail.getText().toString() == "") {
                        Toast.makeText(UyeOl.this, "E-Posta boş olamaz... Lütfen E-Posta adresinizi giriniz.", Toast.LENGTH_LONG).show();
                    } else if (!etUyeOlMail.getText().toString().contains("@")) {
                        Toast.makeText(UyeOl.this, "Lütfen geçerli bir E-Posta adresi giriniz.", Toast.LENGTH_LONG).show();
                    } else if (etUyeOlPass.getText().toString() == "") {
                        Toast.makeText(UyeOl.this, "Şifre boş olamaz... Lütfen şifrenizi giriniz.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnUyeOlGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),GirisEkrani.class));
                finish();
            }
        });
    }
}