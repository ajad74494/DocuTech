package io.naztech.nuxeoclient.service;

public class NuxeoDocumentServiceException extends Exception {
	private static final long serialVersionUID = 79609591388624359L;

	public NuxeoDocumentServiceException() {
		super();
	}

	public NuxeoDocumentServiceException(String message) {
		super(message);
	}

	public NuxeoDocumentServiceException(Throwable cause) {
		super(cause);
	}

	public NuxeoDocumentServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
