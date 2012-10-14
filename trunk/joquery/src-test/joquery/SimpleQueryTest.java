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

public class SimpleQueryTest
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
    public void Execute_WithNoFilter_ShouldReturnAll() throws QueryException
    {
        SimpleQuery<Dto> query = Q.<Dto>simple()
                .from(testList);

        Collection<Dto> filtered = query.execute();
        Assert.assertEquals(filtered.size(),testList.size());
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
