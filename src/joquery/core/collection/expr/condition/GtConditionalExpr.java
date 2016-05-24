package joquery.core.collection.expr.condition;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GtConditionalExpr<T> extends ConditionalExpr<T>
{
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        LeConditionalExpr<T> leExpr = new LeConditionalExpr<>();
        leExpr.left = left; leExpr.right = right;

        boolean le = (Boolean)leExpr.evaluate(t);
        return  !le;
    }
}
