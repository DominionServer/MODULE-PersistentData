package net.dominionserver.persistentdata;

import net.dominionserver.persistentdata.filemanagement.ISectionedFile;
import net.dominionserver.persistentdata.filemanagement.YAMLFile;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.configuration.InvalidConfigurationException;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.io.NotActiveException;

public class PersistentData implements IPersistentData
{
    /**
     * The actual file and data behind Persistent Data
     */
    ISectionedFile actualData;

    public PersistentData(ISectionedFile sectionedFile)
    {
        if(sectionedFile == null)
        {
            throw new NullArgumentException(PersistentData.class.getName() + ".sectionedFile");
        }
        this.actualData = sectionedFile;
    }

    @Override
    public String GetDocumentLocation()
    {
        return this.actualData.GetDocumentLocation();
    }

    @Override
    public char GetPathSeparator()
    {
        return this.actualData.GetPathSeparator();
    }

    @Override
    public <Object> Object ReadData(String location) throws AttributeNotFoundException
    {
        return this.actualData.ReadData(location);
    }

    @Override
    public <Object> void WriteData(String location, Object data) throws IOException
    {
        this.actualData.WriteData(location, data);
    }

    @Override
    public void Save()
    {
        try
        {
            this.actualData.WriteToFile();
        }
        catch (IOException e)
        {

        }
    }

    @Override
    public void ReloadFromFile()
    {
        try
        {
            this.actualData.ReadFromFile();
        }
        catch (IOException e)
        {

        }
        catch (InvalidConfigurationException e)
        {

        }
    }
}
