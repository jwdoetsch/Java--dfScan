/*
 * Copyright (C) 2014 Jacob Wesley Doetsch
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.doetsch.dfscan.core;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.HashableFile;

/**
 * Report is the realization of a dfScan duplicate file scan results
 * report.
 * 
 * Report cannot be directly instantiated; it's factory method
 * createReport(..) will build a new report reflecting the system's
 * current time, date, host name, and the user who scheduled the scan,
 * and of course the results of the scan (the duplicate file groups)
 * 
 * @author Jacob Wesley Doetsch
 */
public class Report {
	
	private String profileName;
	private String startDate;
	private String startTime;
	private String finishDate;
	private String finishTime;
	private int detectionTime;
	private String fileName;
	private String host;
	private String user;
	private ArrayList<ContentIndex> groups;

	/**
	 * Creates a new Report reflecting the current date, time, host name,
	 * and user name, and the given groups of duplicate files.
	 * 
	 * @param dupeGroups the groups of duplicate files
	 * @param detectionTime 
	 * @return the new Report instance
	 */
	public static Report generate (String detectionProfileName, ArrayList<ContentIndex> dupeGroups, Date start, Date finish) {
		return new Report(detectionProfileName, dupeGroups, start, finish);
	}
	
	/*
	 * Creates a new Report reflecting the current date, time, host name,
	 * and user name, and the given groups of duplicate files.
	 * 
	 * @param dupeGroups the groups of duplicate files
	 */
	private Report (String detectionProfileName, ArrayList<ContentIndex> dupeGroups, Date start, Date finish) {
		
		try {
			this.detectionTime  = (int)((finish.getTime() - start.getTime()) / 1000);
		} catch (Exception e) {
			
		}
		
		this.groups = new ArrayList<ContentIndex>();
		
		for (ContentIndex index : dupeGroups) {
			//addGroup(index);
			this.groups.add(index);
		}
		
		if (start != null) {
			this.startDate = (new SimpleDateFormat("MM/dd/yyyy")).format(start);
			this.startTime = (new SimpleDateFormat("hh:mm:ss a z")).format(start);
		}
		
		if (finish != null) {
			this.finishDate = (new SimpleDateFormat("MM/dd/yyyy")).format(finish);
			this.finishTime = (new SimpleDateFormat("hh:mm:ss a z")).format(finish);
		}

		
//		this.detectionTime = String.valueOf(detectionTime);
		
		try {
			this.host = InetAddress.getLocalHost().getHostName();
			
		} catch (UnknownHostException e) {
			//TODO
			e.printStackTrace();
		}
		
		this.user = System.getProperty("user.name");
		
		this.fileName = "dfScan Report "
				+ (new SimpleDateFormat("MM-dd-yyyy HH_mm_ss")).format(new Date())
				+ ".dfscan.report.xml";
		
		this.profileName = detectionProfileName;
				
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate () {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate (String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime () {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime (String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the finishDate
	 */
	public String getFinishDate () {
		return finishDate;
	}

	/**
	 * @param finishDate the finishDate to set
	 */
	public void setFinishDate (String finishDate) {
		this.finishDate = finishDate;
	}

	/**
	 * @return the finishTime
	 */
	public String getFinishTime () {
		return finishTime;
	}

	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime (String finishTime) {
		this.finishTime = finishTime;
	}

	
	/**
	 * Set the time elapsed during the scan.
	 * 
	 * @return the elapsed scan time (in seconds)
	 */
	public int getDetectionTime () {
		return detectionTime;
	}

	/**
	 * Returns the time elapsed, in seconds, during the scan.
	 * 
	 * @param detectionTime the elapsed time (in seconds)
	 */
	public void setDetectionTime (int detectionTime) {
		this.detectionTime = detectionTime;
	}

	/**
	 * Returns the host name of the machine that generated the report
	 * 
	 * @return a String representation of the host name
	 */
	public String getHost () {
		return host;
	}
	
	/**
	 * Sets the host name of the report.
	 * 
	 * @param host the new host name String
	 */
	public void setHost (String host) {
		this.host = host;
	}
	
	/**
	 * Returns the name of the detection profile used to generate the report
	 * 
	 * @return a String representation of the detection profile's name
	 */
	public String getProfileName () {
		return profileName;
	}
	
	/**
	 * Setes the name of the detection profile used to generate the report
	 * 
	 * @param name the name of the detection profile
	 */
	public void setProfileName (String name) {
		this.profileName = name;
	}

	/**
	 * Returns the user who generated the report
	 * 
	 * @return a String representation of the username of the user
	 */
	public String getUser () {
		return user;
	}

	/**
	 * Sets the user name of the report.
	 * 
	 * @param user the new user name String
	 */
	public void setUser (String user) {
		this.user = user;
	}
	
	/**
	 * Returns the collection of duplicate file groups encapsulated
	 * within the report.
	 * 
	 * @return an ArrayList collection of ContentIndex instances, each
	 * representing a group of files that are considered duplicate
	 */
	public ArrayList<ContentIndex> getGroups () {
		return groups;
	}

	/**
	 * Adds a group to the collection of duplicate file groups.
	 * 
	 * @param group the ContentIndex representing the group of files to add
	 */
	public void addGroup (ContentIndex group) {
		this.groups.add(group);
	}
	
	/**
	 * Returns the file name associated with the Report.
	 * 
	 * @return a String representation of the filename
	 */
	public String getFileName () {
		return fileName;
	}

	/**
	 * Sets the file name to associate with the Report.
	 * 
	 * @param fileName the file name to associate with the Report
	 */
	public void setFileName (String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Returns a string representation of the Report
	 */
	public String toString () {

		String report = "Start Time: " + this.startDate + " at " + this.startTime + "\n";
		report += "Finish Time: " + this.finishDate + " at " + this.finishTime + "\n";
		report += "Elapsed Time: " + this.detectionTime + " seconds " + "\n";
		report += "Host: " + this.host + "\n";
		report += "User: " + this.user + "\n";;
		report += "Groups: " + this.groups.size() + "\n";
		
		int fileCount = 0;
		for (ContentIndex index : this.groups) {
			for (HashableFile file : index) {
				fileCount++;
			}
		}
		
		report += "Files: " + fileCount + "\n";
		
		for (ContentIndex index : this.groups) {
			report += index.toString() + "\n";
		}
			
		return report;		
	}

	/**
	 * Formats and saves an XML representation of the results generated
	 * and returned by an instance of DetectionTask.
	 * 
	 * @param dupeGroups the results of duplicate file detection 
	 */
	public static void save (Report report) {
		
		DocumentBuilderFactory docBuilderFactory;
		DocumentBuilder docBuilder;
		Document document;
		Element rootElement;
		Transformer transformer;
		Element groupsElement;
		Element groupElement;
		
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			return;
		}		
		
		document = docBuilder.newDocument();
		
		/*
		 * Generate the XML root element
		 */
		rootElement = document.createElement("results");

		//Add start date property
		rootElement.setAttribute("start-date", report.getStartDate());
		
		//Add start time property
		rootElement.setAttribute("start-time", report.getStartTime());
		
		//Add start date property
		rootElement.setAttribute("finish-date", report.getFinishDate());
				
		//Add start time property
		rootElement.setAttribute("finish-time", report.getFinishTime());
		
		//Add elapsed scan time property
		rootElement.setAttribute("elapsed-time", String.valueOf(report.getDetectionTime()));

		//Add the name of the local host to the root element
		rootElement.setAttribute("host", report.getHost());
		
		//Add the detection profile used to generate the results
		rootElement.setAttribute("profile", report.getProfileName());

		
		//Add the name of the user who generated the report
		rootElement.setAttribute("user", report.getUser());
		
		//Add the root child to the document
		document.appendChild(rootElement);
		
		
		/*
		 * Add groups element to the document's root element
		 */
		groupsElement = document.createElement("groups");
		rootElement.appendChild(groupsElement);
		
		/*
		 * Iterate through the collection of ContentIndex representations,
		 * writing the contents of each ContentIndex to it's own element.
		 */
		for (ContentIndex index : report.getGroups()) {
			
			groupElement = document.createElement("group");
			
			//Record the common hash of the files within the current ContentIndex
			groupElement.setAttribute("hash", String.valueOf(index.getContents().get(0).getHash()));
			
			//Record the common size of the files within the group
			groupElement.setAttribute("size", String.valueOf(index.getContents().get(0).length()));
			
			/*
			 * Iterate through the contents of the current index, recording new XML elements for
			 * each file
			 */
			for (HashableFile file : index) {
				Element fileEntry = document.createElement("file");
				fileEntry.setAttribute("name", file.getName());
				fileEntry.setAttribute("path", file.getPath());
				groupElement.appendChild(fileEntry);
			}
			
			//Append the finished group to the groups element
			groupsElement.appendChild(groupElement);
			
		}

		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
//			String fileName = "dfScan_results_" + report.getDateStamp()
//					+ "_" + report.getTimeStamp() + ".xml";
			
			
			File resultsFolder = new File("results");
			if (!resultsFolder.exists()) {
				resultsFolder.mkdir();				
			}
			
			transformer.transform(new DOMSource(document),
					new StreamResult(new File("results/" + report.getFileName())));
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	public static Report load (File reportPath) {

		String profileName;
		String detectionTime;
		String startDate;
		String startTime;
		String finishDate;
		String finishTime;
		String fileName = reportPath.getName();
		String host;
		String user;
		ArrayList<ContentIndex> groups = new ArrayList<ContentIndex>();
		Document document;
		
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(reportPath);
			
		} catch (SAXException e) {
			return null;
			
		} catch (IOException e) {
			return null;
			
		} catch (ParserConfigurationException e) {
			return null;
		}
		
		document.getDocumentElement().normalize();
		
		/*
		 * Retrieve the root element <results ..> and record the report's attributes 
		 */
		Node rootNode = document.getElementsByTagName("results").item(0);
		NamedNodeMap rootAttributeNodes = rootNode.getAttributes();
		
		startDate = rootAttributeNodes.getNamedItem("start-date").getNodeValue();
		startTime = rootAttributeNodes.getNamedItem("start-time").getNodeValue();
		
		finishDate = rootAttributeNodes.getNamedItem("finish-date").getNodeValue();
		finishTime = rootAttributeNodes.getNamedItem("finish-time").getNodeValue();
		
		host = rootAttributeNodes.getNamedItem("host").getNodeValue();
		user = rootAttributeNodes.getNamedItem("user").getNodeValue();
		detectionTime = rootAttributeNodes.getNamedItem("elapsed-time").getNodeValue();
		profileName = rootAttributeNodes.getNamedItem("profile").getNodeValue();
		
		/*
		 * Retrieve the groups element <groups>, iterating through it's child elements
		 * for instances of <group> from which to record <file> instances
		 */
		Node groupsNode = document.getElementsByTagName("groups").item(0);
		
		NodeList groupsChildList = groupsNode.getChildNodes();
		Node currentGroupsChildNode;
		
		ContentIndex currentGroup;
		Node currentGroupChildNode;
		NodeList groupChildList;
		
		String filePath;
		HashableFile currentFile;
		
		String groupHash;
		
		
		/*
		 * Iterate through the <groups> element's child nodes for occurrences of 
		 * <group> elements.
		 */
		for (int i = 0; i < groupsChildList.getLength(); i++) {
			
			//Retrieve the next of the <groups> element's sub-elements 
			currentGroupsChildNode = groupsChildList.item(i);
			
			/*
			 * If there is an occurrence of <group> within <groups> then 
			 * record the <group>'s hash attribute and iterate through it's child
			 * nodes for instances of <file> with which to build a new contentIndex
			 */
			if (currentGroupsChildNode.getNodeName().equals("group")) {
				
				//Instantiate a ContentIndex for the current group;			
				currentGroup = new ContentIndex();
				
				//Get the group's common hash value
				groupHash = currentGroupsChildNode.getAttributes().getNamedItem("hash").getNodeValue();			
				
				//Retrieve the current <group> element's sub-elements
				groupChildList = currentGroupsChildNode.getChildNodes();
				
				/*
				 * Iterate through the current <group> element's child nodes for occurrences
				 * of <file> 
				 */
				for (int j = 0; j < groupChildList.getLength(); j++) {
					
					currentGroupChildNode = groupChildList.item(j);
					
					/*
					 * If the child node within the current <group> element is a <file> node
					 * then add a new HashableFile to the current group index according to the
					 * <file> occurrence's attributes
					 */
					if (currentGroupChildNode.getNodeName().equals("file")) {
						filePath = currentGroupChildNode.getAttributes().getNamedItem("path").getNodeValue();
						currentGroup.add(new HashableFile(filePath, groupHash));
					}
					
				}
				
				/*
				 * If the current group index contains two or more elements then add it to
				 * the group index collection.
				 */
				if (currentGroup.getSize() > 1) {
					groups.add(currentGroup);
				}
				
			}
			
		}
		
		
		/*
		 * Build the report
		 */
		Report report = Report.generate(profileName, groups, null, null);
		report.setStartDate(startDate);
		report.setStartTime(startTime);
		report.setFinishDate(finishDate);
		report.setFinishTime(finishTime);
		report.setUser(user);
		report.setHost(host);
		report.setFileName(reportPath.getName());
		report.setDetectionTime(Integer.valueOf(detectionTime));
		//report.setProfileName(profileName);

		return report;

	}
	
}
