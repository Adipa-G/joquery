package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GtCndExpr<T> extends CndExpr<T>
{
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        LeCndExpr<T> leExpr = new LeCndExpr<>();
        leExpr.left = left; leExpr.right = right;

        boolean le = (Boolean)leExpr.evaluate(t);
        return  !le;
    }
}
