package joquery;

import joquery.core.QueryException;

import java.util.Collection;
import java.util.function.Function;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 12:43 PM
 */
public interface ResultTransformedQuery<T,U,W extends ResultTransformedQuery<T,U,W>> extends Query<T,W>
{
    U first() throws QueryException;

    U last() throws QueryException;

    Collection<U> list() throws QueryException;

	<V> U project(IProject<T,V,U> project,Function<T,V> property) throws QueryException;

    ResultTransformedQuery<T,U,W> transformDirect(ResultTransformer<T, U> transformer);

    ResultTransformedQuery<T,U,W> transformSelection(ResultTransformer<Object[], U> transformer);

    <X,Y> JoinQuery<U,X,Y> innerJoin(ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException;

    <X,Y> JoinQuery<U,X,Y> leftOuterJoin(ResultTransformedQuery<X, X, ?> rightQuery)  throws QueryException;

    <X,Y> JoinQuery<U,X,Y> rightOuterJoin(ResultTransformedQuery<X, X, ?> rightQuery)  throws QueryException;

    <Key> GroupQuery<Key,U> group()  throws QueryException;
}
