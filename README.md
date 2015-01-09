#Enumerables


##What?
Enumerables is a wrapper for Java 8 collections offering functional methods like map and reduce while being mutable and re-iterable at all times.

```java
Enumerable.of("foo", "bar")
          .map(x -> x + "bar")
          .forEach(System.out::println);

//-> foobar
//-> barbar
```
Looks like [Stream](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html) right? Enumerables actually is mostly inspired by .NET's [IEnumerable](http://msdn.microsoft.com/en-us/library/ckzcawb8.aspx) extension methods, but it tries to resemble Java's Stream where possible.

##Why?
###tl;dr
- Stream vs. Enumerable
- use once vs. implicitly re-iterable
- performance optimized vs. developer balanced between readbility/performance
- type changes array->stream->list vs. iterable type that can be safely passed as a method argument and used throughout the system


Streams in Java 8 are built for performance and the inability to re-iterate a collection can save you from for example accidentally querying a database multiple times on runtime. And that's a good thing.

However, it's not uncommon that you have a situation where you just have to re-iterate a collection multiple times.
```java
Stream<Integer> ints = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Stream<Integer> evens = ints.filter(x -> x % 2 == 0);
Stream<Integer> odds = ints.filter(x -> x % 2 != 0); <-- IllegalStateException

```
Or you have a reasonably sized in-memory collection for which re-iteration
isn't a performance issue at all.
```java
// So, we need to create a new stream every time we need to re-iterate.
Stream<Integer> evens = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                              .filter(x -> x % 2 == 0);

Stream<Integer> odds = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                             .filter(x -> x % 2 != 0);
```
Or you have heavy mapping functions that you would like to run only once.
```java
// With Streams, you want to collect the mapped elements and then create
// new streams from the collection for further operations.
List<Integer> mappedList = Stream.of(1, 2, 3)
                                 .map(x -> timeConsumingStuff(x))
                                 .collect(Collectors.toList());

mappedList.stream().findFirst();
mappedList.stream().findAny();
```
Now, don't get me wrong - I like that my programs perform well. But - I do believe that the developers should be given more control - in this case  control over balancing between performance and readability.
```java
Enumerable<Integer> ints = Enumerable.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
Enumerable<Integer> evens = ints.filter(x -> x % 2 == 0);
// -> [2,4,6,8,10]

Enumerable<Integer> odds = ints.filter(x -> x % 2 != 0);
// -> [1,3,5,7,9]

// Disclaimer: filter() runs lazily as most of the functions in Enumerables,
// so the collection won't be actually iterated at all before it's reduced,
// ordered or collected.

// You can use copy() to store the elements after it's iterated for the
// first time.
Enumerable<Integer> copy = Enumerable.of(1,2,3).map(x -> timeConsumingStuff(x)).copy();

// time consuming mapping stuff takes place here.
List<Integer> ints1 = copy.toList();

// re-iteration happens using the copy.
Optional<Integer> first = copy.findFirst();
```
Because Enumerable is re-iterable and mutable, Enumerable objects are safe to pass and use as method arguments. This is a distinct difference compared to Streams. Therefore there's no need to collect them into lists and then back to an Enumerable later - this improves the readability of the code.

#Usage examples

## Constructing
```java
// From arbitrary number of elements
Enumerable<Integer> ints = Enumerable.of(1, 2, 3);

// From array
Enumerable<String> strings = Enumerable.of(new String[]{"a", "b", "c"});

// From any Iterable
Enumerable<Double> doubles = Enumerable.of(new ArrayList<Double>());

Enumerable<String> empty = Enumerable.empty();
// -> []

Enumerable<Integer> range = Enumerable.range(1, 5);
// -> [1,2,3,4,5]

Enumerable<Integer> repeat = Enumerable.repeat(() -> 1, 5);
// -> [1,1,1,1,1]

```
##Mapping
```java
HashMap<String, Integer[]> hashMap = new HashMap<>();
hashMap.put("foo", new Integer[]{1, 2});
hashMap.put("bar", new Integer[]{3, 4, 5});

Enumerable<String> keys = Enumerable.of(hashMap.keySet());

keys.map(x -> x + hashMap.get(x).length);
// -> ["foo2", "bar3"]

keys.flatMap(x -> hashMap.get(x));
// -> [1,2,3,4,5]

```
##Filtering
```java
Enumerable.range(1, 10).filter(x -> x % 2 == 0);
// -> [2,4,6,8,10]

Enumerable.of(1, "foo", 2.0f, "bar").filterType(String.class);
// -> ["foo", "bar"]

Enumerable.range(1, 10).allMatch(x -> x > 0);
// -> true

Enumerable.range(1, 10).noneMatch(x -> x > 10);
// -> true

Enumerable.range(1, 10).anyMatch(x -> x == 5);
// -> true

Enumerable.range(1, 10).contains(11);
// -> false
```
##Sorting
```java
Enumerable.of("a", "foo3", "bar").orderBy(x -> x.length());
// -> ["a", "bar", "foo3"]

Enumerable.of("a", "foo3", "bar").orderByDescending(x -> x.length());
// -> ["foo3", "bar", "a"]

Enumerable.range(1,10).reverse();
// -> [10,9,8,7,6,5,4,3,2,1]
```
##Reducing
```java
Enumerable.of("foo", "bar").reduce("", (acc,x) -> acc + x);
// -> "foobar"

Enumerable.of(1,2,3).sum(x -> x);
// -> 6

Enumerable.of(1,2,3).average(x -> x);
// -> 3

Enumerable.of(1,2,3).min(x -> x);
// -> 1

Enumerable.of(1,2,3).max(x -> x);
// -> 3

Enumerable.of(1,2,3).count();
// -> 3
```
##Selecting and Collecting
```java
Enumerable.range(1, 10).limit(5);
// -> [1,2,3,4,5]

Enumerable.range(1, 10).skip(5);
// -> [6,7,8,9,10]

Enumerable.range(1, 10).findFirst();
// -> 1

Enumerable.range(1, 10).findLast();
// -> 10

Enumerable.range(1, 10).findSingle();
// -> NoSuchElementException

Enumerable.of(1).findSingle();
// -> 1

// Stream Collectors are also supported
Enumerable.of(1,2,3).collect(Collectors.toSet());

// Shortcut for collecting a list
Enumerable.of(1,2,3).toList();
```
##Other functions
```java
// concatenates with an Iterable<T> or an array
Enumerable.of(1).concat(2,3);
// -> [1,2,3]

// sizeIs* functions can be used to determine sizes without iterating
// the collection through (like count() does)
Enumerable.of(1,2,3).sizeIsExactly(3); //true
Enumerable.of(1,2,3).sizeIsLessThan(1); // false
Enumerable.of(1,2,3).sizeIsGreaterThan(1); //true

// copy() can be used to store the collection on first iteration so that
// it's parent won't be iterated again.
Enumerable<Integer> ints = Enumerable.of(1,2,3).copy();
```
# Future features?
- Sum/Average/Min/Max functions also for Double,Float,Long
- Distinct/Union/Intersect/Difference functions
- Join function
- Merge function
- SkipWhile/LimitWhile functions
- _Something missing? Send a **Pull Request**_
