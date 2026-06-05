package at.randorf.json;

import at.randorf.item.ItemFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.function.BiConsumer;

public class JsonManager {
    public static String getJson(String fileName){
        try (InputStream in = ItemFactory.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) return "{}";
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }
    public static void scanFolder(String folderName, BiConsumer<String, InputStream> fileInterpreter) {
        try {
            URI uri = JsonManager.class.getClassLoader().getResource(folderName).toURI();
            Path myPath;

            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                myPath = fileSystem.getPath(folderName);
            } else {
                myPath = Paths.get(uri);
            }

            try (var walk = Files.walk(myPath, 1)) {
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".json"))
                        .forEach(p -> {
                            String fileName = p.getFileName().toString();
                            String key = fileName.replace(".json", "").toLowerCase();

                            // Stream für die Datei öffnen und an den Interpreter übergeben
                            try (InputStream in = JsonManager.class.getClassLoader()
                                    .getResourceAsStream(folderName + "/" + fileName)) {

                                if (in != null) {
                                    // Hier übergeben wir die Daten an deine Logik!
                                    fileInterpreter.accept(key, in);
                                }
                            } catch (Exception e) {
                                System.err.println("Fehler beim Lesen der Datei: " + fileName);
                                e.printStackTrace();
                            }
                        });
            }
        } catch (Exception e) {
            System.err.println("Konnte den Ordner " + folderName + " nicht scannen.");
            e.printStackTrace();
        }
    }
}
