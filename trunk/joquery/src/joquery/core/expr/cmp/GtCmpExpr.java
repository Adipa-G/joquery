package joquery.core.expr.cmp;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GtCmpExpr<T> extends CmpExpr<T>
{
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        LeCmpExpr<T> leExpr = new LeCmpExpr<>();
        leExpr.left = left; leExpr.right = right;

        boolean le = (Boolean)leExpr.evaluate(t);
        return  !le;
    }
}
