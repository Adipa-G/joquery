package joquery.core.collection.expr.condition;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class GeConditionalExpr<T> extends ConditionalExpr<T>
{
    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        LtConditionalExpr<T> ltExpr = new LtConditionalExpr<>();
        ltExpr.left = left; ltExpr.right = right;

        boolean lt = (Boolean)ltExpr.evaluate(t);
        return  !lt;
    }
}
