# ld-jax-rs
Helpers for serving linked-data from JAX-RS services. 

## Status
This is still in preliminary development (not even the name is final). For now, install locally:
```bash
mvn clean install
```

Only Jena is supported at the moment. 

## Project Layout

* `ld-jax-rs-api` Annotations that control the behavior of providers from other modules
* `ld-jax-rs-core` Anything that is not dependent of a specific RDF library or other format. 
  Contains most of the functionality logic.
* `ld-jax-rs-jena` JAX-RS Providers specific to Jena:
** Message body writers and readers for `Model`
** Message body writer for `Resource 

## Using

Add the desired dependency:
```xml
<dependency>
  <groupId>br.ufsc.inf.lapesd.ld-jax-rs</groupId>
  <artifactId>ld-jax-rs-jena</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Register the providers scanning `br.ufsc.inf.lapesd.ld_jaxrs` or adding 
the provider classes to your `Application` subclass:
```java
ResourceConfig application = new ResourceConfig();
JenaProviders.getProviders().forEach(application::register);
```

You can then send/receive whole jena Models using all syntaxes supported by:
 
```java
@Path("translate")
public class TranslatorService {
    @POST
    public Model post(Model model) {
        return model;
    }
}
```

It is also possible to serve [Concise Bounded Descriptions](https://www.w3.org/Submission/CBD/) 
of `Resource`s:

```java
    @CBD
    public Resource get(/* ... */) {
        Resource resource;
        /* ... */
        return resource;
    }
```

The `@CBD` annotation supports several arguments that deviate from the W3C submission, 
such as ignoring reifications, treating named nodes as blank nodes, max path limits, etc. 
See the javadoc for details.   
 
