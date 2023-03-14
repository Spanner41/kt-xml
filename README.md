# kt-xml

A fluent DSL for building XML in Kotlin.

## Getting Started

Creating beautiful XML is no longer a chore, and you don't need to use any bloated software. With `kt-xml`, it takes just 2 easy steps:

1. Build the DOM
2. Print it!

### Building the DOM

Creating a DOM is easy using the fluent DSL. Start with the `document` wrapper and then declare any element structure.

```kotlin
val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
    element("svg") {
        "width" to 100
        "height" to 110

        comment("This is a rectangle")
        element("rect") {
            "width" to 100
            "height" to 100
            "style" to "fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)"
        }
    }
}
```

You can also use code to procedurally generate the XML:

```kotlin
data class Rectangle(val x: Int, val y: Int, val width: Int, val height: Int)

val rectangles = (1..4).map { Rectangle(it, it, it, it) } // Add logic here to create rectangles

val rectangleElements = rectangles.map { r ->
    element("rect") {
        "x" to r.x
        "y" to r.y
        "width" to r.width
        "height" to r.height
        "style" to "fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)"
    }
}

val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
    element("svg") {
        rectangleElements.forEach(::add)
    }
}
```

### The XML Printer

The XML Printer takes a DOM and a format.  The format can be used to customize the way the DOM prints.
We've provided a `PrettyFormat` to make the output more human-readable or a `CompressedFormat` for efficiency.
This gives you the flexibility to output the same DOM in multiple formats:

```kotlin
val printer = XmlPrinter()

println(printer.print(xml, PrettyFormat()))
println("\n-----\n")
print(printer.print(xml, CompressedFormat()))
```

You may want to compress the output in production but make it easier to read in the lower environments.
In Spring, it would look like this:

```kotlin
@Configuration
class XmlConfig {
    @Bean
    @Profile("production")
    fun xmlFormatProd() = CompressedFormat()

    @Bean
    @Profile("!production")
    fun xmlFormatDefault() = PrettyFormat()
}
```

## Using Dynamic Values

When creating attributes, you can supply a hard-coded value or a function.  If you use a function, the value is not resolved until the DOM is printed.
You can re-use the same XML structure and print it multiple times with different attribute values.

*WARNING: The DOM implementation is not thread-safe. There is no guarantee what will happen if your function's result is changed asynchronously.*

```kotlin
var x = 1
var y = 1

val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
    element("point") {
        "x" { x }
        "y" { y }
    }
}

val printer = XmlPrinter()
val format = PrettyFormat()

println(printer.print(xml, format))
println("\n-----\n")

x += 1
y *= 2

println(printer.print(xml, format))
```

## Creating your own DOM Elements

If you want to implement a new type of DOM element, you can extend the DSL using the `Element` class.
For example, you could make XHTML tags like this:

```kotlin
class Div(
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    children: MutableList<Node> = mutableListOf()
) : Element("div", canCollapseWhenEmpty = false, attributes = attributes, children = children)

class Html(
    attributes: MutableMap<String, () -> Any> = mutableMapOf(),
    children: MutableList<Node> = mutableListOf()
) : Element("html", canCollapseWhenEmpty = false, attributes = attributes, children = children)

private fun html(lambda: Element.() -> Unit = {}) = Html().apply(lambda)
private fun Html.div(lambda: Element.() -> Unit = {}) = Div().apply(lambda).also(::add)

val xml = document(XmlVersion.V1_1, XmlEncoding.UTF_8) {
    html {
        div { text("Hello World!") }
    }
}
```