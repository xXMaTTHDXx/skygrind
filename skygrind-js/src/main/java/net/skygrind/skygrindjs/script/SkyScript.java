package net.skygrind.skygrindjs.script;

import net.skygrind.skygrindjs.SkyGrindJS;
import org.bukkit.event.Listener;

import javax.script.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-26.
 */
public class SkyScript implements Runnable {

    private ScriptEngineManager manager;
    private ScriptEngine engine;

    private Invocable invocable;

    private String header = "load(\"nashorn:mozilla_compat.js\");";
    private File file;

    private List<Listener> listeners = new ArrayList<>();

    public SkyScript(File file) {
        this.file = file;
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        System.out.println(engine == null);
        invocable = (Invocable) engine;

        String[] javaImports = new String[]{"org.bukkit.event.player"};

        for (String imp : javaImports) {
            header = header + "importPackage(" + imp + ");";
        }

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            Bindings b = engine.createBindings();
            b.put("bukkit", new ScriptBukkit(this));
            engine.setBindings(b, ScriptContext.ENGINE_SCOPE);
            engine.eval("load(\'C:/Users/Matt/Desktop/Server/plugins/skygrind-js/test.js\');");

        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public Object invokeLibraryFunction(String function, Object... args) throws ScriptException, NoSuchMethodException {
        if (invocable != null) {
            return invocable.invokeFunction(function, args);
        } else {
            throw new NoSuchMethodException("ScriptEngine does not implement javax.script.Invocable, you need to call plugin.setInvocable(Invocable) " +
                    "in order to invoke named scripts in your library");
        }
    }
}
