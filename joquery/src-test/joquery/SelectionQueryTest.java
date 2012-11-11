package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    public void first_WithNoFilter_ShouldReturnAll() throws QueryException
    {
        SrcDto first = CQ.<SrcDto,SrcDto>query(testList).first();

        A.exp(testList.iterator().next()).act(first);
    }

    @Test
    public void last_WithNoFilter_ShouldReturnAll() throws QueryException
    {
        SrcDto last = CQ.<SrcDto,SrcDto>query(testList).last();

        Iterator<SrcDto> iterator = testList.iterator();
        SrcDto expected = null;
        while (iterator.hasNext())
        {
            expected = iterator.next();
        }
        A.exp(expected).act(last);
    }

    @Test
    public void list_UseWithoutTransformation_ShouldReturnAll() throws QueryException
    {
        SelectionQuery<SrcDto,SrcDto> query = CQ.<SrcDto,SrcDto>query()
                .from(testList);

        Collection<SrcDto> filtered = query.list();
        A.exp(filtered.size()).act(testList.size());
    }

    @Test
    public void list_WithNoSelectionNoFilterWithTransformer_ShouldReturnAll() throws QueryException
    {
        SelectionQuery<SrcDto,DestDto> query = CQ.<SrcDto,DestDto>query()
                .from(testList);

        Collection<DestDto> filtered = query
                .transformDirect(new ResultTransformer<SrcDto, DestDto>()
                {
                    @Override
                    public DestDto transform(SrcDto srcDto)
                    {
                        return new DestDto(srcDto.getId());
                    }
                })
                .list();
        verifyEquals(testList, filtered);
    }

    @Test
    public void list_WithSelectionNoFilterWithTransformer_ShouldReturnAll() throws QueryException
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

        Collection<DestDto> filtered = query
                .transformSelection(new ResultTransformer<Object[], DestDto>()
                {
                    @Override
                    public DestDto transform(Object[] selection)
                    {
                        int id = (Integer) selection[0];
                        return new DestDto(id);
                    }
                })
                .list();

        verifyEquals(testList, filtered);
    }

    private static void verifyEquals(Collection<SrcDto> srcDtos, Collection<DestDto> destDtos)
    {
        A.exp(srcDtos.size()).act(destDtos.size());
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
