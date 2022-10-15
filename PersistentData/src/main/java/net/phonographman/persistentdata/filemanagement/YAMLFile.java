package net.phonographman.persistentdata.filemanagement;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class YAMLFile implements ISectionedFile
{
    /**
     * Current document location loaded from construction
     */
    private final String documentLocation;

    /**
     * The actual Yaml Configuration file
     */
    private IYamlConfiguration yamlConfiguration;

    /**
     * Allows you to perform actions upon files
     */
    private IFileInterface fileInterface;

    /**
     * Logs messages and info to the console
     */
    private Logger logger;

    public YAMLFile(String documentLocation, IFileTypeFactory fileTypeFactory)
    {
        if(documentLocation == null)
        {
            throw new NullArgumentException(YAMLFile.class.getName() + ".documentLocation");
        }
        this.documentLocation = documentLocation;

        if(fileTypeFactory == null)
        {
            throw new NullArgumentException(YAMLFile.class.getName() + ".fileTypeFactory");
        }
        this.yamlConfiguration = fileTypeFactory.GetYamlFile();

        this.fileInterface = fileTypeFactory.GetFileInterface(documentLocation);
        LoadFileIfNotFound(documentLocation);
    }

    public YAMLFile(String documentLocation, IFileTypeFactory fileTypeFactory, Logger logger)
    {
        if(documentLocation == null)
        {
            throw new NullArgumentException(YAMLFile.class.getName() + ".documentLocation");
        }
        this.documentLocation = documentLocation;

        if(fileTypeFactory == null)
        {
            throw new NullArgumentException(YAMLFile.class.getName() + ".fileTypeFactory");
        }
        this.yamlConfiguration = fileTypeFactory.GetYamlFile();

        if(logger == null)
        {
            throw new NullArgumentException(YAMLFile.class.getName() + ".logger");
        }
        this.logger = logger;

        this.fileInterface = fileTypeFactory.GetFileInterface(documentLocation);
        LoadFileIfNotFound(documentLocation);

    }

    @Override
    public String GetDocumentLocation()
    {
        return this.documentLocation;
    }

    @Override
    public char GetPathSeparator()
    {
        char separator = '.';
        if(this.yamlConfiguration != null)
        {
            separator = this.yamlConfiguration.GetPathSeparator();
        }

        return separator;
    }

    @Override
    public <Object>Object ReadData(String location) throws AttributeNotFoundException
    {
        try
        {
            if(!this.yamlConfiguration.ContainsLocation(location))
            {
                throw new javax.management.AttributeNotFoundException(YAMLFile.class.getName() + ":" +
                        " ReadData cannot find data at location: " + location);
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new AttributeNotFoundException(YAMLFile.class.getName() + ":" +
                    " Path is invalid: " + location + ". " + e.getMessage());
        }

        if(this.yamlConfiguration.IsConfigurationSection(location))
        {
            throw new AttributeNotFoundException(YAMLFile.class.getName() + ":" +
                    " Path is configuration section: " + location + ". ");
        }

        return this.yamlConfiguration.GetData(location);
    }

    @Override
    public <Object>void WriteData(String location, Object data)
    {
        this.yamlConfiguration.SetData(location, data);
    }

    @Override
    public void WriteToFile() throws IOException
    {
        this.yamlConfiguration.WriteToFile();
    }

    @Override
    public void ReadFromFile() throws IOException, InvalidConfigurationException
    {
        this.yamlConfiguration.ReadFromFile();
    }

    @Override
    public boolean FileExists()
    {
        return true;
    }

    /**
     * Ensures the configuration file is loaded if the file does not exist
     * @param documentLocation Location of the document
     */
    private void LoadFileIfNotFound(String documentLocation)
    {
        if(this.fileInterface.FileExists())
        {
            this.yamlConfiguration.LoadFile(documentLocation);
        }
        else
        {
            try
            {
                this.logger.info(
                        "[PM][PD] File does not exist. Creating directories for: " + this.documentLocation);
                this.fileInterface.CreateDirectoriesForFileLocation();

                this.fileInterface.CreateNewFile();
                this.yamlConfiguration.LoadFile(documentLocation);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
