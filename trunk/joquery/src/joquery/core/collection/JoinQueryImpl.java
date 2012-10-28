package joquery.core.collection;

import joquery.Exec;
import joquery.JoinPair;
import joquery.JoinQuery;
import joquery.ResultTransformedQuery;
import joquery.core.JoinMode;
import joquery.core.QueryException;
import joquery.core.collection.expr.ExecExpr;
import joquery.core.collection.expr.ReflectionExpr;
import joquery.core.collection.join.JoinCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: Adipa
 * Date: 10/28/12
 * Time: 1:35 PM
 */
public class JoinQueryImpl<T,U,V> extends ResultTransformedQueryImpl<JoinPair<T,U>,V,JoinQuery> implements JoinQuery<T,U,V>
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
            }
            return getItems();
        }
        return items;
    }

    @Override
    public JoinQuery<T, U, V> on(Exec<T> left, Exec<U> right)
    {
        joinConditions.add(new JoinCondition<>(new ExecExpr<>(left),new ExecExpr<>(right)));
        return this;
    }

    @Override
    public JoinQuery<T, U, V> on(String left, String right)
    {
        joinConditions.add(new JoinCondition<>(new ReflectionExpr<T>(left),new ReflectionExpr<U>(right)));
        return this;
    }

    private Iterable<JoinPair<T, U>> innerJoin() throws QueryException
    {
        Collection<T> leftList = leftQuery.execute();
        Collection<U> rightList = rightQuery.execute();

        ArrayList<JoinPair<T, U>> results = new ArrayList<>();
        for (T left : leftList)
        {
            for (U right : rightList)
            {
                boolean match = true;
                for (JoinCondition<T, U> condition : joinConditions)
                {
                    Object leftConditionValue = condition.getLeft().evaluate(left);
                    Object rightConditionValue = condition.getRight().evaluate(right);
                    match = match && (leftConditionValue == rightConditionValue
                            || leftConditionValue.equals(rightConditionValue));
                }
                if (match)
                {
                    results.add(new JoinPair<>(left,right));
                }
            }
        }
        return results;
    }
}
