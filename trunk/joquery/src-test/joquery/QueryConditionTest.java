package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class QueryConditionTest
{
    private static Collection<Dto> testList;

    @BeforeClass
    public static void setup()
    {
        testList = new ArrayList<>();
        testList.add(new Dto(1));
        testList.add(new Dto(2));
        testList.add(new Dto(3));
    }

    @AfterClass
    public static void cleanup()
    {
        testList.clear();
        testList = null;
    }

    @Test(expected = QueryException.class)
    public void Condition_WithoutWhere_ShouldThrowException() throws QueryException
    {
        CQ.<Dto>filter()
                .from(testList)
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return null;
                    }
                });
    }

    @Test
    public void Condition_Exec_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId() == 1;
                    }
                });

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    @Test (expected = QueryException.class)
    public void Condition_2ExecWithoutComparisonClause_ShouldThrowException() throws QueryException
    {
        CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return null;
                    }
                }).value(1);
    }

    @Test (expected = QueryException.class)
    public void Condition_2ValuesWithoutComparisonClause_ShouldThrowException() throws QueryException
    {
        CQ.<Dto>filter()
                .from(testList)
                .where()
                .value(1).value(1);
    }

    @Test (expected = QueryException.class)
    public void Condition_2PropertiesWithoutComparisonClause_ShouldThrowException() throws QueryException
    {
        CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id").property("id");
    }

    @Test (expected = QueryException.class)
    public void Condition_EqWithOnlyLeft_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq();

        query.execute();
    }

    @Test
    public void Condition_Eq_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    @Test
    public void Condition_EqSwitchOrder_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .value(1).eq().exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                });

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    @Test
    public void Condition_Lt_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).lt().value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    @Test
    public void Condition_Le_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).le().value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1, 2});
    }

    @Test
    public void Condition_Gt_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).gt().value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{3});
    }

    @Test
    public void Condition_Ge_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).ge().value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{2,3});
    }

    @Test
    public void Condition_InWithArray_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).in().value(new int[]{1,2});

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1,2});
    }

    @Test
    public void Condition_InWithIterable_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).in().value(Arrays.asList(1,2));

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1,2});
    }

    @Test
    public void Condition_Between_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).between().value(1).value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1,2});
    }

    @Test (expected = QueryException.class)
    public void Condition_BetweenWithoutRight2_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).between().value(1);

        query.execute();
    }

    @Test
    public void Condition_And_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1)
                .and()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1});
    }

    @Test
    public void Condition_Or_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1)
                .or()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(2);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1,2});
    }

    @Test
    public void Condition_AndOrCombined_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1)
                .or()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(2)
                .and()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(1);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered,new int[]{1});
    }

    @Test(expected = QueryException.class)
    public void Condition_AndWithLeftNonBoolean_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .value(1)
                .and()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(2);

        query.execute();
    }

    @Test(expected = QueryException.class)
    public void Condition_OrWithRightNonBoolean_ShouldThrowException() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .exec(new Exec<Dto>()
                {
                    public Object exec(Dto simple)
                    {
                        return simple.getId();
                    }
                }).eq().value(2)
                .or()
                .value(1);

        query.execute();
    }

    private static void assertResult(Collection<Dto> list,int[] ids)
    {
        A.exp(ids.length).act(list.size(),String.format("Expected items are not retrieved"));
        for (int id : ids)
        {
            boolean found = false;
            for (Dto dto : list)
            {
                found = dto.id == id;
                if (found) break;
            }
            Assert.assertTrue(String.format("Unable to find item with id %s",id),found);
        }
    }

    static class Dto
    {
        private int id;

        Dto(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }
}
