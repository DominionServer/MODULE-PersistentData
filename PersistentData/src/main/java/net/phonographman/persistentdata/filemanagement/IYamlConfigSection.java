package net.phonographman.persistentdata.filemanagement;

import org.bukkit.Location;

import java.util.List;

/**
 * A single section of the given YAML document
 */
public interface IYamlConfigSection
{
    /**
     * Reads the data at the given location
     * @param location The name of the given boolean
     * @return The Boolean value within the file
     */
    <Object>Object GetData(String location);

    /**
     * Determines if the given location is within the file
     * @param location The location within the file
     * @return True means this location is within the file
     */
    boolean ContainsLocation(String location);

    /**
     * Sets data at the given location.
     * @param location path to set
     * @param newData The data to set in the location
     * @param <Object> The type of the data
     */
    <Object>void SetData(String location, Object newData);

    /**
     * Detirmines if the given location is a configuration section
     * @param location The location to check. If invalid will return false.
     * @return True means is a configuration section. False means either not or not valid
     */
    boolean IsConfigurationSection(String location);


    /**
     * Gets the section names within the given section
     * @param location A section location
     * @return A list of the section names
     */
    List<String> GetSectionNames(String location);
}
