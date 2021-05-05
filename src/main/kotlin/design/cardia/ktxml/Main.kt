package design.cardia.ktxml

import design.cardia.ktxml.builder.CompressedFormat
import design.cardia.ktxml.builder.PrettyFormat
import design.cardia.ktxml.builder.XmlEncoding
import design.cardia.ktxml.builder.XmlVersion
import design.cardia.ktxml.builder.document
import design.cardia.ktxml.builder.element
import design.cardia.ktxml.builder.text

fun main() {
    val xml = document {
        element("svg") {
            "width" { "200px" }
            "height" { "150px" }
            "viewbox" to "0 0 200 150"

            element("defs") {
                text("<Hello World>")
            }
            element("test") {
                element("hi") {
                    "hello" to "hi"
                }
                element("hi") {
                }
            }
        }
    }

    println(xml.toXml(PrettyFormat(), XmlVersion.V1_1, XmlEncoding.UTF_8))
    println()
    println(xml.toXml(CompressedFormat(), XmlVersion.V1_1, XmlEncoding.UTF_8))
}
