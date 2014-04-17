package com.doetsch.dfscan.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import com.doetsch.dfscan.DFScan;
import com.doetsch.dfscan.filter.ContentIndexFilter;
import com.doetsch.dfscan.util.ContentIndex;
import com.doetsch.dfscan.util.HashableFile;
import com.doetsch.dfscan.window.ProgressPanel;

/**
 * DetectionTask is a SwingWorker thread enveloping the detection and
 * scanning procedures necessary to detect and scan for duplicate files.
 * It contains numerous nested SwingWorker threads that assist in indexing,
 * scanning, and detection.
 * 
 * DetectionTask relies on the given Profile to outline the targets, filters,
 * options, and settings that are to guide the detection process.
 */
public class DetectionTask extends SwingWorker<Report, String> {

	/**
	 * EqualityTester defines an interface by which to compare HashabileFile
	 * instances to determine equality.
	 */
	private abstract class EqualityTester {
		
		/**
		 * Implementations of this method are intended to test the equality
		 * and equivalence between the given HashableFile instances.
		 * 
		 * @param a the first HashableFile instance
		 * @param b the second HashableFile instance
		 */
		protected abstract boolean areEqual (HashableFile a, HashableFile b);
		
	}
	
	/**
	 * Grouper provides a mechanism with which to compare and group collections of
	 * HashabileFile instances. The collection is sorted according to the given
	 * Comparator implementation. The equality and equivalence of the HashabileFile
	 * instances encapsulated in the collection is determined by the implementation
	 * of the given EqualityTester. In addition, upon instantiation, a minimum group
	 * size floor limit is declared.
	 */
	private class Grouper {
		private Comparator<HashableFile> comparator;
		private EqualityTester equalityTester;
		private int minimumGroupSize;
		
		/**
		 * Creates as new Grouper instance.
		 * 
		 * @param comparator the Comparator by which to sort the source ContentIndex
		 * when group(..) is called
		 * @param equalityTester the EqualityTester by which to determine equivalence
		 * when group(..) is called
		 * @param minimumGroupSize the minimum size of a group, groups with sizes less
		 * than this limit will be excluded from the results passed by group(..)
		 */
		private Grouper (Comparator<HashableFile> comparator, EqualityTester equalityTester, int minimumGroupSize) {
			this.comparator = comparator;
			this.equalityTester = equalityTester;
			this.minimumGroupSize = minimumGroupSize;
		}
		
		/**
		 * Sorts the contents of the given ContentIndex according to the Comparator
		 * supplied on construction, determines contents' equivalence according to the
		 * EqualityTester implementation supplied upon construction, groups equivalent
		 * contents into separate ContentIndex instances (excluding groups with less than
		 * the supplied limit of members), and finally returns an ArrayList collection of
		 * the ContentIndex instances representing the separate groups.
		 * 
		 * @param sourceIndex the ContentIndex encapsulating the HashabileFile collection
		 * that is to be sorted and grouped
		 * @return the ArrayList collection of the groups of common contents
		 */
		private ArrayList<ContentIndex> group (ContentIndex sourceIndex) {

			//ContentIndex culledIndex = new ContentIndex();
			ContentIndex targetIndex = sourceIndex.copy();
			ContentIndex tempIndex = new ContentIndex();
			ArrayList<ContentIndex> indexGroups = new ArrayList<ContentIndex>();
			
			/*
			 * Sort the ContentIndex targetIndex contents by the sizes of the files
			 * that the HashableFile representations encapsulate
			 */
			Collections.sort(targetIndex.getContents(), comparator);
			
			/*
			 * Iterates through the FileHashPair objects, creating a FileIndex
			 * including any FileHashPair objects representing files with common
			 * sizes. 
			 */
			while (targetIndex.getContents().size() > 0) {

				/*
				 * If the DetectionTask has been cancelled then bail.
				 */
				if (DetectionTask.this.isCancelled()) {
					return null;
				}
				
				/*
				 * Remove the first index from the targetIndex and add it to a temporary
				 * index. 
				 */
				tempIndex.add(targetIndex.getContents().remove(0));

				/*
				 * Iterate through the remaining contents of the targetIndex
				 */
				while (targetIndex.getContents().size() > 0) {

					if (DetectionTask.this.isCancelled()) {
						return null;
					}
					
					//Get a pointer to the first element remaining in the targetIndex
					HashableFile file = targetIndex.getContents().get(0);

					/*
					 * If the first element is considered equal to the file(s) already within
					 * the tempIndex then remove that element and add it to the tempIndex
					 */
					if (equalityTester.areEqual(file, tempIndex.getContents().get(0))) {
						tempIndex.add(targetIndex.getContents().remove(0));
						
					/*
					 * If the first element doesn't have a file size in common with the 
					 * file(s) in the tempIndex then handle the contents of the tempIndex
					 * and point the tempIndex to a new empty ContentIndex instance
					 */
					} else {
						
						/*
						 * tempIndex now contains HashableFile encapsulations of files that
						 * have sizes in common. If there is more than one element in the 
						 * tempIndex then the tempIndex is representative of a group of 
						 * files with common size and it's contents will be merged into the
						 * culledIndex.
						 */
						if (tempIndex.getSize() >= minimumGroupSize) {
							indexGroups.add(tempIndex);
						}
						
						tempIndex = new ContentIndex();
						break;
					}
				}
			}
			
			/*
			 * If, after finishing iterating through the target index, the remaining
			 * tempIndex qualifies as a group representation (size > 1) then record it's
			 * contents
			 */
			if (tempIndex.getSize() >= minimumGroupSize) {
				indexGroups.add(tempIndex);
			}
			
			return indexGroups;
		}
			
	}
	
