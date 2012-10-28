package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 12:43 PM
 */
public interface ResultTransformedQuery<T,U,W extends ResultTransformedQuery> extends Query<T,W>
{
    Collection<U> execute()throws QueryException;

    Collection<U> execute(ResultTransformer<T,U> transformer)throws QueryException;

    Collection<U> executeSelection(ResultTransformer<Object[],U> transformer)throws QueryException;

    <X,Y> JoinQuery<U,X,Y> innerJoin(ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException;
}
