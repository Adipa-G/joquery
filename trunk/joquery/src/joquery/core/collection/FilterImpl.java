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
    public Collection<T> execute() throws QueryException
    {
        return transformDefaultSelection();
    }
}
