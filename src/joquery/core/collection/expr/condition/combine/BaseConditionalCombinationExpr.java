package joquery.core.collection.expr.condition.combine;

import joquery.core.QueryException;
import joquery.core.collection.expr.IExpr;
import joquery.core.collection.expr.condition.ConditionalExpr;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:58 PM
 */
public abstract class BaseConditionalCombinationExpr<T> extends ConditionalExpr<T>
{
    @Override
    public boolean add(IExpr<T> expr)
    {
        if (left != null && expr instanceof BaseConditionalCombinationExpr)
            return false;

        boolean added = super.add(expr);
        if (added) return true;
        boolean canAddToRight = right.add(expr);
        if (canAddToRight) return true;
        boolean canRightAddToExpr = expr.add(right);
        if (canRightAddToExpr)
        {
            right = expr;
            return true;
        }
        return false;
    }

    @Override
    public Object evaluate(T t) throws QueryException
    {
        validate();
        Object leftVal = left.evaluate(t);
        if (!(leftVal instanceof Boolean))
            throw new QueryException(String.format(getConditionName() + " condition requires boolean value return from left segment." +
                                                           " Which is not from the segment %s ",left));
        Object rightVal = right.evaluate(t);
        if (!(rightVal instanceof Boolean))
            throw new QueryException(String.format(getConditionName() + " condition requires boolean value return from right segment." +
                                                           " Which is not from the segment %s ",right));
        return combine((Boolean)leftVal,(Boolean)rightVal);
    }

    public abstract boolean combine(boolean leftVal,boolean rightVal);

    public abstract String getConditionName();
}
