package net.dominionserver;


import net.dominionserver.persistentdata.IPersistentData;
import net.dominionserver.persistentdata.PersistentData;
import net.dominionserver.persistentdata.filemanagement.ISectionedFile;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.AttributeNotFoundException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersistentDataTests
{

    @BeforeEach
    public void SetUp()
    {

    }

    @Test
    public void OnConstruction_ThrowsArgumentNullException_WhenSectionedFileIsNullTest()
    {
        // Arrange Act Assert
        Exception exception = assertThrows(NullArgumentException.class, () ->
        {
            new PersistentData((ISectionedFile) null);
        });
    }

    @Test
    public void GetDocumentLocation_ReturnsTheDocumentPath_WhenSectionedFileHasADocumentLocationTest()
    {
        // Arrange
        String expectedDocumentLocation = "MockLocation";

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);
        expect(mockSectionedFile.GetDocumentLocation()).andReturn(expectedDocumentLocation);
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        String result = testClass.GetDocumentLocation();

        // Assert
        Assert.assertEquals(expectedDocumentLocation, result);
    }

    @Test
    public void GetPathSeparator_ReturnsThePathSeparator_WhenSectionedFileHasAPathSeparatorTest()
    {
        // Arrange
        char expectedPathSeparator = 'Y';

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);
        expect(mockSectionedFile.GetPathSeparator()).andReturn(expectedPathSeparator);
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        char result = testClass.GetPathSeparator();

        // Assert
        Assert.assertEquals(expectedPathSeparator, result);
    }

    @Test
    public void ReadData_ReadsFromSectionedFileTest() throws AttributeNotFoundException
    {
        // Arrange
        String testDocumentLocation = "MockLocation";
        String expectedDocumentData = "MockData";

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);

        mockSectionedFile.ReadData(testDocumentLocation);
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                if(getCurrentArguments()[0] == testDocumentLocation)
                {
                    return expectedDocumentData;
                }

                return new Object();
            }
        }).anyTimes();
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        String result = testClass.ReadData(testDocumentLocation);

        // Assert
        Assert.assertEquals(expectedDocumentData, result);
    }

    @Test
    public void WriteData_WritesDataToSectionedFileCacheTest() throws IOException
    {
        // Arrange
        String testDocumentLocation = "MockLocation";

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);

        AtomicReference didWrite = new AtomicReference(false);
        mockSectionedFile.WriteData(anyString(), anyObject());
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                if(getCurrentArguments()[0] == testDocumentLocation)
                {
                    didWrite.set(true);
                }

                return new Object();
            }
        }).anyTimes();
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        testClass.WriteData(testDocumentLocation, true);

        // Assert
        Assert.assertTrue((boolean) didWrite.get());
    }

    @Test
    public void Save_WritesDataToFileTest() throws IOException
    {
        // Arrange
        String testDocumentLocation = "MockLocation";

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);

        AtomicReference didWrite = new AtomicReference(false);
        mockSectionedFile.WriteToFile();
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                didWrite.set(true);
                return new Object();
            }
        }).anyTimes();
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        testClass.Save();

        // Assert
        Assert.assertTrue((boolean) didWrite.get());
    }

    @Test
    public void ReloadFromFile_ReloadsDataFromFileTest() throws IOException, InvalidConfigurationException
    {
        // Arrange
        String testDocumentLocation = "MockLocation";

        ISectionedFile mockSectionedFile = createNiceMock(ISectionedFile.class);

        AtomicReference didReload = new AtomicReference(false);
        mockSectionedFile.ReadFromFile();
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                didReload.set(true);
                return new Object();
            }
        }).anyTimes();
        replay(mockSectionedFile);

        IPersistentData testClass = new PersistentData(mockSectionedFile);

        // Act
        testClass.ReloadFromFile();

        // Assert
        Assert.assertTrue((boolean) didReload.get());
    }
}
