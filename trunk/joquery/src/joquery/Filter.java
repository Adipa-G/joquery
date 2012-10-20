package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface Filter<T> extends Query<T, Filter<T>>
{
    Collection<T> execute()throws QueryException;
}
