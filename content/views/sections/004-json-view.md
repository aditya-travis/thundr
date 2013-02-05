---
id: jsonView
title: Json View

---

#### JsonView

`JsonView` uses the [Gson library](http://code.google.com/p/google-gson/) to serialize the given object to json, and return it to the request client.

```java
…
MyDto dto = … 
return new JsonView(dto);
…
```