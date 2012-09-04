package me.quantum.qannouncer;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class QAnnouncer extends JavaPlugin {

	public List<String> messages = null;
	public int interval = 60;
	public String prefix = null;
	private ScheduledExecutorService scheduler;
	
	private Random r = new Random();

	static String mainDirectory = "plugins" + File.separator + "QAnnouncer";
	private FileConfiguration config = null;
	private File configFile = null;	
	
	@Override
	public void onEnable() {
		loadConfig();
		
		scheduler = Executors.newScheduledThreadPool(1);
		Runnable announcer = new Announcer(this);
		scheduler.scheduleWithFixedDelay(announcer, 0, interval, TimeUnit.SECONDS);
	}
	
	@Override
	public void onDisable() {
		scheduler.shutdown();
	}
	
	public void loadConfig() {
		if (configFile == null)
			configFile = new File(mainDirectory + File.separator + "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defaultStream = this.getResource("config.yml");
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultStream);
			config.setDefaults(defaultConfig);
		}
		
		if (!config.getBoolean("enabled")) setEnabled(false);
		messages = config.getStringList("messages");
		interval = config.getInt("interval");
		prefix = config.getString("prefix").replace('&', '§');
	}
	
	public void broadcast() {
		getServer().broadcastMessage(prefix + " " + messages.get(r.nextInt(messages.size())).replace('&', '§'));
	}
	
}
