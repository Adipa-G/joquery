package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class FilterTest
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
        Filter<Dto> query = CQ.<Dto>filter()
                .from(testList);

        Collection<Dto> filtered = query.execute();
        A.exp(testList).act(filtered);
    }

    static class Dto
    {
        private int id;

        Dto(int id)
        {
            this.id = id;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Dto dto = (Dto) o;
            return id == dto.id;
        }

        @Override
        public int hashCode()
        {
            return id;
        }
    }
}
