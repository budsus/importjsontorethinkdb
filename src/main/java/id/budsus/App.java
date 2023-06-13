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
    public static void main( String[] args ) throws Exception {
        String[] extensions = {"json", "jsonl"};
        // read for train path 
        List<String> files = FileTools.findFiles(Paths.get(args[0]), extensions);
        // read for test path 
        files.addAll(FileTools.findFiles(Paths.get(args[1]), extensions));
        // read for validation path 
        files.addAll(FileTools.findFiles(Paths.get(args[2]), extensions));

        RethinkDBTools rethinkdb = new RethinkDBTools();
        rethinkdb.createTable(args[4]);

        for(String f:files) {
            File fileJSON = new File(f);
            List<String> str = FileTools.readLines(fileJSON);
            int i = 0;
            System.out.println(f + " ---");
            for(String json:str) {
                i++;
                CodeSearchNet csn = CSNJSONTools.getCSNObjectFromJSON(json);
                csn.setObjectId(Integer.toHexString(System.identityHashCode(csn)));
                rethinkdb.insert(args[4], csn);
                System.out.print(".");
            }
            System.out.println("");
            System.out.println("---- " + i + " done ----- ");
            FileTools.writeStringToFile(args[3], f + ";" + i);
        }

        System.out.println(RethinkDBTools.getCountTableRow(args[4]));
    }
}
