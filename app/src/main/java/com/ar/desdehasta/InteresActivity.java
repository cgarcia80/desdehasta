package com.ar.desdehasta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.ar.desdehasta.adapter.ListAdapter;
import com.ar.desdehasta.pojo.ListElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteresActivity extends AppCompatActivity {

    List<ListElement> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interes_activity);
        init();
    }

    public void init(){

        elements = new ArrayList<>();

        elements.add(new ListElement(color(),"Bicicletas Araoz","Araoz 1458","Palermo","-58.425687936517441","-34.592305089760487","111111111","https://www.google.com/"));
        elements.add(new ListElement(color(),"Roda2oro","Oro 2305","Palermo","-58.425982052385841","-34.58137773534596","2222222","https://www.instagram.com/"));
        elements.add(new ListElement(color(),"Walter","Linch 3914","Nueva Pompeya","-58.41534296034515","-34.653351621289517","33333333","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Bici Shop","Villaroel 1093","Villa Crespo","-58.443244127500215","-34.594350872975618","444444","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Bicicleterias El colo","Rivadavia 770","Monserrat","-58.377334375329205","-34.608363589150585","5555555","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Golden Bike","Ceretti 3596","Villa Urquiza","-58.502561390141238","-34.566551369951142","6666666","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Betrece B13","Juncal 1760","Recoleta","-58.392424855701364","-34.594016573845728","7777777","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Pedal BA","Av. San Martín 1544","Caballito","-58.451406441674251","-34.606483287054296","888888","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Bicicletería Bike Doctor","Fragata Pres. Sarmiento 966","Caballito","-58.454010429825608","-34.613207638566138","999999999","https://www.facebook.com/"));
        elements.add(new ListElement(color(),"Rodados La Esquina Express","Francisco Acuña de Figueroa 1000","Almagro","-58.422176910739168","-34.599454480432541","0000000","https://www.facebook.com/"));

        ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToDescription(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);


    }

    public void moveToDescription(ListElement item) {
        Intent intent = new Intent(this, InteresActivityDetail.class);
        intent.putExtra("ListElement", item);
        startActivity(intent);
    }

    private String color(){
        String[] arcoIris = new String[]{"#800080","#000000","#FF0000","#0000FF","#00FF00","#FFFF00","#FF00FF","#00FFFF","#8800FF","#FF8800"};
        Random random = new Random();
        int color = random.nextInt(10);

        return arcoIris[color];
    }
}