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

It is required to create **TemplateTransaction** bean with at least the required fields. This object is a map of <transactionCode, **ResourceTemplate**>

The attributes of  **ResourceTemplate** are: 

| Field                    | Required | Description                                                                                                      |
|--------------------------|----------|------------------------------------------------------------------------------------------------------------------|
| transactionName          | No       | name of the transaction to be resolved by the template                                                           |
| channel                  | No       | name of the potential consumers of the template                                                                  |
| transaction              | No       | transaction code to be resolved, it is optional but it is **required** on the map as a key                       |
| templateJsonToXml        | **Yes**  | template used to transform the message into the function _jsonToXml_ when the _templateValidations_ return true    |
| templateJsonToXmlError   | **Yes**  | template used to transform the message into the function _jsonToXml_ when the _templateValidations_ return false   |
| templateXmlToObject      | **Yes**  | template used to transform the message into the function _xmlToObject_ when the _templateValidations_ return true  |
| templateXmlToObjectError | **Yes**  | template used to transform the message into the function _xmlToObject_ when the _templateValidations_ return false |
| templateValidations      | **Yes**  | interface used to outsource decision making in messaging transformation                                          |

**templateValidations** is an interface to have two functions
```java
boolean isOkResponseXmlToObject(Map<?, ?> object);
boolean isOkResponseJsonToXml(StringWriter xml);
```
Example:
```java
    @Bean
    public TemplateTransaction templateTransaction(){
        TemplateTransaction templateTransaction=TemplateTransaction.builder().build();
        String transactionCode = "100";
        templateTransaction.put(transactionCode, createResourceTemplate());
        return templateTransaction;
    }
    
    private TemplateTransaction.ResourceTemplate createResourceTemplate(){
        TemplateTransaction.ResourceTemplate resourceTemplate=TemplateTransaction.ResourceTemplate.builder()
        .templateValidations(createTemplateValidations())
        .templateJsonToXml("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><number>123</number>")
        .templateJsonToXmlError("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><number>123</number>")
        .templateXmlToObject("{\"task\":{\"id\": \"123\",\"description\": \"this is a description\", \"name\":\"title\"}}")
        .templateXmlToObjectError("{\"reason\":\"401\",\"domain\":\"na\",\"code\":\"401\",\"message\":\"error message\"}")
        .build();
        return resourceTemplate;
    }
    
    private TemplateValidations createTemplateValidations(){
        return new TemplateValidations(){
            private static final String STATUS="status";
            private static final String CODE="code";
            private static final String OK_CODE="200";
            
            @Override
            public boolean isOkResponseXmlToObject(Map<?, ?> object){
                    return true;
            }
            
            @Override
            public boolean isOkResponseJsonToXml(StringWriter xml){
                    return true;
            }
        };
    }
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

- New implementations of this abstraction with other technologies.





