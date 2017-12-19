package me.PsionicTemplar.templarIndustries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigObject {

	private boolean mainConfig;
	
	private FileConfiguration config;
	private File file;
	private String filePath;
	private String fileName;

	public ConfigObject(FileConfiguration config) {
		this.mainConfig = true;
		this.config = config;
	}

	public ConfigObject(String path, String fileName, HashMap<String, Object> defaults) {
		this.mainConfig = false;
		this.filePath = path;
		this.fileName = fileName;
		try {
			file = new File(this.filePath, this.fileName);
			if (!Start.getPlugin().getDataFolder().exists()) {
				Start.getPlugin().getDataFolder().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			config = new YamlConfiguration();
			config.load(file);

			for(String s : defaults.keySet()){
				config.addDefault(s, defaults.get(s));
			}
			config.options().copyDefaults(true);

			write();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void write() {
		if (this.mainConfig) {
			Start.getPlugin().saveConfig();
		} else {
			try {
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file)));
				out.write(config.saveToString());
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
