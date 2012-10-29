package joquery;

import assertions.A;
import joquery.core.QueryException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * User: Adipa
 * Date: 10/6/12
 * Time: 9:31 PM
 */

public class JoinTest
{
    private static Collection<LeftDto> leftList;
    private static Collection<RightDto> rightList;

    @BeforeClass
    public static void setup()
    {
        leftList = new ArrayList<>();
        rightList = new ArrayList<>();

        //inner joined items
        leftList.add(new LeftDto(1,"1"));
        rightList.add(new RightDto(1,100));
        leftList.add(new LeftDto(2,"2"));
        rightList.add(new RightDto(2,101));
        leftList.add(new LeftDto(3,"3"));
        rightList.add(new RightDto(3,102));
        leftList.add(new LeftDto(4,"4"));
        rightList.add(new RightDto(4,103));

        //left only items
        leftList.add(new LeftDto(5,"5"));
        leftList.add(new LeftDto(6,"6"));
        leftList.add(new LeftDto(7,"7"));

        //right only items
        rightList.add(new RightDto(8,104));
        rightList.add(new RightDto(9,105));
        rightList.add(new RightDto(10,106));
    }

    @AfterClass
    public static void cleanup()
    {
        leftList.clear();
        rightList.clear();
        leftList = null;
        rightList = null;
    }

    @Test
    public void Join_TwoEmptyQueries_ShouldReturnEmpty() throws QueryException
    {
        SelectionQuery<LeftDto,LeftDto> leftQuery = CQ.<LeftDto,LeftDto>query()
                .from(Collections.<LeftDto>emptyList());
        SelectionQuery<RightDto,RightDto> rightQuery = CQ.<RightDto,RightDto>query()
                .from(Collections.<RightDto>emptyList());
        JoinQuery<LeftDto,RightDto,JoinedDto> joinQuery = leftQuery.innerJoin(rightQuery);

        Collection<JoinedDto> results = joinQuery.execute();

        A.exp(0).act(results.size());
    }

    @Test(expected = QueryException.class)
    public void Join_LeftWithTransformation_ShouldThrowException() throws QueryException
    {
        SelectionQuery<LeftDto,RightDto> leftQuery = CQ.<LeftDto,RightDto>query()
                .from(Collections.<LeftDto>emptyList())
                .select().property("id").property("text");
        SelectionQuery<RightDto,RightDto> rightQuery = CQ.<RightDto,RightDto>query()
                .from(Collections.<RightDto>emptyList());
        JoinQuery<RightDto,RightDto,JoinedDto> joinQuery = leftQuery.innerJoin(rightQuery);

        joinQuery.execute();
    }

    @Test
    public void Join_InnerWithExec_ShouldJoin() throws QueryException
    {
        SelectionQuery<LeftDto,LeftDto> leftQuery = CQ.<LeftDto,LeftDto>query()
                .from(leftList);
        SelectionQuery<RightDto,RightDto> rightQuery = CQ.<RightDto,RightDto>query()
                .from(rightList);
        JoinQuery<LeftDto,RightDto,JoinedDto> joinQuery = leftQuery
                .<RightDto,JoinedDto>innerJoin(rightQuery)
                .on(
                        new Exec<LeftDto>()
                        {
                            @Override
                            public Object exec(LeftDto leftDto)
                            {
                                return leftDto.getId();
                            }
                        }
                        , new Exec<RightDto>()
                        {
                            @Override
                            public Object exec(RightDto rightDto)
                            {
                                return rightDto.getLeftId();
                            }
                        }
                   );

        Collection<JoinedDto> results = joinQuery.execute(new ResultTransformer<JoinPair<LeftDto, RightDto>, JoinedDto>()
        {
            @Override
            public JoinedDto transform(JoinPair<LeftDto, RightDto> selection)
            {
                return new JoinedDto(selection.getLeft().getText()
                        ,selection.getLeft().getId()
                        ,selection.getRight().getId());
            }
        });

        A.exp(innerJoin()).act(results);
    }

    @Test
    public void Join_InnerWithProperty_ShouldJoin() throws QueryException
    {
        Collection<JoinedDto> results = CQ.<LeftDto, LeftDto>query()
                .from(leftList).<RightDto, JoinedDto>innerJoin(CQ.<RightDto, RightDto>query().from(rightList))
                .on("id", "leftId").execute(
                        new ResultTransformer<JoinPair<LeftDto, RightDto>, JoinedDto>()
                        {
                            @Override
                            public JoinedDto transform(JoinPair<LeftDto, RightDto> selection)
                            {
                                return new JoinedDto(selection.getLeft().getText()
                                        , selection.getLeft().getId()
                                        , selection.getRight().getId());
                            }
                        });

        A.exp(innerJoin()).act(results);
    }

