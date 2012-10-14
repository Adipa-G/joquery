package joquery;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:19 AM
 */
public interface SimpleQuery<T> extends Query<T,T>
{
    @Override
    SimpleQuery<T> from(Iterable<T> list);

    @Override
    SimpleQuery<T> where();

    @Override
    SimpleQuery<T> exec(Exec<T> exec) throws QueryException;

    @Override
    SimpleQuery<T> property(String property) throws QueryException;

    @Override
    SimpleQuery<T> value(Object value) throws QueryException;

    @Override
    SimpleQuery<T> eq() throws QueryException;

    @Override
    SimpleQuery<T> lt() throws QueryException;

    @Override
    SimpleQuery<T> le() throws QueryException;

    @Override
    SimpleQuery<T> gt() throws QueryException;

    @Override
    SimpleQuery<T> ge() throws QueryException;

    @Override
    SimpleQuery<T> in() throws QueryException;

    @Override
    SimpleQuery<T> between() throws QueryException;
}
