package joquery.core.collection.join;

import joquery.core.collection.expr.IExpr;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 7:07 PM
 */
public class JoinCondition<T,U>
{
    private IExpr<T> left;
    private IExpr<U> right;

    public JoinCondition(IExpr<T> left, IExpr<U> right)
    {
        this.left = left;
        this.right = right;
    }

    public IExpr<T> getLeft()
    {
        return left;
    }

    public IExpr<U> getRight()
    {
        return right;
    }
}
