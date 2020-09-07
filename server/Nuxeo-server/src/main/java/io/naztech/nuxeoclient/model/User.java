
/**
 * @author: MD. Mahbub Hasan Mohiuddin
 * @since: 2020-01-01
 **/

package io.naztech.nuxeoclient.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class User {

	private static Map<String, String> sql2BeanMap = null;
	private static Map<String, String> rs2BeanMap = null;

	private Integer userId;
	private Integer userVer;
	private Integer remitUserId;
	private Integer remitUserVer;
	private String userName;
	private String password;
	private String newPassword;

	private String firstName;
	private String lastName;
	private String loginName;
	private String userAlias;
	private Integer regionKey;
	private Integer groupKey;
	private Integer legalEntityKey;
	private String legalEntityName;
	private Integer isDisabled;
	private Integer isAllowLogin;
	private Integer primaryGroupId;
	private String primaryGroupName;
	
	private Integer userModKey;
	private String errMsg;
	private Date dateModified;
	private Integer isDefault;
	private Integer loginKey;
	private Integer loginVer;
	private Integer envId;
	private Integer isLoggedIn;
	private String userAuthCode;
	private Integer customerId;
	private String accountNo;
	private String customerEmail;
	private String customerName;
	private Integer custUserId;
	
	private List<User> userList = new ArrayList<User>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserVer() {
		return userVer;
	}

	public void setUserVer(Integer userVer) {
		this.userVer = userVer;
	}

	public Integer getRemitUserId() {
		return remitUserId;
	}

	public void setRemitUserId(Integer remitUserId) {
		this.remitUserId = remitUserId;
	}

	public Integer getRemitUserVer() {
		return remitUserVer;
	}

	public void setRemitUserVer(Integer remitUserVer) {
		this.remitUserVer = remitUserVer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public Integer getRegionKey() {
		return regionKey;
	}

	public void setRegionKey(Integer regionKey) {
		this.regionKey = regionKey;
	}

	public Integer getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(Integer groupKey) {
		this.groupKey = groupKey;
	}

	public Integer getLegalEntityKey() {
		return legalEntityKey;
	}

	public void setLegalEntityKey(Integer legalEntityKey) {
		this.legalEntityKey = legalEntityKey;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public Integer getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Integer getIsAllowLogin() {
		return isAllowLogin;
	}

	public void setIsAllowLogin(Integer isAllowLogin) {
		this.isAllowLogin = isAllowLogin;
	}

	public Integer getPrimaryGroupId() {
		return primaryGroupId;
	}

	public void setPrimaryGroupId(Integer primaryGroupId) {
		this.primaryGroupId = primaryGroupId;
	}

	public String getPrimaryGroupName() {
		return primaryGroupName;
	}

	public void setPrimaryGroupName(String primaryGroupName) {
		this.primaryGroupName = primaryGroupName;
	}

	public Integer getUserModKey() {
		return userModKey;
	}

	public void setUserModKey(Integer userModKey) {
		this.userModKey = userModKey;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(Integer loginKey) {
		this.loginKey = loginKey;
	}

	public Integer getLoginVer() {
		return loginVer;
	}

	public void setLoginVer(Integer loginVer) {
		this.loginVer = loginVer;
	}

	public Integer getEnvId() {
		return envId;
	}

	public void setEnvId(Integer envId) {
		this.envId = envId;
	}

	public Integer getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Integer isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getUserAuthCode() {
		return userAuthCode;
	}

	public void setUserAuthCode(String userAuthCode) {
		this.userAuthCode = userAuthCode;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustUserId() {
		return custUserId;
	}

	public void setCustUserId(Integer custUserId) {
		this.custUserId = custUserId;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public static final Map<String, String> getSql2BeanMap() {

		if (sql2BeanMap == null) {
			sql2BeanMap = new LinkedHashMap<String, String>();

			sql2BeanMap.put("@id_group_key", "primaryGroupId");
			sql2BeanMap.put("@tx_group_name", "primaryGroupName");			
			
			sql2BeanMap.put("@id_user_key", "userId");			
			sql2BeanMap.put("@id_user_ver", "userVer");		
			sql2BeanMap.put("@id_remittance_user_key", "remitUserId");			
			sql2BeanMap.put("@id_remittance_user_ver", "remitUserVer");	
			sql2BeanMap.put("@id_region_key", "regionKey");			
			sql2BeanMap.put("@id_legal_entity_key", "legalEntityKey");
			sql2BeanMap.put("@tx_first_name", "firstName");	
			sql2BeanMap.put("@tx_last_name", "lastName");			
			sql2BeanMap.put("@tx_user_alias", "userAlias");	
			sql2BeanMap.put("@tx_login_name", "loginName");	
			sql2BeanMap.put("@tx_password",	"password");	
			sql2BeanMap.put("@is_disabled",	"isDisabled");	
			sql2BeanMap.put("@is_allow_login", "isAllowLogin");	
			sql2BeanMap.put("@id_user_mod_key", "userModKey");	
			sql2BeanMap.put("@tx_new_password", "newPassword");	
			sql2BeanMap.put("@id_env_key", "envId");
			
			sql2BeanMap.put("@tx_account_no", "accountNo");
		};
		return sql2BeanMap;
	}
	
	public static final Map<String, String> getRs2BeanMap() {

		if (rs2BeanMap == null) {
			rs2BeanMap = new LinkedHashMap<String, String>();

			rs2BeanMap.put("id_user_key", "userId");
			rs2BeanMap.put("id_user_ver", "userVer");		
			rs2BeanMap.put("id_remittance_user_key", "remitUserId");			
			rs2BeanMap.put("id_remittance_user_ver", "remitUserVer");
			rs2BeanMap.put("tx_first_name", "firstName");
			rs2BeanMap.put("tx_last_name", "lastName");
			rs2BeanMap.put("tx_login_name", "loginName");
			rs2BeanMap.put("tx_user_alias", "userAlias");
			rs2BeanMap.put("tx_user_password", "password");
			rs2BeanMap.put("is_disabled",	"isDisabled");
			rs2BeanMap.put("is_allow_login", "isAllowLogin");
			rs2BeanMap.put("id_group_key", "primaryGroupId");
			rs2BeanMap.put("tx_group_name", "primaryGroupName");
			rs2BeanMap.put("id_legal_entity_key", "legalEntityKey");
			rs2BeanMap.put("tx_legal_entity_name", "legalEntityName");
			rs2BeanMap.put("id_user_mod_key", "userModKey");
			rs2BeanMap.put("id_login_key", "loginKey");
			rs2BeanMap.put("id_login_ver", "loginVer");
			rs2BeanMap.put("is_logged_in", "isLoggedIn");
			rs2BeanMap.put("tx_account_no", "accountNo");	
			rs2BeanMap.put("id_customer_key", "customerId");	
			rs2BeanMap.put("tx_email", "customerEmail");
		}
		
		return rs2BeanMap;
	}
}
