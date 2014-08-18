# Arca
[![Build Status](https://travis-ci.org/cfmobile/arca-android.svg?branch=dev)](https://travis-ci.org/cfmobile/arca-android)

The Arca library provides a comprehensive framework for caching and displaying data. It is modular by design and compatible with native Android components.

This [Google I/O talk](http://www.youtube.com/watch?v=xHXn3Kg2IQE) explains the basis of the architecture.

# Overview

Below is a list of all the major components within the Arca framework. Each major component has a variety of minor components. Each component can be used independently or as part of the entire framework.

##[Arca-Core](arca-core)

The Arca Core package provides all the groundwork for caching data within a ContentProvider and interacting with a remote store using a Service (i.e. outside the context of your Fragments/Activities). This means your application can use less battery, less bandwidth and provide a much more responsive user experience.

##[Arca-App](arca-app)

The Arca App package makes it really easy to fetch data from a ContentProvider and bind it to elements in your layout. This means your UI will always be up to date with the most recent data without having to write a single additional line of code.

# Getting Started

### Levo

The easiest way to get started with the Arca library is to scaffold a project with the [levo cli](https://github.com/cfmobile/levo).

```bash
levo -t github.com/cfmobile/arca-android-templates -m "User id:long name:string age:int"
```

### Gradle

```groovy
compile 'io.pivotal:arca:1.0-beta'
```

# Further Reading

The Arca library takes all the pieces that Google provides as part of their best practices for building Android applications and bundles them with our own interface for convenience and ease of use.

In order to fully understand the entirety of this library you should familiarize yourself with the following components:

[Content Providers](http://developer.android.com/guide/topics/providers/content-providers.html)

[Loaders](http://developer.android.com/guide/components/loaders.html)

[CursorAdapter](http://developer.android.com/reference/android/widget/CursorAdapter.html)
