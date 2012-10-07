package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GeCmpExpr<T> extends CmpExpr<T>
{
    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        LtCmpExpr<T> ltExpr = new LtCmpExpr<>();
        ltExpr.left = left; ltExpr.right = right;

        boolean lt = (Boolean)ltExpr.evaluate(t);
        return  !lt;
    }
}
