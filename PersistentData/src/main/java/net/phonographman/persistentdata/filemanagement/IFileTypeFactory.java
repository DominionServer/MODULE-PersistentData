package net.phonographman.persistentdata.filemanagement;

/**
 * Supplies implementations required for sectioned files
 */
public interface IFileTypeFactory
{
    /**
     * @return Returns a Standard Yaml Configuration
     */
    IYamlConfiguration GetYamlFile();

    /**
     * Returns an interface for the standard File Implementation
     * @param location Location for the file
     * @return Returns a standard file management implementation
     */
    IFileInterface GetFileInterface(String location);
}
