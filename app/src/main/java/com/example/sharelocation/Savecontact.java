package com.example.sharelocation;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tapadoo.alerter.Alerter;

public class Savecontact extends AppCompatActivity {

    EditText id,name;
    Button insert,view,update,delete;
    TextView text;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savecontact);


        id=(EditText)findViewById(R.id.id);
        name=(EditText)findViewById((R.id.name));
        insert=(Button)findViewById(R.id.insert);
        view=(Button)findViewById(R.id.view);
        update=(Button)findViewById(R.id.update);
        delete=(Button)findViewById(R.id.delete);
        text=(TextView)findViewById(R.id.text);

        db=new DBHandler(getApplicationContext());


    }
    public void buttonAction(View view){
        switch (view.getId()){
            case R.id.insert:
                db.insertRecord(name.getText().toString());
                name.setText("");
                Alerter.create(Savecontact.this)
                        .setTitle("Save successfully")
                        .setText("Contact Number")
                        .setIcon(R.drawable.ic_person_add_black_24dp)
                        .setBackgroundColor(R.color.Orangetext)
                        .show();
                break;
            case R.id.view:
                text.setText(db.getRecords());
                break;
            case R.id.update:
                db.updateRecord(id.getText().toString(),name.getText().toString());
                id.setText("");
                Alerter.create(Savecontact.this)
                        .setTitle("Update successfully")
                        .setText("Contact Number")
                        .setIcon(R.drawable.ic_update_black_24dp)
                        .setBackgroundColor(R.color.Update)
                        .show();
                break;
            case R.id.delete:
                db.deleteRecord(id.getText().toString());
                id.setText("");
                Alerter.create(Savecontact.this)
                        .setTitle("Delete successfully")
                        .setText("Contact Number")
                        .setIcon(R.drawable.ic_delete_forever_black_24dp)
                        .setBackgroundColor(R.color.colorAccent)
                        .show();
                break;
        }
    }

}
