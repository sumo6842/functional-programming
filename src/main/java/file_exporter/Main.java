package file_exporter;

import lombok.SneakyThrows;
import lombok.Value;
import org.jooq.lambda.Unchecked;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Value
class User {
    String name;
    String phone;
}

class Exporter {
    public void exporter(String filename, Consumer<Writer> contentWriter) {
        File file = new File("name: " + filename);
        try (Writer writer = new FileWriter(file)) {
            contentWriter.accept(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UserNameExporter {
    private final List<User> user = Collections.singletonList(new User("Duc", "0345184306"));

    @SneakyThrows
    protected void writerOrders(Writer writer) {
        writer.write("Orders;Date");
        user.stream()
                .map(User::getName)
                .forEach(Unchecked.consumer(writer::write));
    }
}

class UserPhoneWriter {
    private final List<User> user = Arrays.asList(
            new User("Duc", "0345184306"),
            new User("Linh", "0339612367"));

    @SneakyThrows
    public void writerUsers(Writer writer) {
        writer.write("User;Date");
        user.stream()
                .map(User::getPhone)
                .forEach(Unchecked.consumer(writer::write));
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        var exporter = new Exporter();
        var usernameWriter = new UserNameExporter();
        var userPhoneWriter = new UserPhoneWriter();
        exporter.exporter("phone.csv", userPhoneWriter::writerUsers);
        exporter.exporter("name.csv", usernameWriter::writerOrders);
    }
}
