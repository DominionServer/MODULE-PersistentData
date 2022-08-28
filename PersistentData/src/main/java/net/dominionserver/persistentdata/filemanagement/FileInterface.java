package net.dominionserver.persistentdata.filemanagement;

import java.io.File;
import java.io.IOException;

public class FileInterface implements IFileInterface
{
    /**
     * The actual file this interfaces
     */
    File actualFile;

    public FileInterface(String location)
    {
        actualFile = new File(location);
    }

    @Override
    public boolean FileExists()
    {
        return actualFile.exists();
    }

    @Override
    public void CreateNewFile() throws IOException
    {
        actualFile.createNewFile();
    }
}
