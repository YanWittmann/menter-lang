# Modules

```id=testmodule---result=(x) -> { x + 1; };;;null
fun = x -> x + 1;;;export [fun] as myModule
```

```after=testmodule---result=null;;;4
import myModule;;;myModule.fun(3)
```

```static
import myModule
doStuff;;;myModule.fun(3)
```

Content!!!
