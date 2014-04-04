package com.doetsch.dfscan.core;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.doetsch.dfscan.filter.ContentIndexFilter;
import com.doetsch.dfscan.filter.HiddenFileFilter;
import com.doetsch.dfscan.filter.NameContainsFilter;
import com.doetsch.dfscan.filter.PathContainsFilter;
import com.doetsch.dfscan.filter.ReadOnlyFileFilter;
import com.doetsch.dfscan.filter.SizeCeilingFilter;
import com.doetsch.dfscan.filter.SizeFloorFilter;

/**
 * Profile is the realization a detection profile, encapsulating the 
 * options, rules, and settings necessary to perform a duplicate
 * detection scan. 
 * 
 * @author Jacob Wesley Doetsch
 */
public class Profile {
	
	private String name;
	private String description;
	private ArrayList<String> targets;
	private ArrayList<ContentIndexFilter> filters;
	private SettingsContainer settings;
	
	/**
	 * Creates an empty Profile instance.
	 */
	public Profile (String profileName, String profileDescription) {
		this.name = profileName;
		this.description = profileDescription;
		targets = new ArrayList<String>();
		filters = new ArrayList<ContentIndexFilter>();
		settings = new SettingsContainer();
	}
	
	/**
	 * Returns an ArrayList collection of String objects representing
	 * the path of the target folders defined within the profile.
	 * 
	 * @return an ArrayList collection of String representations of folder
	 * paths
	 */
	public ArrayList<String> getTargets() {
		return targets;
	}

	/**
	 * Adds the target folder to the Profile.
	 * 
	 * @param targetFolderPath the path of the folder to add
	 */
	public void addTarget(String targetFolderPath) {
		this.targets.add(targetFolderPath);
	}

	/**
	 * Returns an ArrayList collection of ContentIndexFilter objects
	 * representing the filters defined and contained within the Profile.
	 * 
	 * @return an ArrayList of ContentIndexFilter objects representing
	 * defined filters
	 */
	public ArrayList<ContentIndexFilter> getFilters() {
		return filters;
	}

	/**
	 * Adds the given ContentIndexFilter to the Profile
	 *   
	 * @param filter the filter to add
	 */
	public void addFilter (ContentIndexFilter filter) {
		this.filters.add(filter);
	}

	/**
	 * Returns a SettingsContainer representation of the settings
	 * and options defined within the Profile.
	 * 
	 * @return a SettingsContainer of the settings and options defined
	 * by the profile.
	 */
	public SettingsContainer getSettings() {
		return settings;
	}

	/**
	 * Sets the SetingsContainer representation of the settings
	 * and options to be defined within the Profile.
	 * @param settings
	 */
	public void setSettings(SettingsContainer settings) {
		this.settings = settings;
	}

