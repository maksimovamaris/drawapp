package com.example.drawapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btnReset=findViewById(R.id.clearButton);
        ImageButton btnPen=findViewById(R.id.penButton);
        ImageButton btnRectangle = findViewById(R.id.rectangleButton);
        ImageButton btnGesture= findViewById(R.id.gestureButton);
        final DrawView drawView=findViewById(R.id.drawView);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                drawView.clear();
            }

        });
        btnPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setFlag_pen(true);
                drawView.setFlag_rect(false);
                drawView.setFlag_gest(false);
            }
        });
       btnRectangle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               drawView.setFlag_pen(false);
               drawView.setFlag_rect(true);
               drawView.setFlag_gest(false);
           }
       });
       btnGesture.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               drawView.setFlag_pen(false);
               drawView.setFlag_rect(false);
               drawView.setFlag_gest(true);
           }
       });
        GridView palletGridView = (GridView) findViewById(R.id.palletGridView);
        final ArrayList<Pallet> pallets = Pallet.getPallet();
        PalletAdapter palletAdapter = new PalletAdapter(this,pallets);
        palletGridView.setAdapter(palletAdapter);
        palletGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                drawView.setmCurrentColor(Color.parseColor(pallets.get(position).getColor()));
}
});
    }
}
