package net.dominionserver.persistentdata.filemanagement;

import org.bukkit.configuration.InvalidConfigurationException;

import javax.management.AttributeNotFoundException;
import java.io.IOException;

/**
 * A file with storage and sections separated by a character
 */
public interface ISectionedFile
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
     * Reads data from the given file / from the last time the file was read from.
     * @param  location is the location of the data to read.
     * @return The data read
     * @throws AttributeNotFoundException thrown if there is no data at location
     */
    <Object>Object ReadData(String location) throws AttributeNotFoundException;

    /**
     * Writes data at the given location of the given type. Will override.
     * @param  location is the location of the data to write.
     * @param  data is the data to write of the given type.
     */
    <Object>void WriteData(String location, Object data);

    /**
     * Writes all in memory data into the file
     * @throws IOException File does not exist / cannot read
     */
    void WriteToFile() throws IOException;

    /**
     * Reads all data from the file into memory
     * @throws IOException File does not exist / cannot read
     * @throws InvalidConfigurationException File format invalid
     */
    void ReadFromFile() throws IOException, InvalidConfigurationException;

    /**
     * Ensures the file exists
     * @return True means the file exists
     */
    boolean FileExists();
}
