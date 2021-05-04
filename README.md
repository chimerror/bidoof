Bidoof
======

Introduction
------------

Bidoof is a bridge bidding program meant to help teach how to bid in contract bridge.

Building
--------

Build for CLJS and start in figwheel:

```{shell}
clj -M:dev
```

To build a release version for the web:

```{shell}
clj -M:prod
```

To develop the native version:

```{shell}
clj -M:dev native

# NOTE: On Mac OS, you need to add the macos alias:

clj -M:dev:macos native
```

To build the native version as a jar file:

```{shell}
clj -M:prod uberjar
```
