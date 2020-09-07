

package io.naztech.nuxeoclient.service;

import java.sql.SQLException;
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
import io.naztech.nuxeoclient.constants.SPName;
import io.naztech.nuxeoclient.model.User;

/**
 * @author: MD. Mahbub Hasan Mohiuddin
 * @since: 2020-01-01
 **/
public class UserService extends AbstractService<List<User>> {

	private static Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Message<?> serviceSingle(Message msg) throws Exception {

		MessageHeader msgHeader = null;
		Message<?> msgResponse = null;

		try {

			msgHeader = msg.getHeader();

			String actionType = msgHeader.getActionType();

			log.debug("Processing ACTION [{}]", actionType);

			if (actionType.equals(ActionType.LOGIN.toString())) {

				User user = ((Message<List<User>>) msg).getPayload().get(0);

				user = login(null, msg);

				msgResponse = MessageBuilder.withPayload(user).copyHeadersIfAbsent(msgHeader).build();
			} 
			else if (actionType.equals(ActionType.LOGOUT.toString())) {

				User user = ((Message<List<User>>) msg).getPayload().get(0);

				user = logout(null, msg);

				msgResponse = MessageBuilder.withPayload(user).copyHeadersIfAbsent(msgHeader).build();

			}
			else if (actionType.equals(ActionType.TEST.toString())) {

				User user = ((Message<List<User>>) msg).getPayload().get(0);

				user = login(null, msg);

				msgResponse = MessageBuilder.withPayload(user).copyHeadersIfAbsent(msgHeader).build();
			} 
			else {
				throw new Exception("Unknow action " + actionType);
			}

		} catch (Exception ex) {

			log.error("Error {}", ex);
			throw ex;
		}

		return msgResponse;
	}

	public User login(User login, Message<List<User>> msg) throws Exception {

		User user = msg.getPayload().get(0);

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(user, User.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.ACT_USER.toString());

			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());

			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@tx_action_name", SPName.ACTION_LOGIN.toString());

			jdbcResult = getJdbcService().executeSP(SPName.ACTION_LOGIN.toString(), SPName.ACT_USER.toString(), spArgsMap, jdbcResult);

			List<User> refDataList = JdbcUtils.mapRows(User.class, User.getRs2BeanMap(),
					jdbcResult.getRsTypeMap(SPName.RS_TYPE_USER.toString()));

			user = refDataList.get(0);

			user.setErrMsg(null);

		} catch (SQLException sqlEx) {
			getJdbcService().rollbackTran(null, sqlEx, jdbcResult);
			user.setErrMsg(sqlEx.getMessage());
		} catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			user.setErrMsg(ex.getMessage());
		}

		return user;
	}

	private User logout(User logout, Message<List<User>> msg) throws Exception {

		User user = msg.getPayload().get(0);

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(user, User.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.ACT_USER.toString());

			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());

			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@id_login_ver", user.getLoginVer());
			spArgsMap.put("@tx_action_name", SPName.ACTION_LOGOUT.toString());
			
			jdbcResult = getJdbcService().executeSP(SPName.ACTION_LOGOUT.toString(), SPName.ACT_USER.toString(), spArgsMap, jdbcResult);

			JdbcUtils.mapRows(User.class, User.getRs2BeanMap(), jdbcResult.getRsTypeMap(SPName.RS_TYPE_USER.toString()));
			//JdbcUtils.populateBean(user, User.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());
		} catch (SQLException sqlEx) {

			getJdbcService().rollbackTran(null, sqlEx, jdbcResult);

			user.setErrMsg("ERROR");

			throw sqlEx;

		} catch (Exception ex) {

			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());

			user.setErrMsg("ERROR");

			throw ex;
		}
		
		return user;
	}
	
}
