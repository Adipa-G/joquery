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

public class ProjectionTest
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
    public void projection_Sum_ShouldSum() throws QueryException
    {
	    Integer result = CQ.<Dto,Integer>query()
                .from(testList)
                .project(Projections.<Dto,Integer>Sum(),Dto::getId);

        A.exp(6).act(result);
    }

	@Test
	public void projection_Avg_ShouldAvg() throws QueryException
	{
		Integer result = CQ.<Dto,Integer>query()
		                  .from(testList)
		                  .project(Projections.<Dto, Integer,Integer >Avg(), Dto::getId);

		A.exp(2).act(result);
	}

    static class Dto
    {
        private int id;
        private int id2;

        Dto(int id)
        {
            this.id = id;
            this.id2 = id;
        }

        public int getId()
        {
            return id;
        }

        public int getId3()
        {
            return id;
        }
    }
}
