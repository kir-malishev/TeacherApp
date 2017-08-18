package app.teacher;

/**
 * Created by Кирилл on 07.07.2017.
 */
public interface CompatibleWithJSON<T> {
    String getJSON(T object);
    T getFromJSON(String json);
}
