Utility to encode list of numbers into string. For that in memory table is created where each number represents a character. To add some security custom seed must be set.

Highly customizable to set own characters or include just strings or even only numbers.

See
- [ToUpperAndLowerCaseEnum.java](src/test/groovy/com/foxjunior/enums/ToUpperAndLowerCaseEnum.java) how to define the case insensitive enum lookup funciton.
- [TestEnum.java](src/test/groovy/com/foxjunior/enums/TestEnum.java) how to define more advanced functions.

And usage is described at 
- [EnumLookupBuilderSpec.java](src/test/groovy/com/foxjunior/enums/EnumLookupBuilderSpec.groovy)

Usually I create my enums as:

```java
public enum MyEnum {
    first,
    second,
    third;

    static final Function<String, Optional<MyEnum>> LOOKUP = EnumLookupBuilder.buildCaseInsensitive(MyEnum.class);
    static Optional<MyEnum> lookup(String key) {
        return LOOKUP.apply(key);
    }
}
```
