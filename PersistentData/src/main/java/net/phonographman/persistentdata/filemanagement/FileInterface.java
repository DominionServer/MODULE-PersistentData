package net.phonographman.persistentdata.filemanagement;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

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

    @Override
    public void CreateDirectoriesForFileLocation()
    {
        Path path = Paths.get(actualFile.getParentFile().getPath());
        try
        {
            Files.createDirectories(path);
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe(
                    "[PM][PD] Failed to create directories due to an IOException.");
        }
    }
}