	/**
	 * SubWorker defines an abstract interface extension of a SwingWorker
	 * that encapsulates a single ContentIndex provided upon construction
	 * under the assumption that classes implementing SubWorker will be doing
	 * a task with regards to that source ContentIndex.
	 * 
	 * In addition, SubWorker defines the behavior of the SwingWorker extension,
	 * returning a ContentIndex upon completion of it's background task thread
	 * and defining the behavior of publish(..) calls such that
	 * String instances are to be passed.
	 */
	private abstract class SubWorker extends SwingWorker <ContentIndex, String> {
		
		private ContentIndex sourceIndex;
		
		/**
		 * Creates a SubWorker instance defining 
		 * @param sourceIndex
		 */
		private SubWorker (ContentIndex sourceIndex) {
			this.sourceIndex = sourceIndex;
		}
		
		private ContentIndex getSourceIndex () {
			return this.sourceIndex;
		}

		private void setSourceIndex (ContentIndex sourceIndex) {
			this.sourceIndex = sourceIndex;
		}

		/**
		 * Catches publish()'ed log entries and handles them according
		 * to defined log behavior.
		 * @see javax.swing.SwingWorker#process(java.util.List)
		 */
		protected void process (List<String> entries) {
			for (String entry : entries) {
				DetectionTask.this.publish(entry);
			}
		}
		
	}
	
	/**
	 * IndexBuilder is a SwingWorker thread responsible for generating a ContentIndex
	 * representation of the contents of the target folders specified by the given
	 * Profile. It indexes according to the options, settings, and rules also defined
	 * by the given Profile.
	 */
	private class IndexBuilder extends SubWorker {

		/**
		 * Creates an IndexBuilder instance that will do some work with regards to the
		 * contents of the given source ContentIndex.
		 */
		private IndexBuilder (ContentIndex sourceIndex) {
			super(sourceIndex);
		}
		
		/**
		 * Generates a ContentIndex representation of the target folders specified
		 * by the given Profile.
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected ContentIndex doInBackground () throws Exception {
			
			ContentIndex primaryIndex = new ContentIndex();
			ContentIndex tempIndex = new ContentIndex();
			
			/*
			 * Iterate through the list of target folder paths, build an index
			 * of each, merge the indexes into the primary index.
			 */
			for (String targetFolderPath : detectionProfile.getTargets()) {
				tempIndex = buildIndex(targetFolderPath);
				
				/*
				 * If the DetectionTask has been cancelled then bail.
				 */
				if (DetectionTask.this.isCancelled()) {
					return null;
				}
				
				primaryIndex.merge(tempIndex);
			}
			
			return primaryIndex;
		}
		
