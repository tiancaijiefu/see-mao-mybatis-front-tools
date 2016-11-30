package org.see.mao.exception;

/**
 * @author Joshua Wang
 * @date 2016年11月18日
 */
public class MaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MaoException(String message) {
		super(message);
	}

	public MaoException(Throwable throwable) {
		super(throwable);
	}

	public MaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
