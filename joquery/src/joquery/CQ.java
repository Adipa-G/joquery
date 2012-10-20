package joquery;

import joquery.core.collection.FilterImpl;
import joquery.core.collection.SelectionQueryImpl;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 10:52 AM
 */
public class CQ
{
    public static <T> Filter<T> filter()
    {
        return new FilterImpl<>();
    }

    public static <T,U> SelectionQuery<T,U> query()
    {
        return new SelectionQueryImpl<>();
    }
}