    @Test
    public void Join_LeftOuter_ShouldJoin() throws QueryException
    {
        SelectionQuery<LeftDto,LeftDto> leftQuery = CQ.<LeftDto,LeftDto>query()
                .from(leftList);
        SelectionQuery<RightDto,RightDto> rightQuery = CQ.<RightDto,RightDto>query()
                .from(rightList);
        JoinQuery<LeftDto,RightDto,JoinedDto> joinQuery = leftQuery
                .<RightDto,JoinedDto>leftOuterJoin(rightQuery)
                .on(
                        new Exec<LeftDto>()
                        {
                            @Override
                            public Object exec(LeftDto leftDto)
                            {
                                return leftDto.getId();
                            }
                        }
                        , new Exec<RightDto>()
                        {
                            @Override
                            public Object exec(RightDto rightDto)
                            {
                                return rightDto.getLeftId();
                            }
                        }
                   );

        Collection<JoinedDto> results = joinQuery.execute(new ResultTransformer<JoinPair<LeftDto, RightDto>, JoinedDto>()
        {
            @Override
            public JoinedDto transform(JoinPair<LeftDto, RightDto> selection)
            {
                return new JoinedDto(selection.getLeft().getText()
                        ,selection.getLeft().getId()
                        ,(selection.getRight() != null ? selection.getRight().getId() : -1));
            }
        });

        A.exp(leftOuterJoin()).act(results);
    }

    @Test
    public void Join_RightOuter_ShouldJoin() throws QueryException
    {
        SelectionQuery<LeftDto,LeftDto> leftQuery = CQ.<LeftDto,LeftDto>query()
                .from(leftList);
        SelectionQuery<RightDto,RightDto> rightQuery = CQ.<RightDto,RightDto>query()
                .from(rightList);
        JoinQuery<LeftDto,RightDto,JoinedDto> joinQuery = leftQuery
                .<RightDto,JoinedDto>rightOuterJoin(rightQuery)
                .on(
                        new Exec<LeftDto>()
                        {
                            @Override
                            public Object exec(LeftDto leftDto)
                            {
                                return leftDto.getId();
                            }
                        }
                        , new Exec<RightDto>()
                        {
                            @Override
                            public Object exec(RightDto rightDto)
                            {
                                return rightDto.getLeftId();
                            }
                        }
                   );

        Collection<JoinedDto> results = joinQuery.execute(new ResultTransformer<JoinPair<LeftDto, RightDto>, JoinedDto>()
        {
            @Override
            public JoinedDto transform(JoinPair<LeftDto, RightDto> selection)
            {
                return new JoinedDto((selection.getLeft() != null ? selection.getLeft().getText() : "")
                        ,(selection.getLeft() != null ? selection.getLeft().getId() : -1)
                        ,selection.getRight().getId());
            }
        });

        A.exp(rightOuterJoin()).act(results);
    }

    private Collection<JoinedDto> innerJoin()
    {
        Collection<JoinedDto> joinPairs = new ArrayList<>();
        for (LeftDto leftDto : leftList)
        {
            for (RightDto rightDto : rightList)
            {
                if (leftDto.id == rightDto.leftId)
                {
                    joinPairs.add(new JoinedDto(leftDto.getText(),leftDto.getId(),rightDto.getId()));
                }
            }
        }
        return joinPairs;
    }

    private Collection<JoinedDto> leftOuterJoin()
    {
        Collection<JoinedDto> joinPairs = new ArrayList<>();
        for (LeftDto leftDto : leftList)
        {
            RightDto matchingDto = null;
            for (RightDto rightDto : rightList)
            {
                if (leftDto.id == rightDto.leftId)
                {
                    matchingDto = rightDto;
                }
            }
            joinPairs.add(new JoinedDto(leftDto.getText(),leftDto.getId()
                    ,matchingDto != null ? matchingDto.getId(): -1));
        }
        return joinPairs;
    }

    private Collection<JoinedDto> rightOuterJoin()
    {
        Collection<JoinedDto> joinPairs = new ArrayList<>();
        for (RightDto rightDto : rightList)
        {
            LeftDto matchingDto = null;
            for (LeftDto leftDto : leftList)
            {
                if (leftDto.id == rightDto.leftId)
                {
                    matchingDto = leftDto;
                }
            }
            joinPairs.add(new JoinedDto(matchingDto != null ? matchingDto.getText() : ""
                    ,matchingDto != null ? matchingDto.getId(): -1
                    ,rightDto.getId()));
        }
        return joinPairs;
    }

    static class LeftDto
    {
        private int id;
        private String text;

        LeftDto(int id, String text)
        {
            this.id = id;
            this.text = text;
        }

        public int getId()
        {
            return id;
        }

        public String getText()
        {
            return text;
        }
    }

    static class RightDto
    {
        private int leftId;
        private int id;

        RightDto(int leftId, int id)
        {
            this.leftId = leftId;
            this.id = id;
        }

        public int getLeftId()
        {
            return leftId;
        }

        public int getId()
        {
            return id;
        }
    }

    static class JoinedDto
    {
        private String text;
        private int leftId;
        private int rightId;

        JoinedDto(String text, int leftId, int rightId)
        {
            this.text = text;
            this.leftId = leftId;
            this.rightId = rightId;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JoinedDto joinedDto = (JoinedDto) o;

            if (leftId == joinedDto.leftId)
            {
                if (rightId == joinedDto.rightId)
                {
                    if (!(text != null ? !text.equals(
                            joinedDto.text) : joinedDto.text != null))
                    {
                        return true;
                    }
                }
            }
            return false;

        }

        @Override
        public int hashCode()
        {
            int result = text != null ? text.hashCode() : 0;
            result = 31 * result + leftId;
            result = 31 * result + rightId;
            return result;
        }
    }
}
