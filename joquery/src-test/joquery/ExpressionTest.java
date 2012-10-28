package joquery;

import assertions.A;
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

    @Test
    public void Expression_Exec_ShouldFilter() throws QueryException
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

    @Test
    public void Expression_Property_ShouldFilter() throws QueryException
    {
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList)
                .where()
                .property("id")
                .eq()
                .value(1);

        Collection<Dto> filtered = query.execute();
        assertResult(filtered, new int[]{1});
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
            Assert.assertTrue(String.format("Unable to find item with id %s", id), found);
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
