package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface SimpleQuery<T> extends Query<T, SimpleQuery<T>>
{
    Collection<T> execute()throws QueryException;
}
