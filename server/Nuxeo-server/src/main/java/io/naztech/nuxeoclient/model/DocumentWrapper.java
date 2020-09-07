package io.naztech.nuxeoclient.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.client.objects.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;*/

// TODO add an java property 'prefix' later if it differs from 'type'
/*@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)*/
public class DocumentWrapper extends Document {

	private static Logger log = LoggerFactory.getLogger(DocumentWrapper.class);
	String title, description;
	String repoPath;
	String prefix;

	/** {@link File} objects with full path */
	File file;

	Map<String, Object> attributes = new HashMap<>();

	public Document buildDocument() {
		Document ob = Document.createWithName(this.name, this.type);
		ob.setPropertyValue("dc:title", this.title);
		ob.setPropertyValue("dc:description", this.description);
		ob.setPropertyValue("repoPath", this.getRepoPath());
		ob.setPropertyValue("invoice_date", "getValue()");
		ob.setPropertyValue(prefix + ":supplier_name", "getValue()"); 
		for (Entry<String, Object> en : attributes.entrySet()) {
			log.info("key = " + prefix + ":" + en.getKey() + "\t\tValue" + en.getValue());
			ob.setPropertyValue(prefix + en.getKey(), en.getValue());
		}
		// TODO whether there is file in list, if not throw exception
		return ob;
	}

	public DocumentWrapper addAttribute(String key, Object val) {
		this.attributes.put(key, val);
		return this;
	}

//	public DocumentWrapper addFile(File file) {
//		this.file.add(file);
//		return file;
//	}
//
//	public DocumentWrapper addFile(String filePath) {
//		this.files.add(FileUtils.getFile(filePath));
//		return this;
//	}

	public static DocumentWrapper createWithName(String name, String type) {
		DocumentWrapper ob = new DocumentWrapper();
		ob.name = name;
		ob.type = type;
		return ob;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRepoPath() {
		return repoPath;
	}

	public void setRepoPath(String repoPath) {
		this.repoPath = repoPath;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
