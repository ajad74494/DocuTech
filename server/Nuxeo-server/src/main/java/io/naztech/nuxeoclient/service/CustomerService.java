package io.naztech.nuxeoclient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nazdaqTechnologies.core.service.AbstractService;
import com.nazdaqTechnologies.jdbc.JdbcResult;
import com.nazdaqTechnologies.jdbc.JdbcService;
import com.nazdaqTechnologies.jdbc.StoredProcedure.JdbcStoredProcedure;
import com.nazdaqTechnologies.jdbc.util.JdbcUtils;

import io.naztech.nuxeoclient.constants.ActionType;
import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.constants.SPName;
import io.naztech.nuxeoclient.model.Customer;

/**
 * @author Asadullah.galib
 * @since 2019-09-17
 */
public class CustomerService extends AbstractService<Customer> {

	private static Logger log = LoggerFactory.getLogger(CustomerService.class);

	/**
	 * @author Asadullah.galib
	 * @param
	 * @desc:
	 */
	public List<Customer> processAction(String action, Customer customer, String sp) {

		List<Customer> templateList = new ArrayList<Customer>();

		try {
			if (sp.equalsIgnoreCase(Constants.CUSTOMER)) {
				templateList = handleCustomer(action, customer);
			}
			else if (sp.equalsIgnoreCase(Constants.EXCEPTION)) {
				//templateList = handleCustomerException(action, customer);	
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return templateList;
	}

	/**
	 * @author Asadullah.galib
	 * @param
	 * @desc:
	 */
	private List<Customer> handleCustomer(String action, Customer customer) throws Exception {

		log.debug("executing...{}", action);

		List<Customer> customerList = new ArrayList<Customer>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(customer, Customer.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
			        SPName.ACT_CUSTOMER.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_CUSTOMER.toString(), spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				customerList = JdbcUtils.mapRows(Customer.class, Customer.getRs2BeanMap(),
				        jdbcResult.getRsTypeMap(SPName.RS_TYPE_CUSTOMER.toString()));
			}
			else {
				JdbcUtils.populateBean(customer, Customer.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				customerList.add(customer);
			}

		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return customerList;
	}

	/**
	 * @author Asadullah.galib
	 * @param
	 * @desc:
	 */
	//	private List<Template> handleTemplateException(String action, Template tmplt) throws Exception {
	//
	//		log.debug("executing...{}", action);
	//
	//		List<Template> templateList = new ArrayList<Template>();
	//
	//		Map<String, Object> spArgsMap = JdbcService.createSqlMap(tmplt, Template.getSql2BeanMap());
	//
	//		JdbcResult jdbcResult = new JdbcResult();
	//
	//		try {
	//
	//			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService()
	//					.getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_TEMPLATE_EXCEPTION.toString());
	//			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
	//			jdbcResult.setProcessWarnings(false);
	//
	//			spArgsMap.put("@tx_action_name", action);
	//
	//			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_TEMPLATE_EXCEPTION.toString(),
	//					spArgsMap, jdbcResult);
	//
	//			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
	//				templateList = JdbcUtils.mapRows(Template.class, Template.getRs2BeanMap(),
	//						jdbcResult.getRsTypeMap(SPName.RS_TYPE_TEMPLATE_EXCEPTION.toString()));
	//			} else {
	//				JdbcUtils.populateBean(tmplt, Template.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());
	//
	//				templateList.add(tmplt);
	//			}
	//
	//		} catch (Exception ex) {
	//			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
	//			throw ex;
	//		}
	//
	//		return templateList;
	//	}

}
