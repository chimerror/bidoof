Bidoof
======

Introduction
------------

Bidoof is a bridge bidding program meant to help teach how to bid in contract bridge.

Building
--------

### Web Versions

Build for CLJS and start in figwheel:

```{shell}
clj -M:dev
```

To build a release version for the web:

```{shell}
clj -M:prod
```

### Native Versions

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

### Running clj-kondo Tool

To run clj-kondo:

```{shell}
clj -M:clj-kondo
```

Of particular clj-kondo interest is this command that will update the cache to include everything in the classpath:

```{shell}
clj -M:clj-kondo --lint "$(clojure -Spath)" --dependencies --paralell --copy-configs
```
