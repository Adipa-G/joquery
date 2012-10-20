package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface SelectionQuery<T,U> extends Query<T,SelectionQuery<T,U>>
{
    Collection<U> execute()throws QueryException;

    Collection<U> execute(ResultTransformer<U> transformer)throws QueryException;
}
