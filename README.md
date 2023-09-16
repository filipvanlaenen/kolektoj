# Kolektoj — Yet Another Java Collections Framework

The aim of this project is to provide a clean Java collections framework.

* [Design Principles](#design-principles)
* [Collection Properties](#collection-properties)
* [Overview](#overview)
* [Comparison with the Java Platform Collections Framework](#comparison-with-the-java-platform-collections-framework)
  * [JDK 17 Interfaces](#jdk-17-interfaces)
  * [JDK 17 Classes](#jdk-17-classes)

## Design Principles

* Collections come in different forms with different properties, and it should be possible for a developer to pick
  exactly the set of properties he needs for his software to do the job.
* Collection properties that don't have an influence on the API should be treated as attributes. This minimizes the
  number of interfaces in the framework.
* Methods should be declared at the hierarchy level where they start to make sense, and never higher than that.
* Behavioural specializations are not part of this framework. Examples of behavioural specializations are queues and
  stacks.
* Properties not specified by the developer may exhibit some default behaviour, but there is no requirement that the
  behaviour governed by an unspecified property is consistent within an implementation, or even within an instance of an
  implementation within its lifetime.

## Collection Properties

The following collection properties have been identified:

* Order: whether the elements in the collection have an order or not.
* Uniqueness: whether the collection can contain the same element more than once or not.
* Null: whether `null` is allowed as an element of the collection or not.
* Modifiability: whether the content of the collection can be modified or not, and if it can be modified, whether it's
  thread-safe or not.

## Overview

![Overview](Overview.png)

## Comparison with the Java Platform Collections Framework

The comparison below is based on the Java Platform
[Collections Framework Overview](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/doc-files/coll-overview.html)
for JDK 17.

### JDK 17 Interfaces

#### java.util.Collection&lt;E>

The table below shows how the methods defined on the
[java.util.Collection&lt;E>](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collection.html)
interface map to Kolektoj methods.

| JDK 17 Method                             | Kolektoj Class       | Kolektoj Method             |
|-------------------------------------------|----------------------|-----------------------------|
| boolean add(E element)                    | ModifiableCollection | boolean add(E element)      |
| boolean addAll(Collection&lt;? extends E> c) | | |
| void clear() | | |
| boolean contains(Object o)                | Collection           | boolean contains(E element) |
| boolean containsAll(Collection&lt;?> c) | | |
| boolean equals(Object o) | | |
| int hashCode() | | |
| boolean isEmpty()                         | Collection           | default boolean isEmpty()   |
| Iterator&lt;E> iterator() | | |
| default Stream&lt;E> parallelStream() | | |
| boolean remove(Object o)                  | ModifiableCollection | boolean remove(E element)   |
| boolean removeAll(Collection&lt;?> c) | | |
| default boolean removeIf(Predicate&lt;? super E> filter) | | |
| boolean retainAll(Collection&lt;?> c) | | |
| int size()                                | Collection           | int size()                  |
| default Spliterator&lt;E> spliterator() | | |
| default Stream&lt;E> stream() | | |
| Object[] toArray() | | |
| default &lt;T> T[] toArray(IntFunction&lt;T[]> generator) | | |
| &lt;T> T[] toArray(T[] a) | | |


#### Other

* java.util.concurrent.BlockingDeque
* java.util.concurrent.BlockingQueue
* java.util.concurrent.ConcurrentMap
* java.util.concurrent.ConcurrentNavigableMap
* java.util.Deque
* java.util.List
* java.util.Map
* java.util.NavigableMap
* java.util.NavigableSet
* java.util.Queue
* java.util.Set
* java.util.SortedMap
* java.util.SortedSet
* java.util.concurrent.TransferQueue

### JDK 17 Classes

* AbstractCollection
* AbstractList
* AbstractMap
* AbstractSequentialList
* AbstractSet
* ArrayBlockingQueue
* ArrayDeque
* ArrayList
* BlockingDeque
* BlockingQueue
* ConcurrentHashMap
* ConcurrentMap
* ConcurrentNavigableMap
* ConcurrentSkipListMap
* ConcurrentSkipListSet
* CopyOnWriteArrayList
* CopyOnWriteArraySet
* DelayQueue
* HashMap
* HashSet
* LinkedBlockingDeque
* LinkedBlockingQueue
* LinkedHashMap
* LinkedHashSet
* LinkedList
* LinkedTransferQueue
* PriorityBlockingQueue
* SynchronousQueue
* TransferQueue
* TreeMap
* TreeSet
