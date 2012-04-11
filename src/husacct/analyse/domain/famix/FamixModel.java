package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.directory.InvalidAttributesException;

/**
 * The Class Model. A Model represents information concerning the particular
 * system being modelled. Parsers must ensure that there is only instance of a
 * Model in a complete transfer. Model is a concrete class inheriting from
 * Object. Besides inherited attributes, it has the following attributes:
 */
public class FamixModel extends FamixObject
{

	private HashMap<String, FamixBehaviouralEntity> behaviouralEntities = new HashMap<String, FamixBehaviouralEntity>();
	private HashMap<String, FamixStructuralEntity> structuralEntities = new HashMap<String, FamixStructuralEntity>();
	private HashMap<String, FamixPackage> packages = new HashMap<String, FamixPackage>();
	private HashMap<String, FamixClass> classes = new HashMap<String, FamixClass>();
	private ArrayList<FamixAssociation> associations = new ArrayList<FamixAssociation>();

	public void addObject(Object e) throws InvalidAttributesException
	{

		if (e instanceof FamixEntity)
		{
			if (e instanceof FamixBehaviouralEntity)
			{
				behaviouralEntities.put(((FamixEntity) e).getUniqueName(), (FamixBehaviouralEntity) e);
			}
			else if (e instanceof FamixStructuralEntity)
			{
				structuralEntities.put(((FamixEntity) e).getUniqueName(), (FamixStructuralEntity) e);
			}
			else if (e instanceof FamixPackage)
			{
				packages.put(((FamixEntity) e).getUniqueName(), (FamixPackage) e);
			}
			else if (e instanceof FamixClass)
			{
				classes.put(((FamixEntity) e).getUniqueName(), (FamixClass) e);
			}
		}
		else if (e instanceof FamixAssociation)
		{
			associations.add((FamixAssociation) e);
		}
		else
		{
			throw new InvalidAttributesException("Wrongtype (not of type entity or association) ");
		}

	}

	public ArrayList<FamixAssociation> getAssociations()
	{
		return associations;
	}

	public ArrayList<FamixInvocation> getInvocations()
	{
		ArrayList<FamixInvocation> result = new ArrayList<FamixInvocation>();

		for (FamixAssociation association : associations)
			if (association instanceof FamixInvocation)
				result.add((FamixInvocation) association);

		return result;
	}

	public HashMap<String, FamixPackage> getPackages()
	{
		return packages;
	}

	public void setPackages(HashMap<String, FamixPackage> packages)
	{
		this.packages = packages;
	}

	public HashMap<String, FamixClass> getClasses()
	{
		return classes;
	}

	public void setClasses(HashMap<String, FamixClass> classes)
	{
		this.classes = classes;
	}

	public HashMap<String, FamixBehaviouralEntity> getBehaviouralEntities()
	{
		return behaviouralEntities;
	}

	public void setBehaviouralEntities(HashMap<String, FamixBehaviouralEntity> behaviouralEntities)
	{
		this.behaviouralEntities = behaviouralEntities;
	}

	public HashMap<String, FamixStructuralEntity> getStructuralEntities()
	{
		return structuralEntities;
	}

	public void setStructuralEntities(HashMap<String, FamixStructuralEntity> structuralEntities)
	{
		this.structuralEntities = structuralEntities;
	}

	public void setAssociations(ArrayList<FamixAssociation> associations)
	{
		this.associations = associations;
	}

	public String toString()
	{
		return 
				"\n ------------Packages------------- \n" + packages
				+ "\n ------------Classes------------- \n" + classes
				+ "\n -----------Imports:-------------- \n" + associations
				+ "\n --------------Methoden (behavioural entities) ----------- \n" + behaviouralEntities
				+ "\n --------------Variabelen (structural entities) ----------- \n" + structuralEntities
				+ "\n -----------Invocations-------------- \n" + associations + "num invocs " + associations.size();

	}

	public FamixStructuralEntity getTypeForVariable(String uniqueVarName) throws Exception
	{
		String temp = uniqueVarName;
		String[] splitted = temp.split("\\.");

		// Iterate over it
		for (int i = splitted.length; i > 1; i--)
		{
			if (structuralEntities.containsKey(temp))
				return structuralEntities.get(temp);

			// Is het dan een instance var?
			temp = splitted[0] + "." + splitted[splitted.length - 1];
		}

		// Ook niet dan exception
		throw new Exception("The unit (or a part of it) '" + temp + " or " + uniqueVarName + "' is not found or defined.");

	}

	/**
	 * The exporter name. Represents the name of the tool that generated the
	 * information.
	 */
	private String exporterName;

