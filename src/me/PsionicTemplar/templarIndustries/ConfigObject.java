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
	
	/**
	 * Used only upon startup to set the main configuration for the plugin.
	 * @param config
	 * @author Nicholas Braniff
	 */

	public ConfigObject(FileConfiguration config) {
		this.mainConfig = true;
		this.config = config;
	}

	/**
	 * Constructor for the Config Object. 
	 * This takes in a filepath and a filename to either create a file or load it into memory.
	 * Takes the default hashmap and adds them to the config file.
	 * 
	 * @param path
	 * @param fileName
	 * @param defaults
	 * @author Nicholas Braniff
	 */
	
	public ConfigObject(String path, String fileName, HashMap<String, Object> defaults) {
		//Set the values
		this.mainConfig = false;
		this.filePath = path;
		this.fileName = fileName;
		//Making the file and loading it
		try {
			file = new File(this.filePath, this.fileName);
			if (!Start.getPlugin().getDataFolder().exists()) {
				Start.getPlugin().getDataFolder().mkdirs();
			}
			File baseFolder = new File(path);
			if(!baseFolder.exists()){
				baseFolder.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			//Creating the configuration file and attatching a file to it
			config = new YamlConfiguration();
			config.load(file);

			//Making defaults for each key in the hashmap
			for(String s : defaults.keySet()){
				config.addDefault(s, defaults.get(s));
			}
			config.options().copyDefaults(true);

			//Using the write function to save the data to the file
			write();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * Used to write all data loaded in the FileConfiguration object and put it in the base file.
	 * 
	 * @author Nicholas Braniff
	 */
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
	
	/**
	 * Returns FileConfguration Object
	 * 
	 * @return FileConfiguration for the given config.
	 * @author Nicholas Braniff
	 */
	
	public FileConfiguration getConfig(){
		return this.config;
	}
	
	/**
	 * Takes in a FileConfiguration object and calls the write function.
	 * 
	 * @param config
	 * @author Nicholas Braniff
	 */
	
	public void setConfigWrite(FileConfiguration config){
		this.config = config;
		write();
	}
	
	/**
	 * Returns whether or not it is the main plugin FileConfiguration or not.
	 * 
	 * @return Boolean stating if it is the main plugin config or not
	 * @author Nicholas Braniff
	 */

	public boolean isMainConfig() {
		return mainConfig;
	}
	
	/**
	 * Returns the file path.
	 * @return String containing the file path
	 * @author Nicholas Braniff
	 */

	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Returns the file name.
	 * @return String containing the file name.
	 * @author Nicholas Braniff
	 */

	public String getFileName() {
		return fileName;
	}

}
