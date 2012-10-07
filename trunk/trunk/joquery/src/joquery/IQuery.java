package joquery;

import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public interface IQuery<T>
{
    IQuery<T> from(Iterable<T> list);

    IQuery<T> where();

    IQuery<T> exec(IExec<T> exec) throws QueryException;

    IQuery<T> property(String property) throws QueryException;

    IQuery<T> value(Object value) throws QueryException;

    IQuery<T> eq() throws QueryException;

    IQuery<T> lt() throws QueryException;

    IQuery<T> le() throws QueryException;

    IQuery<T> gt() throws QueryException;

    IQuery<T> ge() throws QueryException;

    IQuery<T> in() throws QueryException;

    IQuery<T> between() throws QueryException;

    Collection<T> execute()throws QueryException;
}
