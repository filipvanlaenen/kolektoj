# Kolektoj -- Yet Another Java Collections Framework

The aim of this project is to provide a clean Java collections
framework.

## Design Principles

* Collections come in different forms with different properties, and it
  should be possible for a developer to pick exactly the set of
  properties he needs for his software to do the job.
* Collection properties that don't have an influence on the API should
  be treated as attributes. This minimizes the number of interfaces
  in the framework.
* Methods should be declared at the hierarchy level where they start to
  make sense, and never higher than that.
* Behavioural specializations are not part of this framework. Examples
  of behavioural specializations are queues and stacks.
* Properties not specified by the developer may exhibit some default
  behaviour, but there is no requirement that the behaviour governed by
  an unspecified property is consistent within an implementation, or
  even within an instance of an implementation within its lifetime.

## Collection Properties

The following collection properties have been identified:
* Order
* Uniqueness
* Null
* Modifiability
