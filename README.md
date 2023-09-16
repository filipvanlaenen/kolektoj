# Kolektoj — Yet Another Java Collections Framework

The aim of this project is to provide a clean Java collections framework.

* [Design Principles](#design-principles)
* [Collection Properties](#collection-properties)
* [Overview](#overview)
* [Comparison with the Java Platform Collections Framework](#comparison-with-the-java-platform-collections-framework)
  * [Interfaces](#interfaces)
  * [Classes](#classes)

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

### Interfaces

* java.util.Collection
* java.util.Set
* java.util.SortedSet
* java.util.NavigableSet
* java.util.List
* java.util.Queue
* java.util.concurrent.BlockingQueue
* java.util.concurrent.TransferQueue
* java.util.Deque
* java.util.concurrent.BlockingDeque
* java.util.Map
* java.util.SortedMap
* java.util.NavigableMap
* java.util.concurrent.ConcurrentMap
* java.util.concurrent.ConcurrentNavigableMap

### Classes

* HashSet
* TreeSet
* LinkedHashSet
* ArrayList
* LinkedList
* ArrayDeque
* LinkedList
* HashMap
* TreeMap
* LinkedHashMap
* AbstractCollection
* AbstractSet
* AbstractList
* AbstractSequentialList
* AbstractMap
* BlockingQueue
* TransferQueue
* BlockingDeque
* ConcurrentMap
* ConcurrentNavigableMap
* LinkedBlockingQueue
* ArrayBlockingQueue
* PriorityBlockingQueue
* DelayQueue
* SynchronousQueue
* LinkedBlockingDeque
* LinkedTransferQueue
* CopyOnWriteArrayList
* CopyOnWriteArraySet
* ConcurrentSkipListSet
* ConcurrentHashMap
* ConcurrentSkipListMap
