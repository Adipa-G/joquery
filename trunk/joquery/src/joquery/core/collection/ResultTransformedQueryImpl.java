package joquery.core.collection;

import joquery.GroupQuery;
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
    private ResultTransformer<T, U> directTransformer;
    private ResultTransformer<Object[], U> selectionTransformer;

    @Override
    public U first() throws QueryException
    {
        return super.first(list());
    }

    @Override
    public U last() throws QueryException
    {
        return super.last(list());
    }

    @Override
    public Collection<U> list() throws QueryException
    {
        boolean hasSelections = super.getSelections().size() > 0;

        if (hasSelections && selectionTransformer == null)
            throw new QueryException("Cannot list with a select items are available without using a selection" +
            " transformer, use 'transformSelection(ResultTransformer<Object[], U> transformer)' to set the transformer");

        if (hasSelections)
            return super.transformCustomSelection(selectionTransformer);

        if (directTransformer != null)
            return super.transformDefaultSelection(directTransformer);

        return super.transformDefaultSelection();
    }

    @Override
    public ResultTransformedQueryImpl<T,U,W> transformDirect(ResultTransformer<T, U> transformer)
    {
        directTransformer = transformer;
        return this;
    }

    @Override
    public ResultTransformedQueryImpl<T,U,W> transformSelection(ResultTransformer<Object[], U> transformer)
    {
        selectionTransformer = transformer;
        return this;
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

    @Override
    public <Key> GroupQuery<Key,U> group() throws QueryException
    {
        //noinspection unchecked
        return new GroupQueryImpl<Key,U>().from(list());
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
