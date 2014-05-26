package joquery.core.collection;

import joquery.Filter;
import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:20 AM
 */
public class FilterImpl<T> extends QueryImpl<T,Filter<T>> implements Filter<T>
{
    @Override
    public T first() throws QueryException
    {
        return super.first(list());
    }

    @Override
    public T last() throws QueryException
    {
        return super.last(list());
    }

    @Override
    public Collection<T> list() throws QueryException
    {
        return transformDefaultSelection();
    }
}
