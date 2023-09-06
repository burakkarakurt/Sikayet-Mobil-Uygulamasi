package com.burak.sikayet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SikayetOlustur extends AppCompatActivity {

    Button btnSikayetiGonder;
    EditText etSikayetTarih, etSikayetSaat, etSikayetDetay;
    Spinner spnSikayetYeri, spnSikayetIl, spnSikayetIlce;
    TextView txtSikayetTitle;

    String strSikayetTarih, strSikayetSaat, strSikayetDetay, userId, sikayetId;
    String[] yerler, iller, ilceler;
    DatePickerDialog datePickerDialog;

    Calendar calendar;

    int year, month, dayOfMonth,strSikayetYer, strSikayetIl, strSikayetIlce;

    FirebaseAuth mAuth;

    FirebaseUser mUser;

    //Firabase veritabanı bağlantısı için değişkenimizi tanımlıyoruz...
    FirebaseDatabase database;

    //Veritabanı referansını tanımlıyoruz...
    DatabaseReference mySikayetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sikayet_olustur);

        sikayetId = "";

        btnSikayetiGonder = findViewById(R.id.btnSikayetiGonder);


        mAuth = FirebaseAuth.getInstance();


        spnSikayetYeri = findViewById(R.id.spnSikayetYeri);
        spnSikayetIl = findViewById(R.id.spnSikayetIl);
        spnSikayetIlce = findViewById(R.id.spnSikayetIlce);
        etSikayetTarih = findViewById(R.id.etSikayetTarih);
        etSikayetSaat = findViewById(R.id.etSikayetSaat);
        etSikayetDetay = findViewById(R.id.etSikayetDetay);

        txtSikayetTitle = findViewById(R.id.txtSikayetTitle);


        // Veritabanı değişkenimize nesnesini atıyoruz....
        database = FirebaseDatabase.getInstance();

        mUser = mAuth.getCurrentUser();

        userId = mUser.getUid();
        // Veritabanından erişim sağlamak istediğimiz bölümü referansımıza atıyoruz...
        mySikayetRef = database.getReference("sikayetler");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            txtSikayetTitle.setText("Şikayetinizi Güncelleyin");

            sikayetId = extras.getString("id");

            mySikayetRef.child(userId).child(sikayetId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    yerler = getResources().getStringArray(R.array.yerler);
                    iller = getResources().getStringArray(R.array.turkey_city);


                    ArrayAdapter yerlerArr = new ArrayAdapter(SikayetOlustur.this, android.R.layout.simple_spinner_item, yerler);
                    yerlerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSikayetYeri.setAdapter(yerlerArr);

                    spnSikayetYeri.setSelection(Integer.parseInt(snapshot.child("sikayet_yeri").getValue().toString()));

                    spnSikayetYeri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            strSikayetYer = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    ArrayAdapter illerArr = new ArrayAdapter(SikayetOlustur.this, android.R.layout.simple_spinner_item, iller);
                    illerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spnSikayetIl.setAdapter(illerArr);
                    spnSikayetIl.setSelection(Integer.parseInt(snapshot.child("sikayet_il").getValue().toString()));
                    spnSikayetIl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if (i != 0) {
                                strSikayetIl = i;


                                int getRes = getResources().getIdentifier("i" + i, "array", getPackageName());


                                ilceler  = getResources().getStringArray(getRes);


                                ilceler = add_element(ilceler,"Seçiniz",ilceler.length);

                                ArrayAdapter ilcelerArr = new ArrayAdapter(SikayetOlustur.this,android.R.layout.simple_spinner_item,ilceler);
                                ilcelerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //Setting the ArrayAdapter data on the Spinner
                                spnSikayetIlce.setAdapter(ilcelerArr);

                                spnSikayetIlce.setSelection(Integer.parseInt(snapshot.child("sikayet_ilce").getValue().toString()));

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spnSikayetIlce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (strSikayetIl == 0) {
                                Toast.makeText(getApplicationContext(),"Lütfen Önce İl Seçiniz!" , Toast.LENGTH_LONG).show();
                            } else {
                                strSikayetIlce = i;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });






                    /*etSikayetYeri.setText(.toString());
                    etSikayetIl.setText(snapshot.child("sikayet_il").getValue().toString());
                    etSikayetIlce.setText(snapshot.child("sikayet_ilce").getValue().toString());*/
                    etSikayetTarih.setText(snapshot.child("sikayet_tarih").getValue().toString());
                    etSikayetSaat.setText(snapshot.child("sikayet_saat").getValue().toString());
                    etSikayetDetay.setText(snapshot.child("sikayet_detay").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btnSikayetiGonder.setText("Şikayeti Güncelle");
        } else {
            yerler = getResources().getStringArray(R.array.yerler);
            iller = getResources().getStringArray(R.array.turkey_city);


            ArrayAdapter yerlerArr = new ArrayAdapter(this, android.R.layout.simple_spinner_item, yerler);
            yerlerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spnSikayetYeri.setAdapter(yerlerArr);

            spnSikayetYeri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    strSikayetYer = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter illerArr = new ArrayAdapter(this, android.R.layout.simple_spinner_item, iller);
            illerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spnSikayetIl.setAdapter(illerArr);

            spnSikayetIl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i != 0) {
                        strSikayetIl = i;


                        int getRes = getResources().getIdentifier("i" + i, "array", getPackageName());


                        ilceler  = getResources().getStringArray(getRes);


                        ilceler = add_element(ilceler,"Seçiniz",ilceler.length);

                        ArrayAdapter ilcelerArr = new ArrayAdapter(SikayetOlustur.this,android.R.layout.simple_spinner_item,ilceler);
                        ilcelerArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        spnSikayetIlce.setAdapter(ilcelerArr);



                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spnSikayetIlce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (strSikayetIl == 0) {
                        Toast.makeText(getApplicationContext(),"Lütfen Önce İl Seçiniz!" , Toast.LENGTH_LONG).show();
                    } else {
                        strSikayetIlce = i;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


        etSikayetTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(SikayetOlustur.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                etSikayetTarih.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
                datePickerDialog.show();
            }
        });

        etSikayetSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel saat, güncel dakika.
                final Calendar takvim = Calendar.getInstance();
                int saat = takvim.get(Calendar.HOUR_OF_DAY);
                int dakika = takvim.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(SikayetOlustur.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // hourOfDay ve minute değerleri seçilen saat değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.
                                etSikayetSaat.setText(hourOfDay + ":" + minute);
                            }
                        }, saat, dakika, true);
                // timepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                // şimdiki zamanı göstermesi için yukarda tanımladğımız değişkenleri kullanıyoruz.
                // true değeri 24 saatlik format için.

                // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
                tpd.show();
            }
        });

        btnSikayetiGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (strSikayetYer > 0 && strSikayetIl > 0
                        && strSikayetIlce > 0 && etSikayetTarih.getText().toString().trim().length() > 0 &&
                        etSikayetSaat.getText().toString().trim().length() > 0 && etSikayetDetay.getText().toString().trim().length() > 0) {


                    strSikayetTarih = etSikayetTarih.getText().toString();
                    strSikayetSaat = etSikayetSaat.getText().toString();
                    strSikayetDetay = etSikayetDetay.getText().toString();

                    if (sikayetId != "") {
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_yeri").setValue(strSikayetYer);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_il").setValue(strSikayetIl);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_ilce").setValue(strSikayetIlce);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_tarih").setValue(strSikayetTarih);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_saat").setValue(strSikayetSaat);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_detay").setValue(strSikayetDetay);
                        mySikayetRef.child(userId).child(sikayetId).child("sikayet_c_time").setValue(currentDate + " " + currentTime);

                        Toast.makeText(SikayetOlustur.this, "Şikayet başarıyla güncellendi.", Toast.LENGTH_SHORT).show();


                    } else {
                        mySikayetRef.child(userId).orderByChild("sikayet_yeri").equalTo(strSikayetYer).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.getValue() != null) {

                                    Toast.makeText(SikayetOlustur.this, "Aynı yer ile ilgili daha önce şikayet oluşturulmuş.", Toast.LENGTH_SHORT).show();


                                } else {
                                    mySikayetRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            long count = dataSnapshot.getChildrenCount();

                                            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_yeri").setValue(strSikayetYer);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_il").setValue(strSikayetIl);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_ilce").setValue(strSikayetIlce);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_tarih").setValue(strSikayetTarih);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_saat").setValue(strSikayetSaat);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_detay").setValue(strSikayetDetay);
                                            mySikayetRef.child(userId).child(String.valueOf(Integer.parseInt(String.valueOf(count)) + 1)).child("sikayet_c_time").setValue(currentDate + " " + currentTime);

                                            Toast.makeText(SikayetOlustur.this, "Şikayet kaydı başarılı bir şekilde oluşturuldu.", Toast.LENGTH_SHORT).show();

                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(SikayetOlustur.this, "Beklenmedik bir hata oluştu...", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                } else {
               if (strSikayetYer == 0) {
                        Toast.makeText(SikayetOlustur.this, "Yer boş olamaz...", Toast.LENGTH_LONG).show();
                    } else if (strSikayetIl == 0) {
                        Toast.makeText(SikayetOlustur.this, "İl boş olamaz...", Toast.LENGTH_LONG).show();
                    } else if (strSikayetIlce == 0) {
                        Toast.makeText(SikayetOlustur.this, "İlçe boş olamaz...", Toast.LENGTH_LONG).show();
                    } else
                    if (etSikayetTarih.getText().toString().trim().length() == 0) {
                        Toast.makeText(SikayetOlustur.this, "Tarih boş olamaz...", Toast.LENGTH_LONG).show();
                    } else if (etSikayetSaat.getText().toString().trim().length() == 0) {
                        Toast.makeText(SikayetOlustur.this, "Saat boş olamaz...", Toast.LENGTH_LONG).show();
                    } else if (etSikayetDetay.getText().toString().trim().length() == 0) {
                        Toast.makeText(SikayetOlustur.this, "Detay boş olamaz...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static String[] add_element(String myarray[], String ele, int lebb) {
        int i;

        String newArray[] = new String[lebb+1];

        newArray[0] = ele;
        //copy original array into new array
        for (i = 1; i < lebb+1; i++)
            newArray[i] = myarray[i-1];

        //add element to the new array

        return newArray;
    }
}