package net.deadpvp.lobby.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Configuration {

    private final File file;
    private final FileConfiguration fileConfiguration;
    private final File dataFolder;

    public Configuration(File dataFolder) {
        this.dataFolder = dataFolder;
        this.file = new File(dataFolder,"config.yml");
        this.createIfNotExist(this.file);
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        this.loadData();
    }

    private void loadData() {

    }

    private void createIfNotExist(File file) {
        if(!file.exists()){
            try {
                this.copyYamlFileToDestination("config.yml");
                file.createNewFile();
                Bukkit.getConsoleSender().sendMessage("§a§lFile "+file.getName()+".yml created !");
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§4§lError: "+file.getName()+".yml");
                e.printStackTrace();
            }
        }
    }

    public void copyYamlFileToDestination(String sourceFilePath) {
        try {
            String destinationFilePath = this.dataFolder.getPath() + '/' +sourceFilePath;
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sourceFilePath);
            if (inputStream != null) {
                File destinationFile = new File(destinationFilePath);

                destinationFile.getParentFile().mkdirs();

                OutputStream outputStream = new FileOutputStream(destinationFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                Bukkit.getConsoleSender().sendMessage("§a"+sourceFilePath+" vient d'être copié !");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c§lUne erreur est survenue lors de la copie du fichier "+sourceFilePath+" !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }
}
