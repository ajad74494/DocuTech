package io.naztech.nuxeoclient.constants;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-03
 **/
public enum Destination {

	NUXEO_SERVER("Nuxeo-server"),
	DOCUTECH_CLIENT("Docutech-client");

	private final String destinationName;

	private Destination(String destinationName) {
		this.destinationName = destinationName;
	}

	@Override
	public String toString() {
		return destinationName;
	}

}
