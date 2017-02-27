package net.skygrind.core;

import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

/**
 * Created by Matt on 2017-02-11.
 */
public class ServerPubSub implements Runnable {

    private String host;
    private String pass;
    private Jedis jedis;

    public ServerPubSub(String host, String pass) {
        this.host = host;
        this.pass = pass;
    }

    public void connect() {
        if (jedis != null && jedis.isConnected())
            return;

        jedis = new Jedis(host);
        jedis.auth(pass);
        jedis.connect();
    }

    public void disconnect() {
        if (jedis == null || !jedis.isConnected())
            return;
        jedis.disconnect();
    }

    public void run() {
        connect();
        String serializedServerInfo = Bukkit.getServerName() + ":" + Bukkit.getOnlinePlayers().size() + ":" + Bukkit.getMaxPlayers();
        jedis.publish("updater", serializedServerInfo);
    }
}
