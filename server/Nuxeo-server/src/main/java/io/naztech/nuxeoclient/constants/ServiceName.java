package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum ServiceName {

	SERVICE_POSTFIX("Service");

	private final String serviceName;

	private ServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return serviceName;
	}

}
