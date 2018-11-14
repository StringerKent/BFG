package com.example.kentstringer.bfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCreationActivity extends AppCompatActivity {
    private User user;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        final Button button = findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userFirstName = ((TextView)findViewById(R.id.userFirstName)).getText().toString();
                String userLastName = ((TextView)findViewById(R.id.userLastName)).getText().toString();
                String characterName = ((TextView)findViewById(R.id.nameSelect)).getText().toString();
                String characterClass = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
                ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
                user = new User(userFirstName + " " + userLastName, 1, 0, playerCharacters, 0 ,0);
                PlayerCharacter pc;
                switch (characterClass){
                    case "Fighter - Strength Based":
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 25, 25, 25, 25, 0 );
                        break;
                    case "Ranger - Balanced Class":
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 20, 20, 20, 20, 20 );
                        break;
                    default:
                        pc = new PlayerCharacter(characterClass, characterName, 10, 10, 10,
                                10, 10, 25, 25, 25, 0, 25 );
                        break;
                }
                user.addNewCharacter(pc);
                user.setActivePlayerCharacter(pc);
                String str = user.toJSON();
                try {
                    JSONObject j = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sharedpreferences = getSharedPreferences("userSave", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("user", str);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}
