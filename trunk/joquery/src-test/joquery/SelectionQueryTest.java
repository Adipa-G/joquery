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

public class SelectionQueryTest
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
    public void Execute_UseWithoutTransformation_ShouldReturnAll() throws QueryException
    {
        SelectionQuery<SrcDto,SrcDto> query = CQ.<SrcDto,SrcDto>query()
                .from(testList);

        Collection<SrcDto> filtered = query.execute();
        Assert.assertEquals(filtered.size(),testList.size());
    }

    @Test
    public void Execute_WithNoSelectionNoFilterWithTransformer_ShouldReturnAll() throws QueryException
    {
        SelectionQuery<SrcDto,DestDto> query = CQ.<SrcDto,DestDto>query()
                .from(testList);

        Collection<DestDto> filtered = query.execute(new ResultTransformer<DestDto>()
        {
            @Override
            public DestDto transform(Object[] selection)
            {
                SrcDto srcDto = (SrcDto) selection[0];
                return new DestDto(srcDto.getId());
            }
        });
        VerifyEquals(testList,filtered);
    }

    @Test
    public void Execute_WithSelectionNoFilterWithTransformer_ShouldReturnAll() throws QueryException
    {
        SelectionQuery<SrcDto,DestDto> query = CQ.<SrcDto,DestDto>query()
                .from(testList)
                .select().exec(new Exec<SrcDto>()
                {
                    @Override
                    public Object exec(SrcDto srcDto)
                    {
                        return srcDto.getId();
                    }
                });

        Collection<DestDto> filtered = query.execute(new ResultTransformer<DestDto>()
        {
            @Override
            public DestDto transform(Object[] selection)
            {
                int id = (Integer)selection[0];
                return new DestDto(id);
            }
        });
        VerifyEquals(testList,filtered);
    }

    private static void VerifyEquals(Collection<SrcDto> srcDtos,Collection<DestDto> destDtos)
    {
        Assert.assertEquals(srcDtos.size(),destDtos.size());
        for (SrcDto srcDto : srcDtos)
        {
            boolean found = false;
            for (DestDto destDto : destDtos)
            {
                if (found = srcDto.getId() == destDto.getId())
                    break;
            }
            Assert.assertTrue(String.format("Could not found dest dto with id %s",srcDto.getId()),found);
        }
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
