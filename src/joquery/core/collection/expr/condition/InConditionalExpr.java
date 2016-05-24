package joquery.core.collection.expr.condition;

import joquery.core.QueryException;

import java.lang.reflect.Array;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */
public class InConditionalExpr<T> extends ConditionalExpr<T>
{
    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public Object evaluate(T t)  throws QueryException
    {
        validate();
        Object leftVal = left.evaluate(t);
        Object rightVal = right.evaluate(t);
        if (rightVal == null)
            return false;
        if (checkArray(leftVal, rightVal)) return true;
        if (checkIterable(leftVal, rightVal)) return true;
        return leftVal == rightVal || rightVal.equals(leftVal);
    }

    private static boolean checkArray(Object leftVal, Object rightVal)
    {
        if (rightVal.getClass().isArray())
        {
            int length = Array.getLength(rightVal);
            for (int i = 0; i < length; i++)
            {
                Object value = Array.get(rightVal,i);
                if (leftVal == value || leftVal != null && leftVal.equals(value))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkIterable(Object leftVal, Object rightVal)
    {
        if (rightVal instanceof Iterable)
        {
            Iterable iterable = (Iterable) rightVal;
            for (Object value : iterable)
            {
                if (leftVal == value || leftVal != null && leftVal.equals(value))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
