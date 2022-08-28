package net.dominionserver;

import net.dominionserver.persistentdata.filemanagement.*;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.easymock.IAnswer;
import org.easymock.internal.matchers.Any;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

import static org.easymock.EasyMock.*;
import static org.easymock.internal.matchers.Any.ANY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YAMLFileTests
{
    private String mockDocumentLocation;
    private IFileTypeFactory mockFileTypeFactory;
    private IYamlConfiguration mockYamlConfiguration;
    private IFileInterface mockFileInterface;
    private ISectionedFile testClass;

    @BeforeEach
    public void SetUp()
    {
        mockFileTypeFactory = createNiceMock(IFileTypeFactory.class);
        mockYamlConfiguration = createNiceMock(IYamlConfiguration.class);
        mockFileInterface = createNiceMock(IFileInterface.class);
        replay(mockFileInterface);
        expect(mockFileTypeFactory.GetYamlFile()).andReturn(mockYamlConfiguration);
        expect(mockFileTypeFactory.GetFileInterface(anyString())).andReturn(mockFileInterface);
        replay(mockFileTypeFactory);

        mockDocumentLocation = "MockLocation";
    }

    @Test
    public void OnConstruction_ThrowsArgumentNullException_WhenYAMLConfigurationIsNullTest()
    {
        // Arrange Act Assert
        Exception exception = assertThrows(NullArgumentException.class, () ->
        {
            new YAMLFile(mockDocumentLocation, (IFileTypeFactory)null);
        });
    }

    @Test
    public void OnConstruction_ThrowsArgumentNullException_WhenNullTest()
    {
        // Arrange Act Assert
        Exception exception = assertThrows(NullArgumentException.class, () ->
        {
            testClass = new YAMLFile((String)null, mockFileTypeFactory);
        });
    }

    @Test
    public void GetDocumentLocation_ReturnsTheDocumentPath_WhenCalledTest()
    {
        // Arrange
        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        String result = this.testClass.GetDocumentLocation();

        // Assert
        Assert.assertEquals(mockDocumentLocation, result);
    }

    @Test
    public void ReadData_ThrowsAttributeNotFoundException_WhenThereIsNoDataTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act Assert
        Exception exception = assertThrows(javax.management.AttributeNotFoundException.class, () ->
        {
            testClass.ReadData(testLocation);
        });
    }

    @Test
    public void ReadData_ReturnsBooleanFileData_WhenThereIsDataInPrimarySectionTest()
    {
        // Arrange
        String testLocation = "testLocation";
        Boolean testAnswer = true;

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.ContainsLocation(testLocation)).andReturn(true);
        expect(mockYamlConfiguration.GetData(testLocation)).andReturn(testAnswer);
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        boolean result = !testAnswer;
        try
        {
            result = testClass.ReadData(testLocation);
        }
        catch(Exception e)
        {
            Assert.fail("Exception: " + e.getMessage());
        }

        // Assert
        Assert.assertEquals(testAnswer, result);
    }

    @Test
    public void ReadData_ThrowsAttributeNotFoundException_WhenLocationIsAConfigSectionTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.ContainsLocation(testLocation)).andReturn(true);
        expect(mockYamlConfiguration.IsConfigurationSection(testLocation))
                .andReturn(true);
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act Assert
        Exception exception = assertThrows(javax.management.AttributeNotFoundException.class, () ->
        {
            testClass.ReadData(testLocation);
        });
    }


    @Test
    public void SetData_CallsSetDataInYaml_WhenCalledWithValidLocationTest()
    {
        // Arrange
        String testLocation = "testLocation";
        int expectedAnswer = 1337;
        int defaultAnswer = -1;

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.ContainsLocation(testLocation)).andReturn(true);

        AtomicReference fileData = new AtomicReference(defaultAnswer);

        mockYamlConfiguration.SetData(anyString(), anyObject());
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                if(getCurrentArguments()[0] == testLocation)
                {
                    fileData.set(getCurrentArguments()[1]);
                }

                return getCurrentArguments()[0];
            }
        }).anyTimes();
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        int readData = defaultAnswer;
        try
        {
            testClass.WriteData(testLocation, expectedAnswer);
        }
        catch(Exception e)
        {
            Assert.fail("Exception: " + e.getMessage());
        }

        // Assert
        Assert.assertEquals(expectedAnswer, fileData.get());
    }

    @Test
    public void SetDataThenReadData_ReturnsDataGivenAtSetWhenRead_WhenDataIsGivenAtSetAtTheSameLocationReadTest()
    {
        // Arrange
        String testLocation = "testLocation";
        int expectedAnswer = 1337;
        int defaultAnswer = -1;

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.ContainsLocation(testLocation)).andReturn(true);

        AtomicReference fileData = new AtomicReference(defaultAnswer);
        expect(mockYamlConfiguration.GetData(testLocation))
            .andAnswer(() -> {return fileData.get();});

        mockYamlConfiguration.SetData(anyString(), anyObject());
        expectLastCall().andAnswer(new IAnswer<Object>() {
            public Object answer() throws Throwable
            {
                if(getCurrentArguments()[0] == testLocation)
                {
                    fileData.set(getCurrentArguments()[1]);
                }

                return getCurrentArguments()[0];
            }
        }).anyTimes();
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        int readData = defaultAnswer;
        try
        {
            testClass.WriteData(testLocation, expectedAnswer);
            readData = testClass.ReadData(testLocation);
        }
        catch(Exception e)
        {
            Assert.fail("Exception: " + e.getMessage());
        }

        // Assert
        Assert.assertEquals(expectedAnswer, readData);
    }

    @Test
    public void GetPathSeparator_ReturnsPathSeparatorFromYaml_WhenItExistsTest()
    {
        // Arrange
        char expectedSeparator = 'a';

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.GetPathSeparator()).andReturn(expectedSeparator);

        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        char actualSeparator = testClass.GetPathSeparator();

        // Assert
        Assert.assertEquals(expectedSeparator, actualSeparator);
    }

    @Test
    public void ReadData_ThrowsAttributeNotFoundException_WhenContainsThrowsIllegalArgumentExceptionTest()
    {
        // Arrange
        String testLocation = "sometest";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        expect(mockYamlConfiguration.ContainsLocation(testLocation))
                .andThrow(new IllegalArgumentException("Some exception message"));
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act Assert
        Exception exception = assertThrows(AttributeNotFoundException.class, () ->
        {
            testClass.ReadData(testLocation);
        });
    }

    @Test
    public void OnConstruction_ConfigurationFileIsCreated_WhenFileDoesNotExistTests()
    {
        IFileTypeFactory mockFileFactory = createNiceMock(IFileTypeFactory.class);
        IFileInterface mockFile = createNiceMock(IFileInterface.class);

        // Arrange
        expect(mockFileFactory.GetYamlFile()).andReturn(mockYamlConfiguration);
        expect(mockFileFactory.GetFileInterface(anyString())).andReturn(mockFile);
        replay(mockFileFactory);

        expect(mockFile.FileExists()).andReturn(false);

        AtomicReference haveCreatedFile = new AtomicReference(false);
        try
        {
            mockFile.CreateNewFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    haveCreatedFile.set(true);

                    return new Object();
                }
            }).anyTimes();
        }
        catch(IOException e)
        {

        }
        replay(mockFile);

        // Act
        testClass = new YAMLFile(mockDocumentLocation, mockFileFactory);

        // Assert
        Assert.assertTrue((boolean) haveCreatedFile.get());
    }

    @Test
    public void OnConstruction_ConfigurationFileIsNotCreated_WhenExistsTests()
    {
        IFileTypeFactory mockFileFactory = createNiceMock(IFileTypeFactory.class);
        IFileInterface mockFile = createNiceMock(IFileInterface.class);

        // Arrange
        expect(mockFileFactory.GetYamlFile()).andReturn(mockYamlConfiguration);
        expect(mockFileFactory.GetFileInterface(anyString())).andReturn(mockFile);
        replay(mockFileFactory);

        expect(mockFile.FileExists()).andReturn(true);

        AtomicReference haveCreatedFile = new AtomicReference(false);
        try
        {
            mockFile.CreateNewFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    haveCreatedFile.set(true);

                    return new Object();
                }
            }).anyTimes();
        }
        catch(IOException e)
        {

        }
        replay(mockFile);

        // Act
        testClass = new YAMLFile(mockDocumentLocation, mockFileFactory);

        // Assert
        Assert.assertFalse((boolean) haveCreatedFile.get());
    }

    @Test
    public void WriteToFile_CallsWriteToFileTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);

        AtomicReference didCall = new AtomicReference(false);
        try
        {
            mockYamlConfiguration.WriteToFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    didCall.set(true);
                    return new Object();
                }
            }).anyTimes();
        }
        catch (IOException e)
        {

        }

        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        try
        {
            testClass.WriteToFile();
        }
        catch(Exception e)
        {
            Assert.fail("Exception: " + e.getMessage());
        }

        // Assert
        Assert.assertTrue((boolean) didCall.get());
    }

    @Test
    public void WriteToFile_ThrowsIOException_WhenWriteToFileThrowsTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        try
        {
            mockYamlConfiguration.WriteToFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    throw new IOException();
                }
            }).anyTimes();
        }
        catch (Exception e)
        {

        }
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        Exception exception = assertThrows(IOException.class, () ->
        {
            testClass.WriteToFile();
        });
    }

    @Test
    public void ReadFromFile_CallsReadFromFileTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);

        AtomicReference didCall = new AtomicReference(false);
        try
        {
            mockYamlConfiguration.ReadFromFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    didCall.set(true);
                    return new Object();
                }
            }).anyTimes();
        }
        catch (Exception e)
        {

        }

        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        try
        {
            testClass.ReadFromFile();
        }
        catch(Exception e)
        {
            Assert.fail("Exception: " + e.getMessage());
        }

        // Assert
        Assert.assertTrue((boolean) didCall.get());
    }

    @Test
    public void ReadFromFile_ThrowsIOException_WhenReadFromFileThrowsTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        try
        {
            mockYamlConfiguration.ReadFromFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    throw new IOException();
                }
            }).anyTimes();
        }
        catch (Exception e)
        {

        }
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        Exception exception = assertThrows(IOException.class, () ->
        {
            testClass.ReadFromFile();
        });
    }

    @Test
    public void ReadFromFile_ThrowsInvalidConfigurationException_WhenReadFromFileThrowsTest()
    {
        // Arrange
        String testLocation = "testLocation";

        expect(mockYamlConfiguration.LoadFile(mockDocumentLocation)).andReturn(true);
        try
        {
            mockYamlConfiguration.ReadFromFile();
            expectLastCall().andAnswer(new IAnswer<Object>() {
                public Object answer() throws Throwable
                {
                    throw new InvalidConfigurationException();
                }
            }).anyTimes();
        }
        catch (Exception e)
        {

        }
        replay(mockYamlConfiguration);

        testClass = new YAMLFile(mockDocumentLocation, mockFileTypeFactory);

        // Act
        Exception exception = assertThrows(InvalidConfigurationException.class, () ->
        {
            testClass.ReadFromFile();
        });
    }
}
