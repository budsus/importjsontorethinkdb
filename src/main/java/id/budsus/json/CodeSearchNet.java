package id.budsus.json;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class CodeSearchNet {
    private String repo;
    private String path;
    private String func_name;
    private String[] code_tokens;
    private String sha;
    private String url;
    private String partition;
    private String objectId;

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        setObjectId(Integer.toHexString(System.identityHashCode(this)));
        String json = gson.toJson(this);

        return json;
    }
}
