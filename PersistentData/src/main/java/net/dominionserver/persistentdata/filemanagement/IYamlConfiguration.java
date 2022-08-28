package net.dominionserver.persistentdata.filemanagement;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

/**
 * Represents a File in a YAML file format
 */
public interface IYamlConfiguration extends IYamlConfigSection
{
    /**
     * Loads a Yaml Configuration file.
     * This should be done before anything else in this file as this links
     * the file with the class.
     * @param location File location of the YAML
     * @return <c>true</c> means the file was loaded successfully
     */
    boolean LoadFile(String location);

    /**
     * Defines the separator for subsections within the sectioned file
     * @return The separator between sections within a combined path
     */
    char GetPathSeparator();

    /**
     * Writes all in memory data into the file
     */
    void WriteToFile() throws IOException;

    /**
     * Reads all data from the file into memory
     */
    void ReadFromFile() throws IOException, InvalidConfigurationException;
}
