package joquery.core;

import joquery.ResultTransformer;
import joquery.SimpleQuery;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 11:20 AM
 */
public class SimpleQueryImpl<T> extends QueryImpl<T,T,SimpleQuery<T>> implements SimpleQuery<T>
{
    @Override
    public Collection<T> execute() throws QueryException
    {
        ResultTransformer<T,T> transformer = new ResultTransformer<T,T>()
        {
            @Override
            public T transform(T t)
            {
                return t;
            }
        };
        return super.executeSelect(transformer);
    }
}
