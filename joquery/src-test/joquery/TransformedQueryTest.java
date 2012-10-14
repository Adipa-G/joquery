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

public class TransformedQueryTest
{
    private static Collection<SrcDto> testList;

    @BeforeClass
    public static void setup()
    {
        testList = new ArrayList<>();
        testList.add(new SrcDto(1));
        testList.add(new SrcDto(2));
        testList.add(new SrcDto(3));
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
        TransformedQuery<SrcDto,DestDto> query = Q.<SrcDto,DestDto>transformed()
                .from(testList);

        Collection<DestDto> filtered = query.execute(new ResultTransformer<SrcDto, DestDto>()
        {
            @Override
            public DestDto transform(SrcDto srcDto)
            {
                return new DestDto(srcDto.getId());
            }
        });
        Assert.assertEquals(filtered.size(),testList.size());
    }

    static class SrcDto
    {
        private int id;

        SrcDto(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }

    static class DestDto
    {
        private int id;

        DestDto(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }
    }
}
