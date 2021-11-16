package com.example.goal.models;

import static com.example.goal.managers.SearchInternet.GET;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.goal.managers.SearchInternet;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SerializationInfosTest {

    @Test
    public void jsonArrayToArray() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        try {
            // Criação da Tarefa Assincrona e do Metodo que busca na Internet
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            SearchInternet searchInternet = new SearchInternet(context);

            // Configura a Tarefa Assincrona que Retorna uma String
            Set<Callable<String[]>> callable = new HashSet<>();
            callable.add(() -> {
                String json_api = searchInternet.SearchInAPI(
                        "http://makeup-api.herokuapp.com/api/v1/products.json", GET, null);
                if (json_api != null) {
                    SerializationInfos serializationInfos = new SerializationInfos(context);
                    return serializationInfos.jsonArrayToArray(json_api, new String[]{"name", "price"});
                }
                return null;
            });

            List<Future<String[]>> futureTasksList;
            futureTasksList = executorService.invokeAll(callable);
            String[] jsonTeste = futureTasksList.get(0).get();

            Log.e("Teste", Arrays.toString(jsonTeste));

        } catch (Exception ex) {
            Log.e("Teste2", "erro - " + ex.getClass().getName());
        }
    }
}