	/**
	 * The exporter version. Represents the version of the tool that generated
	 * the information.
	 */
	private String exporterVersion;

	/** The exporter date. Represents the date the information was generated. */
	private String exporterDate;

	/**
	 * The exporter time. Represents the time of the day the information was
	 * generated.
	 */
	private String exporterTime;

	/**
	 * The publisher name. Represents the name of the person that generated the
	 * information. Provide an empty string if this information is not known.
	 */
	private String publisherName;

	/**
	 * The parsed system name. Represents the name of the system where the
	 * information was extracted from.
	 */
	private String parsedSystemName;

	/**
	 * The extraction level. Represents the level of extraction used when
	 * generating the information (see Table 1: Levels of Extraction - p. 7).
	 */
	private String extractionLevel;

	/**
	 * The source language. dentifies the implementation language of the parsed
	 * source code. For the implementation languages that are relevant in FAMOOS
	 * this should be one of "C++", "Ada", "Java", or "Smalltalk".
	 */
	private String sourceLanguage;

	/**
	 * The source dialect. Identifies the dialect of the implementation language
	 * of the parsed source code. The exact contents of the string is a language
	 * dependent issue, e.g. "Borland", "ANSI", for C++.
	 */
	private String sourceDialect;

	/**
	 * Gets the exporter name.
	 * 
	 * @return the exporter name
	 */
	public String getExporterName()
	{
		return exporterName;
	}

	/**
	 * Sets the exporter name.
	 * 
	 * @param exporterName the new exporter name
	 */
	public void setExporterName(String exporterName)
	{
		this.exporterName = exporterName;
	}

	/**
	 * Gets the exporter version.
	 * 
	 * @return the exporter version
	 */
	public String getExporterVersion()
	{
		return exporterVersion;
	}

	/**
	 * Sets the exporter version.
	 * 
	 * @param exporterVersion the new exporter version
	 */
	public void setExporterVersion(String exporterVersion)
	{
		this.exporterVersion = exporterVersion;
	}

	/**
	 * Gets the exporter date.
	 * 
	 * @return the exporter date
	 */
	public String getExporterDate()
	{
		return exporterDate;
	}

	/**
	 * Sets the exporter date.
	 * 
	 * @param exporterDate the new exporter date
	 */
	public void setExporterDate(String exporterDate)
	{
		this.exporterDate = exporterDate;
	}

	/**
	 * Gets the exporter time.
	 * 
	 * @return the exporter time
	 */
	public String getExporterTime()
	{
		return exporterTime;
	}

	/**
	 * Sets the exporter time.
	 * 
	 * @param exporterTime the new exporter time
	 */
	public void setExporterTime(String exporterTime)
	{
		this.exporterTime = exporterTime;
	}

	/**
	 * Gets the publisher name.
	 * 
	 * @return the publisher name
	 */
	public String getPublisherName()
	{
		return publisherName;
	}

	/**
	 * Sets the publisher name.
	 * 
	 * @param publisherName the new publisher name
	 */
	public void setPublisherName(String publisherName)
	{
		this.publisherName = publisherName;
	}

	/**
	 * Gets the parsed system name.
	 * 
	 * @return the parsed system name
	 */
	public String getParsedSystemName()
	{
		return parsedSystemName;
	}

	/**
	 * Sets the parsed system name.
	 * 
	 * @param parsedSystemName the new parsed system name
	 */
	public void setParsedSystemName(String parsedSystemName)
	{
		this.parsedSystemName = parsedSystemName;
	}

	/**
	 * Gets the extraction level.
	 * 
	 * @return the extraction level
	 */
	public String getExtractionLevel()
	{
		return extractionLevel;
	}

	/**
	 * Sets the extraction level.
	 * 
	 * @param extractionLevel the new extraction level
	 */
	public void setExtractionLevel(String extractionLevel)
	{
		this.extractionLevel = extractionLevel;
	}

	/**
	 * Gets the source language.
	 * 
	 * @return the source language
	 */
	public String getSourceLanguage()
	{
		return sourceLanguage;
	}

	/**
	 * Sets the source language.
	 * 
	 * @param sourceLanguage the new source language
	 */
	public void setSourceLanguage(String sourceLanguage)
	{
		this.sourceLanguage = sourceLanguage;
	}

	/**
	 * Gets the source dialect.
	 * 
	 * @return the source dialect
	 */
	public String getSourceDialect()
	{
		return sourceDialect;
	}

	/**
	 * Sets the source dialect.
	 * 
	 * @param sourceDialect the new source dialect
	 */
	public void setSourceDialect(String sourceDialect)
	{
		this.sourceDialect = sourceDialect;
	}

}
