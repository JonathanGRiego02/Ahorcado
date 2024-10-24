package dad.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;

public class StringPropertyAdapter extends TypeAdapter<StringProperty> {

    @Override
    public void write(JsonWriter out, StringProperty value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.get());  // Escribir el valor del StringProperty
        }
    }

    @Override
    public StringProperty read(JsonReader in) throws IOException {
        if (in.peek().equals(null)) {
            in.nextNull();
            return null;
        } else {
            return new SimpleStringProperty(in.nextString());  // Leer el valor y devolver un StringProperty
        }
    }
}