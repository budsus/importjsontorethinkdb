package id.budsus;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import id.budsus.files.FileTools;
import id.budsus.json.CSNJSONTools;
import id.budsus.json.CodeSearchNet;
import id.budsus.rethinkdb.RethinkDBTools;

/**
 * Upload json CodeSearchNet to rethinkdb
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        String[] extensions = { "json", "jsonl" };
        String[] langs = { "go", "java", "javascript", "python", "php", "ruby" };
        
        for (String lang : langs) {
            String pathStrLangBase = args[0].concat(lang)
                    .concat(System.getProperty("file.separator"))
                    .concat("final")
                    .concat(System.getProperty("file.separator"))
                    .concat("jsonl")
                    .concat(System.getProperty("file.separator"));
            String pathStrLangTrain = pathStrLangBase.concat("train");
            String pathStrLangTest = pathStrLangBase.concat("test");
            String pathStrLangValid = pathStrLangBase.concat("valid");

            // read for train path
            List<String> files = FileTools.findFiles(Paths.get(pathStrLangTrain), extensions);
            // read for test path
            files.addAll(FileTools.findFiles(Paths.get(pathStrLangTest), extensions));
            // read for validation path
            files.addAll(FileTools.findFiles(Paths.get(pathStrLangValid), extensions));

            RethinkDBTools rethinkdb = new RethinkDBTools();
            rethinkdb.createTable(lang);

            for (String f : files) {
                File fileJSON = new File(f);
                List<String> str = FileTools.readLines(fileJSON);
                int i = 0;
                System.out.println(f + " ---");
                for (String json : str) {
                    i++;
                    CodeSearchNet csn = CSNJSONTools.getCSNObjectFromJSON(json);
                    csn.setObjectId(Integer.toHexString(System.identityHashCode(csn)));
                    rethinkdb.insert(lang, csn);
                    System.out.print(".");
                }
                System.out.println("");
                System.out.println("---- " + i + " done ----- ");
                FileTools.writeStringToFile(args[1], f + ";" + i);
            }
        }
    }
}
