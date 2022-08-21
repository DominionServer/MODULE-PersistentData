# MODULE-PersistentData
This module is a method for us to have a tested method for persistent data.
Having tested code is important but time-consuming to repeat therefore this code is reusable and
well tested.
It also ensures that when one refers to persistent data everyone knows what this means.

# How to use

## Top level - Persistent Data
This is unfinished.

## Sub-level - Sectioned File
Sectioned file should not be used unless you would like to not have the default implementation from
persistent data.

### Creation
```new YAMLFile(documentLocation, new FileTypeFactory());```
1. YAML File is a Sectioned File
2. Send in the Document Location
3. A new FileTypeFactory is required for implementations

The YAML file format will load or create the document on construction.

### GetDocumentLocation
Retrieves the document location after construction. Useful if you're passing around PersistentData.

### GetPathSeparator
The Separator within the sectioned File (set by the type) which separates the sections.
So `section1.section2.data = hello` this would ensure two sections exist and then in location data 
sets the string hello as the fullstops in this instance are Separators.

### ReadData
Reads **locally cached** data within the file. Throws AttributeNotFoundException for several errors.
* If the location does not exist
* If the location is a section not a data location

### WriteData
Writes data into the **cached** data within the file. Will create the path given if it does not
exist (does not fail).

```⚠️ Will override a data structure or section with another. This never fails so be sure you are writing to the correct locations!```

### WriteToFile
Writes all Cached data to the actual file. May throw IOException if the File does not exist/cannot write.

### ReadFromFile
Reads from file into Cached data. May throw IOException if the File does not exist/cannot read.
May throw InvalidConfigurationException if the file is an invalid format.