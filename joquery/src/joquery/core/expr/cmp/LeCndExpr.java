package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class LeCndExpr<T> extends CndExpr<T>
{
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        LtCndExpr<T> ltExpr = new LtCndExpr<>();
        ltExpr.left = left; ltExpr.right = right;

        EqCndExpr<T> eqExpr = new EqCndExpr<>();
        eqExpr.left = left; eqExpr.right = right;

        boolean lt = (Boolean)ltExpr.evaluate(t);
        boolean eq = (Boolean)eqExpr.evaluate(t);
        return  lt || eq;
    }
}
