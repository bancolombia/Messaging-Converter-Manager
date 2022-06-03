# Messaging Converter Manager

Messaging Converter Manager is a library that aims to abstract different implementations for messaging transformation.
# How to use
The library can be imported like this:
```gradle
   //Async (Compatible with Project Reactor)
   implementation 'co.com.bancolombia:freemarker-converter-async:<latest-version-here>'
   //Sync
   implementation 'co.com.bancolombia:freemarker-converter-sync:<latest-version-here>'

```


## Define your configuration
TemplateTransaction is a map of ResourceTemplate the key is a unique identifier for this template.

Is required to create a bean of **TemplateTransaction** with at least the required fields

| Field               | Required | Description                                                                                                      |
|---------------------|----------|------------------------------------------------------------------------------------------------------------------|
| transactionName     | No       | name of the transaction to be resolved by the template                                                           |
| channel             | No       | name of the potential consumers of the template                                                                  |
| transaction         | No       | transaction code to be resolved                                                                                  |
| templateIn          | **Yes**  | template used to transform the message into the function _jsonToXml_                                             |
| templateOut         | **Yes**  | template used to transform the message into the function _xmlToObject_ when the templateValidations return true  |
| templateError       | **Yes**  | template used to transform the message into the function _xmlToObject_ when the templateValidations return false |
| templateValidations | **Yes**  | interface used to outsource decision making in messaging transformation                                          |

**templateValidations** is an interface to have one function
```java
boolean isOkResponse(Map<?, ?> response);
```
Example:
```java
    @Bean
    public TemplateTransaction templateTransaction() {
        TemplateValidations templateValidations = new TemplateValidations() {
            private static final String STATUS = "status";
            private static final String CODE = "code";
            private static final String OK_CODE = "200";

            @Override
            public boolean isOkResponse(Map<?, ?> response) {
                return ((Map<?, ?>)  response.get(STATUS)).get(CODE).equals(OK_CODE);
            }
        };
        TemplateTransaction templateTransaction = TemplateTransaction.builder().build();
        TemplateTransaction.ResourceTemplate resourceTemplate = TemplateTransaction.ResourceTemplate.builder()
                .templateValidations(templateValidations)
                .templateIn("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><number>123</number>")
                .templateOut("{\"task\":{\"id\": \"123\",\"description\": \"this is a description\", \"name\":\"title\"}}")
                .templateError("{\"reason\":\"401\",\"domain\":\"na\",\"code\":\"401\",\"message\":\"error message\"}")
                .build();
        templateTransaction.put("100", resourceTemplate);
        return templateTransaction;
```

Create the connector:
```java
//Async (Compatible with Project Reactor)
private final FreeMarkerConverseAsync freeMarkerConverse;
//Sync
private final FreeMarkerConverseSync freeMarkerConverse;
```


convert Json to Xml by template:
```java
freeMarkerConverse.jsonToXml(json, templateCode, context);
//or without context
freeMarkerConverse.jsonToXml(json, templateCode);
```
convert Xml to object by template :
```java
freeMarkerConverse.xmlToObject(xml, templateCode, Object.class)
```
## How can I contribute ?

Great !!:

    Clone this repo
    Create a new feature branch
    Add new features or improvements
    Send us a Pull Request

### To Do

- New implementantions of this abstranction with other technologies.





