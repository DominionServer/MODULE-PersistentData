package net.dominionserver.persistentdata.filemanagement;

public class FileTypeFactory implements IFileTypeFactory
{
    @Override
    public IYamlConfiguration GetYamlFile()
    {
        return new YamlConfigurationInterface();
    }

    @Override
    public IFileInterface GetFileInterface(String location)
    {
        return new FileInterface(location);
    }
}
