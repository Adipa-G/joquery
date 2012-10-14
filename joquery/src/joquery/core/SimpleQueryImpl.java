package joquery.core;

import joquery.Exec;
import joquery.SimpleQuery;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:20 AM
 */
public class SimpleQueryImpl<T> extends QueryImpl<T,T> implements SimpleQuery<T>
{
    @Override
    public SimpleQuery<T> from(Iterable<T> list)
    {
        return (SimpleQuery<T>)super.from(list);
    }

    @Override
    public SimpleQuery<T> where()
    {
        return (SimpleQuery<T>)super.where();
    }

    @Override
    public SimpleQuery<T> exec(Exec<T> exec) throws QueryException
    {
        return (SimpleQuery<T>)super.exec(exec);
    }

    @Override
    public SimpleQuery<T> property(String property) throws QueryException
    {
        return (SimpleQuery<T>)super.property(property);
    }

    @Override
    public SimpleQuery<T> value(Object value) throws QueryException
    {
        return (SimpleQuery<T>)super.value(value);
    }

    @Override
    public SimpleQuery<T> eq() throws QueryException
    {
        return (SimpleQuery<T>)super.eq();
    }

    @Override
    public SimpleQuery<T> lt() throws QueryException
    {
        return (SimpleQuery<T>)super.lt();
    }

    @Override
    public SimpleQuery<T> le() throws QueryException
    {
        return (SimpleQuery<T>)super.le();
    }

    @Override
    public SimpleQuery<T> gt() throws QueryException
    {
        return (SimpleQuery<T>)super.gt();
    }

    @Override
    public SimpleQuery<T> ge() throws QueryException
    {
        return (SimpleQuery<T>)super.ge();
    }

    @Override
    public SimpleQuery<T> in() throws QueryException
    {
        return (SimpleQuery<T>)super.in();
    }

    @Override
    public SimpleQuery<T> between() throws QueryException
    {
        return (SimpleQuery<T>)super.between();
    }
}