	/**
	 * @return the name
	 */
	public String getName () {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription () {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription (String description) {
		this.description = description;
	}

	public String getDetailedDescription () {
		
		String detailedDescription = "";
		
		detailedDescription += this.description + "\n";
		detailedDescription += "\nTarget Folders:\n";
		
		for (String targetPath : targets) {
			detailedDescription += "        " + targetPath + "\n";
		}
		
		detailedDescription += "\nFilters:\n";
		
		for (ContentIndexFilter filter : filters) {
			detailedDescription += "        " + filter.getDescription() + "\n";
		}
		
		detailedDescription += "\nSettings & Options:\n";
		
		detailedDescription += "        " +
				(settings.getIndexInclusively() ? "Index inclusively" : "Don't index inclusively") + "\n";
		
		detailedDescription += "        " +
				(settings.getIndexRecursively() ? "Index recursively" : "Don't index exclusively") + "\n";

		detailedDescription += "        " +
				(settings.getIndexHiddenFolders() ? "Index hidden folders" : "Don't index hidden folders") + "\n";

		detailedDescription += "        " +
				(settings.getIndexReadOnlyFolders() ? "Index read-only folders" : "Don't index read-only folders") + "\n";

		
		return detailedDescription;
	}
	
	/**
	 * Returns a String representation of the Profile.
	 */
	public String toString () {
		String returnString = "";
		
		for (String path : targets) {
			returnString += "target path: " + path + "\n";
		}
		
		for (ContentIndexFilter filter : filters) {
			returnString += filter.toString() + "\n";
		}
		
		returnString += settings.toString()+ "\n";
		return returnString;
	}
	
	/**
	 * Parses the XML profile document at the given file-system path and returns 
	 * a Profile representation of it's contents.
	 * 
	 * @param path the URI representation of the profile's path
	 * @return a Profile representation of the profile file's contents  
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static Profile load (String path) throws SAXException, IOException, ParserConfigurationException {
		
		Profile profile;// = new Profile();
		
		File profileFile = new File(path);
//		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//		Document xmlDocument = docBuilder.parse(profileXML);
		Document xmlDocument;
		
		xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(profileFile);
		xmlDocument.getDocumentElement().normalize();

		Node rootNode = xmlDocument.getElementsByTagName("profile").item(0);
		NamedNodeMap rootAttributes = rootNode.getAttributes();
		String profileName = rootAttributes.getNamedItem("name").getNodeValue();
		String profileDescription = rootAttributes.getNamedItem("description").getNodeValue();
		
		profile = new Profile(profileName, profileDescription);
		
		/*
		 * Parse target declarations
		 */
		Node targetsNode = xmlDocument.getElementsByTagName("targets").item(0);
		
		NodeList targetNodeList = targetsNode.getChildNodes();
		
		for (int i = 0; i < targetNodeList.getLength(); i++) {
			Node target = targetNodeList.item(i);
			if (target.hasAttributes()) {
				
				NamedNodeMap attributes = target.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node attribute = attributes.item(j);
					
					if (attribute.getNodeName().equals("path")) {
						profile.getTargets().add(attribute.getNodeValue());
						break;
					}
				}
				
			}
			
		}
		
		/*
		 * Parse filter declarations
		 */
		Node filtersNode = xmlDocument.getElementsByTagName("filters").item(0);
		
		NodeList filterNodeList = filtersNode.getChildNodes();
		
		for (int i = 0; i < filterNodeList.getLength(); i++) {
			Node filter = filterNodeList.item(i);
			
			if (filter.getNodeName().equals("filter")) {
				NamedNodeMap attributeList = filter.getAttributes();
				
				Node mode = attributeList.getNamedItem("mode");
				boolean filterIsInclusive = (mode.getNodeValue().equals("inclusive") ? true : false);
				
				Node type = attributeList.getNamedItem("type");
				String filterType = type.getNodeValue();
				
				Node value = attributeList.getNamedItem("value");
				String filterParam = value.getNodeValue();
				

				switch (type.getNodeValue()) {
					case "name-contains":
						profile.getFilters().add(new NameContainsFilter(filterParam, filterIsInclusive));
						break;
					case "path-contains":
						profile.getFilters().add(new PathContainsFilter(filterParam, filterIsInclusive));
						break;
					case "size-floor":
						profile.getFilters().add(new SizeFloorFilter(Long.valueOf(filterParam), filterIsInclusive));
						break;
					case "size-ceiling":
						profile.getFilters().add(new SizeCeilingFilter(Long.valueOf(filterParam), filterIsInclusive));
						break;
					case "is-hidden":
						profile.getFilters().add(new HiddenFileFilter(filterIsInclusive));
						break;
					case "is-readonly":
						profile.getFilters().add(new ReadOnlyFileFilter(filterIsInclusive));
						break;
				}
			}
		}
		
		
		/*
		 * Parse settings declarations
		 */
		Node settingsNode = xmlDocument.getElementsByTagName("settings").item(0);
		
		NodeList settingNodeList = settingsNode.getChildNodes();
		
		
		for (int i = 0; i < settingNodeList.getLength(); i++) {
			Node settingNode = settingNodeList.item(i);
			
			if (settingNode.getNodeName().equals("index_inclusively") ||
				settingNode.getNodeName().equals("index_recursively") ||
				settingNode.getNodeName().equals("index_hidden_folders") ||
				settingNode.getNodeName().equals("index_readonly_folders")) {
				
				Node attribute = settingNode.getAttributes().getNamedItem("value");
				
				String attributeValue = attribute.getNodeValue();
				
				switch (settingNode.getNodeName()) {
					case "index_inclusively":
						profile.getSettings().setIndexInclusively(attributeValue.equals("yes"));
						break;
					case "index_recursively":
						profile.getSettings().setIndexRecursively(attributeValue.equals("yes"));
						break;
					case "index_hidden_folders":
						profile.getSettings().setIndexHiddenFolders(attributeValue.equals("yes"));
						break;
					case "index_readonly_folders":
						profile.getSettings().setIndexReadOnlyFolders(attributeValue.equals("yes"));
						break;
				}

				
			}
			
		}
		
		return profile;
		
	}


