package net.phonographman.persistentdata.filemanagement;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlConfigurationInterface implements IYamlConfiguration
{
    /**
     * The Data this represents as a YAML file
     */
    private YamlConfiguration yamlConfigurationFile;

    /**
     * The last document location loaded
     */
    private String documentLocation;

    @Override
    public boolean LoadFile(String location)
    {
        boolean foundFile = false;
        try
        {
            File file = new File(location);
            this.yamlConfigurationFile = YamlConfiguration.loadConfiguration(file);
            documentLocation = location;

            foundFile = true;
        }
        catch (Exception e)
        {
            // The boolean is not set if this fails
        }

        return foundFile;
    }

    @Override
    public char GetPathSeparator()
    {
        return this.yamlConfigurationFile.options().pathSeparator();
    }

    @Override
    public void WriteToFile() throws IOException
    {
        this.yamlConfigurationFile.save(documentLocation);
    }

    @Override
    public void ReadFromFile() throws IOException, InvalidConfigurationException
    {
        this.yamlConfigurationFile.load(documentLocation);
    }

    @Override
    public <Object> Object GetData(String location)
    {
        return (Object) this.yamlConfigurationFile.get(location);
    }

    public boolean ContainsLocation(String location)
    {
        return this.yamlConfigurationFile.contains(location);
    }

    @Override
    public <Object> void SetData(String location, Object newData)
    {
        this.yamlConfigurationFile.set(location, newData);
    }

    @Override
    public boolean IsConfigurationSection(String location)
    {
        return this.yamlConfigurationFile.isConfigurationSection(location);
    }

    @Override
    public List<String> GetSectionNames(String location)
    {
        List<String> sectionNames = new ArrayList<>();
        if(this.yamlConfigurationFile.isConfigurationSection(location))
        {
            sectionNames.addAll(
                    this.yamlConfigurationFile.getConfigurationSection(location).getKeys(false));
        }

        return sectionNames;
    }
}
