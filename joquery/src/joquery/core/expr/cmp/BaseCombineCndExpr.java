package joquery.core.expr.cmp;

import joquery.core.QueryException;
import joquery.core.expr.IExpr;

/**
 * User: Adipa
 * Date: 10/14/12
 * Time: 12:58 PM
 */
public abstract class BaseCombineCndExpr<T> extends CndExpr<T>
{
    @Override
    public boolean add(IExpr<T> expr)
    {
        if (left != null && expr instanceof BaseCombineCndExpr)
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
