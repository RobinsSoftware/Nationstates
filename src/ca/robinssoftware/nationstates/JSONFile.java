package ca.robinssoftware.nationstates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONFile {

    private final File file;
    private final boolean isNew;
    private JSONObject options;

    public JSONFile(File file) {
        this(file, false, null);
    }

    public JSONFile(File file, boolean create) {
        this(file, create, null);
    }

    public JSONFile(File file, boolean create, JSONObject defaults) {
        this.file = file;

        isNew = !file.exists();
        
        if (defaults != null)
            options = defaults;
        else
            options = new JSONObject();
        
        try {
            if (!file.exists()) {
                if (!create)
                    return;
                    
                file.getParentFile().mkdirs();
                file.createNewFile();
                save();
                return;
            }

            char[] buffer = new char[(int) file.length()];
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            reader.read(buffer);
            reader.close();
            options = new JSONObject(new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected boolean isNew() {
        return isNew;
    }
    
    protected File getFile() {
        return file;
    }
    
    protected void save() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8);
        writer.write(options.toString(2));
        writer.close();
    }
    
    protected void removeFromList(String key, Object value) {
        if(!options.has(key))
            return;
        
        List<Object> list = getList(key);
        list.remove(value);
        getOptions().put(key, list);
    }
    
    protected void addToList(String key, Object value) {
        if(!options.has(key))
            getOptions().put(key, new JSONArray());
        
        getOptions().getJSONArray(key).put(value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void replace(JSONObject object) {
        options = object;
        
        try {
            save();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, boolean value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, int value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, long value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, float value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, double value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, Collection<?> value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, Map<?, ?> value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void set(String key, Object value) {
        options.put(key, value);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected JSONObject getOptions() {
        return options;
    }

    protected JSONObject getJSONObject(String key) {
        if (!options.has(key))
            return null;
        return options.getJSONObject(key);
    }
    
    protected String getString(String key) {
        if (!options.has(key))
            return null;
        return options.getString(key);
    }
    
    protected List<Object> getList(String key) {
        if (!options.has(key))
            return List.of();
        return options.getJSONArray(key).toList();
    }
    
    protected int getInt(String key) {
        if (!options.has(key))
            return -1;
        return options.getInt(key);
    }
    
    protected long getLong(String key) {
        if (!options.has(key))
            return -1;
        return options.getLong(key);
    }
}
