package joquery;

import joquery.core.collection.FilterImpl;
import joquery.core.collection.SelectionQueryImpl;

import java.util.Collection;

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

    public static <T> Filter<T> filter(Collection<T> ts)
    {
        return new FilterImpl<T>().from(ts);
    }

    public static <T,U> SelectionQuery<T,U> query()
    {
        return new SelectionQueryImpl<>();
    }

    public static <T,U> SelectionQuery<T,U> query(Collection<T> ts)
    {
        return new SelectionQueryImpl<T,U>().from(ts);
    }
}
