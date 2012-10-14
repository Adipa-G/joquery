package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface TransformedQuery<T,U> extends Query<T,TransformedQuery<T,U>>
{
    Collection<U> execute(ResultTransformer<T,U> transformer)throws QueryException;
}
