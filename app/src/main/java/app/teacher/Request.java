package app.teacher;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Кирилл on 06.11.2017.
 */
public class Request extends Application {

    private static API api;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.malishevkir.fvds.ru") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        api = retrofit.create(API.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static API getApi() {
        return api;
    }
}