package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.nazdaqTechnologies.core.message.Message;
import com.nazdaqTechnologies.core.message.MessageBuilder;
import com.nazdaqTechnologies.core.message.MessageHeader;
import com.nazdaqTechnologies.core.service.AbstractService;
import com.nazdaqTechnologies.jdbc.JdbcResult;
import com.nazdaqTechnologies.jdbc.JdbcService;
import com.nazdaqTechnologies.jdbc.StoredProcedure.JdbcStoredProcedure;
import com.nazdaqTechnologies.jdbc.util.JdbcUtils;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.SPName;
import io.naztech.nuxeoclient.model.DocCount;
import io.naztech.nuxeoclient.model.Template;
import io.naztech.nuxeoclient.utility.EmailSearchTerm;

public class DocCountService extends AbstractService<DocCount> {
	private static Logger log = LoggerFactory.getLogger(DocCountService.class);

	@Autowired
	private EmailSearchTerm emailSearchTerm;

	@Value("${import.root.exception_folder_path}")
	private String exceptionFolderPath;

	@Value("${import.root.abby.inbox}")
	private String abbyInbox;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Message<?> serviceSingle(Message msg) throws Exception {

		MessageHeader msgHeader = null;
		Message<?> msgResponse = null;
		List<Template> templateList = new ArrayList<Template>();

		try {

			msgHeader = msg.getHeader();

			String actionType = msgHeader.getActionType();

			log.debug("Processing ACTION [{}]", actionType);

			if (actionType.equals(ActionType.NEW.toString()) || actionType.equals(ActionType.UPDATE.toString())
					|| actionType.equals(ActionType.SELECT.toString())) {

				templateList = execute(actionType, msg);

				msgResponse = MessageBuilder.withPayload(templateList).copyHeadersIfAbsent(msgHeader).build();
			} else {
				throw new Exception("Unknow action " + actionType);
			}
		} catch (Exception ex) {
			log.error("Error {}", ex);
			throw ex;
		} finally {
			templateList = null;
		}

		return msgResponse;
	}

	public List<DocCount> processAction(String action, DocCount docCount) {

		List<DocCount> templateList = new ArrayList<DocCount>();

		try {
			templateList = handleDocCount(action, docCount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return templateList;
	}

	/**
	 * @since 2019-12-23
	 * @author mahbub.hasan
	 * @param action name and message
	 * @desc: Execute doc count function from servicesingle method
	 */
	private List<DocCount> execute(String action, Message<List<DocCount>> msg) throws Exception {

		DocCount docCount = msg.getPayload().get(0);

		return handleDocCount(action, docCount);
	}

	/**
	 * @author Asadullah.galib
	 * @param
	 * @desc:
	 */
	private List<DocCount> handleDocCount(String action, DocCount docCount) throws Exception {

		log.debug("executing...{}", action);

		List<DocCount> docCountList = new ArrayList<DocCount>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(docCount, DocCount.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService()
					.getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_DOC_COUNT.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			if (null != docCount.getFromDate()) {
				spArgsMap.put("@dtt_valid_from", docCount.getFromDate());
				spArgsMap.put("@dtt_valid_to", docCount.getToDate());
			}

			spArgsMap.put("@tx_action_name", action);
			spArgsMap.put("@id_user_mod_key", 1000000);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_DOC_COUNT.toString(),
					spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())
					|| action.equalsIgnoreCase(ActionType.SEQUENCE.toString())) {

				docCountList = JdbcUtils.mapRows(DocCount.class, DocCount.getRs2BeanMap(),
						jdbcResult.getRsTypeMap(SPName.RS_TYPE_DOC_COUNT.toString()));

				log.debug("Selected data from db: [{}]", docCountList.size());

			} else {
				JdbcUtils.populateBean(docCount, DocCount.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				docCountList.add(docCount);

				if (action.equalsIgnoreCase(ActionType.UPDATE.toString()) && (docCount.getIsAbbyError() == 0)
						&& null != docCount.getFileName()) {
					moveErrorFile(docCount.getFileName());
				}
			}

		} catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return docCountList;
	}

	private void moveErrorFile(String name) {

		//First clear the file result list
		List<String> result = new ArrayList<>();
		result.clear();
		
		emailSearchTerm.setResult(result);
		
		// Search files in directory and add to list
		result = emailSearchTerm.search(new File(exceptionFolderPath), name);

		if (result.parallelStream().findFirst().isPresent()) {

			for (String path : result) {

				File file = new File(path);

				try {
					Path temp = Files.move(Paths.get(path), Paths.get(abbyInbox + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
					
					if (temp != null) {
						log.info("File moved successfully");
					} else {
						log.info("Failed to move the file");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
