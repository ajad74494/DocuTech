package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum ActionType {

	TEST("TEST"),
	
	NEW("NEW"),
	UPDATE("UPDATE"),
	SELECT("SELECT"),
	
	NEW_EXCEPTION("NEW_EXCEPTION"),
	UPDATE_EXCEPTION("UPDATE_EXCEPTION"),
	SELECT_EXCEPTION("SELECT_EXCEPTION"),
	
	NEW_DOC_DETAILS("NEW_DOC_DETAILS"),
	UPDATE_DOC_DETAILS("UPDATE_DOC_DETAILS"),
	SELECT_DOC_DETAILS("SELECT_DOC_DETAILS"),
	
	NEW_CUSTOMER("NEW_CUSTOMER"),
	UPDATE_CUSTOMER("UPDATE_CUSTOMER"),
	SELECT_CUSTOMER("SELECT_CUSTOMER"),
	SELECT_ID("SELECT_ID"),
	SEQUENCE("SEQUENCE"),
	LOGIN("LOGIN"),
	LOGOUT("LOGOUT");

	private final String actionType;

	private ActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return actionType;
	}

}
