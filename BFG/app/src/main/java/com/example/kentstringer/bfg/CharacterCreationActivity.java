package com.example.kentstringer.bfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kentstringer.bfg.models.PlayerCharacter;
import com.example.kentstringer.bfg.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CharacterCreationActivity extends Fragment {
    private User user;
    SharedPreferences sharedpreferences;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.activity_character_creation, container, false);
        user = ((SecondActivity)getActivity()).user;
        Button button = view.findViewById(R.id.charSubmitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView tv = getActivity().findViewById(R.id.nameSelect);
                String name = tv.getText().toString();
                String characterClass = (String)((Spinner)getActivity().findViewById(R.id.classChooser)).getSelectedItem();
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
                Toast.makeText(getContext(), "Character created!", Toast.LENGTH_SHORT).show();
                user.addNewCharacter(pc);
                user.setActivePlayerCharacter(pc);
                String str = user.toJSON();
                try {
                    JSONObject j = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sharedpreferences = getActivity().getSharedPreferences("userSave", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("user", str);
                editor.commit();

                final Intent intent = new Intent(getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        return view;
    }



}
