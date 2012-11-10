package joquery;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public interface Query<T,W extends Query<T,W>>
{
    W from(Iterable<T> list);

    W where();

    W select();

    W exec(Exec<T> exec) throws QueryException;

    W property(String property) throws QueryException;

    W value(Object value) throws QueryException;

    W and() throws QueryException;

    W or() throws QueryException;

    W eq() throws QueryException;

    W lt() throws QueryException;

    W le() throws QueryException;

    W gt() throws QueryException;

    W ge() throws QueryException;

    W in() throws QueryException;

    W between() throws QueryException;

    W orderBy() throws  QueryException;
}
