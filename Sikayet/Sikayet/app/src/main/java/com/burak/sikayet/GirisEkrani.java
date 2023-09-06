package com.burak.sikayet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisEkrani extends AppCompatActivity {

    //Firebase kullanıcı işlemleri için mAuth adında bir değişken tanımlıyoruz...
    private FirebaseAuth mAuth;

    //XML dosyasında kullanılan butonlar için değişkenleri tanımlıyoruz...
    private Button btnGirisYap, btnGirisUyeOl;

    //XML dosyasında kullanılan editTextler için değişkenleri tanımlıyoruz...
    private EditText etGirisMail, etGirisPass;

    TextView txtSifremiUnuttum;

    //Metinsel ifadelerde kullanacağımız String türleri için değişkenleri tanımlıyoruz...
    private String mail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);

        //Değişkene FirebaseAuth nesnesini atıyoruz...
        mAuth = FirebaseAuth.getInstance();

        //Butonlar için tanımladığımız değişkenleri XML dosyasıyla ilişkilendiriyoruz...
        btnGirisYap = findViewById(R.id.btnGirisYap);
        btnGirisUyeOl = findViewById(R.id.btnGirisUyeOl);

        //EditTextler için tanımladığımız değişkenleri XML dosyasıyla ilişkilendiriyoruz...
        etGirisMail = findViewById(R.id.etGirisMail);
        etGirisPass = findViewById(R.id.etGirisPass);

        txtSifremiUnuttum = findViewById(R.id.txtSifremiUnuttum);

        txtSifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GirisEkrani.this);
                builder.setTitle("E-Posta Adresiniz");

// Set up the input
                final EditText input = new EditText(GirisEkrani.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("SIFIRLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(input.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(GirisEkrani.this, "Şifre sıfırlama E-Postası gönderildi. Lütfen E-Posta'nızı kontrol ediniz.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
                builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        });

        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etGirisMail.getText().toString() != "" && etGirisMail.getText().toString().contains("@") && etGirisPass.getText().toString() != "") {

                    mail = etGirisMail.getText().toString();
                    pass = etGirisPass.getText().toString();

                    mAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(GirisEkrani.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(),AnaEkran.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(GirisEkrani.this, "Kullanıcı bulunamadı...",
                                                Toast.LENGTH_LONG).show();
                                    }

                                    // ...
                                }
                            });

                } else {
                    if (etGirisMail.getText().toString() == "") {
                        Toast.makeText(GirisEkrani.this, "E-Posta boş olamaz... Lütfen E-Posta adresinizi giriniz.", Toast.LENGTH_LONG).show();
                    } else if (!etGirisMail.getText().toString().contains("@")) {
                        Toast.makeText(GirisEkrani.this, "Lütfen geçerli bir E-Posta adresi giriniz.", Toast.LENGTH_LONG).show();
                    } else if (etGirisPass.getText().toString() == "") {
                        Toast.makeText(GirisEkrani.this, "Şifre boş olamaz... Lütfen şifrenizi giriniz.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnGirisUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UyeOl.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}