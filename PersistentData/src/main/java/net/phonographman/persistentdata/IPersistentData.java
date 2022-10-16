package net.phonographman.persistentdata;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Stores data persistently so that it may remain through restarts.
 */
public interface IPersistentData
{
    /**
     * Returns the document file location
     * @return The document full file location
     */
    String GetDocumentLocation();

    /**
     * Defines the separator for subsections within the sectioned file
     * @return The separator between sections within a combined path
     */
    char GetPathSeparator();

    /**
     * Reads persistent data
     * @param  location is the location of the data to read.
     * @return The data read
     * @throws AttributeNotFoundException thrown if there is no data at location or
     * if the location is not a location for data.
     */
    <Object>Object ReadData(String location) throws AttributeNotFoundException;

    /**
     * Writes data at the given location of the given type. Will override.
     * @param  location is the location of the data to write.
     * @param  data is the data to write of the given type.
     */
    <Object>void WriteData(String location, Object data) throws IOException;

    /**
     * Saves data persistently. Gives you the control over when files are written
     */
    void Save();

    /**
     * Reloads data from file into the persistent data
     */
    void ReloadFromFile();

    /**
     * Gets the section names within the given section
     * @param location A section location
     * @return A list of the section names
     */
    List<String> GetSectionNames(String location);
}
