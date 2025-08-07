package io.github.memoriagame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class JsonLoader {

    public static ArrayList<String> carregarFrutas() {
        ArrayList<String> frutasList = new ArrayList<>();

        // Lê o arquivo JSON
        String jsonString = Gdx.files.internal("frutas.json").readString();

        // Faz parsing
        JsonValue root = new JsonReader().parse(jsonString);

        // Pega o array "frutas"
        JsonValue frutasArray = root.get("frutas");

        // Itera e adiciona ao ArrayList
        for (JsonValue fruta : frutasArray) {
            frutasList.add(fruta.asString());
        }

        return frutasList;
    }
}
