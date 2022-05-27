# Converse Wrapper


Freemarker Converse Wrapper is a wrapper to transform xml to json and json to xml using freemarker and jackson libraries.

# How to use



## Wrapper Conversor Sync
The library can be imported like this:

```gradle
implementation 'com.github.bancolombia:wrapper-conversor-sync-client:<latest-version-here>'
```
## Wrapper Conversor Async (Compatible with Reactor)

The library can be imported like this:

```gradle
implementation 'com.github.bancolombia:wrapper-conversor-async-client:<latest-version-here>'
```
```gradle

dependencies {
// Reactor Core is required!
implementation group: 'io.projectreactor', name: 'reactor-core', version: '3.4.17'
// wrapper-conversor-async    
implementation 'com.github.bancolombia:wrapper-conversor-async-client:0.1.0'
}
```


### Define your configuration

#### Configurations


## Get the json by Xml:

## Get the xml by Json:




