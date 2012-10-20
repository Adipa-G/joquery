package joquery.core.collection;

import joquery.ResultTransformer;
import joquery.SelectionQuery;
import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:28 PM
 */
public class SelectionQueryImpl<T,U> extends QueryImpl<T,SelectionQuery<T,U>> implements SelectionQuery<T,U>
{
    @Override
    public Collection<U> execute() throws QueryException
    {
        return super.selectTransformed();
    }

    @Override
    public Collection<U> execute(ResultTransformer<U> transformer) throws QueryException
    {
        return super.selectTransformed(transformer);
    }
}
