package com.ar.desdehasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.desdehasta.pojo.Agenda;
import com.ar.desdehasta.pojo.Grupo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaCircuitosActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView circuitos;
    private TextView fechaCircuito;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference refCircuitos;
    private ImageButton agregar;
    FirebaseAuth auth;
    private List<Grupo> listGrupo = new ArrayList<>();
    ArrayAdapter<Grupo> arrayAdapterGrupo;
    private ArrayList<String> arrayList =new ArrayList<>();
    private Spinner listado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_circuitos);

        calendarView= findViewById(R.id.calendarView);
        circuitos=findViewById(R.id.text_agenda_circuitos);
        fechaCircuito=findViewById(R.id.text_fecha);
        agregar= findViewById(R.id.imageButtonAdd);
        listado= findViewById(R.id.spinneragenda);
        auth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        ref= database.getReference("agenda");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String fechaSeleccionada=dayOfMonth+ "-"+month+"-"+ year;
                ref.child(auth.getCurrentUser().getUid()).child(fechaSeleccionada).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Agenda agenda = snapshot.getValue(Agenda.class);
                            if(agenda!=null){
                                circuitos.setText(agenda.getCircuito()+"");
                                fechaCircuito.setText(agenda.getFecha());
                            }else{
                                fechaCircuito.setText(fechaSeleccionada);
                                circuitos.setText(circuitos.getText());
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });



            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAgenda(fechaCircuito.getText().toString(),circuitos.getText().toString());
            }
        });

        showDataSpinner();

    }
    public void guardarAgenda(String fecha,String circuito){
        Agenda registroAgenda=new Agenda(fecha, circuito);
        if(auth.getCurrentUser()!=null){
            ref.child(auth.getCurrentUser().getUid()).child(fecha).setValue(registroAgenda).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AgendaCircuitosActivity.this, "se guardo ok",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AgendaCircuitosActivity.this, "no se guardo ok",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
    private void showDataSpinner() {

        refCircuitos= database.getReference();

        refCircuitos.child("Circuito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList.clear();
                arrayList.add("_");
                for (DataSnapshot item: snapshot.getChildren()){
                    String nombre=item.child("nombre").getValue(String.class);
                    //int kilometros=item.child("kilometros").getValue(Integer.class);
                    //String uid=item.child("uid").getValue(String.class);
                    //arrayList.add(nombre + " --> Cant Kilometros: "+(String.valueOf(kilometros))+ " |"+ uid );
                    arrayList.add(nombre);
                }
                ArrayAdapter<String> arrayAdapter =new ArrayAdapter<>(AgendaCircuitosActivity.this,R.layout.style_spinner,arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                listado.setAdapter(arrayAdapter);

                listado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getSelectedItem().toString();
                        String s =item;
                        String s2 = s.substring(s.indexOf("|")+1);
                        circuitos.setText(s2);

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

}