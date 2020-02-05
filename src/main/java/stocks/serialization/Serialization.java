package stocks.serialization;

import stocks.output.Navigation;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;

public class Serialization {

    private Serialization() {}

    public static void save(Navigation navigation) throws IOException {
        FileOutputStream fos = new FileOutputStream("save.xml");
        XMLEncoder encoder = new XMLEncoder(fos);
        encoder.setExceptionListener(new ExceptionListener() {
            @Override
            public void exceptionThrown(Exception e) {
                System.out.println("Saving Failed!");
                e.printStackTrace();
            }
        });
        encoder.writeObject(navigation);
        encoder.close();
        fos.close();
    }
}
