package com.example.kentstringer.bfg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import java.util.ArrayList;

public class UserCreationActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        final Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userFirstName = (String)((TextView)findViewById(R.id.userFirstName)).getText();
                String userLastName = (String)((TextView)findViewById(R.id.userLastName)).getText();
                String characterName = (String)((TextView)findViewById(R.id.characterName)).getText();
                String characterClass = (String)((Spinner)findViewById(R.id.classChooser)).getSelectedItem();
                ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
                user = new User(userFirstName + " " + userLastName, 1, 0, playerCharacters, 0 ,0);
                PlayerCharacter pc;
                switch (characterClass){
                    case "Fighter":
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 25, 25, 25, 25, 0 );
                        break;
                    case "Scout":
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 25, 25, 25, 0, 25 );
                        break;
                    default:
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 20, 20, 20, 20, 20 );
                        break;
                }
                user.addNewCharacter(pc);
            }
        });
    }
}
