package dad.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public class IntegerPropertyAdapter extends TypeAdapter<IntegerProperty> {

    @Override
    public void write(JsonWriter out, IntegerProperty value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.get());  // Escribir el valor del IntegerProperty
        }
    }

    @Override
    public IntegerProperty read(JsonReader in) throws IOException {
        if (in.peek().equals(null)) {
            in.nextNull();
            return null;
        } else {
            return new SimpleIntegerProperty(in.nextInt());  // Leer el valor y devolver un IntegerProperty
        }
    }
}