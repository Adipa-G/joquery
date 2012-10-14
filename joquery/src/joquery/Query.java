package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public interface Query<T, U>
{
    Query<T, U> from(Iterable<T> list);

    Query<T, U> where();

    Query<T, U> exec(Exec<T> exec) throws QueryException;

    Query<T, U> property(String property) throws QueryException;

    Query<T, U> value(Object value) throws QueryException;

    Query<T, U> eq() throws QueryException;

    Query<T, U> lt() throws QueryException;

    Query<T, U> le() throws QueryException;

    Query<T, U> gt() throws QueryException;

    Query<T, U> ge() throws QueryException;

    Query<T, U> in() throws QueryException;

    Query<T, U> between() throws QueryException;

    Collection<U> execute()throws QueryException;

    Collection<U> execute(ResultTransformer<T,U> transformer)throws QueryException;
}
