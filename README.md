#Enumerables


##What?
Enumerables is a wrapper for Java 8 collections offering functional methods like map and reduce while staying immutable and (re-)iterable at all times.

In comparison to Stream, Enumerables improves the usability and readability of the code especially when dealing with small in-memory collections by removing the need of having to convert the collection into a stream for each iteration round.

In short, when using Enumerables, there's no in-between collecting the results after every iteration round, no writing code with objects turning from lists to streams and back, and no runtime exceptions. You can use enumerable collections like any other iterables.

```java
// With Enumerables, you can do this:
Enumerable<Integer> ints = Enumerable.range(1,10);
ints.filter(x -> x % 2 == 0).forEach(x -> System.out.println(x));
// -> 2,4,6,8,10

ints.filter(x -> x % 2 != 0).forEach(x -> System.out.println(x));
// -> 1,3,5,7,9

// There's also a split function that you can use to optimize flow and performance.
Enumerable<Integer>[] evensAndOdds = Enumerable.range(1, 10).split(x -> x % 2 == 0);

evensAddOdd[0].forEach(x -> System.out.println(x));
// -> 2,4,6,8,10

evensAddOdds[1].forEach(x -> System.out.println(x));
// -> 1,3,5,7,9

// With Streams, given the same situation,
// you are usually forced to replicate the same stream like this:
IntStream.rangeClosed(1, 10)
         .filter(x -> x % 2 == 0)
         .forEach(x -> System.out.println(x));
// -> 2,4,6,8,10

IntStream.rangeClosed(1, 10)
         .filter(x -> x % 2 != 0)
         .forEach(x -> System.out.println(x));
// -> 1,3,5,7,9
```
Enumerables is mostly inspired by .NET's [IEnumerable](http://msdn.microsoft.com/en-us/library/ckzcawb8.aspx) extension methods, but it tries to resemble Java's [Stream](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html) API where possible.

##Why?
Streams in Java 8 are built for performance and it's autoclosing feature which stops you from re-iterating a stream can save you from for example accidentally querying a database multiple times on runtime.

However, it's not uncommon that you have a situation where you just have to re-iterate a collection multiple times.
```java
Stream<Integer> ints = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Stream<Integer> evens = ints.filter(x -> x % 2 == 0);
Stream<Integer> odds = ints.filter(x -> x % 2 != 0); <-- IllegalStateException

```
You might have a reasonably sized in-memory collection for which re-iteration
isn't a performance issue at all.
```java
// So, we need to create a new stream every time we need to re-iterate.
Stream<Integer> evens = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                              .filter(x -> x % 2 == 0);

Stream<Integer> odds = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                             .filter(x -> x % 2 != 0);
```
Or you might even have heavy mapping functions that you would like to run only once.
```java
// With Streams, you want to collect the mapped elements and then create
// new streams from the collection for further operations.
List<Integer> mappedList = Stream.of(1, 2, 3)
                                 .map(x -> timeConsumingStuff(x))
                                 .collect(Collectors.toList());

mappedList.stream().findFirst();
mappedList.stream().findAny();
```
Performance is of course important. However, code usability and readability is also important. Enumerables enables you to adjust the balance between performance and readability to whatever suits your needs. Enumerables uses lazy iteration where possible - in practice methods that reduce or order the collection will force iteration for the whole collection.
```java
Enumerable<Integer> ints = Enumerable.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Enumerable<Integer> evens = ints.filter(x -> x % 2 == 0);
// -> [2,4,6,8,10]

Enumerable<Integer> odds = ints.filter(x -> x % 2 != 0);
// -> [1,3,5,7,9]

// You can use copy() to store the elements after it's iterated for the
// first time.
Enumerable<Integer> copy = Enumerable.of(1,2,3).map(x -> timeConsumingStuff(x)).copy();

// time consuming mapping stuff takes place here, colleting the copy to a list will iterate it through.
List<Integer> ints1 = copy.toList();

// re-iteration happens using the copy and no time consuming mapping actions are no longer done.
Optional<Integer> first = copy.findFirst();
```
Because Enumerable is re-iterable and mutable, Enumerable objects are safe to pass and use as method arguments. This is a distinct difference compared to Streams. Therefore there's actually no need to collect them into lists and then back to an Enumerable later - this improves the readability of the code.
```java
private Enumerable<Integer> doSomeRandomStuffWeWantToExtract(int n) {
    return Enumerable.range(1, n)
                     .map(Random::new)
                     .map(Random::nextInt);
}
```
###Check out the [Examples](src/test/java/com/github/saulis/enumerables/Examples.java) for more!

# Future features?
- Distinct/Union/Intersect/Difference functions
- Join function
- Merge function
- SkipWhile/LimitWhile functions
- _Something missing? Send a **Pull Request**_
