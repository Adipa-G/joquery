package joquery.core.collection;

import joquery.JoinQuery;
import joquery.ResultTransformedQuery;
import joquery.ResultTransformer;
import joquery.core.JoinMode;
import joquery.core.QueryException;

import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 12:44 PM
 */
public abstract class ResultTransformedQueryImpl<T,U,W extends ResultTransformedQuery>
        extends QueryImpl<T,W>
        implements ResultTransformedQuery<T,U,W>
{
    @Override
    public Collection<U> execute() throws QueryException
    {
        return super.transformDefaultSelection();
    }

    @Override
    public Collection<U> execute(ResultTransformer<T, U> transformer) throws QueryException
    {
        return super.transformDefaultSelection(transformer);
    }

    @Override
    public Collection<U> executeSelection(ResultTransformer<Object[], U> transformer) throws QueryException
    {
        return super.transformCustomSelection(transformer);
    }

    @Override
    public <X,Y> JoinQuery<U,X,Y> innerJoin(ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException
    {
        return doJoin(JoinMode.INNER,rightQuery);
    }

    @Override
    public <X, Y> JoinQuery<U, X, Y> leftOuterJoin(ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException
    {
        return doJoin(JoinMode.LEFT_OUTER,rightQuery);
    }

    @Override
    public <X, Y> JoinQuery<U, X, Y> rightOuterJoin(ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException
    {
        return doJoin(JoinMode.RIGHT_OUTER,rightQuery);
    }

    private <X, Y> JoinQuery<U, X, Y> doJoin(JoinMode joinMode,ResultTransformedQuery<X, X, ?> rightQuery) throws QueryException
    {
        if (getSelections().size() > 0)
        {
            throw new QueryException("Join is not possible when there is a transformation");
        }
        //noinspection unchecked
        return new JoinQueryImpl<>(joinMode,(ResultTransformedQuery<U,U,?>) this,rightQuery);
    }
}
