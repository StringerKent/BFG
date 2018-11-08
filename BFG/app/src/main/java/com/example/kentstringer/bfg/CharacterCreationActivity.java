package com.example.kentstringer.bfg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

public class CharacterCreationActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);
        final Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = (String)((TextView)findViewById(R.id.characterName)).getText();
                String characterClass = (String)((Spinner)findViewById(R.id.classChooser)).getSelectedItem();
                PlayerCharacter pc;
                switch (characterClass){
                    case "Fighter":
                        pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                10, 10, 25, 25, 25, 25, 0 );
                        break;
                    case "Scout":
                        pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                10, 10, 25, 25, 25, 0, 25 );
                        break;
                    default:
                        pc = new PlayerCharacter(characterClass, name, 10, 10, 10,
                                10, 10, 20, 20, 20, 20, 20 );
                        break;
                }
                user.addNewCharacter(pc);

            }
        });
    }



}
