package joquery.core.collection.expr.condition;

import joquery.core.QueryException;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class EqConditionalExpr<T> extends ConditionalExpr<T>
{
    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        Object leftVal = left.evaluate(t);
        Object rightVal = right.evaluate(t);
        return leftVal == rightVal || leftVal != null && leftVal.equals(rightVal);
    }
}
