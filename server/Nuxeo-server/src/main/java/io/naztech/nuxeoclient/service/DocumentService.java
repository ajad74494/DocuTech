package io.naztech.nuxeoclient.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import io.naztech.nuxeoclient.model.Document;

/**
 * @author asadullah.galib
 * @since 2019-09-17
 */

public class DocumentService extends AbstractService<Document> {

	private static Logger log = LoggerFactory.getLogger(DocumentService.class);

	/**
	 * @author Md. Mahbub Hasan Mohiuddin
	 * @return Document List
	 * @desc: calls proper insert/update method depending on sp name
	 * @since 2019-09-03
	 **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Message<?> serviceSingle(Message msg) throws Exception {

		MessageHeader msgHeader = null;
		Message<?> msgResponse = null;
		List<Document> docList = new ArrayList<Document>();

		try {

			msgHeader = msg.getHeader();

			String actionType = msgHeader.getActionType();

			log.debug("Processing ACTION [{}]", actionType);

			if (actionType.equals(ActionType.NEW.toString()) || actionType.equals(ActionType.UPDATE.toString())
					|| actionType.equals(ActionType.SELECT.toString())) {

				docList = handleDocument(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(docList).copyHeadersIfAbsent(msgHeader).build();
			} else if (actionType.equals(ActionType.NEW_CUSTOMER.toString())
					|| actionType.equals(ActionType.UPDATE_CUSTOMER.toString())
					|| actionType.equals(ActionType.SELECT_CUSTOMER.toString())) {

				if (actionType.equals(ActionType.NEW_CUSTOMER.toString())) {
					actionType = ActionType.NEW.toString();
				} else if (actionType.equals(ActionType.UPDATE_CUSTOMER.toString())) {
					actionType = ActionType.UPDATE.toString();
				} else {
					actionType = ActionType.SELECT.toString();
				}

				docList = handleCustomer(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(docList).copyHeadersIfAbsent(msgHeader).build();
			} else {
				throw new Exception("Unknow action " + actionType);
			}
		} catch (Exception ex) {
			log.error("Error {}", ex);
			throw ex;
		} finally {
			docList = null;
		}

		return msgResponse;
	}

	/**
	 * @author asadullah.galib
	 * @param action (action name)
	 * @param doc    (Document)
	 * @param sp     (stored procedure name)
	 * @return Document List
	 * @desc: calls proper insert/update method depending on sp name
	 */
	public List<Document> processAction(String action, Document doc, String sp) {

		List<Document> docList = new ArrayList<Document>();

		try {
			if (sp.equalsIgnoreCase(Constants.DOC)) {
				docList = handleDocument(action, doc, null);
			} else if (sp.equalsIgnoreCase(Constants.CUSTOMER)) {
				docList = handleCustomer(action, doc, null);
			}
			// } else if(sp.equalsIgnoreCase(Constants.DOC_DETAILS)) {
			// docList = handleDocDetails(action, doc);
			// }
			else if (sp.equalsIgnoreCase(Constants.EXCEPTION)) {
				// templateList = handleDocException(action, doc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return docList;
	}

	/**
	 * @author asadullah.galib
	 * @param action (action name)
	 * @param doc    (Document)
	 * @return
	 * @throws Exception
	 * @desc: Inserts/update data in doc details table depending on action name
	 */
	// private List<Document> handleDocDetails(String action, Document doc) throws
	// Exception {
	// log.debug("executing...{}", action);
	//
	// List<Document> docList = new ArrayList<Document>();
	//
	// Map<String, Object> spArgsMap = JdbcService.createSqlMap(doc,
	// Document.getSql2BeanMap());
	//
	// JdbcResult jdbcResult = new JdbcResult();
	//
	// try {
	//
	// JdbcStoredProcedure jdbcStoredProcedure = getJdbcService()
	// .getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
	// SPName.ACT_DOC_DETAILS.toString());
	// jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
	// jdbcResult.setProcessWarnings(false);
	//
	// spArgsMap.put("@tx_action_name", action);
	//
	// jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(),
	// SPName.ACT_DOC_DETAILS.toString(),
	// spArgsMap, jdbcResult);
	//
	// if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
	// docList = JdbcUtils.mapRows(Document.class, Document.getRs2BeanMap(),
	// jdbcResult.getRsTypeMap(SPName.RS_TYPE_DOC_DETAILS.toString()));
	// } else {
	// JdbcUtils.populateBean(doc, Document.getSql2BeanMap(),
	// jdbcResult.getOutputParamValueMap());
	//
	// docList.add(doc);
	// }
	//
	// } catch (Exception ex) {
	// log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
	// throw ex;
	// }
	//
	// return docList;
	// }

	/**
	 * @author asadullah.galib
	 * @param action (action name)
	 * @param doc    (Document)
	 * @return
	 * @throws Exception
	 * @desc: Inserts/update data in customer table
	 */
	private List<Document> handleCustomer(String action, Document doc, Message<List<Document>> msg) throws Exception {
		log.debug("executing...{}", action);

		// Document document = msg.getPayload().get(0);
		//
		// if (null == doc) {
		// doc = document;
		// }

		List<Document> docList = new ArrayList<Document>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(doc, Document.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService()
					.getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_CUSTOMER.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_CUSTOMER.toString(),
					spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				docList = JdbcUtils.mapRows(Document.class, Document.getRs2BeanMap(),
						jdbcResult.getRsTypeMap(SPName.RS_TYPE_CUSTOMER.toString()));
			} else {
				JdbcUtils.populateBean(doc, Document.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				docList.add(doc);
			}

		} catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return docList;
	}

	/**
	 * @author asadullah.galib
	 * @param action (action name)
	 * @param doc    (Document)
	 * @return
	 * @throws Exception
	 * @desc: Inserts/update data in document table
	 */
	private List<Document> handleDocument(String action, Document doc, Message<List<Document>> msg) throws Exception {

		log.debug("executing...{}", action);

		if (null == doc) {
			doc = msg.getPayload().get(0);
		}

		List<Document> docList = new ArrayList<Document>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(doc, Document.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService()
					.getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_DOC.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			if(null != doc.getFromDate()) {
				spArgsMap.put("@dtt_valid_from", doc.getFromDate());
				spArgsMap.put("@dtt_valid_to", doc.getToDate());
			}
			
			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_DOC.toString(),
					spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				docList = JdbcUtils.mapRows(Document.class, Document.getRs2BeanMap(),
						jdbcResult.getRsTypeMap(SPName.RS_TYPE_DOC.toString()));
			} else {
				JdbcUtils.populateBean(doc, Document.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				docList.add(doc);
			}

			if (!docList.isEmpty()) {
				docList.parallelStream().forEachOrdered(d -> {
					d.setDocumentDate(d.getDocDate());
					d.setDespatchDate(d.getDespDate());
				});
			}

		} catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return docList;
	}
	
	/**
	 * @author bm.alamin
	 * @param startDate
	 * @param endDate
	 * @param action
	 * @return list of invoices
	 */
	public List<Document> getPurchaseInvoiceList(String startDate, String endDate, String action) { //get transaction data from here
		
		List<Document> purchaseInvoices = null;
		
		JdbcResult jdbcResult = new JdbcResult();
		try {
			Map<String, Object> spArgsMap = new HashMap<String, Object>();
			spArgsMap.put("@dt_begin_date", startDate);
			spArgsMap.put("@dt_end_date", endDate);
			spArgsMap.put("@tx_action_type", action);
			
			
			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_SAGE50_TRANS_EXEL.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);
			
			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_SAGE50_TRANS_EXEL.toString(), spArgsMap, jdbcResult);
			purchaseInvoices = JdbcUtils.mapRows(Document.class, Document.getRs2BeanMap(), jdbcResult.getRsTypeMap(SPName.RS_TYPE_DOCUMENT.toString()));
		}
		catch (Exception e) {

			log.error("Error occurs with ACT_sage50_trans_exel {} ", e);
		}
		return purchaseInvoices;
	}

}
