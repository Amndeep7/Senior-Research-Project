package shared;

import java.io.Serializable;

public enum Command implements Serializable
{
	ADD_CAR, GET_CARS;

	public static class CommandUnknownException extends IllegalArgumentException
	{
		private static final long serialVersionUID = 7433016527096037688L;

		public CommandUnknownException()
		{
			super();
		}
		public CommandUnknownException(String message)
		{
			super(message);
		}
		public CommandUnknownException(String message, Throwable cause)
		{
			super(message, cause);
		}
		public CommandUnknownException(Throwable cause)
		{
			super(cause);
		}
	}
}
