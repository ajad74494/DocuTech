package io.naztech.nuxeoclient.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import io.naztech.nuxeoclient.model.SupplierMeta;

/**
 * @author Md. Asadullahill Galib
 * @return SupplierMeta List
 * @desc: calls proper insert/update method depending on sp name
 * @since 2019-12-29
 **/

public class SupplierMetaService extends AbstractService<SupplierMeta> {
	private static Logger log = LoggerFactory.getLogger(SupplierMetaService.class);

	public List<SupplierMeta> processAction(String action, SupplierMeta supplierMeta, String sp) {

		List<SupplierMeta> templateList = new ArrayList<SupplierMeta>();

		try {
			if (sp.equalsIgnoreCase(Constants.SUPPLIER_META)) {
				templateList = handleSupplierMeta(action, supplierMeta);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return templateList;
	}

	private List<SupplierMeta> handleSupplierMeta(String action, SupplierMeta supplierMeta) throws Exception {

		log.debug("executing...{}", action);

		List<SupplierMeta> supplierMetaList = new ArrayList<SupplierMeta>();

		Map<String, Object> spArgsMap = JdbcService.createSqlMap(supplierMeta, SupplierMeta.getSql2BeanMap());

		JdbcResult jdbcResult = new JdbcResult();

		try {

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(),
			        SPName.ACT_SUPPLIER_META.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			spArgsMap.put("@tx_action_name", action);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_SUPPLIER_META.toString(), spArgsMap, jdbcResult);

			if (action.equalsIgnoreCase(ActionType.SELECT.toString())) {
				supplierMetaList = JdbcUtils.mapRows(SupplierMeta.class, SupplierMeta.getRs2BeanMap(),
				        jdbcResult.getRsTypeMap(SPName.RS_TYPE_SUPPLIER_META.toString()));
			}
			else {
				JdbcUtils.populateBean(supplierMeta, SupplierMeta.getSql2BeanMap(), jdbcResult.getOutputParamValueMap());

				supplierMetaList.add(supplierMeta);
			}

		}
		catch (Exception ex) {
			log.error("error {}, \nMessage *** : {}", ex, ex.getLocalizedMessage());
			throw ex;
		}

		return supplierMetaList;
	}

	/**
	 * @author bm.alamin
	 * @param startDate
	 * @param endDate
	 * @param action
	 * @return list of supplier's Accounts
	 */
	public List<SupplierMeta> getSupplierAccoutList(String action) { //get transaction data from here

		List<SupplierMeta> supplierAccounts = null;
		JdbcResult jdbcResult = new JdbcResult();
		try {
			Map<String, Object> spArgsMap = new HashMap<String, Object>();
			spArgsMap.put("@tx_action_type", action);

			JdbcStoredProcedure jdbcStoredProcedure = getJdbcService().getJdbcStoredProcedure(SPName.DOCUTECH.toString(), SPName.ACT_SAGE50_TRANS_EXEL.toString());
			jdbcResult.setFilteredOutputParamMap(jdbcStoredProcedure.getSpOutputParamMap());
			jdbcResult.setProcessWarnings(false);

			jdbcResult = getJdbcService().executeSP(action, SPName.DOCUTECH.toString(), SPName.ACT_SAGE50_TRANS_EXEL.toString(), spArgsMap, jdbcResult);
			supplierAccounts = JdbcUtils.mapRows(SupplierMeta.class, SupplierMeta.getRs2BeanMap(), jdbcResult.getRsTypeMap(SPName.RS_TYPE_SUPPLIER.toString()));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();

			log.error("Caught Error {} / {}", e, e);
		}
		return supplierAccounts;
	}

}
