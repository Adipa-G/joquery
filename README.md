# News #
24-May-2016 :
Added group conditions for both 1.7 branch and for 1.8 branch

25-May-2014 :
Upgraded to Java 1.8
older version supporting Java 1.7 is moved to a branch

10-Nov-2012 :
Added grouping functionality

04-Nov-2012 :
Added joins and sorting functionality

14-Oct-2012 :
Modified base structure for easy use of simple filtering queries while extending support for complicated joined queries.
Added and and or condition support

07-Oct-2012 :
Initial check in

# Introduction #
JOQuery allows you to use SQL style querying on java collections. Syntax is easy to use and library is with a small footprint. With it's generic typed interface it's easy to filter/sort/group/join object collections.

## Features ##
  1. Basic filtering (equality,less than,less than or equal,greater than,greater than or equal,in,between)
  1. Sorting
  1. Grouping
  1. Joins
  1. Group Conditions

## Using the library ##
No runtime dependencies are required.

# Quick Start #

Following class and object definitions are referred in the examples
	class Dto
	{
		private int id;
		private String text;

		public int getId()
		{
			return id;
		}

		public int getText()
		{
			return text;
		}
	}

	Collection<Dto> testList = new ArrayList<>();
	testList.Add(new Dto());


## Conditions ##
To filter objects with id = 1, following syntax can be used

	Filter<Dto> query = CQ.<Dto>filter(testList)
		.where()
		.property("id").eq().value(1);
	Collection<Dto> filtered = query.list();


It can also re written as,

	Filter<Dto> query = CQ.<Dto>filter(testList)
		.where()
		.property(Dto::getId)
		.eq().value(1);
	Collection<Dto> filtered = query.list();


Another example,

	Filter<Dto> query = CQ.<Dto>filter(testList)
		.where()
		.property(Dto::getId)
		.in().value(new int[]{1,2});
	Collection<Dto> filtered = query.list();


## Sorting ##

    Filter<Dto> query = CQ.<Dto>filter(testList)
        .orderBy()
        .property(Dto::getId)
        .property(Dto::getName)
    Collection<Dto> sorted = query.list();


## Grouping ##

    GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query(testList)
        .group()
        .groupBy(Dto::getId)
    Collection<Grouping<Integer,Dto>> grouped = query.list();

## Group Conditions ##

	GroupQuery<Integer,Dto> query = CQ.<Dto,Dto>query()
		.from(dtoList)
		.<Integer>group()
		.groupBy(Dto::getId)
		.where()
		.exec(Grouping::getKey).eq().value(1);
	
	Collection<Grouping<Integer,Dto>> grouped = query.list();
	
## Joins ##

	class LeftDto
	{
		private int id;
		private String text;

		public int getId()
		{
			return id;
		}

		public int getText()
		{
			return text;
		}
	}

	class RightDto
	{
		private int id;
		private int leftId;
		private String text;

		public int getId()
		{
			return id;
		}

		public int getLeftId()
			{
				return leftId;
			}

		public int getText()
		{
			return text;
		}
	}

	Collection<LeftDto> leftList = new ArrayList<>();
	Collection<RightDto> rightList = new ArrayList<>();

	Collection<JoinPair<LeftDto,RightDto>> results = CQ.<LeftDto, LeftDto>query(leftList)
		.<RightDto, JoinPair<LeftDto,RightDto>>innerJoin(CQ.<RightDto, RightDto>query(rightList))
		.on("id", "leftId")
		.list();

More examples can be found in the wiki. Also there are ample samples available in the tests.

# Setting up for development #
If you are interested in contributing please drop an email

Fastest way to setup the system is to
  1. checkout the project,
  1. generate the ant build from the IDE
  1. run the build.xml script

## Checking out sources ##
Sources can be checked out or can be downloaded.

## Compile From Sources ##
The editor used is Intellij IDEA Community Edition. An ant script is included to run the unit tests. Compilation ant script needed to be re-generated from the editor to run the build. Required libraries are included in the lib folder.

## Things to be done ##
  1. Projections (count,sum,....,custom)
  1. Group conditions

## License
GNU GPL V3