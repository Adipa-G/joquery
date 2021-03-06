package joquery.core.collection.expr.condition;

import joquery.core.QueryException;
import joquery.core.QueryMode;
import joquery.core.collection.expr.IExpr;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public abstract class ConditionalExpr<T> implements IExpr<T>
{
    protected IExpr<T> left;
    protected IExpr<T> right;

    @Override
    public boolean supportsMode(QueryMode mode)
    {
        return mode == QueryMode.WHERE;
    }

    @Override
    public boolean add(IExpr<T> expr)
    {
        if (left == null)
        {
            left = expr;
            return true;
        }
        else if (right == null)
        {
            right = expr;
            return true;
        }
        return false;
    }

    protected void validate() throws QueryException
    {
        if (left == null || right == null)
            throw new QueryException(String.format("%s segment of %s is null",left == null ? "Left":"Right",this));
    }
}
