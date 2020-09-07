package io.naztech.nuxeoclient.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.Constants;

/**
 * @author Asadullah.galib
 * @since 2020-04-03
 */
@Service
public class DirectoryCreator {
	private static Logger log = LoggerFactory.getLogger(DirectoryCreator.class);

	@Value("${import.root.folder_path}")
	private String rootIn;

	@Value("${import.root.out.folder_path}")
	private String rootOut;

	/*
	 * This Class will create folders Inside of OUT Folder according to IN folder.
	 * 
	 * @return boolean
	 * 
	 * @param null
	 */

	@SuppressWarnings("unlikely-arg-type")
	public boolean outDirectorycreatorForSuppliers(String folderName) {
		try {
//			File inFile = new File(rootIn);
//			// Listing all directories Inside In Folder
//			String[] inDirectories = inFile.list(new FilenameFilter() {
//				@Override
//				public boolean accept(File current, String name) {
//					return new File(current, name).isDirectory();
//				}
//			});

			// OUT ROOT Folder PATH
			File outFile = new File(rootOut);
			// Listing all directories Inside OUT Folder
			String[] outDirectories = outFile.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}
			});

			if (!Arrays.asList(outDirectories).contains(folderName)) {
				File newDir = new File(rootOut + Constants.FWD_SL + folderName);
				// If folder Doesn't exist Inside OUT folder, Creating new folders
				boolean created = newDir.mkdir();
				if (created) {
					log.info("Folder created Successfully for " + folderName);
					new File(newDir.getAbsolutePath() + Constants.FWD_SL + Constants.Archive).mkdir();
					new File(newDir.getAbsolutePath() + Constants.FWD_SL + Constants.Bad).mkdir();
					new File(newDir.getAbsolutePath() + Constants.FWD_SL + Constants.NuxeoFailed).mkdir();
					log.info("Archive, Bad & NuxeoFailed folders are Created inside " + folderName);
				}
			}

		} catch (Exception e) {
			log.error("Could not Create Directories :{}", e.getMessage());
			return false;
		}

		return true;

	}
}
