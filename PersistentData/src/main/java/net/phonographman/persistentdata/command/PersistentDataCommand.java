package net.phonographman.persistentdata.command;

import net.phonographman.persistentdata.IPersistentData;
import net.phonographman.persistentdata.PersistentData;
import net.phonographman.persistentdata.filemanagement.FileTypeFactory;
import net.phonographman.persistentdata.filemanagement.ISectionedFile;
import net.phonographman.persistentdata.filemanagement.YAMLFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.management.AttributeNotFoundException;
import java.io.IOException;

public class PersistentDataCommand implements CommandExecutor
{
    /**
     * Stores data persistently so that it may remain through restarts.
     */
    IPersistentData persistentData;

    /**
     * The location of the document
     */
    public final String documentLocation = "testdocument.yml";

    public PersistentDataCommand()
    {
        ISectionedFile sectionedFile = new YAMLFile(documentLocation, new FileTypeFactory());
        persistentData = new PersistentData(sectionedFile);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean commandSent = false;
        if (sender instanceof Player)
        {
            commandSent = true;
            Player player = (Player) sender;

            switch(args[0])
            {
                case "set":
                    SubCommandSet(player, args);
                    break;
                case "read":
                     SubCommandRead(player, args);
                    break;
                case "reload":
                    SubCommandReload(player);
                    break;
            }
        }

        return commandSent;
    }

    /**
     * Runs the command for set
     * @param player Player who ran the command
     * @param args The arguments sent (will include the sub command)
     * @return True means this ran successfully
     */
    private boolean SubCommandSet(Player player, String[] args)
    {
        boolean foundAnswer = false;
        if(args[2] == null)
        {
            player.sendMessage("Send data as the last argument");
        }
        else
        {
            try
            {
                WriteDataToFile(args[1],args[2]);
                player.sendMessage("Data written");

                this.persistentData.Save();
                foundAnswer = true;
            }
            catch (IOException e)
            {

            }

        }

        return foundAnswer;
    }

    /**
     * Runs the command for read
     * @param player Player who ran the command
     * @param args The arguments sent (will include the sub command)
     * @return True means this ran successfully
     */
    private boolean SubCommandRead(Player player, String[] args)
    {
        boolean foundAnswer = false;

        String answer = "-1";
        try
        {
            answer = ReadDataFromFile(args[1]);
            player.sendMessage("Data was: " + answer);
            foundAnswer = true;
        }
        catch(AttributeNotFoundException e)
        {
            player.sendMessage("Could not read: " + e.getMessage());
        }

        return foundAnswer;
    }

    /**
     * Runs the command for reload
     * @param player Player who ran the command
     * @return True means this ran successfully
     */
    private boolean SubCommandReload(Player player)
    {
        boolean foundAnswer = false;

        this.persistentData.ReloadFromFile();
        player.sendMessage("Reloaded");
        foundAnswer = true;

        return foundAnswer;
    }

    /**
     * Writes data to file
     * @param location Location within file. Use fullstop . as section divider.
     * @param contents Contents to write at location
     */
    private void WriteDataToFile(String location, String contents) throws IOException
    {
        this.persistentData.WriteData(location,contents);
    }

    /**
     * Reads data from the given location
     * @param location Location within file. Use fullstop . as section divider.
     * @return Data found at location if any
     * @exception AttributeNotFoundException if the location does not exist or is a section
     */
    private String ReadDataFromFile(String location) throws AttributeNotFoundException
    {
        String answer = "-1";
        try
        {
            answer = this.persistentData.ReadData(location);
        }
        catch(AttributeNotFoundException e)
        {
            throw new AttributeNotFoundException(e.getMessage());
        }

        return answer;
    }
}
