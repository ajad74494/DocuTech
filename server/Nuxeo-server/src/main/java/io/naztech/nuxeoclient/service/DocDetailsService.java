package io.naztech.nuxeoclient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nazdaqTechnologies.core.message.Message;
import com.nazdaqTechnologies.core.message.MessageBuilder;
import com.nazdaqTechnologies.core.message.MessageHeader;
import com.nazdaqTechnologies.core.service.AbstractService;
import com.nazdaqTechnologies.jdbc.JdbcResult;
import com.nazdaqTechnologies.jdbc.JdbcService;
import com.nazdaqTechnologies.jdbc.StoredProcedure.JdbcStoredProcedure;
import com.nazdaqTechnologies.jdbc.util.JdbcUtils;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.constants.SPName;
import io.naztech.nuxeoclient.model.DocDetails;
import io.naztech.nuxeoclient.model.Document;

/**
 * @author muhammad.tarek
 * @since 2019-09-17
 */
public class DocDetailsService extends AbstractService<DocDetails> {
	private static Logger log = LoggerFactory.getLogger(DocDetailsService.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Message<?> serviceSingle(Message msg) throws Exception {

		MessageHeader msgHeader = null;
		Message<?> msgResponse = null;

		try {

			msgHeader = msg.getHeader();

			String actionType = msgHeader.getActionType();

			log.debug("Processing ACTION [{}]", actionType);

			if (actionType.equals(ActionType.NEW.toString()) || actionType.equals(ActionType.UPDATE.toString())
			        || actionType.equals(ActionType.SELECT.toString())) {

				List<Document> docList = handleDoc(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(docList).copyHeadersIfAbsent(msgHeader).build();

			}
			else if (actionType.equals(ActionType.NEW_DOC_DETAILS.toString()) || actionType.equals(ActionType.UPDATE_DOC_DETAILS.toString())
			        || actionType.equals(ActionType.SELECT_DOC_DETAILS.toString())) {

				if (actionType.equals(ActionType.NEW_DOC_DETAILS.toString())) {
					actionType = ActionType.NEW.toString();
				}
				else if (actionType.equals(ActionType.UPDATE_DOC_DETAILS.toString())) {
					actionType = ActionType.UPDATE.toString();
				}
				else {
					actionType = ActionType.SELECT.toString();
				}

				List<DocDetails> docDetailList = handleDocDetails(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(docDetailList).copyHeadersIfAbsent(msgHeader).build();

			}
			else {
				throw new Exception("Unknow action " + actionType);
			}
		}
		catch (Exception ex) {
			log.error("Error {}", ex);
			throw ex;
		}

		return msgResponse;
	}

	public List<DocDetails> processAction(String action, DocDetails document, String sp) {
		List<DocDetails> templateList = new ArrayList<DocDetails>();

		try {
			if (sp.equalsIgnoreCase(Constants.DOC_DETAILS)) {
				templateList = handleDocDetails(action, document, null);
			}
			else if (sp.equalsIgnoreCase(Constants.EXCEPTION)) {
				// templateList = handleDocDetailsException(action, docDetails);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return templateList;
	}

	public List<Document> DocProcessAction(String action, Document docu, String sp) {
		List<Document> templateList = new ArrayList<Document>();
		try {
			if (sp.equalsIgnoreCase(Constants.DOC)) {
				templateList = handleDoc(action, docu, null);
			}
			else if (sp.equalsIgnoreCase(Constants.EXCEPTION)) {
				// templateList = handleDocDetailsException(action, docDetails);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return templateList;
	}

	private List<DocDetails> handleDocDetails(String action, DocDetails docDetails, Message<List<DocDetails>> msg) throws Exception {
		log.debug("executing...{}", action);

		DocDetails details = null;

		if (msg != null) {
			details = msg.getPayload().get(0);
		}

		List<DocDetails> docDetailsList = new ArrayList<DocDetails>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(docDetails, DocDetails.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {
			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
			        SPName.ACT_DOC_DETAILS.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);
			spArgsMap.put("@tx_action_name", action);

			Integer docId = null;
			if (details != null) {
				docId = details.getDocId();
			}
			if (docId != null) {
				spArgsMap.put("@id_doc_key", docId);
			}

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_DOC_DETAILS.toString(), spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				docDetailsList = JdbcUtils.mapRows(DocDetails.class, DocDetails.getRs2BeanMap(),
				        jdbcResult.getRsTypeMap(SPName.RS_TYPE_TEMPLATE.toString()));
			}
			else {
				JdbcUtils.populateBean(docDetails, DocDetails.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());
				docDetailsList.add(docDetails);
			}
		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}
		return docDetailsList;
	}

	private List<Document> handleDoc(String action, Document docu, Message<List<Document>> msg) throws Exception {

		log.debug("executing...{}", action);

		//		Document details = msg.getPayload().get(0);
		//
		//		if (null == docu) {
		//			docu = details;
		//		}

		List<Document> docList = new ArrayList<Document>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(docu, Document.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {
			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_DOC.toString());

			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);
			spArgsMap.put("@tx_action_name", action);
			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_DOC.toString(), spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				docList = JdbcUtils.mapRows(Document.class, Document.getRs2BeanMap(), jdbcResult.getRsTypeMap(SPName.RS_TYPE_DOC.toString()));
			}
			else {
				JdbcUtils.populateBean(docu, Document.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				docList.add(docu);
			}

		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}
		return docList;
	}
}
