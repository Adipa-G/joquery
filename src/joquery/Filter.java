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
    T first() throws QueryException;

    T last() throws QueryException;

    Collection<T> list() throws QueryException;
}
