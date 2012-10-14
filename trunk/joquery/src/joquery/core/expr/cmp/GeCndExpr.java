package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GeCndExpr<T> extends CndExpr<T>
{
    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        LtCndExpr<T> ltExpr = new LtCndExpr<>();
        ltExpr.left = left; ltExpr.right = right;

        boolean lt = (Boolean)ltExpr.evaluate(t);
        return  !lt;
    }
}
