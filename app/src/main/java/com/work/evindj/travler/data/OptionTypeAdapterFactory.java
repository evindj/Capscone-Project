package com.work.evindj.travler.data;
        import com.google.gson.Gson;
        import com.google.gson.JsonElement;
        import com.google.gson.JsonObject;
        import com.google.gson.TypeAdapter;
        import com.google.gson.TypeAdapterFactory;
        import com.google.gson.reflect.TypeToken;
        import com.google.gson.stream.JsonReader;
        import com.google.gson.stream.JsonWriter;

        import org.json.JSONObject;

        import java.io.IOException;

/**
 * Created by evindj on 12/6/15.
 */
public class OptionTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final  TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out,value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                jsonElement = jsonElement.getAsJsonObject().get("trips");
                if(jsonElement.isJsonObject()){
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    boolean ar = jsonObject.get("tripOption").isJsonArray();
                    boolean ob = jsonObject.get("tripOption").isJsonObject();
                    jsonElement=jsonObject.get("tripOption");

                }
                T a =  delegate.fromJsonTree(jsonElement);
                return a;
            }
        }.nullSafe();
    }
}