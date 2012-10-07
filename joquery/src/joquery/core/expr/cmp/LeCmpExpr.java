package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class LeCmpExpr<T> extends CmpExpr<T>
{
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        LtCmpExpr<T> ltExpr = new LtCmpExpr<>();
        ltExpr.left = left; ltExpr.right = right;

        EqCmpExpr<T> eqExpr = new EqCmpExpr<>();
        eqExpr.left = left; eqExpr.right = right;

        boolean lt = (Boolean)ltExpr.evaluate(t);
        boolean eq = (Boolean)eqExpr.evaluate(t);
        return  lt || eq;
    }
}
