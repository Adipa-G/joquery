package joquery.core.collection;

import joquery.Exec;
import joquery.GroupQuery;
import joquery.Grouping;
import joquery.core.QueryException;
import joquery.core.collection.expr.ExecExpr;
import joquery.core.collection.expr.IExpr;
import joquery.core.collection.expr.ReflectionExpr;

import java.util.*;

/**
 * User: Adipa
 * Date: 11/10/12
 * Time: 7:05 PM
 */
public class GroupQueryImpl<Key,U>
        extends QueryImpl<Grouping<Key,U>,GroupQuery<Key,U>>
        implements GroupQuery<Key,U>
{
    private Collection<U> itemsToGroup;
    private List<IExpr<U>> groupExpressions;

    public GroupQueryImpl(Collection<U> itemsToGroup)
    {
        this.itemsToGroup = itemsToGroup;
        groupExpressions = new ArrayList<>();
    }

    @Override
    public GroupQuery<Key,U> groupBy(Exec<U> by)
    {
        groupExpressions.add(new ExecExpr<>(by));
        return this;
    }

    @Override
    public GroupQuery<Key,U> groupBy(String by)
    {
        groupExpressions.add(new ReflectionExpr<U>(by));
        return this;
    }

    @Override
    public Collection<Grouping<Key, U>> list() throws QueryException
    {
        Collection<Grouping<Key, U>> items = getItems();
        setItems(items);
        return transformDefaultSelection();
    }

    @Override
    public Collection<Grouping<Key, U>> getItems() throws QueryException
    {
        HashMap<Object[],Grouping<Key,U>> keyGroupMap = new HashMap<>();
        for (U u : itemsToGroup)
        {
            Object[] keyAsArray = getKey(u);
            Grouping<Key,U> group = getMatchingGroupingIfExists(keyGroupMap,keyAsArray);
            if (group == null)
            {
                group = new Grouping<>((getKeyFromArray(keyAsArray)));
                keyGroupMap.put(keyAsArray,group);
            }
            group.Add(u);
        }
        return keyGroupMap.values();
    }

    private Object[] getKey(U u) throws QueryException
    {
        Object[] keyArray = new Object[groupExpressions.size()];
        for (int i = 0, groupExpressionsSize = groupExpressions.size(); i < groupExpressionsSize; i++)
        {
            IExpr<U> expr = groupExpressions.get(i);
            keyArray[i] = expr.evaluate(u);
        }
        return keyArray;
    }

    private Grouping<Key,U> getMatchingGroupingIfExists(HashMap<Object[],Grouping<Key,U>> map,Object[] searchKey)
    {
        for (Object[] mapKey : map.keySet())
        {
            if (Arrays.equals(mapKey,searchKey))
            {
                return map.get(mapKey);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Key getKeyFromArray(Object[] keyArray)
    {
        if (keyArray.length == 1)
            return (Key) keyArray[0];
        return (Key)keyArray;
    }
}
