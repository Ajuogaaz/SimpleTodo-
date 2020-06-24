package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.addbutton);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvitems);

        items = new ArrayList<>();
        items.add("Buy Milk");
        items.add("Go to the gym");
        items.add("Have fun");


        ItemsAdapter.OnLongClickListener onLongClickListener =
                new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //Delete the item form the model
                items.remove(position);
                //Notify the adapter what is clicked
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),
                        "Item was removed",
                        Toast.LENGTH_SHORT).show();

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();

                //Add item to the model
                items.add(todoItem);

                //notify adapter that an item i inserted
                itemsAdapter.notifyItemInserted(items.size()-1);

                etItem.setText("");

                Toast.makeText( getApplicationContext() ,
                        "Items was added",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
        
    }
    /* This function will load items by reading every line of our File */
    private void loadItems(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "error reading items", e);
            items = new ArrayList<>();
        }
    }
    public void SaveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "error reading items", e);
        }
    }
    
    
    //This function wil save file\\\
    
}