	/**
	 * Formats and saves an XML representation of the given Profile.
	 * 
	 * @param profile the Profile to save
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 */
	public static void save (Profile profile) throws TransformerFactoryConfigurationError, TransformerException {
		
		DocumentBuilderFactory docBuilderFactory;
		DocumentBuilder docBuilder;
		Document document;
		Element rootElement;
		Transformer transformer;
		Element targetsElement;
		Element targetElement;
		Element filtersElement;
		Element filterElement;
		Element settingsElement;
		Element settingElement;
		
		
		
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			return;
		}
		
		document = docBuilder.newDocument();
		
		/*
		 * Instantiate and define root element
		 */
		rootElement = document.createElement("profile");
		rootElement.setAttribute("name", profile.getName());
		rootElement.setAttribute("description", profile.getDescription());
		
		
		/*
		 * Instantiate and add detection scan targets
		 */
		rootElement.appendChild(document.createComment("Define the target folders to be scanned"));
		targetsElement = document.createElement("targets");
		
		for (String targetPath : profile.getTargets()) {
			targetElement = document.createElement("folder");
			targetElement.setAttribute("path", targetPath);
			targetsElement.appendChild(targetElement);
		}
		
		rootElement.appendChild(targetsElement);
		
		
		
		/*
		 * Instantiate and add detection filters
		 */
		rootElement.appendChild(document.createComment("Define the filters to use during scanning"));
		filtersElement = document.createElement("filters");
		
		for (ContentIndexFilter filter : profile.getFilters()) {
			filterElement = document.createElement("filter");
			filterElement.setAttribute("mode",
					(filter.isInclusive() ? "inclusive" : "exclusive"));
			filterElement.setAttribute("type", filter.getType());
			filterElement.setAttribute("value", filter.getQualifierValue().toString());
			
			filtersElement.appendChild(filterElement);
		}
		
		rootElement.appendChild(filtersElement);
		
		/*
		 * Instantiate and add indexing settings and options 
		 */
		rootElement.appendChild(document.createComment("Define indexing settings and options"));
		settingsElement = document.createElement("settings");
		
		settingElement = document.createElement("index_inclusively");
		settingElement.setAttribute("value", (profile.getSettings().getIndexInclusively() ? "yes" : "no"));
		settingsElement.appendChild(settingElement);
		
		settingElement = document.createElement("index_recursively");
		settingElement.setAttribute("value", (profile.getSettings().getIndexRecursively() ? "yes" : "no"));
		settingsElement.appendChild(settingElement);
		
		settingElement = document.createElement("index_hidden_folders");
		settingElement.setAttribute("value", (profile.getSettings().getIndexHiddenFolders() ? "yes" : "no"));
		settingsElement.appendChild(settingElement);
		
		settingElement = document.createElement("index_readonly_folders");
		settingElement.setAttribute("value", (profile.getSettings().getIndexReadOnlyFolders() ? "yes" : "no"));
		settingsElement.appendChild(settingElement);
		
		
		rootElement.appendChild(settingsElement);
		
		document.appendChild(rootElement);
		
		/*
		 * Transform and write out the XML document
		 */
		
		transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");


		String fileName = fileNameTransformer(profile.getName());
		
		File resultsFolder = new File("profiles");
		if (!resultsFolder.exists()) {
			resultsFolder.mkdir();				
		}
		
		transformer.transform(new DOMSource(document),
				new StreamResult(
						new File("profiles/" + fileName + ".dfscan.profile.xml")));
	}
	
	private static String fileNameTransformer (String sourceName) {
		String newName = "";
		int charCode;
		
		for (int i = 0; i < sourceName.length(); i++) {
			
			charCode = (int) sourceName.charAt(i);
			
			/*
			 * Valid file name characters are a-z, A-Z, 0-9, _, space, and
			 * -.
			 */
			if (((charCode > 47) && (charCode < 58)) ||
			 ((charCode > 64) && (charCode < 91)) ||
			 ((charCode > 96) && (charCode < 123)) ||
			 (charCode == 32) || (charCode == 95) || (charCode == 45)) {
				
				newName += sourceName.substring(i, i + 1);
				
			}
		}
		
		
			
		
		
		return newName;
	}
	
}
