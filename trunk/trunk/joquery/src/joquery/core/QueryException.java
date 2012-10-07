package joquery.core;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class QueryException extends Exception
{
    public QueryException()
    {
    }

    public QueryException(String message)
    {
        super(message);
    }

    public QueryException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public QueryException(Throwable cause)
    {
        super(cause);
    }

    public QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
