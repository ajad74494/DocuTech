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
import io.naztech.nuxeoclient.model.Template;

/**
 * @author asadullah.galib
 * @since 2019-09-16
 */
public class TemplateService extends AbstractService<Template> {

	private static Logger log = LoggerFactory.getLogger(TemplateService.class);
		
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

				templateList = handleTemplate(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(templateList).copyHeadersIfAbsent(msgHeader).build();
			}
			else if (actionType.equals(ActionType.NEW_EXCEPTION.toString()) || actionType.equals(ActionType.UPDATE_EXCEPTION.toString())
			        || actionType.equals(ActionType.SELECT_EXCEPTION.toString())) {

				if (actionType.equals(ActionType.NEW_EXCEPTION.toString())) {
					actionType = ActionType.NEW.toString();
				}
				else if (actionType.equals(ActionType.UPDATE_EXCEPTION.toString())) {
					actionType = ActionType.UPDATE.toString();
				}
				else {
					actionType = ActionType.SELECT.toString();
				}

				templateList = handleTemplateException(actionType, null, msg);

				msgResponse = MessageBuilder.withPayload(templateList).copyHeadersIfAbsent(msgHeader).build();

			}
			else {
				throw new Exception("Unknow action " + actionType);
			}
		}
		catch (Exception ex) {
			log.error("Error {}", ex);
			throw ex;
		}
		finally {
			templateList = null;
		}

		return msgResponse;
	}

	/**
	 * @author asadullah.galib
	 * @param
	 * @desc:
	 */
	public List<Template> processAction(String action, Template tmplt, String sp) {

		List<Template> templateList = new ArrayList<Template>();

		try {
			if (sp.equalsIgnoreCase(Constants.TEMPLATE)) {
								
				templateList = handleTemplate(action, tmplt, null);
				
			}
			else if (sp.equalsIgnoreCase(Constants.EXCEPTION)) {
				templateList = handleTemplateException(action, tmplt, null);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return templateList;
	}

	/**
	 * @author asadullah.galib
	 * @param
	 * @desc:
	 */
	private List<Template> handleTemplate(String action, Template tmplt, Message<List<Template>> msg) throws Exception {

		log.debug("executing...{}", action);

		if(null == tmplt && null != msg) {
			tmplt = msg.getPayload().get(0);
		}
		
		List<Template> templateList = new ArrayList<Template>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(tmplt, Template.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
			        SPName.ACT_TEMPLATE.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			if(null != tmplt.getFromDate()) {
				spArgsMap.put("@dtt_from", tmplt.getFromDate());
				spArgsMap.put("@dtt_to", tmplt.getToDate());
			}
			
			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_TEMPLATE.toString(), spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())
					|| action.equalsIgnoreCase(ActionType.SELECT_ID.toString())) {
				templateList = JdbcUtils.mapRows(Template.class, Template.getRs2BeanMap(),
				        jdbcResult.getRsTypeMap(SPName.RS_TYPE_TEMPLATE.toString()));
			}
			else {
				JdbcUtils.populateBean(tmplt, Template.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				templateList.add(tmplt);
			}
		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return templateList;
	}

	/**
	 * @author asadullah.galib
	 * @param
	 * @desc:
	 */
	private List<Template> handleTemplateException(String action, Template tmplt, Message<List<Template>> msg) throws Exception {

		log.debug("executing...{}", action);

		if(null == tmplt && null != msg) {
			tmplt = msg.getPayload().get(0);
		}

		List<Template> templateList = new ArrayList<Template>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(tmplt, Template.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
			        SPName.ACT_TEMPLATE_EXCEPTION.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_TEMPLATE_EXCEPTION.toString(), spArgsMap,
			        jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				templateList = JdbcUtils.mapRows(Template.class, Template.getRs2BeanMap(),
				        jdbcResult.getRsTypeMap(SPName.RS_TYPE_TEMPLATE_EXCEPTION.toString()));
			}
			else {
				JdbcUtils.populateBean(tmplt, Template.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				templateList.add(tmplt);
			}

		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return templateList;
	}

}
