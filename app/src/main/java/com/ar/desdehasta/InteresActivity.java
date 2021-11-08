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
        //011-4833-7887
        elements.add(new ListElement(color(),"Bicicletas Araoz","Araoz 1458","Palermo","-58.425687936517441","-34.592305089760487","null","https://bicicletasaraoz.com.ar/"));
        elements.add(new ListElement(color(),"Roda2oro","Oro 2305","Palermo","-58.425982052385841","-34.58137773534596","4774-0403","http://www.roda2oro.com/"));
        elements.add(new ListElement(color(),"Walter","Linch 3914","Nueva Pompeya","-58.41534296034515","-34.653351621289517","4912-2738","null"));
        elements.add(new ListElement(color(),"Bici Shop","Villaroel 1093","Villa Crespo","-58.443244127500215","-34.594350872975618","4855-8329","http://www.tubicicleteria.com/b"));
        elements.add(new ListElement(color(),"Bicicleterias El colo","Rivadavia 770","Monserrat","-58.377334375329205","-34.608363589150585","4342--3887","https://www.bicicleteriaelcolo.com.ar/"));
        elements.add(new ListElement(color(),"Golden Bike","Ceretti 3596","Villa Urquiza","-58.502561390141238","-34.566551369951142","4571-7101","https://www.goldenbike.com.ar/"));
        elements.add(new ListElement(color(),"Betrece B13","Juncal 1760","Recoleta","-58.392424855701364","-34.594016573845728","4811-1135","http://b13bicis.com/"));
        elements.add(new ListElement(color(),"Pedal BA","Av. San Martín 1544","Caballito","-58.451406441674251","-34.606483287054296","2063-8558","https://www.facebook.com/bicicleteriapedal"));
        elements.add(new ListElement(color(),"Bicicletería Bike Doctor","Fragata Pres. Sarmiento 966","Caballito","-58.454010429825608","-34.613207638566138","3976-5602","https://www.instagram.com/bikedoctorbici/"));
        elements.add(new ListElement(color(),"Rodados La Esquina Express","Francisco Acuña de Figueroa 1000","Almagro","-58.422176910739168","-34.599454480432541","2659-7421","https://www.facebook.com/rodadoslaesquinaexpress"));

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