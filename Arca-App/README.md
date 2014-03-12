# Arca-App

The Arca App package makes it really easy to fetch data from a ContentProvider and bind it to elements in your layout. This means your UI will always be up to date with the most recent data without having to write a single additional line of code.

## [Arca-Adapters](Arca-Adapters)
Within the Android framework, adapters act as a bridge between your data and your UI. The Arca Adapters package builds off the standard CursorAdapter implementation adding a number of improvements.

## [Arca-Dispatcher](Arca-Dispatcher)
The Arca Dispatcher package makes it really easy for you to request data synchronously or asynchronously from a ContentProvider. It accepts Query, Update, Insert and Delete requests and returns a corresponding QueryResult, UpdateResult, InsertResult or DeleteResult.

## [Arca-Fragments](Arca-Fragments)
The Arca Fragments package manages an Arca Dispatcher object which lets you easily execute a Query, Update, Insert or Delete request. It also allows you to register an Arca Monitor to observe callbacks before and after each request is executed.

## [Arca-Monitor](Arca-Monitor)
The Arca Monitor package adds a layer on top of the Arca Dispatcher that allows you to monitor every request that gets executed in the system. This allows you to verify every Query, Updated, Insert or Delete result and resolve differences with a remote store when appropriate.