		/**
		 * A class-private helper method that generates a ContentIndex representation
		 * of the folder at the given path.
		 * 
		 * @param targetFolderPath the path of the folder to index
		 */
		private ContentIndex buildIndex (String targetFolderPath) {
			
			ContentIndex index = new ContentIndex();
			File targetFolder = new File(targetFolderPath);
			File[] targetFolderContents = null;
			
			targetFolderContents = targetFolder.listFiles();
			
			/*
			 * If the target folder contents can't be enumerated then bail
			 */
			if (targetFolderContents == null) {
				//publish("Can't list the contents of the folder at " + targetFolderPath);
				publish("Can't list the contents of the folder at " + targetFolderPath);
				
				/*
				 * Determine and report why
				 */
				if (!targetFolder.exists()) {
					publish(targetFolderPath + " doesn't exist.");
					
				} else if (!targetFolder.canRead()) {
					publish(targetFolderPath + " can't be read.");
				}
				
				return index;
			}
			
			publish("Indexing " + targetFolderPath);
			
			/*
			 * Iterate through the contents of the target folder, building the
			 * ContentIndex according to the specifications of the detection profile.
			 */
			for (File f : targetFolderContents) {
		
				
				/*
				 * If the DetectionTask has been cancelled then bail.
				 */
				if (DetectionTask.this.isCancelled()) {
					return null;
				}
				
				if (f.isFile()) {
					
					/*
					 * Add a new HashableFile instance representing the current File f
					 */
					index.merge(new HashableFile(f.getPath()));					
					
				} else if (f.isDirectory()) {
					
					/*
					 * Skip the folder if the profile specifies not to recurse sub-folders 
					 */
					if (!detectionProfile.getSettings().getIndexRecursively()) {
						continue;
					}
					
					/*
					 * Skip the folder if the profile specifies not to index hidden folders
					 * and the folder is hidden
					 */
					if (!detectionProfile.getSettings().getIndexHiddenFolders() && 
							f.isHidden()) {
						continue;
					}
					
					/*
					 * Skip the folder if the profile specifies not to index read-only folders
					 * and the folder is read-only
					 */
					if (!detectionProfile.getSettings().getIndexReadOnlyFolders() &&
							!f.canWrite()) {
						continue;
					}
					
					/*
					 * Build an index of the contents of the folder recursively and merge
					 * them into the index.
					 */
					index.merge(buildIndex(f.getPath()));
					
				}
			
			}
			
			return index;			
		}
		
	}
	
	
	/**
	 * IndexFilterer is a SubWorker task responsible for applying the filters
	 * defined by the given Profile. When executed, IndexFilterer will return
	 * a new ContentIndex instance containing the contents of the supplied
	 * ContentIndex sourceIndex which qualify and pass filtering.
	 * 
	 * Filtering is non-destructive to the supplied ContentIndex sourceIndex.
	 */
	private class IndexFilterer extends SubWorker {

		/**
		 * Create an IndexFilterer instance.
		 * 
		 * @param sourceIndex the ContentIndex to filter
		 */
		private IndexFilterer (ContentIndex sourceIndex) {
			super(sourceIndex);
		}
		
		/**
		 * Returns a ContentIndex representation of the contents of the 
		 * source index that qualify and pass filtering.
		 * 
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected ContentIndex doInBackground () throws Exception {
			
			ContentIndex filteredIndex;
			
			/*
			 * If primary indexing is to take place inclusively (that all indexed
			 * files will be added to the primary index before any filtering) then
			 * fill the running filter index with copies of the contents of the 
			 * built source index.
			 */
			if (detectionProfile.getSettings().getIndexInclusively()) {
				filteredIndex = super.getSourceIndex().copy();
				
			} else {
				filteredIndex = new ContentIndex();
			}
					
			
			for (ContentIndexFilter filter : detectionProfile.getFilters()) {
				
				publish("Applying " + filter.getDescription() + "...");
				
				/*
				 * If the DetectionTask has been cancelled then bail.
				 */
				if (DetectionTask.this.isCancelled()) {
					return null;
				}
				/*
				 * If the filter is inclusive then merge in any files from the source index
				 * that qualify through the filter to the running filtered index. 
				 */
				if (filter.isInclusive()) {
					filteredIndex.merge(filter.enforce(super.getSourceIndex()));
					
				/*
				 * If the filter is exclusive then remove any occurrences of the files qualified
				 * by the filter enforced on the running filter index from the running filter index
				 * itself. 
				 */
				} else {
					for (HashableFile f : filter.enforce(filteredIndex)) {
						filteredIndex.remove(f.getPath());
					}
				}
			}
			
			return filteredIndex;
		}
	
	}
	
	
	/**
	 * IndexCullerBySize is a SubWorker responsible for extracting the contents of
	 * the supplied ContentIndex sourceIndex that represent a file-system file that
	 * doesn't contain a common file size. IndexCullerBySize returns a ContentIndex
	 * representation of any source index file representation that has a file size in
	 * common with at least one other file in the source index.
	 * 
	 * Whereas files are considered duplicates if they have the same contents, files
	 * without a common file size can't possibly be a duplicate of another.
	 * 
	 * Culling is non-destructive to the source index.
	 */
	private class IndexCullerBySize extends SubWorker {

		/**
		 * Creates an IndexCullerbySize index.
		 * 
		 * @param sourceIndex the ContentIndex to be culled
		 */
		private IndexCullerBySize (ContentIndex sourceIndex) {
			super (sourceIndex);
		}
		
		/**
		 * Returns a ContentIndex representation of files contained with the
		 * source index that have a file size in common with at least one other
		 * file in the source index.
		 *
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected ContentIndex doInBackground() throws Exception {
			
			ContentIndex culledIndex = new ContentIndex();
			
			Grouper grouperBySize = new Grouper(new Comparator<HashableFile> () {

				@Override
				public int compare(HashableFile a, HashableFile b) {
					return Long.compare(a.length(), b.length());
				}
				
			}, new EqualityTester() {

				@Override
				protected boolean areEqual(HashableFile a, HashableFile b) {
					return a.length() == b.length();
				}
				
			}, 2);
			
			for (ContentIndex index : grouperBySize.group(super.sourceIndex.copy())) {
				
				for (HashableFile file : index) {
					
					/*
					 * If the DetectionTask has been cancelled then bail.
					 */
					if (DetectionTask.this.isCancelled()) {
						return null;
					}
					
					culledIndex.add(file);
				}
			}
			
			return culledIndex;
			
		}
	}
	
	
	/**
	 * IndexHasher is a SubWorker responsible for generating and adding hashes
	 * to the contents of the given ContentIndex sourceIndex.
	 * 
	 * Hashing is not destructive to the source index.
	 */
	private class IndexHasher extends SubWorker {

		/**
		 * Create an IndexHasher instance.
		 * 
		 * @param sourceIndex the ContentIndex to generate hashes for
		 */
		private IndexHasher (ContentIndex sourceIndex) {
			super(sourceIndex);
		}
		
		/**
		 * Calculates the hash value of the given file File.
		 * @param file the File to hash.
		 * @param algorithm the hashing algorithm. Valid options include:
		 * MD2, MD5, SHA-1, SHA-256, SHA-384, and SHA-512.
		 * @return a hexadecimal string representation of the computed hash.
		 */
		public String hash (File file, String algorithm) {
			/* 
			 * readBuffer encapsulates the bytes read from inputStream.
			 * Then it's processed through the messageDigest to generate
			 * the specified hash. The array size of readBuffer could 
			 * possibly affect read performance or hashing performance. 
			 */
			byte[] hash;								//store the byte array representation of the hash
			byte[] readBuffer = new byte[1024 * 1024];
			int bytesRead;								//number of bytes read into readBuffer

			StringBuilder hashStringBuilder;			//implemented to build up the hex representation of the hash
			Formatter hashStringFormatter;
			MessageDigest messageDigest;				//encapsulates and generates the hash
			BufferedInputStream inputStream;			//the unbuffered FileInputStream is much too slow
			
			publish("Hashing " + file.getPath() + "...");
			
			try {
				messageDigest = MessageDigest.getInstance(algorithm);
				inputStream = new BufferedInputStream(new FileInputStream(file));
				//read n(bytesRead) bytes to the read buffer and update the MessageDigest 
				//	with the new n bytes.
				while ((bytesRead = inputStream.read(readBuffer)) != -1) {
					
					/*
					 * If the DetectionTask has been cancelled then bail.
					 */
					if (DetectionTask.this.isCancelled()) {
						return "";
					}
					
					messageDigest.update(readBuffer, 0, bytesRead);
					
				}
				
				inputStream.close();
				
				hash = messageDigest.digest();
				hashStringBuilder = new StringBuilder(hash.length * 2);  
			    hashStringFormatter = new Formatter(hashStringBuilder);  
			    
			    for (byte b : hash) {  
			    	hashStringFormatter.format("%02x", b);
			    	
			    } 
			    hashStringFormatter.close();
				
				return hashStringBuilder.toString();

			} catch (NoSuchAlgorithmException e) {
				publish (e.toString());
				return "";
				
			} catch (FileNotFoundException e) {
				publish("Can't open \"" + file.getPath()
						+ "\". Check file system permissions.");
				return "";
				
			} catch (IOException e) {
				publish("Can't read \"" + file.getPath() 
						+ "\". Check file system permissions.");
				return "";
			}
			
		}
		
		/**
		 * Hashes and returns a ContentIndex representation of the encapsulated
		 * HashableFile contents of the source index.
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected ContentIndex doInBackground() throws Exception {
			ContentIndex hashedIndex = super.getSourceIndex().copy();
			
			/*
			 * Generate and store a hash for all the contents of the hashedIndex
			 */
			for (HashableFile f : hashedIndex) {
				
				/*
				 * If the DetectionTask has been cancelled then bail.
				 */
				if (DetectionTask.this.isCancelled()) {
					return null;
				}
				
				f.setHash(hash(f, "MD5"));
			}
			
			return hashedIndex;
		}
		
	}
	
	
	/**
	 * IndexCullerByHash is a SubWorker responsible for extracting the contents
	 * of the given ContentIndex source index that don't have a hash in common
	 * with any other file in the ContentIndex.
	 * 
	 * Culling is non-destructive to the source index.
	 */
	private class IndexCullerByHash extends SubWorker {

		/**
		 * Creates an IndexCullerbyHash instance.
		 * 
		 * @param sourceIndex the ContentIndex to cull
		 */
		private IndexCullerByHash (ContentIndex sourceIndex) {
			super(sourceIndex);
		}
		
		
		/**
		 * Returns a ContentIndex representation of files contained with the
		 * source index that have a hash value in common with at least one other
		 * file in the source index.
		 *
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected ContentIndex doInBackground() throws Exception {
			ContentIndex culledIndex = new ContentIndex();
			
			Grouper grouperByHash = new Grouper(new Comparator<HashableFile> () {

				@Override
				public int compare(HashableFile a, HashableFile b) {
					return a.getHash().compareTo(b.getHash());
				}
				
			}, new EqualityTester() {

				@Override
				protected boolean areEqual(HashableFile a, HashableFile b) {
					return a.getHash().equals(b.getHash());
				}
				
			}, 2);
			
			for (ContentIndex index : grouperByHash.group(super.sourceIndex.copy())) {
				for (HashableFile file : index) {
					
					/*
					 * If the DetectionTask has been cancelled then bail.
					 */
					if (DetectionTask.this.isCancelled()) {
						return null;
					}
					
					culledIndex.add(file);
				}
			}
			
			return culledIndex;
			
		}
	}
	
	
	private Profile detectionProfile;
	private ProgressPanel parentWindow;
	
	private ContentIndex sourceIndex;
	private ContentIndex filteredIndex;
	private ContentIndex culledBySizeIndex;
	private ContentIndex hashedIndex;
	private ContentIndex culledByHashIndex;
	private ArrayList<ContentIndex> indexGroups;
	private long detectionTime;
	private long startTime;
	
	//Indexing subtask status flags
	private final String INDEX_BUILDER_STARTED = "Building index...";
	private final String INDEX_BUILDER_FINISHED = "Finished building index.";
	private final String INDEX_FILTERER_STARTED = "Filtering index...";
	private final String INDEX_FILTERER_FINISHED = "Finished filtering index";
	private final String INDEX_CULLERBYSIZE_STARTED = "Refining by common size...";	//"Purifying" ? 
	private final String INDEX_CULLERBYSIZE_FINISHED = "Finished refiing by common size.";
	private final String INDEX_HASHER_STARTED = "Hashing suspects...";
	private final String INDEX_HASHER_FINISHED = "Finished hashing suspects.";
	private final String INDEX_CULLERBYHASH_STARTED = "Refining by common hash..."; //"Purifying" ?
	private final String INDEX_CULLERBYHASH_FINISHED = "Finished refining by common hash.";
	private final String INDEX_GROUP_RESULTS_STARTED = "Grouping duplicate files...";
	private final String INDEX_GROUP_RESULTS_FINISHED = "Finished grouping duplicate files.";
	
	//Indexing status icons
	private final ImageIcon PROGRESS_ICON =
			new ImageIcon(DFScan.class.getResource("resources/icons/progress_icon.gif"));
	private final ImageIcon YIELD_ICON = 
			new ImageIcon(DFScan.class.getResource("resources/icons/yield_icon.png"));
	private final ImageIcon SUCCESS_ICON = 
			new ImageIcon(DFScan.class.getResource("resources/icons/success_icon.png"));
	
	/**
	 * Create a new DetectionTask instance.
	 * 
	 * @param detectionProfile the Profile defining the targets, settings, and options
	 * that will guide the detection process
	 * @param progressPanel the AppWindow UI component that is considered the DetectionTask's
	 * parent, pop-up windows will be spawned with regard to the parentWindow.
	 */
	public DetectionTask (Profile detectionProfile, ProgressPanel progressPanel) {
		this.detectionProfile = detectionProfile;
		this.parentWindow = progressPanel;
	}

	/**
	 * Returns an ArrayList collection of ContentIndex instances containing groups of
	 * HashableFiles at represent files that have common hash values. Each ContentIndex
	 * contains HashableFiles that have one hash value in common.
	 * 
	 * @param sourceIndex the ContentIndex the contents of which will be grouped
	 * @return an ArrayList collection of ContentIndex instances
	 */
	private ArrayList<ContentIndex> groupFilesByHash (ContentIndex sourceIndex) {
		
		Grouper grouperByHash = new Grouper(new Comparator<HashableFile> () {

			@Override
			public int compare(HashableFile a, HashableFile b) {
				return a.getHash().compareTo(b.getHash());
			}

		}, new EqualityTester() {

			@Override
			protected boolean areEqual(HashableFile a, HashableFile b) {
				return a.getHash().equals(b.getHash());
			}

		}, 1);
		
		return grouperByHash.group(sourceIndex.copy());
		
	}
	
	private ContentIndex enactSubWorker (SubWorker worker) {
		worker.execute();

		try {
			return worker.get();
			
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Sub-task interrupted.");
		}
		
		return worker.getSourceIndex();
	}
	
	@Override
	protected Report doInBackground () throws Exception {
		
		startTime = System.currentTimeMillis();
		Date start = new Date();
		
		/*
		 * Generate a source ContentIndex representation of the contents of
		 * the target folders.
		 */
		publish(INDEX_BUILDER_STARTED);
		sourceIndex = enactSubWorker (new IndexBuilder(null));
		publish(INDEX_BUILDER_FINISHED);
		
		/*
		 * If the DetectionTask has been cancelled then bail.
		 */
		if (DetectionTask.this.isCancelled()) {
			return null;
		}
		
		/*
		 * Build a ContentIndex containing the contents of the source index
		 * according to the rules defined by the given Profile. 
		 */
		publish(INDEX_FILTERER_STARTED);
		filteredIndex = enactSubWorker (new IndexFilterer(sourceIndex));
		publish(INDEX_FILTERER_FINISHED);
		
		/*
		 * If the DetectionTask has been cancelled then bail.
		 */
		if (DetectionTask.this.isCancelled()) {
			return null;
		}
		
		/*
		 * Exclude files without a common file size from the filtered index.
		 */
		publish(INDEX_CULLERBYSIZE_STARTED);
		culledBySizeIndex = enactSubWorker(new IndexCullerBySize(filteredIndex));
		publish(INDEX_CULLERBYSIZE_FINISHED);
		
		/*
		 * If the DetectionTask has been cancelled then bail.
		 */
		if (DetectionTask.this.isCancelled()) {
			return null;
		}
		
		/*
		 * Hash the culled index
		 */
		publish(INDEX_HASHER_STARTED);
		hashedIndex = enactSubWorker(new IndexHasher(culledBySizeIndex));
		publish(INDEX_HASHER_FINISHED);
	
		/*
		 * If the DetectionTask has been cancelled then bail.
		 */
		if (DetectionTask.this.isCancelled()) {
			return null;
		}
		
		/*
		 * Exclude files without a common hash value from the index.
		 */
		publish(INDEX_CULLERBYHASH_STARTED);
		culledByHashIndex = enactSubWorker(new IndexCullerByHash(hashedIndex));
		publish(INDEX_CULLERBYHASH_FINISHED);

		/*
		 * If the DetectionTask has been cancelled then bail.
		 */
		if (DetectionTask.this.isCancelled()) {
			return null;
		}
		
		/*
		 * Generate results; group files with common hashes into hash-exclusive
		 * ContentIndex instances. 
		 */
		publish(INDEX_GROUP_RESULTS_STARTED);
		indexGroups = groupFilesByHash(culledByHashIndex);
		publish(INDEX_GROUP_RESULTS_FINISHED);
		
		detectionTime = (int)((System.currentTimeMillis() - startTime) / 1000); 
		Report resultsReport = Report.generate(detectionProfile.getName(), indexGroups, start, new Date());
		
		Report.save(resultsReport);
		
		return resultsReport;
	}

	/**
	 * Handles the progress message flags and updates the parent ProgressPanel's
	 * status indicators.
	 */
	protected void process (List<String> entries) {
		
		int timeElapsed = (int)((System.currentTimeMillis() - startTime) / 1000);
		
		parentWindow.setTabTitle("Scan: " + detectionProfile.getName() + " (" + timeElapsed + " s) ");
		
		for (String entry : entries) {

			/*
			 * If the DetectionTask has been cancelled then bail.
			 */
			if (DetectionTask.this.isCancelled()) {
				return;
			}
			
			parentWindow.getLogTextPane().append(entry + "\n");
			
			if (entry == INDEX_BUILDER_STARTED) {
					parentWindow.getIndexingLabel().setIcon(PROGRESS_ICON);
					parentWindow.getFilteringLabel().setIcon(YIELD_ICON);
					parentWindow.getHashingLabel().setIcon(YIELD_ICON);
					parentWindow.getGroupingLabel().setIcon(YIELD_ICON);
					parentWindow.getIndexingLabel().setEnabled(true);
					
			} else if (entry == INDEX_BUILDER_FINISHED) {
					parentWindow.getIndexingLabel().setIcon(SUCCESS_ICON); 
					
			} else if (entry == INDEX_FILTERER_STARTED) {
					parentWindow.getFilteringLabel().setIcon(PROGRESS_ICON);
					parentWindow.getFilteringLabel().setEnabled(true);
					
			} else if (entry == INDEX_FILTERER_FINISHED) {
				
			} else if (entry == INDEX_CULLERBYSIZE_STARTED) {
				
			} else if (entry == INDEX_CULLERBYSIZE_FINISHED) {
					parentWindow.getFilteringLabel().setIcon(SUCCESS_ICON);
					
			} else if (entry == INDEX_HASHER_STARTED) {
					parentWindow.getHashingLabel().setIcon(PROGRESS_ICON);
					parentWindow.getHashingLabel().setEnabled(true);
					
			} else if (entry == INDEX_HASHER_FINISHED) {
					parentWindow.getHashingLabel().setIcon(SUCCESS_ICON);
					
			} else if (entry == INDEX_CULLERBYHASH_STARTED) {
					parentWindow.getGroupingLabel().setIcon(PROGRESS_ICON);
					parentWindow.getGroupingLabel().setEnabled(true);
					
			} else if (entry == INDEX_CULLERBYHASH_FINISHED) {
					
			} else if (entry == INDEX_GROUP_RESULTS_STARTED) {
				
			} else if (entry == INDEX_GROUP_RESULTS_FINISHED) {
					parentWindow.getGroupingLabel().setIcon(SUCCESS_ICON);
			}
		}
	}
	
}
