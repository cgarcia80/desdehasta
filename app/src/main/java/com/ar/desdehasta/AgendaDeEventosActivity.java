package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AgendaDeEventosActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private Spinner spinner;
    private ArrayList<String> arrayList =new ArrayList<>();

    ImageButton btn_fechaSeleccionada,btn_horaSeleccionada;
    Button btn_guardar,btn_eliminar;
    TextView hora,fecha;

    String circuito;

    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    String valor;

    private int minutos,hora_reloj,dia,mes,anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_de_eventos);
        btn_fechaSeleccionada = findViewById(R.id.btn_fechaSeleccionada);
        btn_horaSeleccionada = findViewById(R.id.btn_horaSeleccionada);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        hora = findViewById(R.id.hora);
        fecha = findViewById(R.id.fecha);



        inicializarFirebase();
        spinner = findViewById(R.id.btn_spinner);

         btn_fechaSeleccionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anio = actual.get(Calendar.YEAR);
                mes = actual.get(Calendar.MONTH);
                dia = actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        calendar.set(Calendar.DAY_OF_MONTH,d);
                        calendar.set(Calendar.MONTH,m);
                        calendar.set(Calendar.YEAR,y);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = format.format(calendar.getTime());
                        fecha.setText(strDate);
                    }
                },anio,mes,dia);
                datePickerDialog.show();

            }
        });
        btn_horaSeleccionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora_reloj = actual.get(Calendar.HOUR_OF_DAY);
                minutos = actual.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        calendar.set(Calendar.HOUR_OF_DAY,h);
                        calendar.set(Calendar.MINUTE,m);

                        hora.setText(String.format("%02d:%02d",h,m));
                    }
                },hora_reloj,minutos,true);
                timePickerDialog.show();



            }
        });
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = generarKey();
                Long Alerttime = (calendar.getTimeInMillis() - System.currentTimeMillis());
                int random = (int)(Math.random() * 50 + 1);

                Data data = guardarData("Hora del Circuito", circuito ,random);
                WorkManagerNotificacion.GuardarNotificacion(Alerttime,data,"tag1");

                Toast.makeText(AgendaDeEventosActivity.this, "Alarma Agendada!", Toast.LENGTH_SHORT).show();

            }
        });
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarNotificacion("tag1");

            }
        });

            valor = getIntent().getStringExtra("circuito");
            showDataSpinner();



    }// fin del oncreate

    private void showDataSpinner() {
        databaseReference.child("Circuito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add("-Seleccione su circuito-");
                for (DataSnapshot item: snapshot.getChildren()){
                    String nombre=item.child("nombre").getValue(String.class);
                    // int kilometros=item.child("kilometros").getValue(Integer.class);
                    // String uid=item.child("uid").getValue(String.class);
                    // arrayList.add(nombre + " --> Cant Kilometros: "+(String.valueOf(kilometros))+ " |"+ uid );
                    arrayList.add(nombre);

                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<>(AgendaDeEventosActivity.this,R.layout.style_spinner,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(arrayAdapter);

                spinner.setSelection(arrayAdapter.getPosition(valor));

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getSelectedItem().toString();
                        String s =item;
                        circuito = s;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    private void EliminarNotificacion(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(AgendaDeEventosActivity.this, "Alarma Eliminada del calendario!", Toast.LENGTH_SHORT).show();
    }

    private String generarKey(){
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti){

        return new Data.Builder()
                .putString("titulo",titulo)
                .putString("detalle",detalle)
                .putInt("id_noti",id_noti).build();
    }
}
