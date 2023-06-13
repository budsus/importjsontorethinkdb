package id.budsus.rethinkdb;

import java.util.List;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Contains;
import com.rethinkdb.model.GroupedResult;
import com.rethinkdb.model.OptArgs;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;

import id.budsus.json.CodeSearchNet;

public class RethinkDBTools {
    public final RethinkDB r = RethinkDB.r;
    public Connection conn = r.connection().hostname("localhost").port(28015).connect();

    public RethinkDBTools() {
    }

    public void createTable(String tableName) {
        Result<Object> result = r.db("codesearchnet").tableList().contains(tableName).run(conn);
        boolean exist = ((Boolean) result.next()).booleanValue();
        if (!exist) {
            r.db("codesearchnet").tableCreate(tableName).run(conn);
        }
    }

    public void insert(String tableName, CodeSearchNet csn) {
        r.db("codesearchnet").table(tableName).insert(csn).run(conn, OptArgs.of("array_limit",1000000));
    }

    public static int getCountTableRow(String tableName) {
        int result = 0;
        Result<Object> cursor = r.db("codesearchnet").table(tableName).count().run(conn);
        for (Object row : cursor) {
            System.out.println(row);
        }
        return result;
    }

    public void closeConn() {
        conn.close();
    }
}
