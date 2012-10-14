package joquery.core;

import joquery.ResultTransformer;
import joquery.TransformedQuery;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:28 PM
 */
public class TransformedQueryImpl<T,U> extends QueryImpl<T,U,TransformedQuery<T,U>> implements TransformedQuery<T,U>
{
    @Override
    public Collection<U> execute(ResultTransformer<T, U> transformer) throws QueryException
    {
        return super.executeSelect(transformer);
    }
}
