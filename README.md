#Enumerables


##What?
Enumerables is a wrapper for Java 8 collections offering functional methods like map and reduce while being mutable and re-iterable at all times.

```
Enumerable.of("foo", "bar")
          .map(x -> x + "bar")
          .forEach(System.out::println);

//-> foobar
//-> barbar
```
Looks like [Stream](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html) right? Enumerables actually is mostly inspired by .NET's [IEnumerable](http://msdn.microsoft.com/en-us/library/ckzcawb8.aspx) extension methods, but it tries to resemble Java's Stream where possible.

##Why?
Well, the inspiration came from small things like these:
```
IntStream range = IntStream.range(1, 10);
OptionalInt first = range.findFirst();
range.limit(5);

//->java.lang.IllegalStateException: stream has already been operated upon or closed
```
Now, of course the one way to overcome the exception in this case would be to either create another stream to call the limit(5) on, or use a collector to create a list from the range first and then get first item of the list and then take the first 5 items.
```
//Like so
IntStream range = IntStream.range(1, 10);
OptionalInt first = range.findFirst();

IntStream anotherRange = IntStream.range(1, 10);
anotherRange.limit(5);

// Or, alternatively
IntStream range = IntStream.range(1, 10);
//range needs to be mapped since IntStream doesn't support built-in Collectors
List<Integer> list = range.mapToObj(x -> x)
                             .collect(Collectors.toList());
Integer first = list.get(0);
Stream.of(list.subList(0, 4));
```
For me, neither of these options really feel intuitive. And there are probably two or three more ways to do the same thing which I'm just not aware of.
Basically all I ever wanted was to get the first item of the collection and get the first 5 items after that.

What if the Stream would just allow re-iteration? Some of the performance would be lost, for sure - but I would be able to control the balance between code readability and performance:
```
Enumerable<Integer> range = Enumerable.range(1, 10);
Optional<Integer> first = range.first();
Enumerable<Integer> firstFive = range.take(5);

//At this point, the Enumerable range hasn't actually iterated through at all yet.

//Enumerables support Stream Collectors
Set<Integer> set = range.collect(Collectors.toSet());

//Still re-iterable after collection
range.map(x -> x.toString())
     .forEach(System.out::println);
```
Being re-iterable and mutable, Enumerable collections are also safe to pass around as arguments and return values so there's not as much mapping between collection types as there is when using Stream.

#Usage
Map
```
//Enumerables
Enumerable.of("foo", "bar")
          .map(x -> x + "bar");

//Stream
Stream.of("foo", "bar")
      .map(x -> x + "bar");

//IEnumerable in C#
var array = new [] { "foo", "bar"};
array.Select(x => x + "bar");
```
