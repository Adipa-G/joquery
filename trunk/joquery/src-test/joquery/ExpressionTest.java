package joquery;

import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class ExpressionTest
{
    private static Collection<Simple> testList;

    @BeforeClass
    public static void setup()
    {
        testList = new ArrayList<>();
        testList.add(new Simple(1));
        testList.add(new Simple(2));
        testList.add(new Simple(3));
    }

    @AfterClass
    public static void cleanup()
    {
        testList.clear();
        testList = null;
    }

    @Test
    public void Expression_Exec_ShouldFilter() throws QueryException
    {
        SimpleQuery<Simple> query = Q.<Simple>simple()
                .from(testList)
                .where()
                .exec(new Exec<Simple>()
                {
                    public Object exec(Simple simple)
                    {
                        return simple.getId() == 1;
                    }
                });

        Collection<Simple> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    @Test
    public void Expression_Property_ShouldFilter() throws QueryException
    {
        SimpleQuery<Simple> query = Q.<Simple>simple()
                .from(testList)
                .where()
                .property("id")
                .eq()
                .value(1);

        Collection<Simple> filtered = query.execute();
        assertResult(filtered, new int[]{1});
    }

    private static void assertResult(Collection<Simple> list,int[] ids)
    {
        Assert.assertEquals(String.format("Expected items are not retrieved"), ids.length, list.size());
        for (int id : ids)
        {
            boolean found = false;
            for (Simple simple : list)
            {
                found = simple.id == id;
                if (found) break;
            }
            Assert.assertTrue(String.format("Unable to find item with id %s",id),found);
        }
    }

    static class Simple
    {
        private int id;

        Simple(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }
}
