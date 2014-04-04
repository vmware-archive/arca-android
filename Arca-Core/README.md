# Arca-Core

The Arca Core package provides all the groundwork for caching data within a ContentProvider and interacting with a remote store using a Service (i.e. outside the context of your Fragments/Activities). This means your application can use less battery, less bandwidth and provide a much more responsive user experience.

## [Arca-Broadcaster](Arca-Broadcaster)
The Arca Broadcaster package adds the ability to send local broadcasts. This package is used by various other Arca packages in order to communicate errors and/or results.

## [Arca-Provider](Arca-Provider)
The Arca Provider package builds off of the ContentProvider construct but delegates all the heavy lifting to a variety of different Dataset classes. Datasets are class representations for various storage types. This includes SQLiteTables, SQLiteViews or any custom implementation you create.

## [Arca-Service](Arca-Service)
The Arca Service package lets you offload long running operations away from the lifecycle of your Activities and Fragments. It provides a Service implementation which accepts Operations, where each Operation is designed to encapsulate all the networking and storage required by an individual Dataset (see [Arca-Provider](Arca-Provider)). 

## [Arca-Threading](Arca-Threading)
The Arca Threading package provides a scheduler for background threads that allows for highly customizable, high-performance task scheduling. Developers can use this library to optimize tasks such as precaching, assigning/reassigning priorities, cancelling requests, and more.

## [Arca-Utils](Arca-Utils)
The Arca Utils package contains a set of useful helper classes used by a variety of other Arca packages. It also contains convenience classes that may be useful when implementing your own ContentProvider.