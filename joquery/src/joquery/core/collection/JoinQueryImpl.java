package joquery.core.collection;

import joquery.JoinPair;
import joquery.JoinQuery;
import joquery.ResultTransformedQuery;
import joquery.core.JoinMode;
import joquery.core.QueryException;
import joquery.core.collection.expr.FunctionExpr;
import joquery.core.collection.expr.ReflectionExpr;
import joquery.core.collection.join.JoinCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 1:35 PM
 */
public class JoinQueryImpl<T,U,V> extends ResultTransformedQueryImpl<JoinPair<T,U>,V,JoinQuery<T,U,V>> implements JoinQuery<T,U,V>
{
    private JoinMode joinMode;
    private ResultTransformedQuery<T,T,?> leftQuery;
    private ResultTransformedQuery<U,U,?> rightQuery;
    private List<JoinCondition<T,U>> joinConditions;

    public JoinQueryImpl(JoinMode joinMode,
                         ResultTransformedQuery<T,T,?> leftQuery,
                         ResultTransformedQuery<U,U,?> rightQuery)
    {
        this.joinMode = joinMode;
        this.leftQuery = leftQuery;
        this.rightQuery = rightQuery;
        this.joinConditions = new ArrayList<>();
    }

    @Override
    public Iterable<JoinPair<T, U>> getItems() throws QueryException
    {
        Iterable<JoinPair<T, U>> items = super.getItems();
        if (items == Collections.EMPTY_LIST)
        {
            switch (joinMode)
            {
                case INNER:
                    setItems(innerJoin());
                    break;
                case LEFT_OUTER:
                    setItems(leftOuterJoin());
                    break;
                case RIGHT_OUTER:
                    setItems(rightOuterJoin());
                    break;
            }
            return getItems();
        }
        return items;
    }

    @Override
    public JoinQuery<T, U, V> on(Function<T,?> left, Function<U,?> right)
    {
	    //noinspection Convert2Diamond
	    joinConditions.add(new JoinCondition<>(new FunctionExpr<T>(left),new FunctionExpr<U>(right)));
        return this;
    }

    @Override
    public JoinQuery<T, U, V> on(String left, String right)
    {
	    //noinspection Convert2Diamond
        joinConditions.add(new JoinCondition<>(new ReflectionExpr<T>(left),new ReflectionExpr<U>(right)));
        return this;
    }

    private Iterable<JoinPair<T, U>> innerJoin() throws QueryException
    {
        Collection<T> leftList = leftQuery.list();
        Collection<U> rightList = rightQuery.list();

        ArrayList<JoinPair<T, U>> results = new ArrayList<>();
        for (T left : leftList)
        {
            for (U right : rightList)
            {
                boolean match = isMatch(left, right);
                if (match)
                {
                    results.add(new JoinPair<>(left,right));
                }
            }
        }
        return results;
    }

    private Iterable<JoinPair<T, U>> leftOuterJoin() throws QueryException
    {
        Collection<T> leftList = leftQuery.list();
        Collection<U> rightList = rightQuery.list();

        ArrayList<JoinPair<T, U>> results = new ArrayList<>();
        for (T left : leftList)
        {
            U rightMatch = null;
            for (U right : rightList)
            {
                boolean match = isMatch(left, right);
                if (match)
                {
                    rightMatch = right;
                    break;
                }
            }
            results.add(new JoinPair<>(left,rightMatch));
        }
        return results;
    }

    private Iterable<JoinPair<T, U>> rightOuterJoin() throws QueryException
    {
        Collection<T> leftList = leftQuery.list();
        Collection<U> rightList = rightQuery.list();

        ArrayList<JoinPair<T, U>> results = new ArrayList<>();
        for (U right : rightList)
        {
            T leftMatch = null;
            for (T left : leftList)
            {
                boolean match = isMatch(left, right);
                if (match)
                {
                    leftMatch = left;
                    break;
                }
            }
            results.add(new JoinPair<>(leftMatch,right));
        }
        return results;
    }

    private boolean isMatch(T left, U right) throws QueryException
    {
        boolean match = true;
        for (JoinCondition<T, U> condition : joinConditions)
        {
            Object leftConditionValue = condition.getLeft().evaluate(left);
            Object rightConditionValue = condition.getRight().evaluate(right);
            match = match && (leftConditionValue == rightConditionValue
                    || leftConditionValue.equals(rightConditionValue));
        }
        return match;
    }
}
