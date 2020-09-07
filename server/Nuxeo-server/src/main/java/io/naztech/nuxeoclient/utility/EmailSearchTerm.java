package io.naztech.nuxeoclient.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailSearchTerm extends SearchTerm {

	/**
	 * Default serial versin ID
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(EmailSearchTerm.class);

	private Date afterDate;
	private String fromEmail;

	public EmailSearchTerm() {
		result.clear();
	}

	public EmailSearchTerm(Date afterDate, String fromEmail) {
		this.afterDate = afterDate;
		this.fromEmail = fromEmail;
		result.clear();
	}

	private List<String> result = new ArrayList<String>();

	public List<String> getResult() {
		return result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

	@Override
	public boolean match(Message message) {
		try {
			Address[] fromAddress = message.getFrom();

			if (fromAddress != null && fromAddress.length > 0) {

				String from = ((InternetAddress) fromAddress[0]).getAddress();

				if (DateUtils.isSameDay(message.getSentDate(), afterDate))
					log.info("Message sent date & last download time & is after: [{}/{}/{}]", message.getSentDate(),
							afterDate, message.getSentDate().after(afterDate));

				if (from.contains(fromEmail) && message.getSentDate().after(afterDate)) {
					return true;
				}
			} else {
				if (message.getSentDate().after(afterDate)) {
					return true;
				}
			}
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Search files inside directory
	 */
	public List<String> search(File file, String name) {
		
		if (file.isDirectory()) {
			log.debug("Searching directory:[{}]", file.getAbsoluteFile());

			// do you have permission to read this directory?
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						search(temp, name);
					} else {
						if (name.equals(temp.getName())) {
							result.add(temp.getAbsoluteFile().toString());
						}
					}
				}

			} else {
				log.info("Permission Denied: [{}]", file.getAbsoluteFile());
			}
		}

		return result;
	}
}
