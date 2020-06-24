package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE= 20;

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


        loadItems();


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

                saveItems();

            }
        };

        ItemsAdapter.OnclickListener onclickListener =
                new ItemsAdapter.OnclickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        Log.d("MainActivity", "we got in");

                        Intent i = new Intent(MainActivity.this, EditActivity.class);

                        i.putExtra(KEY_ITEM_TEXT, items.get(position));
                        i.putExtra(KEY_ITEM_POSITION, position);

                        startActivityForResult(i, EDIT_TEXT_CODE);
                    }
                };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onclickListener);
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
                saveItems();
            }
        });

    }

    // Handle result
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE ){
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();

            Toast.makeText( getApplicationContext() ,
                    "Items was updated",
                    Toast.LENGTH_SHORT).show();


        }else{
            Log.w("MainActivity", "Unknown call");
        }

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
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "error reading items", e);
        }
    }
    
    
    //This function wil save file\\\
    
}