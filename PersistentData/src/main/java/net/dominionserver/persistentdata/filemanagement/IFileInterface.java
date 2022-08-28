package net.dominionserver.persistentdata.filemanagement;

import java.io.IOException;

/**
 * Standard File Interface allowing for polymorph or testing
 */
public interface IFileInterface
{
    /**
     * Determines if the file exists on disc
     * @return True means the file exists
     */
    boolean FileExists();

    /**
     * Creates a new file.
     */
    void CreateNewFile() throws IOException;
}
